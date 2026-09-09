package fr.gemsofrod.tradingor

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Portefeuille virtuel persisté localement (SharedPreferences, aucune
 * dépendance de base de données) — un seul compte, démarrant à 1000
 * USD. L'app est sa propre contrepartie : il n'y a pas de courtier
 * réel, pas d'argent réel, uniquement un suivi fidèle du cours réel
 * appliqué à un solde virtuel.
 */
class Wallet(context: Context) {

    companion object {
        const val STARTING_CAPITAL = 1000.0
        private const val MAX_TRADES_KEPT = 300
        private const val MAX_EQUITY_POINTS_KEPT = 500
    }

    class OrderError(message: String) : Exception(message)

    private val prefs = context.getSharedPreferences("trading_or_wallet", Context.MODE_PRIVATE)

    @Synchronized
    private fun load(): JSONObject {
        val raw = prefs.getString("state", null) ?: return defaultState()
        return try {
            JSONObject(raw)
        } catch (_: Exception) {
            defaultState()
        }
    }

    private fun defaultState(): JSONObject = JSONObject().apply {
        put("cash", STARTING_CAPITAL)
        put("position_oz", 0.0)
        put("trades", JSONArray())
        put("equity_history", JSONArray())
    }

    @Synchronized
    private fun save(state: JSONObject) {
        prefs.edit().putString("state", state.toString()).apply()
    }

    @Synchronized
    fun reset() {
        save(defaultState())
    }

    @Synchronized
    fun summary(price: Double): JSONObject {
        val state = load()
        val cash = state.getDouble("cash")
        val position = state.getDouble("position_oz")
        val equity = cash + position * price
        val pnl = equity - STARTING_CAPITAL
        return JSONObject().apply {
            put("cash_balance", cash)
            put("position_oz", position)
            put("price", price)
            put("equity", equity)
            put("pnl", pnl)
            put("pnl_pct", if (STARTING_CAPITAL != 0.0) pnl / STARTING_CAPITAL * 100 else 0.0)
            put("starting_capital", STARTING_CAPITAL)
        }
    }

    /** side: "buy" ou "sell". Fournir soit qtyOz, soit amountUsd (l'autre à null). */
    @Synchronized
    fun executeOrder(side: String, qtyOz: Double?, amountUsd: Double?, price: Double,
                      source: String, strategy: String?): JSONObject {
        if (side != "buy" && side != "sell") throw OrderError("side doit valoir buy ou sell")
        if (price <= 0) throw OrderError("Cours invalide")

        val state = load()
        var cash = state.getDouble("cash")
        var position = state.getDouble("position_oz")

        val qty = when {
            qtyOz != null && qtyOz > 0 -> qtyOz
            amountUsd != null && amountUsd > 0 -> amountUsd / price
            else -> throw OrderError("Préciser une quantité (oz) ou un montant (USD) positif")
        }
        val cost = qty * price

        when (side) {
            "buy" -> {
                if (cost > cash + 1e-9) {
                    throw OrderError("Solde insuffisant : ${"%.2f".format(cash)} USD disponibles, ${"%.2f".format(cost)} requis")
                }
                cash -= cost
                position += qty
            }
            "sell" -> {
                if (qty > position + 1e-9) {
                    throw OrderError("Position insuffisante : ${"%.4f".format(position)} oz détenues, ${"%.4f".format(qty)} oz demandées")
                }
                cash += cost
                position -= qty
            }
        }

        state.put("cash", cash)
        state.put("position_oz", position)

        val trade = JSONObject().apply {
            put("side", side)
            put("qty_oz", qty)
            put("price", price)
            put("amount", cost)
            put("source", source)
            put("strategy", strategy ?: JSONObject.NULL)
            put("created_at", System.currentTimeMillis() / 1000.0)
        }
        val trades = state.getJSONArray("trades")
        trades.put(trade)
        while (trades.length() > MAX_TRADES_KEPT) trades.remove(0)

        val equity = cash + position * price
        val equityHistory = state.getJSONArray("equity_history")
        equityHistory.put(JSONObject().apply {
            put("equity", equity); put("price", price); put("created_at", System.currentTimeMillis() / 1000.0)
        })
        while (equityHistory.length() > MAX_EQUITY_POINTS_KEPT) equityHistory.remove(0)

        save(state)
        return trade
    }

    @Synchronized
    fun trades(limit: Int): JSONArray {
        val all = load().getJSONArray("trades")
        val out = JSONArray()
        val start = maxOf(0, all.length() - limit)
        for (i in all.length() - 1 downTo start) out.put(all.getJSONObject(i))
        return out
    }

    @Synchronized
    fun positionOz(): Double = load().getDouble("position_oz")

    @Synchronized
    fun cashBalance(): Double = load().getDouble("cash")
}
