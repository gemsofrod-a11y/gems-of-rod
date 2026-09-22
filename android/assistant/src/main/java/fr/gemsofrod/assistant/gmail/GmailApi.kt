package fr.gemsofrod.assistant.gmail

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/** API Gmail REST v1 appelée directement depuis le téléphone (plus de
 * backend intermédiaire) — voir GmailClient pour la logique métier
 * (équivalent Kotlin de backend/app/gmail_client.py). Auth : jeton Bearer
 * ajouté par un intercepteur OkHttp dans GmailClientFactory.
 */
interface GmailApi {
    @GET("gmail/v1/users/me/messages")
    suspend fun listMessages(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
    ): GmailMessagesListResponse

    @GET("gmail/v1/users/me/messages/{id}")
    suspend fun getMessage(
        @Path("id") id: String,
        @Query("format") format: String = "full",
    ): GmailMessage

    @GET("gmail/v1/users/me/messages/{id}/attachments/{attachmentId}")
    suspend fun getAttachment(
        @Path("id") messageId: String,
        @Path("attachmentId") attachmentId: String,
    ): GmailAttachment

    @POST("gmail/v1/users/me/messages/{id}/modify")
    suspend fun modifyMessage(@Path("id") id: String, @Body body: GmailModifyRequest): GmailMessage

    @POST("gmail/v1/users/me/messages/{id}/trash")
    suspend fun trashMessage(@Path("id") id: String): GmailMessage

    @GET("gmail/v1/users/me/labels")
    suspend fun listLabels(): GmailLabelsListResponse

    @POST("gmail/v1/users/me/labels")
    suspend fun createLabel(@Body body: GmailLabel): GmailLabel

    @POST("gmail/v1/users/me/drafts")
    suspend fun createDraft(@Body body: GmailDraftRequest): GmailDraftResponse

    @POST("gmail/v1/users/me/drafts/send")
    suspend fun sendDraft(@Body body: GmailDraftIdRequest): GmailSendResponse

    @POST("gmail/v1/users/me/messages/send")
    suspend fun sendRawMessage(@Body body: GmailSendRequest): GmailSendResponse
}
