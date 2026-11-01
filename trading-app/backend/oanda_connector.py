"""Connecteur OANDA — broker forex/CFD proposant un compte "practice"
(démo) gratuit avec une vraie API de trading (v20), incluant l'or au
comptant XAU_USD. Contrairement au connecteur eToro (voir
etoro_connector.py, écrit à l'aveugle car sa documentation était
inaccessible), l'API v20 d'OANDA est stable, publique et documentée
de longue date : https://developer.oanda.com/rest-live-v20/introduction/

Compte practice : gratuit, aucune carte bancaire, jeton d'API généré
depuis "Manage API Access" dans le portail de gestion de compte OANDA.

Par sécurité, OANDA_ENV vaut "practice" par défaut. Passer à "live"
enverrait de vrais ordres sur un compte réel OANDA : ce n'est pas
recommandé et l'application ne l'active jamais elle-même.
"""
from __future__ import annotations

import json
import urllib.error
import urllib.request
from dataclasses import dataclass

from . import config

TIMEOUT_SEC = 10

BASE_URLS = {
    "practice": "https://api-fxpractice.oanda.com",
    "live": "https://api-fxtrade.oanda.com",
}


class OandaError(Exception):
    pass


@dataclass
class OandaRate:
    bid: float
    ask: float
    mid: float
    raw: dict


@dataclass
class OandaOrderResult:
    order_id: str | None
    fill_price: float | None
    raw: dict


def is_configured() -> bool:
    return bool(config.OANDA_API_TOKEN and config.OANDA_ACCOUNT_ID)


def _require_configured() -> None:
    if not config.OANDA_API_TOKEN or not config.OANDA_ACCOUNT_ID:
        raise OandaError(
            "OANDA non configuré : renseignez OANDA_API_TOKEN et OANDA_ACCOUNT_ID "
            "dans trading-app/.env (compte practice gratuit, voir README)"
        )
    if config.OANDA_ENV not in BASE_URLS:
        raise OandaError("OANDA_ENV doit valoir 'practice' ou 'live'")


def _base_url() -> str:
    return BASE_URLS[config.OANDA_ENV]


def _headers() -> dict:
    return {
        "Authorization": f"Bearer {config.OANDA_API_TOKEN}",
        "Content-Type": "application/json",
    }


def _request(method: str, path: str, body: dict | None = None) -> dict:
    url = _base_url() + path
    data = json.dumps(body).encode("utf-8") if body is not None else None
    req = urllib.request.Request(url, data=data, method=method, headers=_headers())
    try:
        with urllib.request.urlopen(req, timeout=TIMEOUT_SEC) as resp:
            raw = resp.read().decode("utf-8")
            return json.loads(raw) if raw else {}
    except urllib.error.HTTPError as exc:
        detail = exc.read().decode("utf-8", errors="replace")
        raise OandaError(f"OANDA a répondu {exc.code} sur {method} {path} : {detail}") from exc
    except urllib.error.URLError as exc:
        raise OandaError(f"Impossible de joindre l'API OANDA : {exc.reason}") from exc


def get_rate() -> OandaRate:
    _require_configured()
    path = f"/v3/accounts/{config.OANDA_ACCOUNT_ID}/pricing?instruments={config.OANDA_INSTRUMENT}"
    payload = _request("GET", path)
    prices = payload.get("prices") or []
    if not prices:
        raise OandaError(f"Aucune cotation OANDA renvoyée : {payload}")
    quote = prices[0]
    bid = float(quote["bids"][0]["price"])
    ask = float(quote["asks"][0]["price"])
    return OandaRate(bid=bid, ask=ask, mid=(bid + ask) / 2, raw=payload)


def place_market_order(is_buy: bool, qty_oz: float) -> OandaOrderResult:
    """Place un ordre au marché sur XAU_USD. Pour OANDA, les "units"
    d'un CFD sur métal représentent directement des onces troy pour
    XAU_USD, ce qui correspond à notre modèle interne en oz."""
    _require_configured()
    units = qty_oz if is_buy else -qty_oz
    body = {
        "order": {
            "type": "MARKET",
            "instrument": config.OANDA_INSTRUMENT,
            "units": f"{units:.2f}",
            "positionFill": "DEFAULT",
        }
    }
    payload = _request("POST", f"/v3/accounts/{config.OANDA_ACCOUNT_ID}/orders", body)

    fill = payload.get("orderFillTransaction")
    if fill:
        return OandaOrderResult(order_id=str(fill.get("id")), fill_price=float(fill["price"]), raw=payload)

    cancel = payload.get("orderCancelTransaction")
    if cancel:
        raise OandaError(f"Ordre OANDA annulé : {cancel.get('reason', payload)}")

    raise OandaError(f"Réponse OANDA inattendue, pas de fill confirmé : {payload}")
