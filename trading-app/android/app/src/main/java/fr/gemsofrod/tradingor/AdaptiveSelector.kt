package fr.gemsofrod.tradingor

import org.json.JSONArray
import org.json.JSONObject

/**
 * Sélection adaptative de stratégie : à partir des trades du bot déjà
 * réalisés (allers-retours achat → vente, reconstruits depuis
 * l'historique du portefeuille), calcule le taux de réussite récent
 * de chaque stratégie candidate et choisit celle qui a le mieux
 * fonctionné sur ce marché.
 *
 * C'est une forme simple et transparente d'adaptation aux résultats
 * réels — pas un modèle de machine learning (irréaliste à faire
 * tourner de façon fiable et vérifiable dans une appli Android
 * autonome, sans serveur ni données d'entraînement).
 */
object AdaptiveSelector {

    private val CANDIDATES = listOf("sma_crossover", "rsi_mean_reversion")
    private const val MIN_SAMPLE = 3

    data class Choice(val strategyName: String, val params: JSONObject)

    fun choose(trades: JSONArray): Choice {
        val wins = mutableMapOf<String, Int>()
        val total = mutableMapOf<String, Int>()
        for ((strategy, profitable) in roundTrips(trades)) {
            if (strategy !in CANDIDATES) continue
            total[strategy] = (total[strategy] ?: 0) + 1
            if (profitable) wins[strategy] = (wins[strategy] ?: 0) + 1
        }

        // Sans historique suffisant, score neutre (0.5) : encourage à
        // essayer les deux plutôt que de figer un choix arbitraire.
        val scored = CANDIDATES.map { name ->
            val t = total[name] ?: 0
            val score = if (t >= MIN_SAMPLE) (wins[name] ?: 0).toDouble() / t else 0.5
            name to score
        }
        val best = scored.maxByOrNull { it.second }?.first ?: CANDIDATES.first()
        return Choice(best, defaultParams(best))
    }

    private fun defaultParams(strategy: String): JSONObject = when (strategy) {
        "rsi_mean_reversion" -> JSONObject().apply {
            put("period", 14); put("oversold", 30.0); put("overbought", 70.0)
        }
        else -> JSONObject().apply { put("fast", 10); put("slow", 30) }
    }

    /** Reconstruit les allers-retours achat → vente (le bot ne détient
     * jamais qu'une position à la fois) et renvoie, pour chacun, la
     * stratégie qui l'a ouvert et s'il a été gagnant. */
    private fun roundTrips(trades: JSONArray): List<Pair<String, Boolean>> {
        // trades() renvoie du plus récent au plus ancien : on rejoue en ordre chronologique.
        val chronological = (trades.length() - 1 downTo 0).map { trades.getJSONObject(it) }
        val out = mutableListOf<Pair<String, Boolean>>()
        var openBuy: JSONObject? = null
        for (trade in chronological) {
            if (trade.optString("source") != "bot") continue
            when (trade.optString("side")) {
                "buy" -> openBuy = trade
                "sell" -> {
                    val buy = openBuy ?: continue
                    val strategy = buy.optString("strategy", "")
                    if (strategy.isNotEmpty()) {
                        val profit = trade.optDouble("amount", 0.0) - buy.optDouble("amount", 0.0)
                        out.add(strategy to (profit > 0))
                    }
                    openBuy = null
                }
            }
        }
        return out
    }
}
