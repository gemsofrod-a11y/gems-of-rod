import sqlite3
import uuid
from contextlib import contextmanager
from datetime import datetime, timezone

from app import config

SCHEMA = """
CREATE TABLE IF NOT EXISTS processed_messages (
    message_id TEXT PRIMARY KEY,
    category TEXT NOT NULL,
    processed_at TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS pending_actions (
    id TEXT PRIMARY KEY,
    message_id TEXT NOT NULL,
    thread_id TEXT,
    from_addr TEXT,
    subject TEXT,
    snippet TEXT,
    category TEXT NOT NULL,
    reasoning TEXT,
    suggested_reply TEXT,
    status TEXT NOT NULL DEFAULT 'pending',
    created_at TEXT NOT NULL,
    resolved_at TEXT
);

CREATE TABLE IF NOT EXISTS action_log (
    id TEXT PRIMARY KEY,
    message_id TEXT,
    action_type TEXT NOT NULL,
    detail TEXT,
    created_at TEXT NOT NULL
);
"""


def _now() -> str:
    return datetime.now(timezone.utc).isoformat()


@contextmanager
def get_conn():
    config.ensure_dirs()
    conn = sqlite3.connect(config.DB_PATH)
    conn.row_factory = sqlite3.Row
    try:
        yield conn
        conn.commit()
    finally:
        conn.close()


def init_db() -> None:
    with get_conn() as conn:
        conn.executescript(SCHEMA)


def is_processed(message_id: str) -> bool:
    with get_conn() as conn:
        row = conn.execute(
            "SELECT 1 FROM processed_messages WHERE message_id = ?", (message_id,)
        ).fetchone()
        return row is not None


def mark_processed(message_id: str, category: str) -> None:
    with get_conn() as conn:
        conn.execute(
            "INSERT OR REPLACE INTO processed_messages (message_id, category, processed_at) "
            "VALUES (?, ?, ?)",
            (message_id, category, _now()),
        )


def log_action(message_id: str | None, action_type: str, detail: str) -> None:
    with get_conn() as conn:
        conn.execute(
            "INSERT INTO action_log (id, message_id, action_type, detail, created_at) "
            "VALUES (?, ?, ?, ?, ?)",
            (uuid.uuid4().hex, message_id, action_type, detail, _now()),
        )


def create_pending(
    message_id: str,
    thread_id: str | None,
    from_addr: str,
    subject: str,
    snippet: str,
    category: str,
    reasoning: str,
    suggested_reply: str | None,
) -> str:
    pending_id = uuid.uuid4().hex[:10]
    with get_conn() as conn:
        conn.execute(
            "INSERT INTO pending_actions "
            "(id, message_id, thread_id, from_addr, subject, snippet, category, reasoning, "
            "suggested_reply, status, created_at) "
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'pending', ?)",
            (
                pending_id,
                message_id,
                thread_id,
                from_addr,
                subject,
                snippet,
                category,
                reasoning,
                suggested_reply,
                _now(),
            ),
        )
    return pending_id


def list_pending(status: str = "pending") -> list[dict]:
    with get_conn() as conn:
        rows = conn.execute(
            "SELECT * FROM pending_actions WHERE status = ? ORDER BY created_at ASC",
            (status,),
        ).fetchall()
        return [dict(r) for r in rows]


def get_pending(pending_id: str) -> dict | None:
    with get_conn() as conn:
        row = conn.execute(
            "SELECT * FROM pending_actions WHERE id = ?", (pending_id,)
        ).fetchone()
        return dict(row) if row else None


def resolve_pending(pending_id: str, status: str) -> None:
    with get_conn() as conn:
        conn.execute(
            "UPDATE pending_actions SET status = ?, resolved_at = ? WHERE id = ?",
            (status, _now(), pending_id),
        )


def count_today_actions() -> dict:
    today = datetime.now(timezone.utc).date().isoformat()
    with get_conn() as conn:
        rows = conn.execute(
            "SELECT action_type, COUNT(*) as n FROM action_log "
            "WHERE created_at LIKE ? GROUP BY action_type",
            (f"{today}%",),
        ).fetchall()
        pending_count = conn.execute(
            "SELECT COUNT(*) as n FROM pending_actions WHERE status = 'pending'"
        ).fetchone()["n"]
    return {"actions": {r["action_type"]: r["n"] for r in rows}, "pending": pending_count}
