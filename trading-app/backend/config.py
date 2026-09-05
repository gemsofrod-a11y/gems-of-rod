"""Configuration lue depuis l'environnement (variables d'env ou fichier
.env local à trading-app/, chargé manuellement — aucune dépendance
externe requise)."""
from __future__ import annotations

import os
from pathlib import Path

ENV_FILE = Path(__file__).resolve().parent.parent / ".env"


def _load_dotenv(path: Path) -> None:
    if not path.is_file():
        return
    for line in path.read_text().splitlines():
        line = line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, _, value = line.partition("=")
        key = key.strip()
        value = value.strip().strip('"').strip("'")
        os.environ.setdefault(key, value)


_load_dotenv(ENV_FILE)

# --- eToro (connecteur DÉMO uniquement, voir etoro_connector.py) ---
ETORO_API_KEY = os.environ.get("ETORO_API_KEY", "")
ETORO_BASE_URL = os.environ.get("ETORO_BASE_URL", "https://public-api.etoro.com")
ETORO_XAU_INSTRUMENT_ID = os.environ.get("ETORO_XAU_INSTRUMENT_ID", "")
ETORO_AUTH_HEADER = os.environ.get("ETORO_AUTH_HEADER", "Authorization")
ETORO_AUTH_SCHEME = os.environ.get("ETORO_AUTH_SCHEME", "Bearer")

# --- OANDA (broker gratuit, compte practice/démo par défaut) ---
# API v20 stable et documentée publiquement : https://developer.oanda.com/rest-live-v20/introduction/
OANDA_API_TOKEN = os.environ.get("OANDA_API_TOKEN", "")
OANDA_ACCOUNT_ID = os.environ.get("OANDA_ACCOUNT_ID", "")
OANDA_ENV = os.environ.get("OANDA_ENV", "practice")  # "practice" (gratuit) ou "live" (réel, déconseillé)
OANDA_INSTRUMENT = os.environ.get("OANDA_INSTRUMENT", "XAU_USD")
