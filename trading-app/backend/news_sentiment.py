"""Analyse du sentiment de marché sur l'or à partir de l'actualité
publiée, pour compléter l'analyse technique du bot.

Deux méthodes, avec repli automatique :
1. Si ANTHROPIC_API_KEY est configurée (voir .env), les titres
   d'actualité récents sont soumis à un modèle Claude qui renvoie une
   tendance (haussière/baissière/neutre) synthétique.
2. Sinon (ou en cas d'échec), un lexique de mots-clés simple sert de
   repli — moins fin, mais fonctionne hors-ligne et sans clé d'API.

Les titres proviennent du flux RSS public de Google Actualités (aucune
clé requise). En cas d'indisponibilité réseau, le sentiment renvoyé
est neutre : le bot se rabat alors sur la seule analyse technique.
"""
from __future__ import annotations

import json
import re
import threading
import time
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET
from dataclasses import dataclass, field

from . import config

NEWS_QUERY = "gold price OR XAU OR Federal Reserve interest rate OR dollar index"
NEWS_RSS_URL = (
    "https://news.google.com/rss/search?q=" + urllib.parse.quote(NEWS_QUERY) + "&hl=en-US&gl=US&ceid=US:en"
)
FETCH_TIMEOUT_SEC = 6
CACHE_TTL_SEC = 900  # 15 min : on ne martèle ni Google News ni l'API Claude

BULLISH_WORDS = [
    "rate cut", "rate cuts", "dovish", "inflation surge", "safe haven", "safe-haven",
    "record high", "geopolitical", "tensions", "war", "weak dollar", "dollar falls",
    "recession fear", "gold surges", "gold jumps", "gold rallies", "gold rises",
    "central bank buying", "buying spree",
]
BEARISH_WORDS = [
    "rate hike", "hawkish", "dollar strength", "dollar rises", "strong jobs report",
    "gold falls", "gold drops", "gold slides", "gold slumps", "risk-on", "stocks rally",
    "yields rise", "profit-taking", "sell-off",
]

_lock = threading.Lock()
_cache: "SentimentResult | None" = None
_cache_at: float = 0.0


@dataclass
class SentimentResult:
    label: str  # "bullish" | "bearish" | "neutral"
    score: float  # -1..1
    method: str  # "llm" | "keywords" | "indisponible"
    headlines: list[str] = field(default_factory=list)


def _fetch_headlines(max_items: int = 15) -> list[str]:
    req = urllib.request.Request(NEWS_RSS_URL, headers={"User-Agent": "gems-of-rod-trading-app/1.0"})
    with urllib.request.urlopen(req, timeout=FETCH_TIMEOUT_SEC) as resp:
        raw = resp.read()
    root = ET.fromstring(raw)
    titles = [item.findtext("title") or "" for item in root.findall(".//item")]
    return [t for t in titles if t][:max_items]


def _keyword_score(headlines: list[str]) -> SentimentResult:
    text = " ".join(headlines).lower()
    bulls = sum(text.count(w) for w in BULLISH_WORDS)
    bears = sum(text.count(w) for w in BEARISH_WORDS)
    total = bulls + bears
    score = 0.0 if total == 0 else (bulls - bears) / total
    label = "bullish" if score > 0.2 else "bearish" if score < -0.2 else "neutral"
    return SentimentResult(label=label, score=score, method="keywords", headlines=headlines)


def _llm_score(headlines: list[str]) -> SentimentResult | None:
    if not config.ANTHROPIC_API_KEY:
        return None
    prompt = (
        "Voici des titres d'actualité récents pouvant influencer le cours de l'or "
        "(XAU/USD) :\n- " + "\n- ".join(headlines) + "\n\n"
        "Réponds UNIQUEMENT avec un objet JSON de la forme "
        '{"label": "bullish"|"bearish"|"neutral", "score": <nombre entre -1 et 1>}, '
        "où label/score reflètent l'effet net probable de ces titres sur le cours de "
        "l'or à court terme (haussier, baissier ou neutre). Aucun texte hors du JSON."
    )
    body = json.dumps({
        "model": config.ANTHROPIC_MODEL,
        "max_tokens": 200,
        "messages": [{"role": "user", "content": prompt}],
    }).encode("utf-8")
    req = urllib.request.Request(
        "https://api.anthropic.com/v1/messages",
        data=body,
        method="POST",
        headers={
            "x-api-key": config.ANTHROPIC_API_KEY,
            "anthropic-version": "2023-06-01",
            "content-type": "application/json",
        },
    )
    try:
        with urllib.request.urlopen(req, timeout=FETCH_TIMEOUT_SEC) as resp:
            payload = json.loads(resp.read().decode("utf-8"))
        text = payload["content"][0]["text"]
        match = re.search(r"\{.*\}", text, re.S)
        parsed = json.loads(match.group(0)) if match else json.loads(text)
        label = parsed["label"]
        score = float(parsed["score"])
        if label not in ("bullish", "bearish", "neutral"):
            return None
        return SentimentResult(label=label, score=score, method="llm", headlines=headlines)
    except Exception:
        return None


def get_sentiment() -> SentimentResult:
    global _cache, _cache_at
    with _lock:
        now = time.time()
        if _cache is not None and now - _cache_at < CACHE_TTL_SEC:
            return _cache

        try:
            headlines = _fetch_headlines()
        except Exception:
            result = SentimentResult(label="neutral", score=0.0, method="indisponible")
            _cache, _cache_at = result, now
            return result

        result = _llm_score(headlines) or _keyword_score(headlines)
        _cache, _cache_at = result, now
        return result
