package fr.gemsofrod.tradingor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import org.json.JSONObject

/**
 * Service de premier plan : fait tourner le bot même quand l'app est
 * fermée ou l'écran éteint — une notification persistante est
 * requise par Android pour ça (avec un bouton « Arrêter »).
 *
 * Le portefeuille (Wallet) et l'état du bot (BotStatusStore) vivent
 * dans SharedPreferences, partagés avec l'Activity/WebView : rouvrir
 * l'app affiche l'état à jour, même si le bot a tourné pendant ce
 * temps hors de l'app.
 *
 * ⚠️ Android limite de plus en plus l'exécution en arrière-plan
 * (Doze, restrictions par fabricant type Xiaomi/Samsung). Un service
 * de premier plan est le mécanisme le plus fiable disponible, mais
 * pas une garantie absolue : si le bot semble s'arrêter seul,
 * désactiver l'optimisation de batterie pour cette app dans les
 * réglages système aide généralement.
 */
class TradingBotService : Service() {

    companion object {
        const val CHANNEL_ID = "trading_or_bot"
        const val NOTIFICATION_ID = 1
        const val ACTION_STOP = "fr.gemsofrod.tradingor.action.STOP_BOT"

        const val EXTRA_STRATEGY = "strategy"
        const val EXTRA_PARAMS_JSON = "params_json"
        const val EXTRA_INTERVAL_SEC = "interval_sec"
        const val EXTRA_STAKE_AMOUNT = "stake_amount"
        const val EXTRA_TAKE_PROFIT_PCT = "take_profit_pct"
        const val EXTRA_STOP_LOSS_PCT = "stop_loss_pct"
        const val EXTRA_MAX_HOLDING_MIN = "max_holding_min"
        const val EXTRA_TARGET_EQUITY = "target_equity"
        const val EXTRA_FLOOR_PCT = "floor_pct"
    }

    private var bot: TradingBot? = null
    private lateinit var statusStore: BotStatusStore

    override fun onCreate() {
        super.onCreate()
        statusStore = BotStatusStore(this)
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Android exige qu'un service démarré via startForegroundService()
        // appelle startForeground() sous quelques secondes, y compris s'il
        // s'agit en réalité d'un arrêt (ex : "Arrêter" tapé alors que le
        // service avait déjà été tué par le système) — donc systématique,
        // avant toute décision de branche.
        showForeground("…")

        if (intent?.action == ACTION_STOP) {
            stopBotAndService()
            return START_NOT_STICKY
        }
        if (intent == null) {
            stopBotAndService()
            return START_NOT_STICKY
        }

        val strategy = intent.getStringExtra(EXTRA_STRATEGY) ?: "sma_crossover"
        val params = try {
            JSONObject(intent.getStringExtra(EXTRA_PARAMS_JSON) ?: "{}")
        } catch (_: Exception) {
            JSONObject()
        }
        val intervalSec = intent.getIntExtra(EXTRA_INTERVAL_SEC, 5)
        val stakeAmount = intent.getDoubleExtra(EXTRA_STAKE_AMOUNT, 50.0)
        val tp = intent.getDoubleExtra(EXTRA_TAKE_PROFIT_PCT, 0.15)
        val sl = intent.getDoubleExtra(EXTRA_STOP_LOSS_PCT, 0.1)
        val maxHoldingSec = (intent.getIntExtra(EXTRA_MAX_HOLDING_MIN, 1).coerceAtLeast(1) * 60).toLong()
        val targetEquityRaw = intent.getDoubleExtra(EXTRA_TARGET_EQUITY, 0.0)
        val targetEquity = if (targetEquityRaw > 0) targetEquityRaw else null
        val floorPct = intent.getDoubleExtra(EXTRA_FLOOR_PCT, 20.0)

        val wallet = Wallet(this)
        val priceSource = PriceSource()

        bot?.stop()
        val newBot = TradingBot(
            wallet, priceSource, strategy, params, intervalSec.coerceAtLeast(2), stakeAmount,
            tp, sl, maxHoldingSec, targetEquity, floorPct,
        ) { running, signal, error ->
            statusStore.write(running, strategy, signal, error)
            if (running) {
                showForeground(error ?: if (signal.isNotEmpty()) "Dernier signal : $signal" else "En surveillance…")
            } else {
                statusStore.write(false, strategy, signal, error)
                stopForegroundCompat()
                stopSelf()
            }
        }
        bot = newBot
        statusStore.write(true, strategy, "", null)
        newBot.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        bot?.stop()
        bot = null
        super.onDestroy()
    }

    private fun stopBotAndService() {
        bot?.stop()
        bot = null
        statusStore.clear()
        stopForegroundCompat()
        stopSelf()
    }

    private fun createChannel() {
        val manager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(CHANNEL_ID, "Bot de trading", NotificationManager.IMPORTANCE_LOW)
        channel.description = "Notification persistante pendant que le bot de trading est actif"
        manager.createNotificationChannel(channel)
    }

    private fun showForeground(text: String) {
        val notification = buildNotification(text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun stopForegroundCompat() {
        @Suppress("DEPRECATION")
        stopForeground(true)
    }

    private fun buildNotification(text: String): Notification {
        val stopIntent = Intent(this, TradingBotService::class.java).setAction(ACTION_STOP)
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val stopAction = Notification.Action.Builder(
            Icon.createWithResource(this, android.R.drawable.ic_menu_close_clear_cancel),
            "Arrêter", stopPendingIntent,
        ).build()

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Bot Trading Or actif")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setOngoing(true)
            .addAction(stopAction)
            .build()
    }
}
