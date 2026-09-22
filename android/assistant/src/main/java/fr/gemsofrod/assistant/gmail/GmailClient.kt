package fr.gemsofrod.assistant.gmail

import android.util.Base64
import fr.gemsofrod.assistant.auth.GoogleAuthManager
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** Port Kotlin de backend/app/gmail_client.py : mêmes opérations (recherche,
 * lecture, réponse, archivage, étiquetage, spam, corbeille, désabonnement,
 * pièces jointes), mais parlant directement à l'API Gmail REST depuis le
 * téléphone avec le jeton OAuth de GoogleAuthManager — plus de serveur
 * intermédiaire.
 *
 * Scope unique : gmail.modify (lecture, envoi, gestion des libellés,
 * archivage/corbeille — pas de suppression définitive : un message mis à la
 * corbeille reste récupérable 30 jours, comme dans Gmail).
 */
class GmailClient(private val authManager: GoogleAuthManager) {

    private val plainHttp = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private var cachedApi: GmailApi? = null
    private var cachedToken: String? = null
    private val labelCache = mutableMapOf<String, String>()

    private suspend fun api(): GmailApi {
        val token = authManager.getFreshAccessToken()
        if (cachedApi == null || token != cachedToken) {
            cachedToken = token
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val req = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(req)
                }
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            cachedApi = Retrofit.Builder()
                .baseUrl("https://gmail.googleapis.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GmailApi::class.java)
        }
        return cachedApi!!
    }

    private fun header(headers: List<GmailHeader>?, name: String): String =
        headers?.firstOrNull { it.name.equals(name, ignoreCase = true) }?.value ?: ""

    private fun extractBodyText(part: GmailMessagePart?): String {
        if (part == null) return ""
        if (part.mimeType == "text/plain" && !part.body?.data.isNullOrEmpty()) {
            return decodeBase64Url(part.body!!.data!!)
        }
        for (child in part.parts.orEmpty()) {
            val text = extractBodyText(child)
            if (text.isNotEmpty()) return text
        }
        if (!part.body?.data.isNullOrEmpty()) {
            return decodeBase64Url(part.body!!.data!!)
        }
        return ""
    }

    private fun walkAttachments(part: GmailMessagePart?, out: MutableList<AttachmentInfo>) {
        if (part == null) return
        val filename = part.filename
        val attachmentId = part.body?.attachmentId
        if (!filename.isNullOrEmpty() && !attachmentId.isNullOrEmpty()) {
            out.add(
                AttachmentInfo(
                    attachmentId = attachmentId,
                    filename = filename,
                    mimeType = part.mimeType ?: "",
                    size = part.body?.size ?: 0,
                )
            )
        }
        for (child in part.parts.orEmpty()) walkAttachments(child, out)
    }

    private fun parseMessage(raw: GmailMessage): ParsedEmail {
        val headers = raw.payload?.headers
        val attachments = mutableListOf<AttachmentInfo>()
        walkAttachments(raw.payload, attachments)
        return ParsedEmail(
            id = raw.id,
            threadId = raw.threadId,
            messageIdHeader = header(headers, "Message-Id"),
            from = header(headers, "From"),
            to = header(headers, "To"),
            subject = header(headers, "Subject"),
            date = header(headers, "Date"),
            snippet = raw.snippet ?: "",
            bodyText = extractBodyText(raw.payload),
            labels = raw.labelIds ?: emptyList(),
            listUnsubscribe = header(headers, "List-Unsubscribe"),
            listUnsubscribePost = header(headers, "List-Unsubscribe-Post"),
            attachments = attachments,
        )
    }

    suspend fun searchMessages(query: String, maxResults: Int = 10): List<EmailSummary> {
        val list = api().listMessages(query, maxResults)
        return list.messages.orEmpty().map { ref ->
            val full = api().getMessage(ref.id, format = "metadata")
            val headers = full.payload?.headers
            EmailSummary(
                id = full.id,
                threadId = full.threadId,
                from = header(headers, "From"),
                subject = header(headers, "Subject"),
                date = header(headers, "Date"),
                snippet = full.snippet ?: "",
                labels = full.labelIds ?: emptyList(),
            )
        }
    }

    suspend fun getMessage(messageId: String): ParsedEmail = parseMessage(api().getMessage(messageId, "full"))

    suspend fun getAttachmentBytes(messageId: String, attachmentId: String): ByteArray {
        val attachment = api().getAttachment(messageId, attachmentId)
        return decodeBase64UrlBytes(attachment.data ?: "")
    }

    private suspend fun getOrCreateLabelId(name: String): String {
        labelCache[name]?.let { return it }
        val existing = api().listLabels().labels.orEmpty().firstOrNull { it.name == name }
        val id = existing?.id ?: api().createLabel(GmailLabel(name = name)).id
        ?: error("Impossible de créer l'étiquette « $name ».")
        labelCache[name] = id
        return id
    }

    suspend fun addLabel(messageId: String, labelName: String) {
        val labelId = getOrCreateLabelId(labelName)
        api().modifyMessage(messageId, GmailModifyRequest(addLabelIds = listOf(labelId)))
    }

    suspend fun archiveMessage(messageId: String) {
        api().modifyMessage(messageId, GmailModifyRequest(removeLabelIds = listOf("INBOX")))
    }

    suspend fun markRead(messageId: String) {
        api().modifyMessage(messageId, GmailModifyRequest(removeLabelIds = listOf("UNREAD")))
    }

    suspend fun markSpam(messageId: String) {
        api().modifyMessage(
            messageId,
            GmailModifyRequest(addLabelIds = listOf("SPAM"), removeLabelIds = listOf("INBOX")),
        )
    }

