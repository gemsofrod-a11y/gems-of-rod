"""Alimentation en cours XAU/USD (once d'or).

Tente d'abord une source publique gratuite et sans clé d'API
(gold-api.com). En cas d'échec (réseau coupé, source indisponible),
bascule sur une marche aléatoire simulée, ancrée sur le dernier
cours connu, afin que l'application reste utilisable hors-ligne
pour la démonstration et les tests du bot.
"""
from __future__ import annotations

import json
import random
import threading
import time
import urllib.request
from dataclasses import dataclass

LIVE_URL = "https://api.gold-api.com/price/XAU"
FETCH_TIMEOUT_SEC = 5
CACHE_TTL_SEC = 15
FALLBACK_STARTING_PRICE = 2400.0
FALLBACK_DAILY_VOL = 0.01  # volatilité journalière approximative de l'or

_lock = threading.Lock()
_last_price: float = FALLBACK_STARTING_PRICE
_last_source: str = "simule"
_last_fetch_at: float = 0.0


@dataclass
class PriceQuote:
    price: float
    source: str  # "live" ou "simule"
    timestamp: float


def _fetch_live_price() -> float | None:
    try:
        req = urllib.request.Request(LIVE_URL, headers={"User-Agent": "gems-of-rod-trading-app/1.0"})
        with urllib.request.urlopen(req, timeout=FETCH_TIMEOUT_SEC) as resp:
            payload = json.loads(resp.read().decode("utf-8"))
        price = float(payload["price"])
        if price <= 0:
            return None
        return price
    except Exception:
        return None


def _simulate_step(previous: float) -> float:
    # Pas de marche aléatoire (log-normal) calibré sur une volatilité
    # journalière typique de l'or, ramenée à l'intervalle de rafraîchissement.
    step_vol = FALLBACK_DAILY_VOL / (24 * 60 / 0.5) ** 0.5  # ~ pas de 30s
    shock = random.gauss(0, step_vol)
    return max(1.0, previous * (1 + shock))


def get_quote() -> PriceQuote:
    """Retourne le cours courant, avec cache court pour éviter de
    marteler l'API externe à chaque requête du frontend."""
    global _last_price, _last_source, _last_fetch_at
    with _lock:
        now = time.time()
        if now - _last_fetch_at < CACHE_TTL_SEC:
            return PriceQuote(_last_price, _last_source, _last_fetch_at)

        live_price = _fetch_live_price()
        if live_price is not None:
            _last_price = live_price
            _last_source = "live"
        else:
            _last_price = _simulate_step(_last_price)
            _last_source = "simule"
        _last_fetch_at = now
        return PriceQuote(_last_price, _last_source, _last_fetch_at)


def force_refresh_interval(seconds: float) -> None:
    """Utilisé par le thread de fond pour forcer un rafraîchissement
    plus fréquent que le cache par défaut, si besoin."""
    global CACHE_TTL_SEC
    CACHE_TTL_SEC = seconds
