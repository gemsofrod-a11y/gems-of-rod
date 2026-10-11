"""Stratégies de trading algorithmique pour le bot et le backtester.

Chaque stratégie consomme un historique de prix (du plus ancien au
plus récent) et renvoie un signal parmi 'buy', 'sell', 'hold'.
Aucune stratégie ne garantit un gain : ce sont des heuristiques
classiques d'analyse technique, fournies à titre d'outil, pas de
conseil en investissement.
"""
from __future__ import annotations

from abc import ABC, abstractmethod


class Strategy(ABC):
    name: str

    def __init__(self, params: dict):
        self.params = params

    @abstractmethod
    def min_history(self) -> int:
        ...

    @abstractmethod
    def signal(self, prices: list[float]) -> str:
        ...


def _sma(values: list[float]) -> float:
    return sum(values) / len(values)


class SmaCrossoverStrategy(Strategy):
    name = "sma_crossover"

    def __init__(self, params: dict):
        super().__init__(params)
        self.fast = int(params.get("fast", 10))
        self.slow = int(params.get("slow", 30))
        if self.fast >= self.slow:
            raise ValueError("fast doit être < slow")

    def min_history(self) -> int:
        return self.slow + 1

    def signal(self, prices: list[float]) -> str:
        if len(prices) < self.min_history():
            return "hold"
        prev = prices[:-1]
        fast_now = _sma(prices[-self.fast:])
        slow_now = _sma(prices[-self.slow:])
        fast_prev = _sma(prev[-self.fast:])
        slow_prev = _sma(prev[-self.slow:])
        if fast_prev <= slow_prev and fast_now > slow_now:
            return "buy"
        if fast_prev >= slow_prev and fast_now < slow_now:
            return "sell"
        return "hold"


class RsiMeanReversionStrategy(Strategy):
    name = "rsi_mean_reversion"

    def __init__(self, params: dict):
        super().__init__(params)
        self.period = int(params.get("period", 14))
        self.oversold = float(params.get("oversold", 30))
        self.overbought = float(params.get("overbought", 70))

    def min_history(self) -> int:
        return self.period + 1

    def _rsi(self, prices: list[float]) -> float:
        window = prices[-(self.period + 1):]
        gains = []
        losses = []
        for i in range(1, len(window)):
            delta = window[i] - window[i - 1]
            gains.append(max(delta, 0))
            losses.append(max(-delta, 0))
        avg_gain = sum(gains) / self.period
        avg_loss = sum(losses) / self.period
        if avg_loss == 0:
            return 100.0
        rs = avg_gain / avg_loss
        return 100 - (100 / (1 + rs))

    def signal(self, prices: list[float]) -> str:
        if len(prices) < self.min_history():
            return "hold"
        rsi = self._rsi(prices)
        if rsi <= self.oversold:
            return "buy"
        if rsi >= self.overbought:
            return "sell"
        return "hold"


class NewsAwareTrendStrategy(Strategy):
    """Combine une stratégie technique de base avec le sentiment de
    marché tiré de l'actualité récente (voir news_sentiment.py) :
    un signal technique n'est suivi que s'il n'est pas contredit par
    l'actualité (achat bloqué si actualité franchement baissière,
    vente bloquée si franchement haussière)."""

    name = "news_aware_trend"

    def __init__(self, params: dict):
        super().__init__(params)
        base_name = params.get("base", "sma_crossover")
        base_cls = STRATEGIES.get(base_name)  # défini plus bas, résolu à l'instanciation
        if base_cls is None:
            raise ValueError(f"Stratégie de base inconnue : {base_name}")
        self.base = base_cls(params)

    def min_history(self) -> int:
        return self.base.min_history()

    def signal(self, prices: list[float]) -> str:
        from .news_sentiment import get_sentiment  # import tardif : évite un cycle au chargement

        base_signal = self.base.signal(prices)
        if base_signal == "hold":
            return "hold"
        sentiment = get_sentiment()
        if base_signal == "buy" and sentiment.label == "bearish":
            return "hold"
        if base_signal == "sell" and sentiment.label == "bullish":
            return "hold"
        return base_signal


STRATEGIES: dict[str, type[Strategy]] = {
    SmaCrossoverStrategy.name: SmaCrossoverStrategy,
    RsiMeanReversionStrategy.name: RsiMeanReversionStrategy,
}
STRATEGIES[NewsAwareTrendStrategy.name] = NewsAwareTrendStrategy


def build_strategy(name: str, params: dict) -> Strategy:
    cls = STRATEGIES.get(name)
    if cls is None:
        raise ValueError(f"Stratégie inconnue : {name}. Options : {list(STRATEGIES)}")
    return cls(params or {})
