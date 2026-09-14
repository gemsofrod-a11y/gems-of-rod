import json
import os
from pathlib import Path

from dotenv import load_dotenv

BASE_DIR = Path(__file__).parent.parent
REPO_ROOT = BASE_DIR.parent

load_dotenv(BASE_DIR / ".env")
DATA_DIR = BASE_DIR / "var"
DB_PATH = DATA_DIR / "assistant.db"
CREDENTIALS_PATH = BASE_DIR / "credentials.json"
TOKEN_PATH = BASE_DIR / "token.json"
LOG_PATH = REPO_ROOT / "data" / "logs" / "voice_assistant.log"

GMAIL_SCOPES = ["https://www.googleapis.com/auth/gmail.modify"]

ANTHROPIC_API_KEY = os.getenv("ANTHROPIC_API_KEY", "")
ASSISTANT_MODEL = os.getenv("ASSISTANT_MODEL", "claude-opus-4-8")
ASSISTANT_API_TOKEN = os.getenv("ASSISTANT_API_TOKEN", "")
USER_EMAIL = os.getenv("USER_EMAIL", "gemsofrod@gmail.com")
TRIAGE_INTERVAL_SECONDS = int(os.getenv("TRIAGE_INTERVAL_SECONDS", "300"))
TRIAGE_QUERY = os.getenv("TRIAGE_QUERY", "in:inbox is:unread")
TRIAGE_MAX_RESULTS = int(os.getenv("TRIAGE_MAX_RESULTS", "20"))

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
