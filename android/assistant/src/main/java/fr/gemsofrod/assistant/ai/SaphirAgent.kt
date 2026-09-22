package fr.gemsofrod.assistant.ai

import android.content.Context
import fr.gemsofrod.assistant.AppConfig
import fr.gemsofrod.assistant.data.ConversationTurn
import fr.gemsofrod.assistant.data.LocalStore
import fr.gemsofrod.assistant.gmail.EmailSummary
import fr.gemsofrod.assistant.gmail.GmailClient
import fr.gemsofrod.assistant.gmail.ParsedEmail
import org.json.JSONArray
import org.json.JSONObject

data class EmailRef(val messageId: String, val subject: String)
data class DraftRef(val messageId: String, val subject: String, val bodyText: String, val draftId: String?, val sent: Boolean)
data class DocumentRef(val messageId: String, val attachmentId: String, val filename: String)

data class SaphirTurnResult(
    val reply: String,
    val actions: List<String>,
    val emails: List<EmailRef>,
    val drafts: List<DraftRef>,
    val media: List<ImageResult>,
    val documents: List<DocumentRef>,
)

/** Boucle agentique (tool-use Anthropic) qui répond aux commandes vocales de
 * Sébastien depuis le téléphone — port Kotlin de backend/app/voice_agent.py.
 * Le texte renvoyé est destiné à être lu à voix haute : phrases courtes,
 * naturelles, sans markdown.
 */
