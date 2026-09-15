package fr.gemsofrod.assistant.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "assistant_settings")

private val KEY_BASE_URL = stringPreferencesKey("backend_base_url")
private val KEY_API_TOKEN = stringPreferencesKey("api_token")

class SettingsStore(private val context: Context) {

    val baseUrlFlow: Flow<String> =
        context.dataStore.data.map { it[KEY_BASE_URL] ?: "" }

    val apiTokenFlow: Flow<String> =
        context.dataStore.data.map { it[KEY_API_TOKEN] ?: "" }

    suspend fun currentBaseUrl(): String = baseUrlFlow.first()
    suspend fun currentApiToken(): String = apiTokenFlow.first()

    suspend fun save(baseUrl: String, apiToken: String) {
        context.dataStore.edit {
            it[KEY_BASE_URL] = baseUrl.trim()
            it[KEY_API_TOKEN] = apiToken.trim()
        }
    }
}
