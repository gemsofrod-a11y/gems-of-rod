import json
import os
import socket
from pathlib import Path

from dotenv import load_dotenv

# Un appel réseau (Gmail, Anthropic) sans timeout explicite peut rester bloqué
# indéfiniment sur un aléa réseau — observé en production : un déploiement
# Render a mis 15 minutes à démarrer puis a expiré, sans la moindre erreur ni
# ligne de log, signe d'un blocage bas niveau plutôt que d'un plantage. Cette
# limite globale garantit qu'aucun appel bloquant du process ne peut plus
# jamais dépasser 30 secondes.
socket.setdefaulttimeout(30)

BASE_DIR = Path(__file__).parent.parent
REPO_ROOT = BASE_DIR.parent

load_dotenv(BASE_DIR / ".env")
DATA_DIR = BASE_DIR / "var"
DB_PATH = DATA_DIR / "assistant.db"
CREDENTIALS_PATH = BASE_DIR / "credentials.json"
TOKEN_PATH = BASE_DIR / "token.json"
LOG_PATH = REPO_ROOT / "data" / "logs" / "voice_assistant.log"

GMAIL_SCOPES = ["https://www.googleapis.com/auth/gmail.modify"]


def _materialize_secret(env_var: str, path: Path) -> None:
    """Sur un hébergeur cloud sans dépôt de fichiers (Render, Railway, ...),
    credentials.json et token.json peuvent être fournis en variables
    d'environnement (contenu JSON brut) : on les écrit une fois sur le
    disque éphémère au démarrage, aux emplacements attendus par
    google-auth. Sans effet si le fichier existe déjà (dépôt local normal).
    """
    content = os.getenv(env_var)
    if content and not path.exists():
        path.write_text(content, encoding="utf-8")


_materialize_secret("GOOGLE_CREDENTIALS_JSON", CREDENTIALS_PATH)
_materialize_secret("GOOGLE_TOKEN_JSON", TOKEN_PATH)


def _bool_env(name: str, default: bool) -> bool:
    val = os.getenv(name)
    if val is None:
        return default
    return val.strip().lower() not in ("0", "false", "no", "")


ANTHROPIC_API_KEY = os.getenv("ANTHROPIC_API_KEY", "")
ASSISTANT_MODEL = os.getenv("ASSISTANT_MODEL", "claude-opus-4-8")
ASSISTANT_API_TOKEN = os.getenv("ASSISTANT_API_TOKEN", "")
USER_EMAIL = os.getenv("USER_EMAIL", "gemsofrod@gmail.com")
TRIAGE_INTERVAL_SECONDS = int(os.getenv("TRIAGE_INTERVAL_SECONDS", "300"))
TRIAGE_QUERY = os.getenv("TRIAGE_QUERY", "in:inbox is:unread")
TRIAGE_MAX_RESULTS = int(os.getenv("TRIAGE_MAX_RESULTS", "20"))
# Si vrai (par défaut), l'API FastAPI lance elle-même le cycle de triage en
# tâche de fond (thread) : un seul process à déployer, pas besoin de
# scripts/run_triage_loop.py séparé. À mettre à "false" pour désactiver ce
# comportement si le triage tourne déjà ailleurs comme process indépendant.
TRIAGE_AUTOSTART = _bool_env("TRIAGE_AUTOSTART", True)

LABEL_AUTO_TRAITE = "GoR-Traité auto"
LABEL_AUTO_REPONDU = "GoR-Répondu auto"
LABEL_EN_ATTENTE = "GoR-En attente"


def ensure_dirs() -> None:
    DATA_DIR.mkdir(parents=True, exist_ok=True)
    LOG_PATH.parent.mkdir(parents=True, exist_ok=True)


def get_company_info() -> dict:
    path = REPO_ROOT / "agent" / "knowledge" / "company.json"
    if path.exists():
        return json.loads(path.read_text(encoding="utf-8"))
    return {}
