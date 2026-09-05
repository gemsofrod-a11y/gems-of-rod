"""Bot de trading automatique : exécute une stratégie en tâche de
fond pour un compte donné, en passant des ordres via le broker
interne au fil du cours du marché.

Règle de position simple et volontairement prudente : le bot est
soit "à plat" (aucune position) soit "investi" à hauteur d'un
pourcentage paramétrable du solde disponible (risk_pct). Il achète
sur signal 'buy' quand il est à plat, et solde intégralement sa
position sur signal 'sell' quand il est investi.
"""
from __future__ import annotations

import threading
import time
from collections import deque

from . import store
from .broker import OrderError
from .execution import place_order
from .price_feed import get_quote
from .strategies import build_strategy

_MAX_HISTORY = 500
_bots: dict[int, "BotRunner"] = {}
_bots_lock = threading.Lock()


class BotRunner:
    def __init__(self, account_id: int, strategy_name: str, params: dict,
                 interval_sec: int, risk_pct: float):
        self.account_id = account_id
        self.strategy_name = strategy_name
        self.params = params
        self.interval_sec = max(2, interval_sec)
        self.risk_pct = min(max(risk_pct, 1), 100)
        self.strategy = build_strategy(strategy_name, params)
        self._history: deque[float] = deque(maxlen=_MAX_HISTORY)
        self._stop = threading.Event()
        self._thread = threading.Thread(target=self._run, daemon=True)

    def start(self) -> None:
        self._thread.start()

    def stop(self) -> None:
        self._stop.set()

    def _run(self) -> None:
        while not self._stop.is_set():
            try:
                quote = get_quote()
                self._history.append(quote.price)
                signal = self.strategy.signal(list(self._history))
                self._act_on_signal(signal)
                store.upsert_bot_state(
                    self.account_id, True, self.strategy_name, self.params,
                    self.interval_sec, self.risk_pct, last_signal=signal,
                )
            except Exception as exc:  # ne jamais tuer le thread sur une erreur ponctuelle
                store.upsert_bot_state(
                    self.account_id, True, self.strategy_name, self.params,
                    self.interval_sec, self.risk_pct, last_error=str(exc),
                )
            self._stop.wait(self.interval_sec)
        store.upsert_bot_state(
            self.account_id, False, self.strategy_name, self.params,
            self.interval_sec, self.risk_pct,
        )

    def _act_on_signal(self, signal: str) -> None:
        if signal == "hold":
            return
        account = store.get_account(self.account_id)
        if account is None:
            self.stop()
            return
        position = account["position_oz"]
        cash = account["cash_balance"]
        try:
            if signal == "buy" and position <= 1e-9:
                amount = cash * (self.risk_pct / 100)
                if amount > 1:
                    place_order(self.account_id, "buy", amount=amount,
                                source="bot", strategy=self.strategy_name)
            elif signal == "sell" and position > 1e-9:
                place_order(self.account_id, "sell", qty_oz=position,
                            source="bot", strategy=self.strategy_name)
        except OrderError:
            pass  # solde/position insuffisants : on attend le prochain signal


def start_bot(account_id: int, strategy_name: str, params: dict,
              interval_sec: int = 10, risk_pct: float = 25) -> None:
    with _bots_lock:
        existing = _bots.get(account_id)
        if existing is not None:
            existing.stop()
        runner = BotRunner(account_id, strategy_name, params, interval_sec, risk_pct)
        _bots[account_id] = runner
        runner.start()
    store.upsert_bot_state(account_id, True, strategy_name, params, interval_sec, risk_pct)


def stop_bot(account_id: int) -> bool:
    with _bots_lock:
        runner = _bots.pop(account_id, None)
    if runner is None:
        return False
    runner.stop()
    return True


def bot_status(account_id: int) -> dict | None:
    row = store.get_bot_state(account_id)
    if row is None:
        return None
    return dict(row)


def all_bot_statuses() -> list[dict]:
    return [dict(r) for r in store.list_bot_states()]
