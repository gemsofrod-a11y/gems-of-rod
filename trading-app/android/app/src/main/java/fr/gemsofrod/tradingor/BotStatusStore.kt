package fr.gemsofrod.tradingor

import android.content.Context
import org.json.JSONObject

/**
 * État du bot partagé entre le service de premier plan (qui l'écrit
 * pendant qu'il tourne, y compris app fermée) et le pont JS (qui le
 * lit pour l'affichage) — via SharedPreferences, comme le Wallet.
 */
class BotStatusStore(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences("trading_or_bot_status", Context.MODE_PRIVATE)

    @Synchronized
    fun write(running: Boolean, strategy: String?, signal: String, error: String?) {
        prefs.edit()
            .putBoolean("running", running)
            .putString("strategy", strategy)
            .putString("signal", signal)
            .putString("error", error)
            .apply()
    }

    @Synchronized
    fun read(): JSONObject = JSONObject().apply {
        put("running", prefs.getBoolean("running", false))
        put("strategy", prefs.getString("strategy", null) ?: JSONObject.NULL)
        put("last_signal", prefs.getString("signal", "") ?: "")
        put("last_error", prefs.getString("error", null) ?: JSONObject.NULL)
    }

    @Synchronized
    fun clear() {
        write(running = false, strategy = null, signal = "", error = null)
    }
}