class SaphirAgent(
    private val context: Context,
    private val gmailClient: GmailClient,
    private val localStore: LocalStore,
    private val apiKeyProvider: () -> String,
    private val model: String = AppConfig.ANTHROPIC_MODEL,
) {
    private val tools = SaphirTools.buildAll()

    private fun systemPrompt(): String = """
Tu es Saphir, l'assistant vocal personnel de Sébastien, fondateur de Gems of Rod (maison française de pierres précieuses et bijoux d'exception). C'est ton prénom : si Sébastien te demande comment tu t'appelles, réponds simplement « Saphir », sans plus d'explication à moins qu'il n'en redemande. Il te parle à voix haute depuis son téléphone et tes réponses sont lues à voix haute : réponds en français, avec des phrases courtes et naturelles, sans markdown, sans listes à puces.

Tu ne te limites pas à Gmail : tu es aussi un assistant généraliste. Pour la météo, utilise get_weather. Pour l'actualité, les infos récentes, ou toute question de culture générale dont tu n'es pas certain, utilise web_search plutôt que de répondre de mémoire — n'invente jamais un chiffre ou un fait qui pourrait avoir changé. Pour une photo ou un visuel, utilise search_images : elle cherche des images déjà existantes, elle ne génère rien — dis-le si Sébastien demande une image "générée" ou "inventée", ce n'est pas encore possible. Résume toujours la réponse en une ou deux phrases parlées, jamais une liste de résultats bruts.

Personnalité : inspire-toi de J.A.R.V.I.S., l'assistant de confiance calme, précis et discrètement spirituel. Pas de familiarité excessive ni d'exclamations : une élégance sobre, une pointe d'humour fin de temps en temps, jamais au détriment de la clarté. Vouvoie Sébastien. Sois proactif quand c'est pertinent (signaler quelque chose qu'il devrait savoir) sans jamais dépasser les règles d'autonomie ci-dessous.

Règles d'autonomie :
- Tâches simples (chercher, lire, classer, étiqueter, archiver un email, supprimer/désabonner une newsletter ou une publicité, ou envoyer une réponse quand Sébastien te donne une instruction claire et explicite) : agis directement.
- Pour tout ce qui touche à un prix ou une demande de devis, une prise de rendez-vous, un client VIP, une réclamation ou un sujet sensible, ne décide jamais seul : utilise list_pending_confirmations pour les lui présenter et resolve_pending seulement après qu'il a donné sa décision à voix haute.
- Si une demande est ambiguë, pose une question courte avant d'agir plutôt que de deviner.
- Confirme toujours brièvement ce que tu viens de faire (« C'est fait, j'ai archivé le mail de... », « Désabonné et supprimé. »).

Grille tarifaire (ci-dessous) : tu peux t'en servir pour informer Sébastien à l'oral, ou pour l'aider à chiffrer rapidement une demande (catégorie de pierre + poids en carats → tarif, ou poids en grammes × cours au gramme pour un métal). Partage librement les informations factuelles (délais, conditions, cours indicatifs, contenu de la clause de risque — reproduis-la fidèlement, jamais résumée de mémoire). En revanche, un chiffrage total ou un engagement de prix envoyé à un client reste soumis à la règle d'autonomie ci-dessus : pour un email déjà en attente, propose-le via list_pending_confirmations ; pour toute autre demande de devis (par exemple Sébastien te demande de reprendre un devis reçu d'un fournisseur en pièce jointe pour le refaire à son nom, avec sa propre grille et ses marges), rédige-le avec create_draft — jamais avec send_reply, même si la demande te paraît explicite : un prix engagé se relit toujours avant de partir, dans le brouillon affiché à l'écran ou directement dans Gmail.

Pour lire une pièce jointe PDF (devis fournisseur, fiche technique...), utilise read_pdf_attachment : le texte t'est donné pour que tu comprennes son contenu, et le document s'affiche à l'écran pour que Sébastien le lise en même temps que toi. Si un chiffre du PDF est illisible ou ambigu (mauvaise extraction, document scanné), dis-le clairement au lieu de deviner un montant. Important : ne dis jamais "je l'affiche à l'écran" ou "c'est fait" avant d'avoir réellement appelé get_email puis read_pdf_attachment et reçu leur résultat — si l'un des deux renvoie une erreur, dis-le clairement à Sébastien (par exemple le fichier est introuvable) au lieu de prétendre avoir réussi.

Quand Sébastien te demande de transformer un devis fournisseur (lu via read_pdf_attachment) en devis pour un client, ne rédige jamais sans avoir d'abord retrouvé et fait confirmer la bonne demande client : cherche avec search_emails un email récent qui correspond probablement (même type de pierre, poids proche, période proche), lis-le avec get_email pour vérifier, puis annonce ton meilleur candidat à Sébastien à l'oral et attends sa confirmation avant de continuer — s'il n'y a pas de candidat plausible, demande-lui directement à quel email ça correspond plutôt que de deviner. Une fois le lien confirmé, calcule le prix client en majorant le coût fournisseur indiqué dans le PDF de ${PricingReference.SUPPLIER_MARGIN_PERCENT} %, puis rédige le devis avec create_draft (jamais send_reply, comme pour toute demande de devis). Tout devis envoyé à un client — qu'il vienne directement de ta grille tarifaire ou de ce calcul avec marge sur un coût fournisseur — doit toujours mentionner explicitement deux choses : sa durée de validité de ${PricingReference.QUOTE_VALIDITY_DAYS} jours à compter de son émission (le cours des métaux précieux varie), et qu'un acompte de 30 % est requis à la commande, dont le versement par le client vaut validation et confirmation ferme de cette commande.

${PricingReference.TARIFF_REFERENCE}
""".trimIndent()

    private fun ParsedEmail.toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("thread_id", threadId ?: JSONObject.NULL)
        put("from", from)
        put("to", to)
        put("subject", subject)
        put("date", date)
        put("snippet", snippet)
        put("body_text", bodyText)
        put("labels", JSONArray(labels))
        put(
            "attachments",
            JSONArray(attachments.map {
                JSONObject()
                    .put("attachment_id", it.attachmentId)
                    .put("filename", it.filename)
                    .put("mime_type", it.mimeType)
                    .put("size", it.size)
            }),
        )
    }

    private fun EmailSummary.toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("thread_id", threadId ?: JSONObject.NULL)
        put("from", from)
        put("subject", subject)
        put("date", date)
        put("snippet", snippet)
        put("labels", JSONArray(labels))
    }

    private fun ImageResult.toJson(): JSONObject = JSONObject().apply {
        if (error != null) {
            put("error", error)
        } else {
            put("url", url)
            put("thumbnail", thumbnail)
            put("title", title)
            put("creator", creator)
            put("source", source)
            put("license", license)
        }
    }

    private class DispatchAccumulator {
        val emails = mutableListOf<EmailRef>()
        val drafts = mutableListOf<DraftRef>()
        val media = mutableListOf<ImageResult>()
        val documents = mutableListOf<DocumentRef>()
    }

    private suspend fun dispatch(name: String, input: JSONObject, acc: DispatchAccumulator): String {
        return try {
            when (name) {
                "search_emails" -> {
                    val results = gmailClient.searchMessages(
                        input.getString("query"), input.optInt("max_results", 10)
                    )
                    acc.emails.addAll(results.map { EmailRef(it.id, it.subject.ifBlank { "(sans objet)" }) })
                    JSONArray(results.map { it.toJson() }).toString()
                }
                "get_email" -> {
                    val email = gmailClient.getMessage(input.getString("message_id"))
                    acc.emails.add(EmailRef(email.id, email.subject.ifBlank { "(sans objet)" }))
                    email.toJson().toString()
                }
                "send_reply" -> {
                    val messageId = input.getString("message_id")
                    val bodyText = input.getString("body_text")
                    val email = gmailClient.getMessage(messageId)
                    val subject = email.subject.ifBlank { "(sans objet)" }
                    val sentId = gmailClient.sendReply(messageId, bodyText)
                    localStore.logAction(messageId, "voice_send_reply", bodyText)
                    acc.drafts.add(DraftRef(messageId, subject, bodyText, null, true))
                    "Réponse envoyée (id $sentId)."
                }
                "create_draft" -> {
                    val messageId = input.getString("message_id")
                    val bodyText = input.getString("body_text")
                    val email = gmailClient.getMessage(messageId)
                    val subject = email.subject.ifBlank { "(sans objet)" }
                    val draftId = gmailClient.createDraftReply(messageId, bodyText)
                    localStore.logAction(messageId, "voice_create_draft", bodyText)
                    acc.drafts.add(DraftRef(messageId, subject, bodyText, draftId, false))
                    "Brouillon créé (id $draftId)."
                }
                "archive_email" -> {
                    val messageId = input.getString("message_id")
                    val email = gmailClient.getMessage(messageId)
                    val subject = email.subject.ifBlank { "(sans objet)" }
                    gmailClient.archiveMessage(messageId)
                    localStore.logAction(messageId, "voice_archive", subject)
                    "Email « $subject » archivé."
                }
                "label_email" -> {
                    val messageId = input.getString("message_id")
                    val label = input.getString("label")
                    gmailClient.addLabel(messageId, label)
                    localStore.logAction(messageId, "voice_label", label)
                    "Étiquette « $label » appliquée."
                }
                "mark_spam" -> {
                    val messageId = input.getString("message_id")
                    gmailClient.markSpam(messageId)
                    localStore.logAction(messageId, "voice_spam", "")
                    "Email marqué comme spam."
                }
                "trash_email" -> {
                    val messageId = input.getString("message_id")
                    val email = gmailClient.getMessage(messageId)
                    val subject = email.subject.ifBlank { "(sans objet)" }
                    gmailClient.trashMessage(messageId)
                    localStore.logAction(messageId, "voice_trash", subject)
                    "Email « $subject » mis à la corbeille."
                }
                "unsubscribe_email" -> {
                    val messageId = input.getString("message_id")
                    val result = gmailClient.unsubscribe(messageId)
                    localStore.logAction(messageId, "voice_unsubscribe", result.detail)
                    JSONObject()
                        .put("attempted", result.attempted)
                        .put("method", result.method)
                        .put("success", result.success ?: JSONObject.NULL)
                        .put("detail", result.detail)
                        .toString()
                }
                "list_pending_confirmations" -> {
                    val pending = localStore.listPending()
                    JSONArray(
                        pending.map {
                            JSONObject()
                                .put("id", it.id)
                                .put("message_id", it.messageId)
                                .put("from_addr", it.fromAddr)
                                .put("subject", it.subject)
                                .put("snippet", it.snippet)
                                .put("category", it.category)
                                .put("reasoning", it.reasoning)
                                .put("suggested_reply", it.suggestedReply ?: JSONObject.NULL)
                        }
                    ).toString()
                }
                "resolve_pending" -> resolvePendingAction(
                    input.getString("pending_id"),
                    input.getString("decision"),
                    if (input.has("edited_reply") && !input.isNull("edited_reply")) input.getString("edited_reply") else null,
                )
                "get_today_digest" -> {
                    val (actionCounts, pendingCount) = localStore.countTodayActions()
                    val actionsJson = JSONObject()
                    for ((actionType, count) in actionCounts) actionsJson.put(actionType, count)
                    JSONObject()
                        .put("actions", actionsJson)
                        .put("pending", pendingCount)
                        .toString()
                }
                "get_weather" -> WeatherClient.getWeather(input.getString("location")).toString()
                "search_images" -> {
                    val results = ImageSearchClient.searchImages(
                        input.getString("query"), input.optInt("count", 3)
                    )
                    acc.media.addAll(results.filter { it.url != null })
                    JSONArray(results.map { it.toJson() }).toString()
                }
                "read_pdf_attachment" -> {
                    val messageId = input.getString("message_id")
                    val attachmentId = input.getString("attachment_id")
                    val filename = if (input.has("filename")) input.optString("filename") else "document.pdf"
                    println("[read_pdf_attachment] appel : message_id=$messageId attachment_id=$attachmentId filename=$filename")
                    val data = gmailClient.getAttachmentBytes(messageId, attachmentId)
                    val text = PdfTextExtractor.extractText(context, data)
                    acc.documents.add(DocumentRef(messageId, attachmentId, filename.ifBlank { "document.pdf" }))
                    println("[read_pdf_attachment] OK : ${data.size} octets récupérés, ${text.length} caractères extraits.")
                    text
                }
                else -> "Outil inconnu : $name"
            }
        } catch (e: Exception) {
            "Erreur lors de l'exécution de $name : ${e.message}"
        }
    }

    suspend fun resolvePendingAction(pendingId: String, decision: String, editedReply: String?): String {
        val pending = localStore.getPending(pendingId) ?: return "Cet email en attente est introuvable."
        if (decision == "reject") {
            localStore.resolvePending(pendingId, "rejected")
            localStore.logAction(pending.messageId, "voice_reject_pending", "")
            return "Compris, je n'envoie rien."
        }
        val replyText = editedReply ?: pending.suggestedReply
        ?: return "Aucune réponse suggérée à envoyer pour cet email."
        gmailClient.sendReply(pending.messageId, replyText)
        localStore.resolvePending(pendingId, "approved")
        localStore.logAction(pending.messageId, "voice_approve_pending", replyText)
        return "Réponse envoyée."
    }

    suspend fun handleTurn(text: String, sessionId: String = "default"): SaphirTurnResult {
        val apiKey = apiKeyProvider()
        val history = localStore.loadConversation(sessionId).toMutableList()
        history.add(ConversationTurn("user", text))

        val messages = JSONArray()
        for (turn in history.takeLast(CONTEXT_WINDOW)) {
            messages.put(JSONObject().put("role", turn.role).put("content", turn.text))
        }

        val actions = mutableListOf<String>()
        val acc = DispatchAccumulator()
        var finalReply: String? = null

        for (round in 0 until MAX_ROUNDS) {
            val response = AnthropicClient.createMessage(
                apiKey = apiKey, model = model, system = systemPrompt(), messages = messages, tools = tools,
            )
            val contentBlocks = response.optJSONArray("content") ?: JSONArray()
            messages.put(JSONObject().put("role", "assistant").put("content", contentBlocks))

            val stopReason = response.optString("stop_reason")
            if (stopReason != "tool_use") {
                finalReply = (0 until contentBlocks.length())
                    .map { contentBlocks.getJSONObject(it) }
                    .filter { it.optString("type") == "text" }
                    .joinToString("") { it.optString("text") }
                    .trim()
                break
            }

            val toolResults = JSONArray()
            for (i in 0 until contentBlocks.length()) {
                val block = contentBlocks.getJSONObject(i)
                if (block.optString("type") != "tool_use") continue
                val toolName = block.getString("name")
                val toolInput = block.optJSONObject("input") ?: JSONObject()
                val result = dispatch(toolName, toolInput, acc)
                if (toolName !in SaphirTools.READONLY_TOOLS) actions.add("$toolName: $result")
                toolResults.put(
                    JSONObject()
                        .put("type", "tool_result")
                        .put("tool_use_id", block.getString("id"))
                        .put("content", result)
                )
            }
            messages.put(JSONObject().put("role", "user").put("content", toolResults))
        }

        val reply = finalReply ?: "Je n'ai pas réussi à terminer cette action, peux-tu reformuler ?"
        history.add(ConversationTurn("assistant", reply))
        localStore.saveConversation(sessionId, history.takeLast(CONTEXT_WINDOW))

        return SaphirTurnResult(
            reply = reply,
            actions = actions,
            emails = acc.emails,
            drafts = acc.drafts,
            media = acc.media,
            documents = acc.documents,
        )
    }

    companion object {
        private const val CONTEXT_WINDOW = 20
        private const val MAX_ROUNDS = 6
    }
}
