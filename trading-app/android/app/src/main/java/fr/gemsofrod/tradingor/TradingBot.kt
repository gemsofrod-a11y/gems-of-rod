package fr.gemsofrod.tradingor

import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Bot de trading : investit un montant fixe (en USD) à chaque prise
 * de position, décidée par la stratégie choisie (achat) et refermée
 * dès qu'un seuil précis de gain/perte ou une durée maximale de
 * détention est atteint — jamais laissée traîner en attendant un
 * hypothétique signal contraire. Si un objectif de valorisation est
 * donné, le bot enchaîne autant de positions que nécessaire jusqu'à
 * l'atteindre ; un seuil de protection l'arrête si le portefeuille
 * s'érode trop, que l'objectif soit défini ou non.
 *
 * L'historique de prix est pré-rempli avec les bougies réelles
 * disponibles au démarrage (au lieu de repartir de zéro), pour que
 * la stratégie puisse produire un signal dès les premiers cycles
 * plutôt qu'après plusieurs minutes d'attente.
 *
 * Aucun résultat n'est garanti.
 */
class TradingBot(
    private val wallet: Wallet,
    private val priceSource: PriceSource,
    private val strategyName: String,
    private val params: JSONObject,
    private val intervalSec: Int,
    investAmountUsd: Double,
    private val takeProfitPct: Double,
    private val stopLossPct: Double,
    private val maxHoldingSec: Long,
    private val targetEquity: Double?,
    floorPct: Double,
    private val onStatus: (running: Boolean, signal: String, error: String?) -> Unit,
) {
    private val investAmountUsd = investAmountUsd.coerceAtLeast(1.0)
    private val floorPct = floorPct.coerceIn(1.0, 90.0)
    private val stopFlag = AtomicBoolean(false)
    private val history = mutableListOf<Double>()
    private var entryPrice: Double? = null
    private var entryTime: Long? = null
    private var startEquity: Double = 0.0
    private var thread: Thread? = null
    /** Stratégie ayant réellement ouvert la position en cours — utile en
     * mode "adaptive", où elle peut différer de strategyName d'un cycle
     * à l'autre. Enregistrée sur chaque trade pour que AdaptiveSelector
     * puisse évaluer les résultats de chaque stratégie séparément. */
    private var currentSubStrategy: String = strategyName

    fun start() {
        val t = Thread {
            runCatching { loop() }.onFailure { onStatus(false, "erreur fatale", it.message) }
        }
        t.isDaemon = false // le service de premier plan porte le cycle de vie, pas l'Activity
        thread = t
        t.start()
    }

    fun stop() {
        stopFlag.set(true)
    }

    private fun loop() {
        seedHistory()

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

    private fun seedHistory() {
        try {
            val needed = Strategies.minHistory(strategyName, params)
            val (_, candles) = priceSource.getCandles("1m", (needed + 20).coerceAtMost(200))
            history.clear()
            history.addAll(candles.map { it.c })
        } catch (_: Exception) {
            // pas grave : la stratégie se contentera de "hold" le temps d'accumuler des cours en direct
        }
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
            val (activeStrategy, activeParams) = resolveActiveStrategy()
            signal = Strategies.signal(activeStrategy, activeParams, history)
            if (signal == "buy") openPosition(quote.price, summary.getDouble("cash_balance"), activeStrategy)
        }
        onStatus(true, signal, null)
    }

    /** En mode "adaptive", interroge AdaptiveSelector à chaque cycle
     * (léger : ne relit que l'historique des trades déjà en mémoire
     * locale via SharedPreferences) pour choisir la stratégie sous-
     * jacente qui a le mieux fonctionné récemment. Sinon, la
     * stratégie fixe choisie par l'utilisateur. */
    private fun resolveActiveStrategy(): Pair<String, JSONObject> {
        if (strategyName != "adaptive") return strategyName to params
        val choice = AdaptiveSelector.choose(wallet.trades(200))
        return choice.strategyName to choice.params
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

    private fun openPosition(price: Double, cash: Double, strategyForTrade: String) {
        val amount = minOf(investAmountUsd, cash)
        if (amount <= 1) return
        try {
            wallet.executeOrder("buy", null, amount, price, "bot", strategyForTrade)
            entryPrice = price
            entryTime = System.currentTimeMillis() / 1000
            currentSubStrategy = strategyForTrade
        } catch (_: Wallet.OrderError) {
            // solde insuffisant : on attend le prochain signal
        }
    }

    private fun closePosition(reason: String) {
        val position = wallet.positionOz()
        if (position <= 1e-9) return
        try {
            wallet.executeOrder("sell", position, null, priceSource.lastPrice, "bot", currentSubStrategy)
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
                wallet.executeOrder("sell", position, null, priceSource.lastPrice, "bot", currentSubStrategy)
            } catch (_: Wallet.OrderError) {
                // ignore : on arrête quand même le bot
            }
        }
        onStatus(false, reason, null)
        stop()
    }
}
