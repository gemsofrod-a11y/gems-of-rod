package fr.gemsofrod.assistant.ai

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

data class ImageResult(
    val url: String? = null,
    val thumbnail: String? = null,
    val title: String? = null,
    val creator: String? = null,
    val source: String? = null,
    val license: String? = null,
    val error: String? = null,
)

/** Recherche d'images via Openverse (licence Creative Commons, API publique
 * gratuite, sans clé) — port Kotlin de backend/app/images.py. */
object ImageSearchClient {
    private val http = okhttp3.OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    fun searchImages(query: String, count: Int = 3): List<ImageResult> {
        val clamped = count.coerceIn(1, 6)
        val url = "https://api.openverse.org/v1/images/".toHttpUrlOrNull()
            ?.newBuilder()
            ?.addQueryParameter("q", query)
            ?.addQueryParameter("page_size", clamped.toString())
            ?.build()
            ?: return listOf(ImageResult(error = "URL de recherche invalide."))

        val data = try {
            http.newCall(okhttp3.Request.Builder().url(url).build()).execute().use { resp ->
                if (!resp.isSuccessful) throw java.io.IOException("HTTP ${resp.code}")
                org.json.JSONObject(resp.body?.string() ?: "{}")
            }
        } catch (e: Exception) {
            return listOf(ImageResult(error = "Service de recherche d'images indisponible pour le moment."))
        }

        val results = data.optJSONArray("results")
        if (results == null || results.length() == 0) {
            return listOf(ImageResult(error = "Aucune image trouvée pour « $query »."))
        }

        val out = mutableListOf<ImageResult>()
        for (i in 0 until minOf(results.length(), clamped)) {
            val r = results.getJSONObject(i)
            val imgUrl = r.optString("url", "").ifBlank { null } ?: continue
            out.add(
                ImageResult(
                    url = imgUrl,
                    thumbnail = r.optString("thumbnail", "").ifBlank { imgUrl },
                    title = r.optString("title", "").ifBlank { query },
                    creator = r.optString("creator", ""),
                    source = r.optString("source", ""),
                    license = r.optString("license", ""),
                )
            )
        }
        return out
    }
}
