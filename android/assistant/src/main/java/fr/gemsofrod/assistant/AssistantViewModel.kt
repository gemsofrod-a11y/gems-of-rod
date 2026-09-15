package fr.gemsofrod.assistant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.gemsofrod.assistant.network.ApiClientFactory
import fr.gemsofrod.assistant.network.AssistantApi
import fr.gemsofrod.assistant.network.PendingItem
import fr.gemsofrod.assistant.network.ResolveRequest
import fr.gemsofrod.assistant.network.VoiceRequest
import fr.gemsofrod.assistant.settings.SettingsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatLine(val fromUser: Boolean, val text: String)

data class AssistantUiState(
    val baseUrl: String = "",
    val apiToken: String = "",
    val isConfigured: Boolean = false,
    val transcript: List<ChatLine> = emptyList(),
    val isBusy: Boolean = false,
    val error: String? = null,
    val pendingItems: List<PendingItem> = emptyList(),
    val pendingCount: Int = 0,
)

class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsStore = SettingsStore(application)
    private val sessionId = UUID.randomUUID().toString()

    private val _state = MutableStateFlow(AssistantUiState())
    val state: StateFlow<AssistantUiState> = _state

    var onSpeakReply: ((String) -> Unit)? = null

    init {
        viewModelScope.launch {
            val url = settingsStore.currentBaseUrl()
            val token = settingsStore.currentApiToken()
            _state.update { it.copy(baseUrl = url, apiToken = token, isConfigured = url.isNotBlank() && token.isNotBlank()) }
            if (_state.value.isConfigured) refreshPendingDigest()
        }
    }

    private fun api(): AssistantApi? {
        val s = _state.value
        if (s.baseUrl.isBlank() || s.apiToken.isBlank()) return null
        return ApiClientFactory.build(s.baseUrl, s.apiToken)
    }

    fun saveSettings(baseUrl: String, apiToken: String) {
        viewModelScope.launch {
            settingsStore.save(baseUrl, apiToken)
            _state.update {
                it.copy(baseUrl = baseUrl, apiToken = apiToken, isConfigured = baseUrl.isNotBlank() && apiToken.isNotBlank())
            }
            refreshPendingDigest()
        }
    }

    fun sendVoiceText(text: String) {
        val service = api() ?: run {
            _state.update { it.copy(error = "Configurez d'abord l'adresse du serveur dans Réglages.") }
            return
        }
        _state.update { it.copy(transcript = it.transcript + ChatLine(true, text), isBusy = true, error = null) }
        viewModelScope.launch {
            try {
                val resp = service.voice(VoiceRequest(text = text, session_id = sessionId))
                _state.update { it.copy(transcript = it.transcript + ChatLine(false, resp.reply), isBusy = false) }
                onSpeakReply?.invoke(resp.reply)
                refreshPendingDigest()
            } catch (e: Exception) {
                val message = "Erreur de connexion à l'assistant : ${e.message}"
                _state.update { it.copy(isBusy = false, error = message) }
            }
        }
    }

    fun refreshPendingDigest() {
        val service = api() ?: return
        viewModelScope.launch {
            try {
                val pending = service.listPending()
                val digest = service.digestToday()
                _state.update { it.copy(pendingItems = pending, pendingCount = digest.pending) }
            } catch (_: Exception) {
                // Le résumé n'est qu'indicatif ; on ne bloque pas l'UI si le serveur est injoignable.
            }
        }
    }

    fun approvePending(id: String, editedReply: String?) {
        val service = api() ?: return
        viewModelScope.launch {
            try {
                service.approvePending(id, ResolveRequest(editedReply))
                refreshPendingDigest()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Impossible d'approuver : ${e.message}") }
            }
        }
    }

    fun rejectPending(id: String) {
        val service = api() ?: return
        viewModelScope.launch {
            try {
                service.rejectPending(id)
                refreshPendingDigest()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Impossible de rejeter : ${e.message}") }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
