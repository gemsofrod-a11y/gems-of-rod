"""Point d'entrée unique pour passer un ordre : route vers le broker
interne (par défaut) ou vers un broker externe (OANDA, eToro) si le
compte est configuré pour l'utiliser.

Le grand livre interne (store.py / broker.py) reste toujours la
source de vérité affichée dans l'application, même quand un broker
externe exécute l'ordre : on enregistre alors le prix d'exécution et
l'identifiant d'ordre réels renvoyés par ce broker.
"""
from __future__ import annotations

from . import broker, config, etoro_connector, oanda_connector, store
from .broker import ExecutionResult, OrderError


def _qty_from_amount(amount: float | None, qty_oz: float | None, price: float) -> float:
    if qty_oz is not None and qty_oz > 0:
        return qty_oz
    if amount is not None and amount > 0:
        return amount / price
    raise OrderError("Préciser une quantité (oz) ou un montant (>0)")


def place_order(account_id: int, side: str, qty_oz: float | None = None,
                 amount: float | None = None, source: str = "manuel",
                 strategy: str | None = None) -> ExecutionResult:
    account = store.get_account(account_id)
    if account is None:
        raise OrderError("Compte introuvable")

    external = account["external_broker"]

    if external == "oanda":
        if not oanda_connector.is_configured():
            raise OrderError("Compte relié à OANDA mais OANDA_API_TOKEN/OANDA_ACCOUNT_ID absents (.env)")
        try:
            rate = oanda_connector.get_rate()
        except oanda_connector.OandaError as exc:
            raise OrderError(str(exc)) from exc
        reference_price = rate.ask if side == "buy" else rate.bid
        qty = _qty_from_amount(amount, qty_oz, reference_price)
        try:
            result = oanda_connector.place_market_order(side == "buy", qty)
        except oanda_connector.OandaError as exc:
            raise OrderError(str(exc)) from exc
        return broker.execute_order(
            account_id, side, qty_oz=qty, source=source, strategy=strategy,
            forced_price=result.fill_price or reference_price,
            external_order_id=result.order_id, external_broker="oanda",
        )

    if external == "etoro":
        if not etoro_connector.is_configured():
            raise OrderError("Compte relié à eToro mais ETORO_API_KEY/ETORO_XAU_INSTRUMENT_ID absents (.env)")
        try:
            rate = etoro_connector.get_rate()
        except etoro_connector.EtoroError as exc:
            raise OrderError(str(exc)) from exc
        qty = _qty_from_amount(amount, qty_oz, rate.price)
        order_amount = qty * rate.price
        try:
            result = etoro_connector.place_market_order(side == "buy", order_amount)
        except etoro_connector.EtoroError as exc:
            raise OrderError(str(exc)) from exc
        return broker.execute_order(
            account_id, side, qty_oz=qty, source=source, strategy=strategy,
            forced_price=result.fill_price or rate.price,
            external_order_id=result.order_id, external_broker="etoro",
        )

    return broker.execute_order(account_id, side, qty_oz=qty_oz, amount=amount,
                                 source=source, strategy=strategy)


def brokers_status() -> dict:
    return {
        "oanda": {"configured": oanda_connector.is_configured(), "env": config.OANDA_ENV},
        "etoro": {"configured": etoro_connector.is_configured()},
    }
