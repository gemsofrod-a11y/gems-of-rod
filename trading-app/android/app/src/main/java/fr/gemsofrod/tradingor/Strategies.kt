package fr.gemsofrod.tradingor

import org.json.JSONObject

/**
 * Signaux d'analyse technique pour le bot — même logique que
 * trading-app/backend/strategies.py (croisement de moyennes mobiles,
 * RSI retour à la moyenne). Aucune stratégie ne garantit un gain.
 */
object Strategies {

    fun minHistory(name: String, params: JSONObject): Int = when (name) {
        "sma_crossover" -> params.optInt("slow", 30) + 1
        "rsi_mean_reversion" -> params.optInt("period", 14) + 1
        else -> params.optInt("slow", 30) + 1
    }

    fun signal(name: String, params: JSONObject, prices: List<Double>): String = when (name) {
        "rsi_mean_reversion" -> rsi(prices, params.optInt("period", 14),
            params.optDouble("oversold", 30.0), params.optDouble("overbought", 70.0))
        else -> smaCrossover(prices, params.optInt("fast", 10), params.optInt("slow", 30))
    }

    private fun smaCrossover(prices: List<Double>, fast: Int, slow: Int): String {
        if (fast >= slow || prices.size < slow + 1) return "hold"
        val prev = prices.dropLast(1)
        val fastNow = prices.takeLast(fast).average()
        val slowNow = prices.takeLast(slow).average()
        val fastPrev = prev.takeLast(fast).average()
        val slowPrev = prev.takeLast(slow).average()
        return when {
            fastPrev <= slowPrev && fastNow > slowNow -> "buy"
            fastPrev >= slowPrev && fastNow < slowNow -> "sell"
            else -> "hold"
        }
    }

    private fun rsi(prices: List<Double>, period: Int, oversold: Double, overbought: Double): String {
        if (prices.size < period + 1) return "hold"
        val window = prices.takeLast(period + 1)
        var gain = 0.0
        var loss = 0.0
        for (i in 1 until window.size) {
            val delta = window[i] - window[i - 1]
            if (delta > 0) gain += delta else loss += -delta
        }
        val avgGain = gain / period
        val avgLoss = loss / period
        val rsiValue = if (avgLoss == 0.0) 100.0 else 100 - 100 / (1 + avgGain / avgLoss)
        return when {
            rsiValue <= oversold -> "buy"
            rsiValue >= overbought -> "sell"
            else -> "hold"
        }
    }
}
