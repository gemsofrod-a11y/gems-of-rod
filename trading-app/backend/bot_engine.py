"""Bot de trading automatique : exécute une stratégie en tâche de
fond pour un compte donné, en passant des ordres via le broker
interne (ou externe) au fil du cours du marché.

Positions courtes et précises
------------------------------
Le bot n'attend pas indéfiniment un signal contraire pour sortir
d'une position : chaque position ouverte porte un objectif de gain
(take_profit_pct) et un seuil de perte (stop_loss_pct) précis, ainsi
qu'une durée maximale de détention (max_holding_sec). Dès que l'un de
ces seuils est atteint, la position est soldée immédiatement — le
signal de la stratégie ne sert qu'à décider *quand entrer*, jamais à
faire traîner une position en perte.

Mode objectif (target_equity)
------------------------------
Si un objectif de valorisation est fourni, le bot enchaîne autant
d'ordres que nécessaire — dans la limite de la fréquence configurée
(interval_sec) — jusqu'à l'atteindre. La taille de chaque prise de
position est recalculée à chaque tick en fonction de l'écart restant
à l'objectif (plus agressif loin de l'objectif, plus prudent en
l'approchant) et n'est jamais un pari : le bot ne peut pas perdre plus
que le capital du compte (aucun effet de levier sur le broker
interne), et un seuil de protection (floor_pct) arrête le bot avant
que le compte ne soit trop entamé. Rien ne garantit d'atteindre
l'objectif — voir le README.
"""
from __future__ import annotations

import threading
import time
from collections import deque

from . import store
from .broker import OrderError, account_summary
from .execution import place_order
from .price_feed import get_quote
from .strategies import build_strategy

_MAX_HISTORY = 500
_bots: dict[int, "BotRunner"] = {}
_bots_lock = threading.Lock()

DEFAULT_TAKE_PROFIT_PCT = 1.5
DEFAULT_STOP_LOSS_PCT = 0.75
DEFAULT_MAX_HOLDING_SEC = 1800  # 30 min : garde les positions courtes
DEFAULT_FLOOR_PCT = 20  # arrêt de protection si l'équité tombe sous 20% du capital de départ du bot


def _clamp(value: float, low: float, high: float) -> float:
    return max(low, min(high, value))


