package fr.gemsofrod.tradingor

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Pont natif exposé à la page (window.NativeBridge) : cours, bougies,
 * portefeuille virtuel (1000 USD de départ) et bot de trading.
 *
 * Toutes les méthodes @JavascriptInterface délèguent leur travail à
 * un thread dédié (runOnWorker) : selon la version de WebView, ces
 * méthodes peuvent être appelées depuis le thread principal, où un
 * appel réseau bloquant planterait l'app (NetworkOnMainThreadException).
 *
 * Le bot lui-même tourne dans TradingBotService (service de premier
 * plan), pas ici : il continue ainsi même app fermée. Ce pont ne fait
 * que démarrer/arrêter ce service et lire son état partagé.
 */
class NativeBridge(context: Context) {

    private val appContext = context.applicationContext
    private val priceSource = PriceSource()
    private val wallet = Wallet(appContext)
    private val botStatus = BotStatusStore(appContext)
    private val worker = Executors.newSingleThreadExecutor()

    private fun <T> runOnWorker(block: () -> T): T =
        worker.submit(Callable { block() }).get(20, TimeUnit.SECONDS)

    // --- cours & bougies ---

    @JavascriptInterface
    fun getPrice(): String = try {
        runOnWorker {
            val quote = priceSource.getQuote()
            JSONObject().apply {
                put("price", quote.price)
                put("source", quote.source)
                put("provider", quote.provider)
                put("timestamp", System.currentTimeMillis() / 1000.0)
            }.toString()
        }
    } catch (_: Exception) {
        JSONObject().apply {
            put("price", priceSource.lastPrice)
            put("source", "simule"); put("provider", "")
            put("timestamp", System.currentTimeMillis() / 1000.0)
        }.toString()
    }

    @JavascriptInterface
    fun getCandles(timeframe: String, limit: Int): String = try {
        runOnWorker {
            val (provider, candles) = priceSource.getCandles(timeframe, limit)
            JSONObject().apply {
                put("timeframe", timeframe)
                put("provider", provider)
                put("candles", candlesToJson(candles))
            }.toString()
        }
    } catch (_: Exception) {
        JSONObject().apply {
            put("timeframe", timeframe); put("provider", "simule"); put("candles", JSONArray())
        }.toString()
    }

    private fun candlesToJson(candles: List<PriceSource.Candle>): JSONArray {
        val arr = JSONArray()
        for (c in candles) {
            arr.put(JSONObject().apply {
                put("t", c.t); put("o", c.o); put("h", c.h); put("l", c.l); put("c", c.c)
            })
        }
        return arr
    }

    // --- portefeuille ---

    @JavascriptInterface
    fun getWallet(): String = try {
        runOnWorker { wallet.summary(priceSource.getQuote().price).toString() }
    } catch (_: Exception) {
        wallet.summary(priceSource.lastPrice).toString()
    }

    @JavascriptInterface
    fun placeOrderByAmount(side: String, amountUsd: Double): String = try {
        runOnWorker {
            val price = priceSource.getQuote().price
            val trade = wallet.executeOrder(side, null, amountUsd, price, "manuel", null)
            JSONObject().apply { put("ok", true); put("trade", trade) }.toString()
        }
    } catch (e: Exception) {
        JSONObject().apply { put("ok", false); put("error", e.message ?: "Erreur inconnue") }.toString()
    }

    @JavascriptInterface
    fun placeOrderByQty(side: String, qtyOz: Double): String = try {
        runOnWorker {
            val price = priceSource.getQuote().price
            val trade = wallet.executeOrder(side, qtyOz, null, price, "manuel", null)
            JSONObject().apply { put("ok", true); put("trade", trade) }.toString()
        }
    } catch (e: Exception) {
        JSONObject().apply { put("ok", false); put("error", e.message ?: "Erreur inconnue") }.toString()
    }

    @JavascriptInterface
    fun getTrades(limit: Int): String = try {
        runOnWorker { wallet.trades(limit).toString() }
    } catch (_: Exception) {
        JSONArray().toString()
    }

    @JavascriptInterface
    fun resetWallet(): String = try {
        runOnWorker {
            wallet.reset()
            JSONObject().apply { put("ok", true) }.toString()
        }
    } catch (e: Exception) {
        JSONObject().apply { put("ok", false); put("error", e.message ?: "Erreur inconnue") }.toString()
    }

    // --- bot ---

    @JavascriptInterface
    fun startBot(
        strategy: String, paramsJson: String, intervalSec: Int, stakeUsd: Double,
        takeProfitPct: Double, stopLossPct: Double, maxHoldingMin: Int,
        targetEquity: Double, floorPct: Double,
    ): String = try {
        val intent = Intent(appContext, TradingBotService::class.java).apply {
            putExtra(TradingBotService.EXTRA_STRATEGY, strategy)
            putExtra(TradingBotService.EXTRA_PARAMS_JSON, paramsJson)
            putExtra(TradingBotService.EXTRA_INTERVAL_SEC, intervalSec)
            putExtra(TradingBotService.EXTRA_STAKE_AMOUNT, stakeUsd)
            putExtra(TradingBotService.EXTRA_TAKE_PROFIT_PCT, takeProfitPct)
            putExtra(TradingBotService.EXTRA_STOP_LOSS_PCT, stopLossPct)
            putExtra(TradingBotService.EXTRA_MAX_HOLDING_MIN, maxHoldingMin)
            putExtra(TradingBotService.EXTRA_TARGET_EQUITY, targetEquity)
            putExtra(TradingBotService.EXTRA_FLOOR_PCT, floorPct)
        }
        appContext.startForegroundService(intent)
        JSONObject().apply { put("status", "started") }.toString()
    } catch (e: Exception) {
        JSONObject().apply { put("status", "error"); put("error", e.message ?: "Erreur inconnue") }.toString()
    }

    @JavascriptInterface
    fun stopBot(): String = try {
        val intent = Intent(appContext, TradingBotService::class.java).setAction(TradingBotService.ACTION_STOP)
        appContext.startForegroundService(intent)
        JSONObject().apply { put("status", "stopped") }.toString()
    } catch (e: Exception) {
        JSONObject().apply { put("status", "error"); put("error", e.message ?: "Erreur inconnue") }.toString()
    }

    @JavascriptInterface
    fun getBotStatus(): String = try {
        botStatus.read().toString()
    } catch (_: Exception) {
        JSONObject().apply { put("running", false) }.toString()
    }
}
