package fr.gemsofrod.tradingor

import android.webkit.JavascriptInterface
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Pont natif exposé à la page (window.NativeBridge). Les appels
 * réseau sont bloquants mais s'exécutent déjà hors du thread UI
 * (WebView appelle les méthodes @JavascriptInterface sur un thread
 * dédié), donc pas de risque de geler l'app.
 *
 * Reprend la même logique de repli en cascade que le backend Python
 * (trading-app/backend/price_feed.py et candles.py) : source réelle
 * en premier, série simulée en dernier recours seulement, toujours
 * étiquetée comme telle.
 */
class NativeBridge {

    private var lastPrice = 2400.0

    @JavascriptInterface
    fun getPrice(): String {
        val goldApi = fetchGoldApi()
        val metalsLive = if (goldApi == null) fetchMetalsLive() else null
        val (price, provider) = when {
            goldApi != null -> goldApi to "gold-api"
            metalsLive != null -> metalsLive to "metals-live"
            else -> simulateStep() to ""
        }
        lastPrice = price
        val result = JSONObject()
        result.put("price", price)
        result.put("source", if (provider.isNotEmpty()) "live" else "simule")
        result.put("provider", provider)
        result.put("timestamp", System.currentTimeMillis() / 1000.0)
        return result.toString()
    }

    @JavascriptInterface
    fun getCandles(timeframe: String, limit: Int): String {
        val cfg = TIMEFRAMES[timeframe] ?: TIMEFRAMES.getValue("1m")
        val yahoo = fetchYahooCandles(cfg)
        val result = JSONObject()
        if (yahoo != null) {
            result.put("provider", "yahoo-finance")
            result.put("candles", toJsonArray(yahoo.takeLast(limit)))
        } else {
            result.put("provider", "simule")
            result.put("candles", toJsonArray(syntheticCandles(cfg, limit)))
        }
        result.put("timeframe", timeframe)
        return result.toString()
    }

    // --- cours ---

    private fun fetchGoldApi(): Double? = try {
        val json = httpGet("https://api.gold-api.com/price/XAU")
        JSONObject(json).optDouble("price").takeIf { it > 0 && !it.isNaN() }
    } catch (_: Exception) {
        null
    }

    private fun fetchMetalsLive(): Double? = try {
        val json = httpGet("https://api.metals.live/v1/spot/gold")
        val first = JSONArray(json).optJSONObject(0)
        first?.optDouble("gold")?.takeIf { it > 0 && !it.isNaN() }
    } catch (_: Exception) {
        null
    }

    private fun simulateStep(): Double {
        val stepVol = 0.01 / Math.sqrt(24.0 * 60 * 2) // pas d'environ 30s, calé sur la volatilité journalière de l'or
        val shock = Random.nextGaussian(0.0, stepVol)
        return max(1.0, lastPrice * (1 + shock))
    }

    // --- bougies ---

    private data class Candle(val t: Long, val o: Double, val h: Double, val l: Double, val c: Double)
    private data class TimeframeConfig(val seconds: Long, val yahooInterval: String, val yahooRange: String, val agg: Int)

    private val TIMEFRAMES = mapOf(
        "1m" to TimeframeConfig(60, "1m", "1d", 1),
        "5m" to TimeframeConfig(300, "5m", "5d", 1),
        "15m" to TimeframeConfig(900, "15m", "5d", 1),
        "1h" to TimeframeConfig(3600, "60m", "1mo", 1),
        "4h" to TimeframeConfig(14400, "60m", "3mo", 4),
        "1d" to TimeframeConfig(86400, "1d", "6mo", 1),
    )

    private fun fetchYahooCandles(cfg: TimeframeConfig): List<Candle>? = try {
        val url = "https://query1.finance.yahoo.com/v8/finance/chart/XAUUSD=X" +
            "?interval=${cfg.yahooInterval}&range=${cfg.yahooRange}"
        val json = httpGet(url, userAgent = "Mozilla/5.0 (gems-of-rod-trading-app)")
        val result = JSONObject(json).getJSONObject("chart").getJSONArray("result").getJSONObject(0)
        val timestamps = result.getJSONArray("timestamp")
        val quote = result.getJSONObject("indicators").getJSONArray("quote").getJSONObject(0)
        val opens = quote.getJSONArray("open")
        val highs = quote.getJSONArray("high")
        val lows = quote.getJSONArray("low")
        val closes = quote.getJSONArray("close")

        val raw = mutableListOf<Candle>()
        for (i in 0 until timestamps.length()) {
            if (opens.isNull(i) || highs.isNull(i) || lows.isNull(i) || closes.isNull(i)) continue
            raw.add(Candle(timestamps.getLong(i), opens.getDouble(i), highs.getDouble(i), lows.getDouble(i), closes.getDouble(i)))
        }
        if (raw.isEmpty()) null else aggregate(raw, cfg.agg)
    } catch (_: Exception) {
        null
    }

    private fun aggregate(candles: List<Candle>, factor: Int): List<Candle> {
        if (factor <= 1) return candles
        val out = mutableListOf<Candle>()
        var i = 0
        while (i < candles.size) {
            val chunk = candles.subList(i, min(i + factor, candles.size))
            if (chunk.isNotEmpty()) {
                out.add(Candle(
                    t = chunk.first().t,
                    o = chunk.first().o,
                    h = chunk.maxOf { it.h },
                    l = chunk.minOf { it.l },
                    c = chunk.last().c,
                ))
            }
            i += factor
        }
        return out
    }

    private fun syntheticCandles(cfg: TimeframeConfig, limit: Int): List<Candle> {
        val now = System.currentTimeMillis() / 1000
        var price = lastPrice
        val out = mutableListOf<Candle>()
        for (i in 0 until limit) {
            val prev = price
            val stepVol = 0.01 / Math.sqrt(365.0)
            price = max(1.0, price * (1 + Random.nextGaussian(0.0, stepVol)))
            val t = now - (limit - i) * cfg.seconds
            out.add(Candle(t, prev, max(prev, price), min(prev, price), price))
        }
        return out
    }

    private fun toJsonArray(candles: List<Candle>): JSONArray {
        val arr = JSONArray()
        for (c in candles) {
            arr.put(JSONObject().apply {
                put("t", c.t); put("o", c.o); put("h", c.h); put("l", c.l); put("c", c.c)
            })
        }
        return arr
    }

    // --- HTTP ---

    private fun httpGet(url: String, userAgent: String = "gems-of-rod-trading-app/1.0"): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 6000
        connection.readTimeout = 6000
        connection.setRequestProperty("User-Agent", userAgent)
        connection.inputStream.use { stream ->
            return stream.bufferedReader().readText()
        }
    }
}

private fun Random.nextGaussian(mean: Double, stdDev: Double): Double {
    // Transformation de Box-Muller : java.util.Random a nextGaussian(),
    // mais kotlin.random.Random n'expose pas d'équivalent direct.
    val u1 = 1.0 - this.nextDouble()
    val u2 = this.nextDouble()
    val z0 = kotlin.math.sqrt(-2.0 * kotlin.math.ln(u1)) * kotlin.math.cos(2.0 * Math.PI * u2)
    return mean + z0 * stdDev
}
