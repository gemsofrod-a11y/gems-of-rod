package fr.gemsofrod.gestion.sumup

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** Erreur lisible, affichée telle quelle à l'utilisateur. */
class SumUpException(message: String) : Exception(message)

/** Transaction SumUp telle que renvoyée par l'historique. */
data class SumUpTransaction(
    val code: String,
    val amount: Double,
    val date: Long,
    val time: String,
    val paymentType: String,
)

data class SumUpCheckout(val id: String, val url: String?, val status: String, val amount: Double, val transactionCodes: List<String>)

/**
 * Appels à l'API SumUp (https://api.sumup.com) avec la clé API de Sébastien.
 * HTTP brut (HttpURLConnection + org.json, fournis par Android) : aucune
 * dépendance ajoutée. À appeler hors du fil principal.
 */
class SumUpClient(private val apiKey: String) {

    private val base = "https://api.sumup.com"

    /** Code marchand du compte lié à la clé (GET /v0.1/me). */
    fun merchantCode(): String {
        val me = JSONObject(request("GET", "$base/v0.1/me"))
        val code = me.optJSONObject("merchant_profile")?.optString("merchant_code").orEmpty()
            .ifBlank { me.optString("merchant_code") }
        if (code.isBlank()) throw SumUpException("Code marchand introuvable : saisissez-le à la main (application SumUp → Profil).")
        return code
    }

    /**
     * Paiements réussis, du plus récent au plus ancien, en s'arrêtant au
     * premier déjà connu ([knownCodes]) ou au-delà de [maxPages] pages.
     */
    fun transactions(merchantCode: String, knownCodes: Set<String>, maxPages: Int = 5): List<SumUpTransaction> {
        val result = mutableListOf<SumUpTransaction>()
        val first = "$base/v2.1/merchants/${enc(merchantCode)}/transactions/history?limit=100&order=descending"
        var url: String? = first
        var page = 0
        while (url != null && page < maxPages) {
            val body = JSONObject(request("GET", url))
            val items = body.optJSONArray("items") ?: JSONArray()
            var reachedKnown = false
            for (i in 0 until items.length()) {
                val t = items.getJSONObject(i)
                val code = t.optString("transaction_code")
                if (code.isBlank()) continue
                if (code in knownCodes) { reachedKnown = true; continue }
                if (t.optString("status") != "SUCCESSFUL") continue
                if (t.optString("type", "PAYMENT") != "PAYMENT") continue
                val (day, time) = parseTimestamp(t.optString("timestamp"))
                result += SumUpTransaction(code, t.optDouble("amount", 0.0), day, time, t.optString("payment_type"))
            }
            if (reachedKnown || items.length() == 0) break
            url = nextLink(body.optJSONArray("links"), merchantCode)
            page++
        }
        return result
    }

    /** Crée un lien de paiement (Hosted Checkout) pour [amount] €. */
    fun createCheckout(merchantCode: String, reference: String, amount: Double, description: String): SumUpCheckout {
        val payload = JSONObject().apply {
            put("checkout_reference", reference)
            put("amount", amount)
            put("currency", "EUR")
            put("merchant_code", merchantCode)
            put("description", description)
            put("hosted_checkout", JSONObject().put("enabled", true))
        }
        return parseCheckout(JSONObject(request("POST", "$base/v0.1/checkouts", payload.toString())))
    }

    fun checkout(id: String): SumUpCheckout = parseCheckout(JSONObject(request("GET", "$base/v0.1/checkouts/${enc(id)}")))

    private fun parseCheckout(o: JSONObject): SumUpCheckout {
        val tx = o.optJSONArray("transactions") ?: JSONArray()
        val codes = (0 until tx.length()).mapNotNull { tx.optJSONObject(it)?.optString("transaction_code")?.takeIf { c -> c.isNotBlank() } }
        return SumUpCheckout(
            id = o.optString("id"),
            url = o.optString("hosted_checkout_url").takeIf { it.isNotBlank() },
            status = o.optString("status"),
            amount = o.optDouble("amount", 0.0),
            transactionCodes = codes,
        )
    }

