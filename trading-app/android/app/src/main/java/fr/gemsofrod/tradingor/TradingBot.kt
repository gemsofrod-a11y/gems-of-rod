package fr.gemsofrod.tradingor

import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Bot de trading, avec une **mise dédiée** sortie du portefeuille
 * (ex. 50 $) : l'objectif de valorisation porte sur cette mise, pas
 * sur la valeur totale du portefeuille — mettre un objectif de 500 $
 * avec une mise de 50 $ fait trader le bot jusqu'à ce que CETTE mise
 * (cash encore réservé + valeur de la position en cours) vaille
 * 500 $, indépendamment du reste du portefeuille.
 *
 * La taille de chaque trade n'est pas fixe : elle varie selon la
 * conviction du signal (Strategies.confidence, 0 à 1) — un signal
 * franc fait investir une part plus grande de la mise restante, un
 * signal limite peut se limiter à 1 $. Chaque position ouverte porte
 * malgré tout un take-profit/stop-loss précis et une durée maximale
 * de détention, jamais laissée traîner en attendant un hypothétique
 * signal contraire.
 *
 * L'historique de prix est pré-rempli avec les bougies réelles
 * disponibles au démarrage, pour que la stratégie puisse produire un
 * signal dès les premiers cycles plutôt qu'après plusieurs minutes
 * d'attente.
 *
 * Aucun résultat n'est garanti.
 */
class TradingBot(
    private val wallet: Wallet,
    private val priceSource: PriceSource,
    private val strategyName: String,
    private val params: JSONObject,
    private val intervalSec: Int,
    stakeUsd: Double,
    private val takeProfitPct: Double,
    private val stopLossPct: Double,
    private val maxHoldingSec: Long,
    private val targetEquity: Double?,
    floorPct: Double,
    private val onStatus: (running: Boolean, signal: String, error: String?) -> Unit,
) {
    companion object {
        /** Part de la mise restante investie à conviction "moyenne"
         * (confiance ≈ 0.375, milieu de la plage 0.4×–2×) avant d'être
         * modulée par Strategies.confidence(). Choix heuristique. */
        private const val BASE_FRACTION = 0.05
    }

    private val startStakeEquity = stakeUsd.coerceAtLeast(1.0)
    private val floorPct = floorPct.coerceIn(1.0, 90.0)
    private val stopFlag = AtomicBoolean(false)
    private val history = mutableListOf<Double>()

    /** Cash encore réservé à la mise (diminue à l'achat, augmente à la revente). */
    private var stakeCash: Double = startStakeEquity
    /** Onces achetées avec l'argent de la mise (distinct de la position
     * totale du portefeuille, qui peut aussi contenir des achats manuels). */
    private var stakePositionOz: Double = 0.0

    private var entryPrice: Double? = null
    private var entryTime: Long? = null
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
        if (wallet.positionOz() > 1e-9) {
            // Position déjà ouverte (redémarrage après une position en
            // cours) : on l'adopte comme faisant partie de la mise plutôt
            // que de la laisser sans suivi de sortie.
            stakePositionOz = wallet.positionOz()
            stakeCash = 0.0
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

        val stakeEquity = stakeCash + stakePositionOz * quote.price

        if (targetEquity != null && stakeEquity >= targetEquity) {
            finish("objectif atteint")
            return
        }
        if (stakeEquity <= startStakeEquity * (floorPct / 100)) {
            finish("seuil de protection atteint : bot arrêté")
            return
        }

        var signal = "hold"
        if (stakePositionOz > 1e-9) {
            val reason = checkExit(quote.price)
            if (reason != null) closePosition(reason)
        } else {
            val (activeStrategy, activeParams) = resolveActiveStrategy()
            signal = Strategies.signal(activeStrategy, activeParams, history)
            if (signal == "buy") {
                val confidence = Strategies.confidence(activeStrategy, activeParams, history)
                openPosition(quote.price, confidence, activeStrategy)
            }
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

    private fun openPosition(price: Double, confidence: Double, strategyForTrade: String) {
        val available = minOf(stakeCash, wallet.cashBalance())
        if (available < 1.0) return // mise épuisée (ou solde réel insuffisant) : on attend une revente

        val baseAmount = stakeCash * BASE_FRACTION
        val amount = (baseAmount * (0.4 + 1.6 * confidence)).coerceIn(1.0, available)
        try {
            val trade = wallet.executeOrder("buy", null, amount, price, "bot", strategyForTrade)
            stakeCash -= trade.getDouble("amount")
            stakePositionOz += trade.getDouble("qty_oz")
            entryPrice = price
            entryTime = System.currentTimeMillis() / 1000
            currentSubStrategy = strategyForTrade
        } catch (_: Wallet.OrderError) {
            // solde insuffisant : on attend le prochain signal
        }
    }

    private fun closePosition(reason: String) {
        if (stakePositionOz <= 1e-9) return
        try {
            val trade = wallet.executeOrder("sell", stakePositionOz, null, priceSource.lastPrice, "bot", currentSubStrategy)
            stakeCash += trade.getDouble("amount")
        } catch (_: Wallet.OrderError) {
            return
        }
        stakePositionOz = 0.0
        entryPrice = null
        entryTime = null
        onStatus(true, "sortie : $reason", null)
    }

    private fun finish(reason: String) {
        if (stakePositionOz > 1e-9) {
            try {
                val trade = wallet.executeOrder("sell", stakePositionOz, null, priceSource.lastPrice, "bot", currentSubStrategy)
                stakeCash += trade.getDouble("amount")
            } catch (_: Wallet.OrderError) {
                // ignore : on arrête quand même le bot
            }
            stakePositionOz = 0.0
        }
        onStatus(false, reason, null)
        stop()
    }
}
