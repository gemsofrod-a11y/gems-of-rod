"""Connecteur eToro — ENVIRONNEMENT DÉMO UNIQUEMENT.

AVERTISSEMENT IMPORTANT
------------------------
Ce connecteur a été écrit sans accès à la documentation officielle
de l'API publique eToro (https://api-portal.etoro.com/ et
https://builders.etoro.com/ sont bloqués depuis cet environnement de
développement). Ce qui suit est une **meilleure estimation** fondée
sur les informations publiques disponibles au moment de l'écriture
(lancement de l'API publique eToro annoncé en février 2026, URL de
base https://public-api.etoro.com/) :

- Chemins d'URL des instruments/cotations/ordres
- Noms des champs JSON (instrumentId, isBuy, amount…)
- En-tête et schéma d'authentification

TOUT CECI DOIT ÊTRE VÉRIFIÉ ET AJUSTÉ par vous-même face à la vraie
documentation avant utilisation, même en démo. Les constantes
ci-dessous sont volontairement centralisées et surchargeables par
variables d'environnement pour rendre cet ajustement rapide, sans
avoir à modifier le code.

Garde-fou : ce module refuse de fonctionner tant que
ETORO_XAU_INSTRUMENT_ID n'est pas configuré explicitement (voir
config.py / README), pour éviter de deviner un identifiant
d'instrument et de trader involontairement le mauvais actif — même
en démo.
"""
from __future__ import annotations

import json
import urllib.error
import urllib.request
from dataclasses import dataclass

from . import config

TIMEOUT_SEC = 10

# --- Chemins d'API : À VÉRIFIER contre la documentation officielle ---
RATE_PATH_TEMPLATE = "/api/v1/instruments/{instrument_id}/rate"
ORDER_PATH = "/api/v2/orders"


class EtoroError(Exception):
    pass


@dataclass
class EtoroRate:
    price: float
    raw: dict


@dataclass
class EtoroOrderResult:
    order_id: str | None
    fill_price: float | None
    raw: dict


def is_configured() -> bool:
    return bool(config.ETORO_API_KEY and config.ETORO_XAU_INSTRUMENT_ID)


def _require_configured() -> None:
    if not config.ETORO_API_KEY:
        raise EtoroError("ETORO_API_KEY non configurée (voir trading-app/.env)")
    if not config.ETORO_XAU_INSTRUMENT_ID:
        raise EtoroError(
            "ETORO_XAU_INSTRUMENT_ID non configuré : renseignez l'identifiant "
            "de l'instrument Or (XAU/USD) trouvé dans votre espace développeur eToro"
        )


def _headers() -> dict:
    return {
        "Content-Type": "application/json",
        config.ETORO_AUTH_HEADER: f"{config.ETORO_AUTH_SCHEME} {config.ETORO_API_KEY}".strip(),
    }


def _request(method: str, path: str, body: dict | None = None) -> dict:
    url = config.ETORO_BASE_URL.rstrip("/") + path
    data = json.dumps(body).encode("utf-8") if body is not None else None
    req = urllib.request.Request(url, data=data, method=method, headers=_headers())
    try:
        with urllib.request.urlopen(req, timeout=TIMEOUT_SEC) as resp:
            raw = resp.read().decode("utf-8")
            return json.loads(raw) if raw else {}
    except urllib.error.HTTPError as exc:
        detail = exc.read().decode("utf-8", errors="replace")
        raise EtoroError(
            f"eToro a répondu {exc.code} sur {method} {path} : {detail}. "
            "Vérifiez le chemin d'endpoint et le schéma d'authentification "
            "dans etoro_connector.py face à la documentation officielle."
        ) from exc
    except urllib.error.URLError as exc:
        raise EtoroError(f"Impossible de joindre l'API eToro : {exc.reason}") from exc


def get_rate() -> EtoroRate:
    _require_configured()
    path = RATE_PATH_TEMPLATE.format(instrument_id=config.ETORO_XAU_INSTRUMENT_ID)
    payload = _request("GET", path)
    price = payload.get("price") or payload.get("rate") or payload.get("bid")
    if price is None:
        raise EtoroError(
            f"Réponse eToro inattendue pour la cotation : {payload}. "
            "Le nom du champ de prix dans etoro_connector.get_rate() est probablement à ajuster."
        )
    return EtoroRate(price=float(price), raw=payload)


def place_market_order(is_buy: bool, amount_usd: float) -> EtoroOrderResult:
    _require_configured()
    body = {
        "instrumentId": config.ETORO_XAU_INSTRUMENT_ID,
        "isBuy": is_buy,
        "amount": amount_usd,
    }
    payload = _request("POST", ORDER_PATH, body)
    order_id = payload.get("orderId") or payload.get("id")
    fill_price = payload.get("executionPrice") or payload.get("price")
    return EtoroOrderResult(
        order_id=str(order_id) if order_id is not None else None,
        fill_price=float(fill_price) if fill_price is not None else None,
        raw=payload,
    )
