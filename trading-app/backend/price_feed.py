"""Alimentation en cours XAU/USD (once d'or) — pas de courtier requis.

Deux sources publiques gratuites et sans clé d'API sont tentées dans
l'ordre (gold-api.com puis metals.live). Si les deux sont
injoignables (réseau coupé, source en panne), l'application bascule
sur une marche aléatoire simulée, ancrée sur le dernier cours réel
connu, pour rester utilisable hors-ligne. La source effectivement
utilisée est toujours renvoyée avec le cours, affichée dans
l'interface, pour qu'on sache à tout moment si l'on regarde le
marché réel ou un repli simulé.
"""
from __future__ import annotations

import json
import random
import threading
import time
import urllib.request
from dataclasses import dataclass

GOLD_API_URL = "https://api.gold-api.com/price/XAU"
METALS_LIVE_URL = "https://api.metals.live/v1/spot/gold"
FETCH_TIMEOUT_SEC = 5
CACHE_TTL_SEC = 5  # rafraîchissement quasi temps réel côté cours
FALLBACK_STARTING_PRICE = 2400.0
FALLBACK_DAILY_VOL = 0.01  # volatilité journalière approximative de l'or

_lock = threading.Lock()
_last_price: float = FALLBACK_STARTING_PRICE
_last_source: str = "simule"
_last_provider: str = ""
_last_fetch_at: float = 0.0


@dataclass
class PriceQuote:
    price: float
    source: str  # "live" ou "simule"
    provider: str  # "gold-api", "metals-live" ou "" (simulé)
    timestamp: float


def _extract_price(value) -> float | None:
    try:
        price = float(value)
        return price if price > 0 else None
    except (TypeError, ValueError):
        return None


def _fetch_gold_api() -> float | None:
    try:
        req = urllib.request.Request(GOLD_API_URL, headers={"User-Agent": "gems-of-rod-trading-app/1.0"})
        with urllib.request.urlopen(req, timeout=FETCH_TIMEOUT_SEC) as resp:
            payload = json.loads(resp.read().decode("utf-8"))
        return _extract_price(payload.get("price"))
    except Exception:
        return None


def _fetch_metals_live() -> float | None:
    """Repli si gold-api.com est injoignable. Le schéma de réponse de
    metals.live a varié dans le temps : on essaie plusieurs formes
    connues et on abandonne proprement (None) si aucune ne correspond,
    plutôt que de risquer un prix erroné."""
    try:
        req = urllib.request.Request(METALS_LIVE_URL, headers={"User-Agent": "gems-of-rod-trading-app/1.0"})
        with urllib.request.urlopen(req, timeout=FETCH_TIMEOUT_SEC) as resp:
            payload = json.loads(resp.read().decode("utf-8"))

        if isinstance(payload, list) and payload:
            first = payload[0]
            if isinstance(first, dict) and "gold" in first:
                return _extract_price(first["gold"])
            if isinstance(first, (list, tuple)) and len(first) >= 2:
                return _extract_price(first[1])
        if isinstance(payload, dict) and "gold" in payload:
            return _extract_price(payload["gold"])
        return None
    except Exception:
        return None


def _fetch_live_price() -> tuple[float, str] | None:
    price = _fetch_gold_api()
    if price is not None:
        return price, "gold-api"
    price = _fetch_metals_live()
    if price is not None:
        return price, "metals-live"
    return None


def _simulate_step(previous: float) -> float:
    # Pas de marche aléatoire (log-normal) calibré sur une volatilité
    # journalière typique de l'or, ramenée à l'intervalle de rafraîchissement.
    step_vol = FALLBACK_DAILY_VOL / (24 * 60 / 0.5) ** 0.5  # ~ pas de 30s
    shock = random.gauss(0, step_vol)
    return max(1.0, previous * (1 + shock))


def get_quote() -> PriceQuote:
    """Retourne le cours courant, avec cache court pour éviter de
    marteler les API externes à chaque requête du frontend."""
    global _last_price, _last_source, _last_provider, _last_fetch_at
    with _lock:
        now = time.time()
        if now - _last_fetch_at < CACHE_TTL_SEC:
            return PriceQuote(_last_price, _last_source, _last_provider, _last_fetch_at)

        live = _fetch_live_price()
        if live is not None:
            _last_price, _last_provider = live
            _last_source = "live"
        else:
            _last_price = _simulate_step(_last_price)
            _last_source = "simule"
            _last_provider = ""
        _last_fetch_at = now
        return PriceQuote(_last_price, _last_source, _last_provider, _last_fetch_at)


def force_refresh_interval(seconds: float) -> None:
    """Utilisé par le thread de fond pour forcer un rafraîchissement
    plus fréquent que le cache par défaut, si besoin."""
    global CACHE_TTL_SEC
    CACHE_TTL_SEC = seconds