    /** Lien « next » de la pagination, absolu ou relatif. */
    private fun nextLink(links: JSONArray?, merchantCode: String): String? {
        if (links == null) return null
        for (i in 0 until links.length()) {
            val l = links.optJSONObject(i) ?: continue
            if (l.optString("rel") != "next") continue
            val href = l.optString("href").ifBlank { return null }
            return when {
                href.startsWith("http") -> href
                href.startsWith("/") -> base + href
                href.startsWith("?") -> "$base/v2.1/merchants/${enc(merchantCode)}/transactions/history$href"
                else -> "$base/v2.1/merchants/${enc(merchantCode)}/transactions/$href"
            }
        }
        return null
    }

    private fun request(method: String, url: String, body: String? = null): String {
        val conn = try {
            (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = method
                connectTimeout = 15_000
                readTimeout = 20_000
                setRequestProperty("Authorization", "Bearer $apiKey")
                setRequestProperty("Accept", "application/json")
                if (body != null) {
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    outputStream.use { it.write(body.toByteArray()) }
                }
            }
        } catch (e: IOException) {
            throw SumUpException("Pas de connexion à SumUp. Vérifiez internet.")
        }
        try {
            val code = try { conn.responseCode } catch (e: IOException) {
                // Sur certains Android, un 401 sans en-tête WWW-Authenticate
                // lève une IOException au lieu de renvoyer le code.
                if (e.message.orEmpty().contains("authentication challenge", ignoreCase = true)) 401
                else throw SumUpException("Pas de connexion à SumUp. Vérifiez internet.")
            }
            if (code in 200..299) return conn.inputStream.bufferedReader().use { it.readText() }
            val detail = runCatching { conn.errorStream?.bufferedReader()?.use { it.readText() } }.getOrNull()
                ?.let { runCatching { JSONObject(it).optString("message").ifBlank { JSONObject(it).optString("detail") } }.getOrNull() }
                .orEmpty()
            throw SumUpException(
                when (code) {
                    401 -> "Clé API refusée par SumUp. Vérifiez-la (elle commence par sup_sk_)."
                    403 -> "SumUp refuse cette opération pour votre compte" +
                        (if (url.contains("checkouts")) " : les paiements en ligne sont peut-être à activer dans votre compte SumUp." else ".")
                    404 -> "Introuvable chez SumUp (code marchand incorrect ?)."
                    else -> "Erreur SumUp ($code)" + if (detail.isNotBlank()) " : $detail" else "."
                },
            )
        } finally {
            conn.disconnect()
        }
    }

    private fun enc(s: String) = URLEncoder.encode(s, "UTF-8")

    companion object {
        private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        /** « 2026-10-03T14:05:12.345Z » → (jour local, « 16:05 »). */
        fun parseTimestamp(text: String): Pair<Long, String> {
            val instant = runCatching { Instant.parse(text) }.getOrNull()
                ?: runCatching { OffsetDateTime.parse(text).toInstant() }.getOrNull()
                ?: Instant.now()
            val local = instant.atZone(ZoneId.systemDefault())
            return local.toLocalDate().toEpochDay() to local.format(timeFormat)
        }
    }
}

/**
 * Identifiants SumUp, gardés dans les préférences privées de l'app (jamais
 * dans le code ni dans la sauvegarde exportée, exclus de la sauvegarde
 * Google — voir res/xml/backup_rules.xml).
 */
class SumUpSettings(context: Context) {
    private val prefs = context.getSharedPreferences("sumup", Context.MODE_PRIVATE)

    var apiKey: String
        get() = prefs.getString("apiKey", "").orEmpty()
        set(v) = prefs.edit().putString("apiKey", v).apply()

    var merchantCode: String
        get() = prefs.getString("merchantCode", "").orEmpty()
        set(v) = prefs.edit().putString("merchantCode", v).apply()

    /** Dernière synchronisation réussie (millisecondes), 0 = jamais. */
    var lastSync: Long
        get() = prefs.getLong("lastSync", 0L)
        set(v) = prefs.edit().putLong("lastSync", v).apply()

    val isConfigured: Boolean get() = apiKey.isNotBlank() && merchantCode.isNotBlank()

    fun clear() = prefs.edit().clear().apply()
}
