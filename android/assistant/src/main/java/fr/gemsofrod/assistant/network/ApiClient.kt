package fr.gemsofrod.assistant.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface AssistantApi {
    @GET("api/health")
    suspend fun health(): Map<String, String>

    @POST("api/voice")
    suspend fun voice(@Body request: VoiceRequest): VoiceResponse

    @GET("api/pending")
    suspend fun listPending(): List<PendingItem>

    @POST("api/pending/{id}/approve")
    suspend fun approvePending(@Path("id") id: String, @Body request: ResolveRequest): MessageResponse

    @POST("api/pending/{id}/reject")
    suspend fun rejectPending(@Path("id") id: String): MessageResponse

    @GET("api/digest/today")
    suspend fun digestToday(): DigestResponse
}

/** Un client est reconstruit à chaque changement d'URL/jeton dans les Réglages :
 * ce sont des valeurs saisies par l'utilisateur, pas connues au démarrage de l'app. */
object ApiClientFactory {
    fun build(baseUrl: String, token: String): AssistantApi {
        val normalizedUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AssistantApi::class.java)
    }
}
