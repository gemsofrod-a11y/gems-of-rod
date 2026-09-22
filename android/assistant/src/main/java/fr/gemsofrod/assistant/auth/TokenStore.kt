package fr.gemsofrod.assistant.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/** Stockage chiffré (Android Keystore) des secrets de l'app : l'état
 * d'autorisation Google (AuthState d'AppAuth, contient le refresh token) et
 * la clé API Anthropic. Remplace les variables d'environnement du serveur
 * (GOOGLE_TOKEN_JSON, ANTHROPIC_API_KEY) — tout reste sur le téléphone.
 */
class TokenStore(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "assistant_secrets",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    var authStateJson: String?
        get() = prefs.getString(KEY_AUTH_STATE, null)
        set(value) = prefs.edit().putString(KEY_AUTH_STATE, value).apply()

    var anthropicApiKey: String
        get() = prefs.getString(KEY_ANTHROPIC_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_ANTHROPIC_KEY, value.trim()).apply()

    fun clearAuthState() {
        prefs.edit().remove(KEY_AUTH_STATE).apply()
    }

    companion object {
        private const val KEY_AUTH_STATE = "google_auth_state"
        private const val KEY_ANTHROPIC_KEY = "anthropic_api_key"
    }
}
