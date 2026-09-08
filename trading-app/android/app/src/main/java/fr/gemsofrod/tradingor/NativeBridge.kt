package fr.gemsofrod.tradingor

import android.content.Context
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
 */
class NativeBridge(context: Context) {

    private val appContext = context.applicationContext
    private val priceSource = PriceSource()
    private val wallet = Wallet(appContext)
    private val worker = Executors.newSingleThreadExecutor()

    @Volatile private var bot: TradingBot? = null
    @Volatile private var botRunning = false
    @Volatile private var botStrategy: String? = null
    @Volatile private var lastSignal: String = ""
    @Volatile private var lastError: String? = null

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
        strategy: String, paramsJson: String, intervalSec: Int, riskPct: Double,
        takeProfitPct: Double, stopLossPct: Double, maxHoldingMin: Int,
        targetEquity: Double, floorPct: Double,
    ): String = try {
        synchronized(this) {
            bot?.stop()
            val params = try { JSONObject(paramsJson) } catch (_: Exception) { JSONObject() }
            val newBot = TradingBot(
                wallet, priceSource, strategy, params,
                intervalSec.coerceAtLeast(2), riskPct,
                takeProfitPct.coerceAtLeast(0.05), stopLossPct.coerceAtLeast(0.05),
                (maxHoldingMin.coerceAtLeast(1) * 60).toLong(),
                if (targetEquity > 0) targetEquity else null,
                floorPct,
            ) { running, signal, error ->
                botRunning = running
                lastSignal = signal
                lastError = error
                if (!running) bot = null
            }
            botStrategy = strategy
            botRunning = true
            lastSignal = ""
            lastError = null
            bot = newBot
            newBot.start()
        }
        JSONObject().apply { put("status", "started") }.toString()
    } catch (e: Exception) {
        JSONObject().apply { put("status", "error"); put("error", e.message ?: "Erreur inconnue") }.toString()
    }

    @JavascriptInterface
    fun stopBot(): String = try {
        synchronized(this) {
            bot?.stop()
            bot = null
            botRunning = false
        }
        JSONObject().apply { put("status", "stopped") }.toString()
    } catch (e: Exception) {
        JSONObject().apply { put("status", "error"); put("error", e.message ?: "Erreur inconnue") }.toString()
    }

    @JavascriptInterface
    fun getBotStatus(): String = try {
        JSONObject().apply {
            put("running", botRunning)
            put("strategy", botStrategy ?: JSONObject.NULL)
            put("last_signal", lastSignal)
            put("last_error", lastError ?: JSONObject.NULL)
        }.toString()
    } catch (_: Exception) {
        JSONObject().apply { put("running", false) }.toString()
    }
}
