"""Serveur HTTP (bibliothèque standard uniquement, aucune dépendance
à installer) : sert le frontend statique et expose l'API JSON du
broker interne, des comptes, du bot et du backtester.

Lancement : python3 -m backend.server  (depuis le dossier trading-app/)
"""
from __future__ import annotations

import json
import re
import threading
import time
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import urlparse

from . import bot_engine, store
from .backtest import run_backtest
from .broker import OrderError, account_summary
from .execution import brokers_status, place_order
from .price_feed import get_quote

FRONTEND_DIR = Path(__file__).resolve().parent.parent / "frontend"
PRICE_POLL_INTERVAL_SEC = 10

ROUTES: list[tuple[str, re.Pattern]] = []


def _price_poller() -> None:
    while True:
        quote = get_quote()
        store.insert_price_point(quote.price, quote.source)
        time.sleep(PRICE_POLL_INTERVAL_SEC)


class Handler(BaseHTTPRequestHandler):
    server_version = "GemsOfRodTradingApp/1.0"

    def log_message(self, fmt, *args):  # moins verbeux que le défaut
        pass

    def _send_json(self, payload, status: int = 200) -> None:
        body = json.dumps(payload, ensure_ascii=False).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def _read_json_body(self) -> dict:
        length = int(self.headers.get("Content-Length", 0))
        if length == 0:
            return {}
        raw = self.rfile.read(length)
        return json.loads(raw.decode("utf-8")) if raw else {}

    def _serve_static(self, path: str) -> None:
        if path == "/":
            path = "/index.html"
        file_path = (FRONTEND_DIR / path.lstrip("/")).resolve()
        if FRONTEND_DIR not in file_path.parents and file_path != FRONTEND_DIR:
            self.send_error(403)
            return
        if not file_path.is_file():
            self.send_error(404)
            return
        content_type = {
            ".html": "text/html; charset=utf-8",
            ".js": "application/javascript; charset=utf-8",
            ".css": "text/css; charset=utf-8",
            ".json": "application/manifest+json; charset=utf-8",
            ".png": "image/png",
            ".svg": "image/svg+xml",
        }.get(file_path.suffix, "application/octet-stream")
        body = file_path.read_bytes()
        self.send_response(200)
        self.send_header("Content-Type", content_type)
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def do_GET(self):  # noqa: N802
        parsed = urlparse(self.path)
        path = parsed.path
        try:
            if path == "/api/price":
                return self._get_price()
            if path == "/api/price/history":
                return self._get_price_history()
            if path == "/api/accounts":
                return self._get_accounts()
            m = re.fullmatch(r"/api/accounts/(\d+)/trades", path)
            if m:
                return self._get_trades(int(m.group(1)))
            m = re.fullmatch(r"/api/accounts/(\d+)/equity_history", path)
            if m:
                return self._get_equity_history(int(m.group(1)))
            if path == "/api/bot/status":
                return self._get_bot_status()
            if path == "/api/brokers/status":
                return self._get_brokers_status()
            return self._serve_static(path)
        except Exception as exc:
            self._send_json({"error": str(exc)}, 500)

    def do_POST(self):  # noqa: N802
        parsed = urlparse(self.path)
        path = parsed.path
        try:
            m = re.fullmatch(r"/api/accounts/(\d+)/order", path)
            if m:
                return self._post_order(int(m.group(1)))
            m = re.fullmatch(r"/api/accounts/(\d+)/broker", path)
            if m:
                return self._post_account_broker(int(m.group(1)))
            if path == "/api/bot/start":
                return self._post_bot_start()
            if path == "/api/bot/stop":
                return self._post_bot_stop()
            if path == "/api/backtest":
                return self._post_backtest()
            self._send_json({"error": "route inconnue"}, 404)
        except OrderError as exc:
            self._send_json({"error": str(exc)}, 400)
        except Exception as exc:
            self._send_json({"error": str(exc)}, 500)

    # --- handlers ---

    def _get_price(self):
        quote = get_quote()
        self._send_json({"price": quote.price, "source": quote.source, "timestamp": quote.timestamp})

    def _get_price_history(self):
        rows = store.list_price_history()
        self._send_json([dict(r) for r in rows])

    def _get_accounts(self):
        accounts = store.list_accounts()
        self._send_json([account_summary(a["id"]) for a in accounts])

    def _get_trades(self, account_id: int):
        rows = store.list_trades(account_id)
        self._send_json([dict(r) for r in rows])

    def _get_equity_history(self, account_id: int):
        rows = store.list_equity_history(account_id)
        self._send_json([dict(r) for r in rows])

    def _get_bot_status(self):
        self._send_json(bot_engine.all_bot_statuses())

    def _get_brokers_status(self):
        self._send_json(brokers_status())

    def _post_order(self, account_id: int):
        body = self._read_json_body()
        result = place_order(
            account_id,
            side=body.get("side"),
            qty_oz=body.get("qty_oz"),
            amount=body.get("amount"),
            source="manuel",
        )
        self._send_json(result.__dict__)

    def _post_account_broker(self, account_id: int):
        body = self._read_json_body()
        store.set_external_broker(account_id, body.get("broker", "none"))
        self._send_json(account_summary(account_id))

    def _post_bot_start(self):
        body = self._read_json_body()
        account_id = int(body["account_id"])
        bot_engine.start_bot(
            account_id,
            strategy_name=body["strategy"],
            params=body.get("params", {}),
            interval_sec=int(body.get("interval_sec", 10)),
            risk_pct=float(body.get("risk_pct", 25)),
        )
        self._send_json({"status": "started", "account_id": account_id})

    def _post_bot_stop(self):
        body = self._read_json_body()
        account_id = int(body["account_id"])
        stopped = bot_engine.stop_bot(account_id)
        self._send_json({"status": "stopped" if stopped else "not_running", "account_id": account_id})

    def _post_backtest(self):
        body = self._read_json_body()
        result = run_backtest(
            strategy_name=body["strategy"],
            params=body.get("params", {}),
            days=int(body.get("days", 180)),
            seed=body.get("seed"),
        )
        self._send_json(result)


def main(host: str = "0.0.0.0", port: int = 8420) -> None:
    store.init_db()
    threading.Thread(target=_price_poller, daemon=True).start()
    httpd = ThreadingHTTPServer((host, port), Handler)
    print(f"Gems of Rod — Trading Or (XAU) sur http://{host}:{port}")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass


if __name__ == "__main__":
    main()
