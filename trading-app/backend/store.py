"""Couche de persistance SQLite : comptes, transactions, historique de
cours et état du bot. Fait office de "grand livre" du broker interne."""
from __future__ import annotations

import json
import sqlite3
import threading
import time
from pathlib import Path

DB_PATH = Path(__file__).resolve().parent.parent / "data" / "trading.db"
DEFAULT_STARTING_BALANCE = 10_000.0

_lock = threading.Lock()


def _connect() -> sqlite3.Connection:
    DB_PATH.parent.mkdir(parents=True, exist_ok=True)
    conn = sqlite3.connect(DB_PATH, check_same_thread=False)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON")
    return conn


_conn = _connect()


def init_db() -> None:
    with _lock, _conn:
        _conn.executescript(
            """
            CREATE TABLE IF NOT EXISTS accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT UNIQUE NOT NULL,
                mode TEXT NOT NULL CHECK (mode IN ('reel', 'demo')),
                cash_balance REAL NOT NULL,
                position_oz REAL NOT NULL DEFAULT 0,
                created_at REAL NOT NULL,
                external_broker TEXT NOT NULL DEFAULT 'none'
            );

            CREATE TABLE IF NOT EXISTS trades (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                account_id INTEGER NOT NULL REFERENCES accounts(id),
                side TEXT NOT NULL CHECK (side IN ('buy', 'sell')),
                qty_oz REAL NOT NULL,
                price REAL NOT NULL,
                amount REAL NOT NULL,
                source TEXT NOT NULL CHECK (source IN ('manuel', 'bot')),
                strategy TEXT,
                created_at REAL NOT NULL,
                external_order_id TEXT,
                external_broker TEXT
            );

            CREATE TABLE IF NOT EXISTS equity_snapshots (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                account_id INTEGER NOT NULL REFERENCES accounts(id),
                equity REAL NOT NULL,
                price REAL NOT NULL,
                created_at REAL NOT NULL
            );

            CREATE TABLE IF NOT EXISTS price_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                price REAL NOT NULL,
                source TEXT NOT NULL,
                created_at REAL NOT NULL
            );

            CREATE TABLE IF NOT EXISTS bot_state (
                account_id INTEGER PRIMARY KEY REFERENCES accounts(id),
                running INTEGER NOT NULL DEFAULT 0,
                strategy TEXT,
                params TEXT,
                interval_sec INTEGER,
                risk_pct REAL,
                last_signal TEXT,
                last_run_at REAL,
                last_error TEXT
            );
            """
        )
        _ensure_default_accounts()


def _ensure_default_accounts() -> None:
    existing = _conn.execute("SELECT COUNT(*) AS n FROM accounts").fetchone()["n"]
    if existing:
        return
    now = time.time()
    _conn.execute(
        "INSERT INTO accounts (name, mode, cash_balance, position_oz, created_at) VALUES (?,?,?,?,?)",
        ("Compte Réel", "reel", DEFAULT_STARTING_BALANCE, 0.0, now),
    )
    _conn.execute(
        "INSERT INTO accounts (name, mode, cash_balance, position_oz, created_at) VALUES (?,?,?,?,?)",
        ("Compte Démo", "demo", DEFAULT_STARTING_BALANCE, 0.0, now),
    )


def list_accounts() -> list[sqlite3.Row]:
    with _lock:
        return _conn.execute("SELECT * FROM accounts ORDER BY id").fetchall()


def get_account(account_id: int) -> sqlite3.Row | None:
    with _lock:
        return _conn.execute("SELECT * FROM accounts WHERE id = ?", (account_id,)).fetchone()


def update_account_after_trade(account_id: int, new_cash: float, new_position: float) -> None:
    with _lock, _conn:
        _conn.execute(
            "UPDATE accounts SET cash_balance = ?, position_oz = ? WHERE id = ?",
            (new_cash, new_position, account_id),
        )


def set_external_broker(account_id: int, broker_name: str) -> None:
    if broker_name not in ("none", "oanda", "etoro"):
        raise ValueError("broker_name doit être 'none', 'oanda' ou 'etoro'")
    with _lock, _conn:
        _conn.execute(
            "UPDATE accounts SET external_broker = ? WHERE id = ?",
            (broker_name, account_id),
        )


def insert_trade(account_id: int, side: str, qty_oz: float, price: float, amount: float,
                  source: str, strategy: str | None, external_order_id: str | None = None,
                  external_broker: str | None = None) -> int:
    with _lock, _conn:
        cur = _conn.execute(
            """INSERT INTO trades (account_id, side, qty_oz, price, amount, source, strategy,
                                    created_at, external_order_id, external_broker)
               VALUES (?,?,?,?,?,?,?,?,?,?)""",
            (account_id, side, qty_oz, price, amount, source, strategy, time.time(),
             external_order_id, external_broker),
        )
        return cur.lastrowid


def list_trades(account_id: int, limit: int = 100) -> list[sqlite3.Row]:
    with _lock:
        return _conn.execute(
            "SELECT * FROM trades WHERE account_id = ? ORDER BY id DESC LIMIT ?",
            (account_id, limit),
        ).fetchall()


def insert_equity_snapshot(account_id: int, equity: float, price: float) -> None:
    with _lock, _conn:
        _conn.execute(
            "INSERT INTO equity_snapshots (account_id, equity, price, created_at) VALUES (?,?,?,?)",
            (account_id, equity, price, time.time()),
        )


def list_equity_history(account_id: int, limit: int = 500) -> list[sqlite3.Row]:
    with _lock:
        rows = _conn.execute(
            "SELECT * FROM equity_snapshots WHERE account_id = ? ORDER BY id DESC LIMIT ?",
            (account_id, limit),
        ).fetchall()
        return list(reversed(rows))


def insert_price_point(price: float, source: str) -> None:
    with _lock, _conn:
        _conn.execute(
            "INSERT INTO price_history (price, source, created_at) VALUES (?,?,?)",
            (price, source, time.time()),
        )


def list_price_history(limit: int = 500) -> list[sqlite3.Row]:
    with _lock:
        rows = _conn.execute(
            "SELECT * FROM price_history ORDER BY id DESC LIMIT ?", (limit,)
        ).fetchall()
        return list(reversed(rows))


def get_bot_state(account_id: int) -> sqlite3.Row | None:
    with _lock:
        return _conn.execute("SELECT * FROM bot_state WHERE account_id = ?", (account_id,)).fetchone()


def list_bot_states() -> list[sqlite3.Row]:
    with _lock:
        return _conn.execute("SELECT * FROM bot_state").fetchall()


def upsert_bot_state(account_id: int, running: bool, strategy: str | None, params: dict | None,
                      interval_sec: int | None, risk_pct: float | None,
                      last_signal: str | None = None, last_error: str | None = None) -> None:
    with _lock, _conn:
        _conn.execute(
            """INSERT INTO bot_state (account_id, running, strategy, params, interval_sec, risk_pct,
                                       last_signal, last_run_at, last_error)
               VALUES (?,?,?,?,?,?,?,?,?)
               ON CONFLICT(account_id) DO UPDATE SET
                 running=excluded.running,
                 strategy=excluded.strategy,
                 params=excluded.params,
                 interval_sec=excluded.interval_sec,
                 risk_pct=excluded.risk_pct,
                 last_signal=COALESCE(excluded.last_signal, bot_state.last_signal),
                 last_run_at=excluded.last_run_at,
                 last_error=excluded.last_error
            """,
            (account_id, int(running), strategy, json.dumps(params) if params else None,
             interval_sec, risk_pct, last_signal, time.time(), last_error),
        )
