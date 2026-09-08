package fr.gemsofrod.tradingor

import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Bot orienté objectif — même comportement que
 * trading-app/backend/bot_engine.py : on lui donne un objectif de
 * valorisation et il enchaîne autant de positions courtes que
 * nécessaire jusqu'à l'atteindre, chaque position portant un
 * take-profit / stop-loss précis et une durée maximale de détention,
 * avec un seuil de protection qui l'arrête si le portefeuille
 * s'érode trop. Tourne tant que l'app reste ouverte (pas de service
 * en arrière-plan) — il s'arrête si l'app est fermée.
 *
 * Aucun résultat n'est garanti, en particulier pour un objectif
 * ambitieux sans effet de levier.
 */
class TradingBot(
    private val wallet: Wallet,
    private val priceSource: PriceSource,
    private val strategyName: String,
    private val params: JSONObject,
    private val intervalSec: Int,
    baseRiskPct: Double,
    private val takeProfitPct: Double,
    private val stopLossPct: Double,
    private val maxHoldingSec: Long,
    private val targetEquity: Double?,
    floorPct: Double,
    private val onStatus: (running: Boolean, signal: String, error: String?) -> Unit,
) {
    private val baseRiskPct = baseRiskPct.coerceIn(1.0, 100.0)
    private val floorPct = floorPct.coerceIn(1.0, 90.0)
    private val stopFlag = AtomicBoolean(false)
    private val history = mutableListOf<Double>()
    private var entryPrice: Double? = null
    private var entryTime: Long? = null
    private var startEquity: Double = 0.0
    private var thread: Thread? = null

    fun start() {
        val t = Thread {
            runCatching { loop() }.onFailure { onStatus(false, "erreur fatale", it.message) }
        }
        t.isDaemon = true
        thread = t
        t.start()
    }

    fun stop() {
        stopFlag.set(true)
    }

    private fun loop() {
        val quote0 = priceSource.getQuote()
        val summary0 = wallet.summary(quote0.price)
        startEquity = summary0.getDouble("equity")
        if (summary0.getDouble("position_oz") > 1e-9) {
            entryPrice = quote0.price
            entryTime = System.currentTimeMillis() / 1000
        }

        while (!stopFlag.get()) {
            try {
                tick()
            } catch (e: Exception) {
                onStatus(true, "hold", e.message)
            }
            if (stopFlag.get()) break
            Thread.sleep(intervalSec * 1000L)
        }
        onStatus(false, "arrêté", null)
    }

    private fun tick() {
        val quote = priceSource.getQuote()
        history.add(quote.price)
        if (history.size > 500) history.removeAt(0)

        val summary = wallet.summary(quote.price)
        val equity = summary.getDouble("equity")

        if (targetEquity != null && equity >= targetEquity) {
            finish("objectif atteint")
            return
        }
        if (equity <= startEquity * (floorPct / 100)) {
            finish("seuil de protection atteint : bot arrêté")
            return
        }

        var signal = "hold"
        if (summary.getDouble("position_oz") > 1e-9) {
            val reason = checkExit(quote.price)
            if (reason != null) closePosition(reason)
        } else {
            signal = Strategies.signal(strategyName, params, history)
            if (signal == "buy") openPosition(quote.price, summary.getDouble("cash_balance"), equity)
        }
        onStatus(true, signal, null)
    }

    private fun checkExit(price: Double): String? {
        val entry = entryPrice ?: return null
        val changePct = (price - entry) / entry * 100
        return when {
            changePct >= takeProfitPct -> "objectif de gain atteint (+${"%.2f".format(changePct)}%)"
            changePct <= -stopLossPct -> "seuil de perte atteint (${"%.2f".format(changePct)}%)"
            entryTime != null && System.currentTimeMillis() / 1000 - entryTime!! >= maxHoldingSec ->
                "durée maximale de détention atteinte (${"%+.2f".format(changePct)}%)"
            else -> null
        }
    }

    private fun dynamicRiskPct(equity: Double): Double {
        val target = targetEquity
        if (target == null || target <= startEquity) return baseRiskPct
        val progress = ((equity - startEquity) / (target - startEquity)).coerceIn(0.0, 1.0)
        val factor = 1.6 - 0.8 * progress
        return (baseRiskPct * factor).coerceIn(5.0, 90.0)
    }

    private fun openPosition(price: Double, cash: Double, equity: Double) {
        val riskPct = dynamicRiskPct(equity)
        val amount = cash * (riskPct / 100)
        if (amount <= 1) return
        try {
            wallet.executeOrder("buy", null, amount, price, "bot", strategyName)
            entryPrice = price
            entryTime = System.currentTimeMillis() / 1000
        } catch (_: Wallet.OrderError) {
            // solde insuffisant : on attend le prochain signal
        }
    }

    private fun closePosition(reason: String) {
        val position = wallet.positionOz()
        if (position <= 1e-9) return
        try {
            wallet.executeOrder("sell", position, null, priceSource.lastPrice, "bot", strategyName)
        } catch (_: Wallet.OrderError) {
            return
        }
        entryPrice = null
        entryTime = null
        onStatus(true, "sortie : $reason", null)
    }

    private fun finish(reason: String) {
        val position = wallet.positionOz()
        if (position > 1e-9) {
            try {
                wallet.executeOrder("sell", position, null, priceSource.lastPrice, "bot", strategyName)
            } catch (_: Wallet.OrderError) {
                // ignore : on arrête quand même le bot
            }
        }
        onStatus(false, reason, null)
        stop()
    }
}