class BotRunner:
    def __init__(self, account_id: int, strategy_name: str, params: dict,
                 interval_sec: int, risk_pct: float,
                 take_profit_pct: float = DEFAULT_TAKE_PROFIT_PCT,
                 stop_loss_pct: float = DEFAULT_STOP_LOSS_PCT,
                 max_holding_sec: int = DEFAULT_MAX_HOLDING_SEC,
                 target_equity: float | None = None,
                 floor_pct: float = DEFAULT_FLOOR_PCT):
        self.account_id = account_id
        self.strategy_name = strategy_name
        self.params = params
        self.interval_sec = max(2, interval_sec)
        self.base_risk_pct = _clamp(risk_pct, 1, 100)
        self.take_profit_pct = max(0.05, take_profit_pct)
        self.stop_loss_pct = max(0.05, stop_loss_pct)
        self.max_holding_sec = max(30, max_holding_sec)
        self.target_equity = target_equity
        self.floor_pct = _clamp(floor_pct, 1, 90)
        self.strategy = build_strategy(strategy_name, params)

        self._history: deque[float] = deque(maxlen=_MAX_HISTORY)
        self._stop = threading.Event()
        self._thread = threading.Thread(target=self._run, daemon=True)
        self._entry_price: float | None = None
        self._entry_time: float | None = None
        self._start_equity: float | None = None

    def start(self) -> None:
        self._thread.start()

    def stop(self) -> None:
        self._stop.set()

    # --- boucle principale ---

    def _run(self) -> None:
        summary = account_summary(self.account_id)
        self._start_equity = summary["equity"]
        if summary["position_oz"] > 1e-9:
            # Position déjà ouverte (trade manuel ou bot précédent) : on
            # l'adopte avec le cours courant comme référence pour le
            # take-profit / stop-loss, plutôt que de la laisser sans sortie.
            self._entry_price = summary["price"]
            self._entry_time = time.time()
        while not self._stop.is_set():
            try:
                self._tick()
            except Exception as exc:  # ne jamais tuer le thread sur une erreur ponctuelle
                store.upsert_bot_state(
                    self.account_id, True, self.strategy_name, self.params,
                    self.interval_sec, self.base_risk_pct, last_error=str(exc),
                )
            self._stop.wait(self.interval_sec)
        store.upsert_bot_state(
            self.account_id, False, self.strategy_name, self.params,
            self.interval_sec, self.base_risk_pct,
        )

    def _tick(self) -> None:
        quote = get_quote()
        self._history.append(quote.price)

        summary = account_summary(self.account_id)
        equity = summary["equity"]

        if self.target_equity and equity >= self.target_equity:
            self._finish("objectif atteint")
            return
        if equity <= self._start_equity * (self.floor_pct / 100):
            self._finish("seuil de protection atteint : bot arrêté")
            return

        if summary["position_oz"] > 1e-9:
            exit_reason = self._check_exit(quote.price)
            if exit_reason:
                self._close_position(exit_reason)
            signal = "hold"
        else:
            signal = self.strategy.signal(list(self._history))
            if signal == "buy":
                self._open_position(quote.price)

        store.upsert_bot_state(
            self.account_id, True, self.strategy_name, self.params,
            self.interval_sec, self.base_risk_pct, last_signal=signal,
        )

    def _check_exit(self, price: float) -> str | None:
        if self._entry_price is None:
            return None
        change_pct = (price - self._entry_price) / self._entry_price * 100
        if change_pct >= self.take_profit_pct:
            return f"objectif de gain atteint (+{change_pct:.2f}%)"
        if change_pct <= -self.stop_loss_pct:
            return f"seuil de perte atteint ({change_pct:.2f}%)"
        if self._entry_time and time.time() - self._entry_time >= self.max_holding_sec:
            return f"durée maximale de détention atteinte ({change_pct:+.2f}%)"
        return None

    def _dynamic_risk_pct(self, equity: float) -> float:
        if not self.target_equity or self.target_equity <= self._start_equity:
            return self.base_risk_pct
        progress = _clamp((equity - self._start_equity) / (self.target_equity - self._start_equity), 0, 1)
        # Plus agressif loin de l'objectif, plus prudent en l'approchant.
        factor = 1.6 - 0.8 * progress
        return _clamp(self.base_risk_pct * factor, 5, 90)

    def _open_position(self, price: float) -> None:
        account = store.get_account(self.account_id)
        if account is None:
            self.stop()
            return
        risk_pct = self._dynamic_risk_pct(account["cash_balance"] + account["position_oz"] * price)
        amount = account["cash_balance"] * (risk_pct / 100)
        if amount <= 1:
            return
        try:
            place_order(self.account_id, "buy", amount=amount, source="bot", strategy=self.strategy_name)
            self._entry_price = price
            self._entry_time = time.time()
        except OrderError:
            pass

    def _close_position(self, reason: str) -> None:
        account = store.get_account(self.account_id)
        if account is None or account["position_oz"] <= 1e-9:
            return
        try:
            place_order(self.account_id, "sell", qty_oz=account["position_oz"],
                        source="bot", strategy=self.strategy_name)
        except OrderError:
            return
        self._entry_price = None
        self._entry_time = None
        store.upsert_bot_state(
            self.account_id, True, self.strategy_name, self.params,
            self.interval_sec, self.base_risk_pct, last_signal=f"sortie : {reason}",
        )

    def _finish(self, reason: str) -> None:
        account = store.get_account(self.account_id)
        if account and account["position_oz"] > 1e-9:
            try:
                place_order(self.account_id, "sell", qty_oz=account["position_oz"],
                            source="bot", strategy=self.strategy_name)
            except OrderError:
                pass
        store.upsert_bot_state(
            self.account_id, False, self.strategy_name, self.params,
            self.interval_sec, self.base_risk_pct, last_signal=reason,
        )
        self.stop()
        with _bots_lock:
            _bots.pop(self.account_id, None)


def start_bot(account_id: int, strategy_name: str, params: dict,
              interval_sec: int = 10, risk_pct: float = 25,
              take_profit_pct: float = DEFAULT_TAKE_PROFIT_PCT,
              stop_loss_pct: float = DEFAULT_STOP_LOSS_PCT,
              max_holding_sec: int = DEFAULT_MAX_HOLDING_SEC,
              target_equity: float | None = None,
              floor_pct: float = DEFAULT_FLOOR_PCT) -> None:
    with _bots_lock:
        existing = _bots.get(account_id)
        if existing is not None:
            existing.stop()
        runner = BotRunner(account_id, strategy_name, params, interval_sec, risk_pct,
                            take_profit_pct, stop_loss_pct, max_holding_sec,
                            target_equity, floor_pct)
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