    suspend fun trashMessage(messageId: String) {
        api().trashMessage(messageId)
    }

    data class UnsubscribeResult(val attempted: Boolean, val method: String, val success: Boolean?, val detail: String)

    private val listUnsubscribeEntryRegex = Regex("<([^>]+)>")

    /** Tentative de désabonnement via l'en-tête List-Unsubscribe (RFC 8058 :
     * POST one-click quand disponible, sinon GET, sinon mailto:). Best-effort :
     * certains expéditeurs exigent une confirmation manuelle infranchissable
     * automatiquement, le résultat le précise. */
    suspend fun unsubscribe(messageId: String): UnsubscribeResult {
        val original = getMessage(messageId)
        val candidates = listUnsubscribeEntryRegex.findAll(original.listUnsubscribe)
            .map { it.groupValues[1] }.toList()
        if (candidates.isEmpty()) {
            return UnsubscribeResult(false, "none", null, "Aucun en-tête List-Unsubscribe.")
        }
        val httpsLinks = candidates.filter { it.startsWith("http://", true) || it.startsWith("https://", true) }
        val mailtoLinks = candidates.filter { it.startsWith("mailto:", true) }
        val oneClick = original.listUnsubscribePost.contains("one-click", ignoreCase = true)

        if (httpsLinks.isNotEmpty() && oneClick) {
            val url = httpsLinks.first()
            return try {
                val body = FormBody.Builder().add("List-Unsubscribe", "One-Click").build()
                val resp = plainHttp.newCall(Request.Builder().url(url).post(body).build()).execute()
                UnsubscribeResult(true, "one-click", resp.isSuccessful, "POST $url -> ${resp.code}")
            } catch (e: Exception) {
                UnsubscribeResult(true, "one-click", false, e.message ?: "erreur réseau")
            }
        }
        if (httpsLinks.isNotEmpty()) {
            val url = httpsLinks.first()
            return try {
                val resp = plainHttp.newCall(Request.Builder().url(url).get().build()).execute()
                UnsubscribeResult(
                    true, "get", resp.isSuccessful,
                    "GET $url -> ${resp.code} (peut nécessiter une confirmation manuelle)",
                )
            } catch (e: Exception) {
                UnsubscribeResult(true, "get", false, e.message ?: "erreur réseau")
            }
        }
        if (mailtoLinks.isNotEmpty()) {
            val mailto = mailtoLinks.first().removePrefix("mailto:")
            val (toAddr, query) = mailto.split("?", limit = 2).let { it[0] to it.getOrElse(1) { "" } }
            var subject = "unsubscribe"
            for (part in query.split("&")) {
                val (key, value) = part.split("=", limit = 2).let { it[0] to it.getOrElse(1) { "" } }
                if (key.equals("subject", true) && value.isNotEmpty()) {
                    subject = java.net.URLDecoder.decode(value, "UTF-8")
                }
            }
            val raw = buildMimeMessage(to = toAddr, subject = subject, body = "")
            api().sendRawMessage(GmailSendRequest(raw))
            return UnsubscribeResult(true, "mailto", true, "Email envoyé à $toAddr")
        }
        return UnsubscribeResult(false, "none", null, "Aucune méthode de désabonnement exploitable.")
    }

    private fun buildMimeMessage(
        to: String,
        subject: String,
        body: String,
        inReplyTo: String? = null,
    ): String {
        val bodyBytes = body.toByteArray(Charsets.UTF_8)
        val encodedBody = Base64.encodeToString(bodyBytes, Base64.NO_WRAP)
            .chunked(76).joinToString("\r\n")
        val headers = buildList {
            add("To: $to")
            add("Subject: $subject")
            if (!inReplyTo.isNullOrEmpty()) {
                add("In-Reply-To: $inReplyTo")
                add("References: $inReplyTo")
            }
            add("MIME-Version: 1.0")
            add("Content-Type: text/plain; charset=UTF-8")
            add("Content-Transfer-Encoding: base64")
        }
        val mime = headers.joinToString("\r\n") + "\r\n\r\n" + encodedBody
        return encodeBase64UrlBytes(mime.toByteArray(Charsets.UTF_8))
    }

    /** Envoie une réponse — toujours en créant d'abord le brouillon Gmail
     * puis en l'envoyant (jamais un envoi direct sans brouillon). */
    suspend fun sendReply(messageId: String, bodyText: String): String {
        val draftId = createDraftReply(messageId, bodyText)
        return sendDraft(draftId)
    }

    suspend fun createDraftReply(messageId: String, bodyText: String): String {
        val original = getMessage(messageId)
        val subject = if (original.subject.startsWith("re:", true)) original.subject else "Re: ${original.subject}"
        val raw = buildMimeMessage(
            to = original.from, subject = subject, body = bodyText,
            inReplyTo = original.messageIdHeader.takeIf { it.isNotEmpty() },
        )
        val draft = api().createDraft(GmailDraftRequest(GmailDraftMessage(raw = raw, threadId = original.threadId)))
        return draft.id
    }

    suspend fun sendDraft(draftId: String): String = api().sendDraft(GmailDraftIdRequest(draftId)).id

    private fun decodeBase64Url(data: String): String = String(decodeBase64UrlBytes(data), Charsets.UTF_8)

    private fun decodeBase64UrlBytes(data: String): ByteArray =
        Base64.decode(data, Base64.URL_SAFE or Base64.NO_WRAP)

    private fun encodeBase64UrlBytes(data: ByteArray): String =
        Base64.encodeToString(data, Base64.URL_SAFE or Base64.NO_WRAP)
}
