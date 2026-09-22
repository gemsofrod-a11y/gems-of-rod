package fr.gemsofrod.assistant.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

data class ConversationTurn(val role: String, val text: String)

data class PendingAction(
    val id: String,
    val messageId: String,
    val threadId: String?,
    val fromAddr: String,
    val subject: String,
    val snippet: String,
    val category: String,
    val reasoning: String,
    val suggestedReply: String?,
    val status: String,
    val createdAt: String,
    val resolvedAt: String? = null,
)

data class ActionLogEntry(
    val id: String,
    val messageId: String?,
    val actionType: String,
    val detail: String,
    val createdAt: String,
)

/** Stockage local (SQLite embarqué, sur le téléphone) — port Kotlin de
 * backend/app/db.py. Remplace le fichier SQLite du serveur : mêmes tables,
 * mêmes usages (dédoublonnage du tri automatique, emails en attente de
 * décision, journal des actions, historique de conversation). */
class LocalStore(context: Context) : SQLiteOpenHelper(context, "assistant.db", null, 1) {

    private val gson = Gson()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE processed_messages (
                message_id TEXT PRIMARY KEY,
                category TEXT NOT NULL,
                processed_at TEXT NOT NULL
            )"""
        )
        db.execSQL(
            """CREATE TABLE conversation_state (
                session_id TEXT PRIMARY KEY,
                history_json TEXT NOT NULL,
                updated_at TEXT NOT NULL
            )"""
        )
        db.execSQL(
            """CREATE TABLE pending_actions (
                id TEXT PRIMARY KEY,
                message_id TEXT NOT NULL,
                thread_id TEXT,
                from_addr TEXT,
                subject TEXT,
                snippet TEXT,
                category TEXT NOT NULL,
                reasoning TEXT,
                suggested_reply TEXT,
                status TEXT NOT NULL DEFAULT 'pending',
                created_at TEXT NOT NULL,
                resolved_at TEXT
            )"""
        )
        db.execSQL(
            """CREATE TABLE action_log (
                id TEXT PRIMARY KEY,
                message_id TEXT,
                action_type TEXT NOT NULL,
                detail TEXT,
                created_at TEXT NOT NULL
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS processed_messages")
        db.execSQL("DROP TABLE IF EXISTS conversation_state")
        db.execSQL("DROP TABLE IF EXISTS pending_actions")
        db.execSQL("DROP TABLE IF EXISTS action_log")
        onCreate(db)
    }

