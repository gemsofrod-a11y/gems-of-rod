package fr.gemsofrod.assistant.ai

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class AnthropicApiException(val statusCode: Int, message: String) : IOException(message)

/** Appel direct à l'API Anthropic Messages (POST /v1/messages) en HTTP brut
 * — pas le SDK Java officiel : ce module ne peut pas être compilé ni
 * vérifié dans l'environnement où ce code a été écrit, donc on évite tout
 * pari sur des noms de méthode de builder qu'on ne peut pas vérifier. Le
 * format JSON de l'API Messages est stable et bien connu (c'est ce que le
 * SDK Python du backend envoie/reçoit sous le capot) : construire et
 * parser ce JSON à la main est un risque beaucoup plus faible ici.
 *
 * Non-streaming par choix : la réponse arrive d'un bloc plutôt que phrase
 * par phrase (contrairement au backend web). Moins fluide à l'oreille,
 * mais une boucle de streaming est une source d'erreurs bien plus difficile
 * à garantir correcte sans pouvoir compiler — à améliorer plus tard une
 * fois l'app buildable et testable normalement dans Android Studio.
 */
object AnthropicClient {
    private const val API_VERSION = "2023-06-01"
    private const val ENDPOINT = "https://api.anthropic.com/v1/messages"

    private val http = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .build()

    /** Envoie une requête Messages API et renvoie le JSON de réponse complet
     * (content, stop_reason, etc.). Lève AnthropicApiException si l'API
     * répond avec un statut d'erreur. */
    fun createMessage(
        apiKey: String,
        model: String,
        system: String,
        messages: JSONArray,
        tools: JSONArray? = null,
        maxTokens: Int = 1024,
    ): JSONObject {
        val body = JSONObject().apply {
            put("model", model)
            put("max_tokens", maxTokens)
            put("system", system)
            put("messages", messages)
            if (tools != null) put("tools", tools)
        }

        val request = Request.Builder()
            .url(ENDPOINT)
            .addHeader("x-api-key", apiKey)
            .addHeader("anthropic-version", API_VERSION)
            .addHeader("content-type", "application/json")
            .post(body.toString().toRequestBody("application/json".toMediaType()))
            .build()

        http.newCall(request).execute().use { resp ->
            val text = resp.body?.string() ?: ""
            if (!resp.isSuccessful) {
                val detail = try {
                    JSONObject(text).optJSONObject("error")?.optString("message") ?: text
                } catch (e: Exception) {
                    text
                }
                throw AnthropicApiException(resp.code, "Anthropic API ${resp.code} : $detail")
            }
            return JSONObject(text)
        }
    }
}
