import os
import json
from pathlib import Path

BASE_DIR = Path(__file__).parent.parent
AGENT_DIR = BASE_DIR / "agent"
KNOWLEDGE_DIR = AGENT_DIR / "knowledge"
DATA_DIR = BASE_DIR / "data"
PRODUCTS_DIR = DATA_DIR / "products"
DRAFTS_DIR = DATA_DIR / "drafts"
LOGS_DIR = DATA_DIR / "logs"

EMAIL_DIRS = {
    "emails": DRAFTS_DIR / "emails",
    "instagram": DRAFTS_DIR / "instagram",
    "articles": DRAFTS_DIR / "articles",
    "newsletters": DRAFTS_DIR / "newsletters",
}

USER_EMAIL = os.getenv("USER_EMAIL", "gemsofrod@gmail.com")
AGENT_MODEL = "claude-opus-4-8"
THINKING_BUDGET = 8000


def ensure_dirs():
    for path in [PRODUCTS_DIR, LOGS_DIR, *EMAIL_DIRS.values()]:
        path.mkdir(parents=True, exist_ok=True)


def load_json(path: Path) -> dict | list:
    if path.exists():
        return json.loads(path.read_text(encoding="utf-8"))
    return {}


def save_json(path: Path, data: dict | list):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")


def get_company_info() -> dict:
    return load_json(KNOWLEDGE_DIR / "company.json")


def get_products() -> list:
    catalog = load_json(KNOWLEDGE_DIR / "products.json")
    products = list(catalog) if isinstance(catalog, list) else []
    for f in PRODUCTS_DIR.glob("*.json"):
        products.append(load_json(f))
    return products


def get_suppliers() -> list:
    return load_json(KNOWLEDGE_DIR / "suppliers.json")


def get_clients() -> list:
    return load_json(KNOWLEDGE_DIR / "clients.json")
