package fr.gemsofrod.assistant

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.gemsofrod.assistant.ai.SaphirAgent
import fr.gemsofrod.assistant.auth.GoogleAuthManager
import fr.gemsofrod.assistant.auth.TokenStore
import fr.gemsofrod.assistant.data.LocalStore
import fr.gemsofrod.assistant.data.PendingAction
import fr.gemsofrod.assistant.gmail.GmailClient
import fr.gemsofrod.assistant.triage.TriageScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatLine(val fromUser: Boolean, val text: String)

data class AssistantUiState(
    val isSignedIn: Boolean = false,
    val hasApiKey: Boolean = false,
    val isConfigured: Boolean = false,
    val anthropicApiKey: String = "",
    val transcript: List<ChatLine> = emptyList(),
    val isBusy: Boolean = false,
    val error: String? = null,
    val pendingItems: List<PendingAction> = emptyList(),
    val pendingCount: Int = 0,
)

/** Tout tourne désormais directement sur le téléphone : plus d'appel à un
 * serveur distant. GoogleAuthManager gère la connexion Gmail, SaphirAgent
 * appelle l'API Anthropic directement avec la clé enregistrée dans
 * Réglages, LocalStore remplace la base SQLite du serveur.
 */
class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenStore = TokenStore(application)
    private val localStore = LocalStore(application)
    private val authManager = GoogleAuthManager(
        application, tokenStore, AppConfig.GOOGLE_OAUTH_CLIENT_ID, AppConfig.GOOGLE_OAUTH_REDIRECT_URI,
    )
    private val gmailClient = GmailClient(authManager)
    private val saphirAgent = SaphirAgent(
        context = application,
        gmailClient = gmailClient,
        localStore = localStore,
        apiKeyProvider = { tokenStore.anthropicApiKey },
    )

    private val sessionId = UUID.randomUUID().toString()

    private val _state = MutableStateFlow(AssistantUiState())
    val state: StateFlow<AssistantUiState> = _state

    var onSpeakReply: ((String) -> Unit)? = null

    init {
        refreshConfigState()
        if (_state.value.isConfigured) {
            TriageScheduler.schedule(application)
            refreshPendingDigest()
        }
    }

    private fun refreshConfigState() {
        val signedIn = authManager.isSignedIn
        val apiKey = tokenStore.anthropicApiKey
        _state.update {
            it.copy(
                isSignedIn = signedIn,
                hasApiKey = apiKey.isNotBlank(),
                isConfigured = signedIn && apiKey.isNotBlank(),
                anthropicApiKey = apiKey,
            )
        }
    }

    fun buildSignInIntent(): Intent = authManager.buildSignInIntent()

    fun handleSignInResult(data: Intent?) {
        viewModelScope.launch {
            authManager.handleSignInResult(data).onFailure { e ->
                _state.update { it.copy(error = "Connexion Google échouée : ${e.message}") }
            }
            refreshConfigState()
            if (_state.value.isConfigured) {
                TriageScheduler.schedule(getApplication())
                refreshPendingDigest()
            }
        }
    }

    fun signOut() {
        authManager.signOut()
        TriageScheduler.cancel(getApplication())
        refreshConfigState()
    }

    fun saveApiKey(apiKey: String) {
        tokenStore.anthropicApiKey = apiKey
        refreshConfigState()
        if (_state.value.isConfigured) {
            TriageScheduler.schedule(getApplication())
            refreshPendingDigest()
        }
    }

    fun sendVoiceText(text: String) {
        if (!_state.value.isConfigured) {
            _state.update {
                it.copy(error = "Connectez-vous à Google et renseignez votre clé Anthropic dans Réglages.")
            }
            return
        }
        _state.update { it.copy(transcript = it.transcript + ChatLine(true, text), isBusy = true, error = null) }
        viewModelScope.launch {
            try {
                val result = saphirAgent.handleTurn(text, sessionId)
                _state.update { it.copy(transcript = it.transcript + ChatLine(false, result.reply), isBusy = false) }
                onSpeakReply?.invoke(result.reply)
                refreshPendingDigest()
            } catch (e: Exception) {
                _state.update { it.copy(isBusy = false, error = "Erreur : ${e.message}") }
            }
        }
    }

    fun refreshPendingDigest() {
        viewModelScope.launch {
            try {
                val pending = localStore.listPending()
                val (_, pendingCount) = localStore.countTodayActions()
                _state.update { it.copy(pendingItems = pending, pendingCount = pendingCount) }
            } catch (_: Exception) {
                // Le résumé n'est qu'indicatif ; on ne bloque pas l'UI en cas d'échec.
            }
        }
    }

    fun approvePending(id: String, editedReply: String?) {
        viewModelScope.launch {
            try {
                saphirAgent.resolvePendingAction(id, "approve", editedReply)
                refreshPendingDigest()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Impossible d'approuver : ${e.message}") }
            }
        }
    }

    fun rejectPending(id: String) {
        viewModelScope.launch {
            try {
                saphirAgent.resolvePendingAction(id, "reject", null)
                refreshPendingDigest()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Impossible de rejeter : ${e.message}") }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    override fun onCleared() {
        authManager.dispose()
        super.onCleared()
    }
}
