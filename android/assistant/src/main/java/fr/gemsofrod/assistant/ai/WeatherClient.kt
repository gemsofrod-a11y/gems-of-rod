package fr.gemsofrod.assistant.ai

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/** Météo en direct via Open-Meteo (API publique, gratuite, sans clé) — port
 * Kotlin de backend/app/weather.py : géocodage du lieu puis météo actuelle
 * aux coordonnées trouvées. */
object WeatherClient {
    private val http = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val wmoDescriptions = mapOf(
        0 to "ciel dégagé", 1 to "plutôt dégagé", 2 to "partiellement nuageux", 3 to "couvert",
        45 to "brouillard", 48 to "brouillard givrant",
        51 to "bruine légère", 53 to "bruine modérée", 55 to "bruine forte",
        56 to "bruine verglaçante légère", 57 to "bruine verglaçante forte",
        61 to "pluie légère", 63 to "pluie modérée", 65 to "forte pluie",
        66 to "pluie verglaçante légère", 67 to "pluie verglaçante forte",
        71 to "neige légère", 73 to "neige modérée", 75 to "forte neige", 77 to "grains de neige",
        80 to "averses légères", 81 to "averses modérées", 82 to "fortes averses",
        85 to "averses de neige légères", 86 to "averses de neige fortes",
        95 to "orage", 96 to "orage avec grêle légère", 99 to "orage avec forte grêle",
    )

    private fun get(url: String): JSONObject {
        val resp = http.newCall(Request.Builder().url(url).build()).execute()
        resp.use {
            if (!it.isSuccessful) throw java.io.IOException("HTTP ${it.code}")
            return JSONObject(it.body?.string() ?: "{}")
        }
    }

    /** Renvoie un JSONObject "météo" en cas de succès, ou {"error": "..."} —
     * même contrat que weather.get_weather côté Python (pour rester lisible
     * telle quelle à voix haute par Saphir en cas d'échec). */
    fun getWeather(location: String): JSONObject {
        val geo = try {
            val url = "https://geocoding-api.open-meteo.com/v1/search".toHttpUrl().newBuilder()
                .addQueryParameter("name", location)
                .addQueryParameter("count", "1")
                .addQueryParameter("language", "fr")
                .build()
            get(url.toString())
        } catch (e: Exception) {
            return JSONObject().put("error", "Service de géolocalisation météo indisponible pour le moment.")
        }

        val results = geo.optJSONArray("results")
        if (results == null || results.length() == 0) {
            return JSONObject().put("error", "Lieu introuvable : « $location ».")
        }
        val place = results.getJSONObject(0)
        if (!place.has("latitude") || !place.has("longitude")) {
            return JSONObject().put("error", "Coordonnées introuvables pour « $location ».")
        }
        val lat = place.getDouble("latitude")
        val lon = place.getDouble("longitude")
        val name = place.optString("name", location)
        val country = place.optString("country", "")

        val forecast = try {
            val url = "https://api.open-meteo.com/v1/forecast".toHttpUrl().newBuilder()
                .addQueryParameter("latitude", lat.toString())
                .addQueryParameter("longitude", lon.toString())
                .addQueryParameter(
                    "current",
                    "temperature_2m,apparent_temperature,weather_code,wind_speed_10m,relative_humidity_2m",
                )
                .addQueryParameter("timezone", "auto")
                .build()
            get(url.toString())
        } catch (e: Exception) {
            return JSONObject().put("error", "Service météo indisponible pour le moment.")
        }

        val current = forecast.optJSONObject("current") ?: JSONObject()
        val code = if (current.has("weather_code")) current.optInt("weather_code") else null
        val locationLabel = listOf(name, country).filter { it.isNotBlank() }.joinToString(", ")
        return JSONObject().apply {
            put("location", locationLabel)
            put("temperature_c", current.opt("temperature_2m") ?: JSONObject.NULL)
            put("feels_like_c", current.opt("apparent_temperature") ?: JSONObject.NULL)
            put("condition", wmoDescriptions[code] ?: "conditions inconnues")
            put("humidity_pct", current.opt("relative_humidity_2m") ?: JSONObject.NULL)
            put("wind_kmh", current.opt("wind_speed_10m") ?: JSONObject.NULL)
        }
    }
}
