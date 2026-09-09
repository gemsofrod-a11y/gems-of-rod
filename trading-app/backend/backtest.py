"""Backtester : rejoue une stratégie sur une série de cours et calcule
des métriques de performance.

Faute d'historique réel gratuit et fiable sans clé d'API, la série
utilisée est générée par une marche aléatoire log-normale calibrée
sur la volatilité journalière typique de l'or, ancrée sur le dernier
cours connu (réel si disponible). C'est clairement annoncé au
frontend : il s'agit d'un backtest sur données simulées, pas d'un
historique de marché réel.
"""
from __future__ import annotations

import random

from .price_feed import get_quote
from .strategies import build_strategy

DAILY_VOL = 0.01
STARTING_CAPITAL = 10_000.0


def _generate_series(days: int, seed: int | None, start_price: float) -> list[float]:
    rng = random.Random(seed)
    prices = [start_price]
    for _ in range(days - 1):
        shock = rng.gauss(0, DAILY_VOL)
        prices.append(max(1.0, prices[-1] * (1 + shock)))
    return prices


def run_backtest(strategy_name: str, params: dict, days: int = 180,
                  seed: int | None = None) -> dict:
    days = max(30, min(days, 2000))
    start_price = get_quote().price
    prices = _generate_series(days, seed, start_price)
    strategy = build_strategy(strategy_name, params)

    cash = STARTING_CAPITAL
    position = 0.0
    equity_curve = []
    trades = []
    peak_equity = STARTING_CAPITAL
    max_drawdown = 0.0

    for i in range(1, len(prices) + 1):
        window = prices[:i]
        price = window[-1]
        signal = strategy.signal(window)
        if signal == "buy" and position <= 1e-9:
            qty = (cash * 0.95) / price
            cash -= qty * price
            position += qty
            trades.append({"day": i, "side": "buy", "price": price, "qty": qty})
        elif signal == "sell" and position > 1e-9:
            cash += position * price
            trades.append({"day": i, "side": "sell", "price": price, "qty": position})
            position = 0.0

        equity = cash + position * price
        peak_equity = max(peak_equity, equity)
        drawdown = (peak_equity - equity) / peak_equity if peak_equity else 0
        max_drawdown = max(max_drawdown, drawdown)
        equity_curve.append({"day": i, "equity": equity, "price": price})

    final_equity = equity_curve[-1]["equity"] if equity_curve else STARTING_CAPITAL
    total_return_pct = (final_equity - STARTING_CAPITAL) / STARTING_CAPITAL * 100

    round_trip_returns = []
    open_trade = None
    for t in trades:
        if t["side"] == "buy":
            open_trade = t
        elif t["side"] == "sell" and open_trade is not None:
            round_trip_returns.append((t["price"] - open_trade["price"]) / open_trade["price"])
            open_trade = None
    wins = sum(1 for r in round_trip_returns if r > 0)
    win_rate = (wins / len(round_trip_returns) * 100) if round_trip_returns else None

    return {
        "strategy": strategy_name,
        "params": params,
        "days": days,
        "starting_capital": STARTING_CAPITAL,
        "final_equity": final_equity,
        "total_return_pct": total_return_pct,
        "max_drawdown_pct": max_drawdown * 100,
        "num_trades": len(trades),
        "num_round_trips": len(round_trip_returns),
        "win_rate_pct": win_rate,
        "equity_curve": equity_curve,
        "note": "Backtest sur données simulées (marche aléatoire calibrée), pas un historique de marché réel.",
    }
