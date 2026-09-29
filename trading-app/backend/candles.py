"""Bougies (OHLC) pour XAU/USD, de la minute à la journée.

Trois niveaux de repli, du plus fidèle au plus dégradé :
1. Historique réel via l'API publique (sans clé) de Yahoo Finance —
   couvre toutes les échéances demandées, y compris avant le
   lancement de ce serveur.
2. À défaut, agrégation des cours réels déjà collectés localement
   (table price_history) — moins profond dans le temps, mais réel.
3. À défaut (aucun réseau, aucun historique local), une série
   simulée, clairement étiquetée comme telle.
"""
from __future__ import annotations

import json
import threading
import time
import urllib.request

from . import store
from .backtest import _generate_series
from .price_feed import get_quote

YAHOO_SYMBOL = "XAUUSD=X"
YAHOO_URL_TEMPLATE = (
    "https://query1.finance.yahoo.com/v8/finance/chart/{symbol}?interval={interval}&range={range_}"
)
FETCH_TIMEOUT_SEC = 8
CACHE_TTL_SEC = 30

# timeframe -> (secondes par bougie, source Yahoo utilisée, plage Yahoo, facteur d'agrégation depuis la source Yahoo)
TIMEFRAMES: dict[str, dict] = {
    "1m": {"seconds": 60, "yahoo_interval": "1m", "yahoo_range": "1d", "agg": 1},
    "5m": {"seconds": 300, "yahoo_interval": "5m", "yahoo_range": "5d", "agg": 1},
    "15m": {"seconds": 900, "yahoo_interval": "15m", "yahoo_range": "5d", "agg": 1},
    "1h": {"seconds": 3600, "yahoo_interval": "60m", "yahoo_range": "1mo", "agg": 1},
    "4h": {"seconds": 14400, "yahoo_interval": "60m", "yahoo_range": "3mo", "agg": 4},
    "1d": {"seconds": 86400, "yahoo_interval": "1d", "yahoo_range": "6mo", "agg": 1},
}

_lock = threading.Lock()
_cache: dict[str, tuple[float, dict]] = {}  # timeframe -> (fetched_at, result)


def _fetch_yahoo(timeframe: str) -> list[dict] | None:
    cfg = TIMEFRAMES[timeframe]
    url = YAHOO_URL_TEMPLATE.format(symbol=YAHOO_SYMBOL, interval=cfg["yahoo_interval"], range_=cfg["yahoo_range"])
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0 (gems-of-rod-trading-app)"})
        with urllib.request.urlopen(req, timeout=FETCH_TIMEOUT_SEC) as resp:
            payload = json.loads(resp.read().decode("utf-8"))
        result = payload["chart"]["result"][0]
        timestamps = result["timestamp"]
        quote = result["indicators"]["quote"][0]
        raw = []
        for i, ts in enumerate(timestamps):
            o, h, l, c = quote["open"][i], quote["high"][i], quote["low"][i], quote["close"][i]
            if None in (o, h, l, c):
                continue
            raw.append({"t": int(ts), "o": float(o), "h": float(h), "l": float(l), "c": float(c)})
        if not raw:
            return None
        return _aggregate(raw, cfg["agg"])
    except Exception:
        return None


def _aggregate(candles: list[dict], factor: int) -> list[dict]:
    if factor <= 1:
        return candles
    out = []
    for i in range(0, len(candles), factor):
        chunk = candles[i:i + factor]
        if not chunk:
            continue
        out.append({
            "t": chunk[0]["t"],
            "o": chunk[0]["o"],
            "h": max(c["h"] for c in chunk),
            "l": min(c["l"] for c in chunk),
            "c": chunk[-1]["c"],
        })
    return out


def _aggregate_from_history(timeframe: str, limit: int) -> list[dict] | None:
    seconds = TIMEFRAMES[timeframe]["seconds"]
    rows = store.list_price_history(limit=5000)
    if len(rows) < 2:
        return None
    buckets: dict[int, dict] = {}
    for row in rows:
        bucket_ts = int(row["created_at"] // seconds) * seconds
        price = row["price"]
        b = buckets.get(bucket_ts)
        if b is None:
            buckets[bucket_ts] = {"t": bucket_ts, "o": price, "h": price, "l": price, "c": price}
        else:
            b["h"] = max(b["h"], price)
            b["l"] = min(b["l"], price)
            b["c"] = price
    candles = [buckets[k] for k in sorted(buckets)]
    return candles[-limit:] if candles else None


def _synthetic_candles(timeframe: str, limit: int) -> list[dict]:
    seconds = TIMEFRAMES[timeframe]["seconds"]
    start_price = get_quote().price
    ticks = _generate_series(limit, seed=None, start_price=start_price)
    now = int(time.time())
    candles = []
    for i, price in enumerate(ticks):
        t = now - (limit - i) * seconds
        prev = ticks[i - 1] if i > 0 else price
        candles.append({"t": t, "o": prev, "h": max(prev, price), "l": min(prev, price), "c": price})
    return candles


def get_candles(timeframe: str, limit: int = 200) -> dict:
    if timeframe not in TIMEFRAMES:
        raise ValueError(f"Échéance inconnue : {timeframe}. Options : {list(TIMEFRAMES)}")

    with _lock:
        cached = _cache.get(timeframe)
        if cached and time.time() - cached[0] < CACHE_TTL_SEC:
            return cached[1]

    yahoo = _fetch_yahoo(timeframe)
    if yahoo:
        result = {"timeframe": timeframe, "provider": "yahoo-finance", "candles": yahoo[-limit:]}
    else:
        local = _aggregate_from_history(timeframe, limit)
        if local and len(local) >= 5:
            result = {"timeframe": timeframe, "provider": "historique-local", "candles": local}
        else:
            result = {"timeframe": timeframe, "provider": "simule", "candles": _synthetic_candles(timeframe, limit)}

    with _lock:
        _cache[timeframe] = (time.time(), result)
    return result
