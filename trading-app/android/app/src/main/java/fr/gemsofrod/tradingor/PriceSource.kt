package fr.gemsofrod.tradingor

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Cours et bougies XAU/USD — logique partagée entre le pont JS
 * (NativeBridge) et le bot de trading (TradingBot), tous deux
 * appelant ceci depuis un thread qui n'est jamais le thread UI.
 *
 * Même cascade de repli que le backend Python de l'app web
 * (price_feed.py / candles.py) : source réelle d'abord, simulation
 * seulement en dernier recours et toujours étiquetée comme telle.
 */
class PriceSource {

    companion object {
        /** Chaque appel réseau bloque le thread appelant (le pont JS
         * attend la réponse avant de rendre la main) : un cache court
         * évite de retaper le réseau à chaque rafraîchissement de
         * l'interface, seule vraie source de lenteur perçue. Même
         * principe que le cache 5s du serveur de l'app web. */
        private const val QUOTE_CACHE_MS = 2_000L
        private const val CANDLES_CACHE_MS = 8_000L
        /** Nombre de cours réels conservés en mémoire pour reconstruire de
         * vraies bougies localement si Yahoo Finance est injoignable (ex.
         * bloqué par un opérateur/pare-feu) — le cours ponctuel (gold-api)
         * marche souvent même quand l'historique Yahoo échoue. */
        private const val MAX_TICKS = 4000
    }

    @Volatile
    var lastPrice: Double = 2400.0
        private set

    data class Quote(val price: Double, val source: String, val provider: String)
    data class Candle(val t: Long, val o: Double, val h: Double, val l: Double, val c: Double)
    private data class TimeframeConfig(val seconds: Long, val yahooInterval: String, val yahooRange: String, val agg: Int)

    private val timeframes = mapOf(
        "1m" to TimeframeConfig(60, "1m", "1d", 1),
        "5m" to TimeframeConfig(300, "5m", "5d", 1),
        "15m" to TimeframeConfig(900, "15m", "5d", 1),
        "1h" to TimeframeConfig(3600, "60m", "1mo", 1),
        "4h" to TimeframeConfig(14400, "60m", "3mo", 4),
        "1d" to TimeframeConfig(86400, "1d", "6mo", 1),
    )

    @Volatile private var quoteCache: Quote? = null
    @Volatile private var quoteCacheAt: Long = 0
    private val candlesCache = mutableMapOf<String, Pair<Long, Pair<String, List<Candle>>>>()
    private val tickLog = ArrayDeque<Pair<Long, Double>>() // (secondes epoch, cours réel uniquement)

    @Synchronized
    fun getQuote(): Quote {
        val now = System.currentTimeMillis()
        quoteCache?.let { if (now - quoteCacheAt < QUOTE_CACHE_MS) return it }

        val goldApi = fetchGoldApi()
        val metalsLive = if (goldApi == null) fetchMetalsLive() else null
        val (price, provider) = when {
            goldApi != null -> goldApi to "gold-api"
            metalsLive != null -> metalsLive to "metals-live"
            else -> simulateStep() to ""
        }
        lastPrice = price
        val quote = Quote(price, if (provider.isNotEmpty()) "live" else "simule", provider)
        quoteCache = quote
        quoteCacheAt = now
        if (provider.isNotEmpty()) {
            tickLog.addLast((now / 1000) to price)
            while (tickLog.size > MAX_TICKS) tickLog.removeFirst()
        }
        return quote
    }

    @Synchronized
    fun getCandles(timeframe: String, limit: Int): Pair<String, List<Candle>> {
        val now = System.currentTimeMillis()
        candlesCache[timeframe]?.let { (cachedAt, cached) ->
            if (now - cachedAt < CANDLES_CACHE_MS) return cached
        }

        val cfg = timeframes[timeframe] ?: timeframes.getValue("1m")
        val yahoo = fetchYahooCandles(cfg)
        val result = when {
            yahoo != null -> "yahoo-finance" to yahoo.takeLast(limit)
            else -> aggregateFromTicks(cfg, limit)?.let { "cours-reel-local" to it }
                ?: ("simule" to syntheticCandles(cfg, limit))
        }
        candlesCache[timeframe] = now to result
        return result
    }

    /** Reconstruit de vraies bougies à partir des cours réels déjà
     * récupérés (gold-api/metals-live) pendant que l'app tourne, quand
     * Yahoo Finance est injoignable. Peu profond au départ (juste après
     * le lancement de l'app) mais s'étoffe avec le temps, et reste du
     * vrai mouvement de marché plutôt qu'une simulation. */
    private fun aggregateFromTicks(cfg: TimeframeConfig, limit: Int): List<Candle>? {
        if (tickLog.size < 5) return null
        val buckets = linkedMapOf<Long, MutableList<Double>>()
        for ((t, price) in tickLog) {
            val bucketTs = (t / cfg.seconds) * cfg.seconds
            buckets.getOrPut(bucketTs) { mutableListOf() }.add(price)
        }
        if (buckets.size < 3) return null
        val candles = buckets.entries.sortedBy { it.key }.map { (ts, prices) ->
            Candle(ts, prices.first(), prices.max(), prices.min(), prices.last())
        }
        return candles.takeLast(limit)
    }

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
        val stepVol = 0.01 / Math.sqrt(24.0 * 60 * 2)
        val shock = Random.nextGaussian(0.0, stepVol)
        return max(1.0, lastPrice * (1 + shock))
    }

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
                out.add(Candle(chunk.first().t, chunk.first().o, chunk.maxOf { it.h }, chunk.minOf { it.l }, chunk.last().c))
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

fun Random.nextGaussian(mean: Double, stdDev: Double): Double {
    // Transformation de Box-Muller : java.util.Random a nextGaussian(),
    // mais kotlin.random.Random n'expose pas d'équivalent direct.
    val u1 = 1.0 - this.nextDouble()
    val u2 = this.nextDouble()
    val z0 = kotlin.math.sqrt(-2.0 * kotlin.math.ln(u1)) * kotlin.math.cos(2.0 * Math.PI * u2)
    return mean + z0 * stdDev
}
