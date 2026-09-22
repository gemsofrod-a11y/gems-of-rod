package fr.gemsofrod.assistant.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.NoClientAuthentication
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenResponse
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Connexion Gmail directement dans l'app, sans passer par un serveur — la
 * même chose que `scripts/gmail_oauth_setup.py` faisait sur ordinateur pour
 * le backend, mais avec l'écran de consentement Google standard affiché sur
 * le téléphone (OAuth 2.0 + PKCE, client "Android" public — pas de secret
 * client à embarquer dans l'app).
 *
 * Nécessite un ID client OAuth de type "Android" créé dans le même projet
 * Google Cloud que le backend (voir android/assistant/README.md pour la
 * procédure : package name + empreinte SHA-1 du certificat de signature).
 */
class GoogleAuthManager(
    private val context: Context,
    private val tokenStore: TokenStore,
    private val clientId: String,
    private val redirectUri: Uri,
) {
    private val authService = AuthorizationService(context)

    private val serviceConfig = AuthorizationServiceConfiguration(
        Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"),
        Uri.parse("https://oauth2.googleapis.com/token"),
    )

    private var authState: AuthState = tokenStore.authStateJson
        ?.let { runCatching { AuthState.jsonDeserialize(it) }.getOrNull() }
        ?: AuthState(serviceConfig)

    val isSignedIn: Boolean
        get() = authState.isAuthorized

    private fun persist() {
        tokenStore.authStateJson = authState.jsonSerializeString()
    }

    /** Intent à lancer avec un ActivityResultLauncher pour afficher l'écran
     * "Se connecter avec Google". `access_type=offline` + `prompt=consent`
     * forcent Google à renvoyer un refresh token (indispensable pour le tri
     * automatique en tâche de fond, qui tourne sans que Sébastien soit en
     * train de regarder l'app).
     */
    fun buildSignInIntent(): Intent {
        val request = AuthorizationRequest.Builder(
            serviceConfig, clientId, ResponseTypeValues.CODE, redirectUri
        )
            .setScopes(GMAIL_SCOPE)
            .setAdditionalParameters(mapOf("access_type" to "offline", "prompt" to "consent"))
            .build()
        return authService.getAuthorizationRequestIntent(request)
    }

    /** À appeler avec l'Intent reçu dans le callback de l'ActivityResultLauncher
     * ayant lancé buildSignInIntent(). Échange le code d'autorisation contre
     * un jeton et persiste le résultat. */
    suspend fun handleSignInResult(data: Intent?): Result<Unit> {
        val response = AuthorizationResponse.fromIntent(data ?: return Result.failure(
            IllegalStateException("Réponse de connexion vide.")
        ))
        val exception = AuthorizationException.fromIntent(data)
        if (exception != null) return Result.failure(exception)
        if (response == null) return Result.failure(IllegalStateException("Réponse d'autorisation invalide."))

        authState.update(response, null)
        return try {
            val tokenResponse = performTokenRequest(response)
            authState.update(tokenResponse, null)
            persist()
            Result.success(Unit)
        } catch (e: AuthorizationException) {
            Result.failure(e)
        }
    }

    private suspend fun performTokenRequest(response: AuthorizationResponse): TokenResponse =
        suspendCancellableCoroutine { cont ->
            authService.performTokenRequest(
                response.createTokenExchangeRequest(),
                NoClientAuthentication.INSTANCE,
            ) { tokenResponse, ex ->
                when {
                    tokenResponse != null -> cont.resume(tokenResponse)
                    ex != null -> cont.resumeWithException(ex)
                    else -> cont.resumeWithException(IllegalStateException("Échange de jeton sans réponse."))
                }
            }
        }

    /** Access token valide pour un appel Gmail — renouvelé automatiquement
     * via le refresh token si expiré (géré par AppAuth). Lève une exception
     * si l'utilisateur n'est pas connecté : appeler isSignedIn avant. */
    suspend fun getFreshAccessToken(): String = suspendCancellableCoroutine { cont ->
        authState.performActionWithFreshTokens(authService) { accessToken, _, ex ->
            when {
                accessToken != null -> {
                    persist()
                    cont.resume(accessToken)
                }
                ex != null -> cont.resumeWithException(ex)
                else -> cont.resumeWithException(IllegalStateException("Jeton d'accès indisponible."))
            }
        }
    }

    fun signOut() {
        authState = AuthState(serviceConfig)
        tokenStore.clearAuthState()
    }

    fun dispose() {
        authService.dispose()
    }

    companion object {
        const val GMAIL_SCOPE = "https://www.googleapis.com/auth/gmail.modify"
    }
}