    private fun now(): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        fmt.timeZone = TimeZone.getTimeZone("UTC")
        return fmt.format(Date())
    }

    fun isProcessed(messageId: String): Boolean {
        readableDatabase.rawQuery(
            "SELECT 1 FROM processed_messages WHERE message_id = ?", arrayOf(messageId)
        ).use { return it.moveToFirst() }
    }

    fun markProcessed(messageId: String, category: String) {
        val values = ContentValues().apply {
            put("message_id", messageId)
            put("category", category)
            put("processed_at", now())
        }
        writableDatabase.insertWithOnConflict(
            "processed_messages", null, values, SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    fun logAction(messageId: String?, actionType: String, detail: String) {
        val values = ContentValues().apply {
            put("id", UUID.randomUUID().toString().replace("-", ""))
            put("message_id", messageId)
            put("action_type", actionType)
            put("detail", detail)
            put("created_at", now())
        }
        writableDatabase.insert("action_log", null, values)
    }

    fun createPending(
        messageId: String,
        threadId: String?,
        fromAddr: String,
        subject: String,
        snippet: String,
        category: String,
        reasoning: String,
        suggestedReply: String?,
    ): String {
        val id = UUID.randomUUID().toString().replace("-", "").take(10)
        val values = ContentValues().apply {
            put("id", id)
            put("message_id", messageId)
            put("thread_id", threadId)
            put("from_addr", fromAddr)
            put("subject", subject)
            put("snippet", snippet)
            put("category", category)
            put("reasoning", reasoning)
            put("suggested_reply", suggestedReply)
            put("status", "pending")
            put("created_at", now())
        }
        writableDatabase.insert("pending_actions", null, values)
        return id
    }

    private fun cursorToPending(c: android.database.Cursor): PendingAction = PendingAction(
        id = c.getString(c.getColumnIndexOrThrow("id")),
        messageId = c.getString(c.getColumnIndexOrThrow("message_id")),
        threadId = c.getStringOrNull("thread_id"),
        fromAddr = c.getStringOrNull("from_addr") ?: "",
        subject = c.getStringOrNull("subject") ?: "",
        snippet = c.getStringOrNull("snippet") ?: "",
        category = c.getString(c.getColumnIndexOrThrow("category")),
        reasoning = c.getStringOrNull("reasoning") ?: "",
        suggestedReply = c.getStringOrNull("suggested_reply"),
        status = c.getString(c.getColumnIndexOrThrow("status")),
        createdAt = c.getString(c.getColumnIndexOrThrow("created_at")),
        resolvedAt = c.getStringOrNull("resolved_at"),
    )

    private fun android.database.Cursor.getStringOrNull(column: String): String? {
        val idx = getColumnIndexOrThrow(column)
        return if (isNull(idx)) null else getString(idx)
    }

    fun listPending(status: String = "pending"): List<PendingAction> {
        readableDatabase.rawQuery(
            "SELECT * FROM pending_actions WHERE status = ? ORDER BY created_at ASC", arrayOf(status)
        ).use { c ->
            val out = mutableListOf<PendingAction>()
            while (c.moveToNext()) out.add(cursorToPending(c))
            return out
        }
    }

    fun getPending(id: String): PendingAction? {
        readableDatabase.rawQuery(
            "SELECT * FROM pending_actions WHERE id = ?", arrayOf(id)
        ).use { c -> return if (c.moveToFirst()) cursorToPending(c) else null }
    }

    fun resolvePending(id: String, status: String) {
        val values = ContentValues().apply {
            put("status", status)
            put("resolved_at", now())
        }
        writableDatabase.update("pending_actions", values, "id = ?", arrayOf(id))
    }

    fun loadConversation(sessionId: String): List<ConversationTurn> {
        readableDatabase.rawQuery(
            "SELECT history_json FROM conversation_state WHERE session_id = ?", arrayOf(sessionId)
        ).use { c ->
            if (!c.moveToFirst()) return emptyList()
            val json = c.getString(0)
            val type = object : TypeToken<List<ConversationTurn>>() {}.type
            return runCatching { gson.fromJson<List<ConversationTurn>>(json, type) }.getOrDefault(emptyList())
        }
    }

    fun saveConversation(sessionId: String, history: List<ConversationTurn>) {
        val values = ContentValues().apply {
            put("session_id", sessionId)
            put("history_json", gson.toJson(history))
            put("updated_at", now())
        }
        writableDatabase.insertWithOnConflict(
            "conversation_state", null, values, SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    fun clearConversation(sessionId: String) {
        writableDatabase.delete("conversation_state", "session_id = ?", arrayOf(sessionId))
    }

    fun listActionsSince(sinceIso: String?): List<ActionLogEntry> {
        val cursor = if (!sinceIso.isNullOrEmpty()) {
            readableDatabase.rawQuery(
                "SELECT * FROM action_log WHERE created_at > ? ORDER BY created_at ASC", arrayOf(sinceIso)
            )
        } else {
            readableDatabase.rawQuery(
                "SELECT * FROM action_log ORDER BY created_at DESC LIMIT 20", null
            )
        }
        cursor.use { c ->
            val out = mutableListOf<ActionLogEntry>()
            while (c.moveToNext()) {
                out.add(
                    ActionLogEntry(
                        id = c.getString(c.getColumnIndexOrThrow("id")),
                        messageId = c.getStringOrNull("message_id"),
                        actionType = c.getString(c.getColumnIndexOrThrow("action_type")),
                        detail = c.getStringOrNull("detail") ?: "",
                        createdAt = c.getString(c.getColumnIndexOrThrow("created_at")),
                    )
                )
            }
            return if (sinceIso.isNullOrEmpty()) out.reversed() else out
        }
    }

    fun countTodayActions(): Pair<Map<String, Int>, Int> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date())
        val actions = mutableMapOf<String, Int>()
        readableDatabase.rawQuery(
            "SELECT action_type, COUNT(*) as n FROM action_log WHERE created_at LIKE ? GROUP BY action_type",
            arrayOf("$today%"),
        ).use { c ->
            while (c.moveToNext()) {
                actions[c.getString(0)] = c.getInt(1)
            }
        }
        val pendingCount = readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM pending_actions WHERE status = 'pending'", null
        ).use { c -> if (c.moveToFirst()) c.getInt(0) else 0 }
        return actions to pendingCount
    }
}
