package fr.gemsofrod.assistant.gmail

/** Formes brutes de l'API Gmail REST v1 — voir GmailClient pour les formes
 * simplifiées utilisées par le reste de l'app (équivalent de
 * gmail_client.parse_message côté Python). */

data class GmailMessagesListResponse(
    val messages: List<GmailMessageRef>? = null,
    val resultSizeEstimate: Int = 0,
)

data class GmailMessageRef(val id: String, val threadId: String? = null)

data class GmailMessage(
    val id: String,
    val threadId: String? = null,
    val labelIds: List<String>? = null,
    val snippet: String? = null,
    val payload: GmailMessagePart? = null,
)

data class GmailMessagePart(
    val partId: String? = null,
    val mimeType: String? = null,
    val filename: String? = null,
    val headers: List<GmailHeader>? = null,
    val body: GmailMessageBody? = null,
    val parts: List<GmailMessagePart>? = null,
)

data class GmailHeader(val name: String, val value: String)

data class GmailMessageBody(
    val attachmentId: String? = null,
    val size: Int = 0,
    val data: String? = null,
)

data class GmailAttachment(val attachmentId: String? = null, val size: Int = 0, val data: String? = null)

data class GmailLabelsListResponse(val labels: List<GmailLabel>? = null)

data class GmailLabel(
    val id: String? = null,
    val name: String,
    val labelListVisibility: String? = "labelShow",
    val messageListVisibility: String? = "show",
)

data class GmailModifyRequest(
    val addLabelIds: List<String>? = null,
    val removeLabelIds: List<String>? = null,
)

data class GmailDraftMessage(val raw: String, val threadId: String? = null)
data class GmailDraftRequest(val message: GmailDraftMessage)
data class GmailDraftIdRequest(val id: String)
data class GmailSendRequest(val raw: String)

data class GmailDraftResponse(val id: String, val message: GmailMessageRef? = null)
data class GmailSendResponse(val id: String)

/** Pièce jointe repérée sur un email (nom, type, identifiant) — même forme
 * que ce que gmail_client._walk_attachments produisait côté Python. */
data class AttachmentInfo(
    val attachmentId: String,
    val filename: String,
    val mimeType: String,
    val size: Int,
)

/** Email simplifié, équivalent de gmail_client.parse_message. */
data class ParsedEmail(
    val id: String,
    val threadId: String?,
    val messageIdHeader: String,
    val from: String,
    val to: String,
    val subject: String,
    val date: String,
    val snippet: String,
    val bodyText: String,
    val labels: List<String>,
    val listUnsubscribe: String,
    val listUnsubscribePost: String,
    val attachments: List<AttachmentInfo>,
)

/** Résultat léger d'une recherche (pas le corps complet), équivalent de
 * gmail_client.search_messages. */
data class EmailSummary(
    val id: String,
    val threadId: String?,
    val from: String,
    val subject: String,
    val date: String,
    val snippet: String,
    val labels: List<String>,
)
