package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private const val PREFS_NAME = "gems_of_rod_clients"
private const val KEY_CLIENTS = "clients"

/**
 * Un client du carnet d'adresses professionnel : coordonnées et notes
 * libres. Indépendant de [StockRepository] — aucun lien direct (pas de
 * clientId sur [StockItem]) : l'historique d'achats affiché sur la fiche
 * client se calcule par correspondance de nom avec [StockItem.acheteurNom],
 * pas par identifiant, donc sensible aux variantes d'orthographe.
 */
data class Client(
    val id: String,
    val nom: String,
    val telephone: String,
    val email: String,
    val adresse: String,
    val notes: String,
    val createdAtMillis: Long
)

private fun JSONObject.stringOrEmpty(key: String): String = optString(key, "")

private fun Client.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("nom", nom)
    put("telephone", telephone)
    put("email", email)
    put("adresse", adresse)
    put("notes", notes)
    put("createdAtMillis", createdAtMillis)
}

private fun clientFromJson(obj: JSONObject): Client = Client(
    id = obj.getString("id"),
    nom = obj.stringOrEmpty("nom"),
    telephone = obj.stringOrEmpty("telephone"),
    email = obj.stringOrEmpty("email"),
    adresse = obj.stringOrEmpty("adresse"),
    notes = obj.stringOrEmpty("notes"),
    createdAtMillis = if (obj.has("createdAtMillis")) obj.getLong("createdAtMillis") else 0L
)

/**
 * Carnet clients, persisté localement en JSON (SharedPreferences) et exposé
 * comme état observable par Compose — même principe que [StockRepository].
 */
object ClientRepository {
    private var prefs: SharedPreferences? = null
    private val clients = mutableStateListOf<Client>()

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        val raw = sharedPrefs.getString(KEY_CLIENTS, null)
        if (raw != null) {
            runCatching {
                val array = JSONArray(raw)
                for (i in 0 until array.length()) {
                    clients.add(clientFromJson(array.getJSONObject(i)))
                }
            }
        }
    }

    /** Clients triés par ordre alphabétique de nom. */
    fun allClients(): List<Client> = clients.sortedBy { it.nom.lowercase() }

    fun clientById(id: String): Client? = clients.find { it.id == id }

    fun addClient(client: Client): String {
        val id = UUID.randomUUID().toString()
        clients.add(client.copy(id = id, createdAtMillis = System.currentTimeMillis()))
        persist()
        return id
    }

    fun updateClient(client: Client) {
        val index = clients.indexOfFirst { it.id == client.id }
        if (index == -1) return
        clients[index] = client
        persist()
    }

    fun deleteClient(id: String) {
        clients.removeAll { it.id == id }
        persist()
    }

    private fun persist() {
        val array = JSONArray()
        clients.forEach { array.put(it.toJson()) }
        prefs?.edit()?.putString(KEY_CLIENTS, array.toString())?.apply()
    }
}
