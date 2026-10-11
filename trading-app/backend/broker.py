"""Broker interne : exécute les ordres au marché contre le cours
courant et tient à jour le solde / la position de chaque compte.

Il n'y a pas d'exchange externe ici : l'application est sa propre
contrepartie, ce qui permet de suivre un compte "réel" (alimenté et
interprété par l'utilisateur comme représentant de l'argent réel)
et un compte "démo" avec exactement la même logique.
"""
from __future__ import annotations

from dataclasses import dataclass

from . import store
from .price_feed import get_quote


class OrderError(Exception):
    pass


@dataclass
class ExecutionResult:
    trade_id: int
    side: str
    qty_oz: float
    price: float
    amount: float
    cash_balance: float
    position_oz: float


def _resolve_qty(price: float, qty_oz: float | None, amount: float | None) -> float:
    if qty_oz is not None and qty_oz > 0:
        return qty_oz
    if amount is not None and amount > 0:
        return amount / price
    raise OrderError("Préciser une quantité (oz) ou un montant (>0)")


def execute_order(account_id: int, side: str, qty_oz: float | None = None,
                   amount: float | None = None, source: str = "manuel",
                   strategy: str | None = None, forced_price: float | None = None,
                   external_order_id: str | None = None,
                   external_broker: str | None = None) -> ExecutionResult:
    if side not in ("buy", "sell"):
        raise OrderError("side doit valoir 'buy' ou 'sell'")

    account = store.get_account(account_id)
    if account is None:
        raise OrderError("Compte introuvable")

    price = forced_price if forced_price is not None else get_quote().price
    qty = _resolve_qty(price, qty_oz, amount)
    cost = qty * price

    cash = account["cash_balance"]
    position = account["position_oz"]

    if side == "buy":
        if cost > cash + 1e-9:
            raise OrderError(
                f"Solde insuffisant : {cash:.2f} disponible, {cost:.2f} requis"
            )
        cash -= cost
        position += qty
    else:
        if qty > position + 1e-9:
            raise OrderError(
                f"Position insuffisante : {position:.4f} oz détenues, {qty:.4f} oz demandées"
            )
        cash += cost
        position -= qty

    trade_id = store.insert_trade(account_id, side, qty, price, cost, source, strategy,
                                   external_order_id, external_broker)
    store.update_account_after_trade(account_id, cash, position)
    equity = cash + position * price
    store.insert_equity_snapshot(account_id, equity, price)

    return ExecutionResult(trade_id, side, qty, price, cost, cash, position)


def account_summary(account_id: int) -> dict:
    account = store.get_account(account_id)
    if account is None:
        raise OrderError("Compte introuvable")
    quote = get_quote()
    equity = account["cash_balance"] + account["position_oz"] * quote.price
    pnl = equity - store.DEFAULT_STARTING_BALANCE
    pnl_pct = (pnl / store.DEFAULT_STARTING_BALANCE) * 100 if store.DEFAULT_STARTING_BALANCE else 0
    return {
        "id": account["id"],
        "name": account["name"],
        "mode": account["mode"],
        "cash_balance": account["cash_balance"],
        "position_oz": account["position_oz"],
        "price": quote.price,
        "equity": equity,
        "pnl": pnl,
        "pnl_pct": pnl_pct,
        "external_broker": account["external_broker"],
    }
