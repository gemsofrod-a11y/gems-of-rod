package fr.gemsofrod.assistant.network

data class VoiceRequest(val text: String, val session_id: String = "default")
data class VoiceResponse(val reply: String, val actions: List<String> = emptyList())

data class PendingItem(
    val id: String,
    val message_id: String,
    val thread_id: String?,
    val from_addr: String,
    val subject: String,
    val snippet: String,
    val category: String,
    val reasoning: String,
    val suggested_reply: String?,
    val status: String,
    val created_at: String,
)

data class ResolveRequest(val edited_reply: String? = null)
data class MessageResponse(val message: String)
data class DigestResponse(val actions: Map<String, Int> = emptyMap(), val pending: Int = 0)
