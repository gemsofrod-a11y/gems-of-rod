package fr.gemsofrod.assistant.triage

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fr.gemsofrod.assistant.AppConfig
import fr.gemsofrod.assistant.ai.Classifier
import fr.gemsofrod.assistant.auth.GoogleAuthManager
import fr.gemsofrod.assistant.auth.TokenStore
import fr.gemsofrod.assistant.data.LocalStore
import fr.gemsofrod.assistant.gmail.GmailClient
import fr.gemsofrod.assistant.gmail.ParsedEmail

/** Cycle de triage automatique en tâche de fond — port Kotlin de
 * backend/app/triage.py : parcourt les nouveaux emails, les fait classer,
 * et applique l'action correspondant à l'autonomie choisie (voir
 * ai/Classifier.kt). Tourne périodiquement via WorkManager (voir
 * TriageScheduler) — au moins toutes les 15 minutes, le minimum
 * qu'Android autorise pour un travail périodique (contre 5 minutes en
 * continu côté serveur).
 */
class TriageWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val tokenStore = TokenStore(applicationContext)
        val apiKey = tokenStore.anthropicApiKey
        if (apiKey.isBlank()) return Result.success()

        val authManager = GoogleAuthManager(
            applicationContext, tokenStore, AppConfig.GOOGLE_OAUTH_CLIENT_ID, AppConfig.GOOGLE_OAUTH_REDIRECT_URI,
        )
        if (!authManager.isSignedIn) {
            authManager.dispose()
            return Result.success()
        }

        val gmailClient = GmailClient(authManager)
        val localStore = LocalStore(applicationContext)

        return try {
            runCycle(gmailClient, localStore, apiKey)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        } finally {
            authManager.dispose()
        }
    }

    private suspend fun runCycle(gmailClient: GmailClient, localStore: LocalStore, apiKey: String) {
        val messages = gmailClient.searchMessages(TRIAGE_QUERY, TRIAGE_MAX_RESULTS)
        for (meta in messages) {
            if (localStore.isProcessed(meta.id)) continue
            try {
                processOne(meta.id, gmailClient, localStore, apiKey)
            } catch (e: Exception) {
                localStore.logAction(meta.id, "triage_error", e.message ?: "erreur inconnue")
            }
        }
    }

    private suspend fun processOne(
        messageId: String,
        gmailClient: GmailClient,
        localStore: LocalStore,
        apiKey: String,
    ) {
        val email = gmailClient.getMessage(messageId)
        val decision = Classifier.classifyEmail(apiKey, AppConfig.ANTHROPIC_MODEL, email)
        var category = decision.category
        val subject = email.subject.ifBlank { "(sans objet)" }

        when (category) {
            "ignore" -> {
                if (looksLikeSpam(email)) gmailClient.markSpam(messageId) else gmailClient.markRead(messageId)
                localStore.logAction(messageId, "auto_ignore", decision.reasoning)
            }
            "auto_delete" -> {
                val unsub = gmailClient.unsubscribe(messageId)
                gmailClient.trashMessage(messageId)
                localStore.logAction(messageId, "auto_unsubscribe", unsub.detail)
                localStore.logAction(messageId, "auto_delete", subject)
            }
            "auto_triage" -> {
                decision.suggestedLabel?.let { gmailClient.addLabel(messageId, it) }
                gmailClient.addLabel(messageId, LABEL_AUTO_TRAITE)
                gmailClient.archiveMessage(messageId)
                localStore.logAction(messageId, "auto_triage", decision.reasoning)
            }
            "auto_reply" -> {
                val reply = decision.suggestedReply
                if (!reply.isNullOrBlank()) {
                    gmailClient.sendReply(messageId, reply)
                    gmailClient.addLabel(messageId, LABEL_AUTO_REPONDU)
                    gmailClient.archiveMessage(messageId)
                    localStore.logAction(messageId, "auto_reply", reply)
                } else {
                    category = "needs_confirmation"
                }
            }
        }

        if (category == "needs_confirmation") {
            gmailClient.addLabel(messageId, LABEL_EN_ATTENTE)
            localStore.createPending(
                messageId = messageId,
                threadId = email.threadId,
                fromAddr = email.from,
                subject = email.subject,
                snippet = email.snippet,
                category = category,
                reasoning = decision.reasoning,
                suggestedReply = decision.suggestedReply,
            )
            localStore.logAction(messageId, "queued_for_confirmation", decision.reasoning)
            Notifications.notifyPending(applicationContext, subject, email.from)
        }

        localStore.markProcessed(messageId, category)
    }

    private fun looksLikeSpam(email: ParsedEmail): Boolean =
        email.bodyText.lowercase().contains("unsubscribe") && "SPAM" !in email.labels

    companion object {
        const val UNIQUE_WORK_NAME = "saphir_triage"
        private const val TRIAGE_QUERY = "in:inbox is:unread"
        private const val TRIAGE_MAX_RESULTS = 20
        private const val LABEL_AUTO_TRAITE = "GoR-Traité auto"
        private const val LABEL_AUTO_REPONDU = "GoR-Répondu auto"
        private const val LABEL_EN_ATTENTE = "GoR-En attente"
    }
}
