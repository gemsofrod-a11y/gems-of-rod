package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private const val PREFS_NAME = "gems_of_rod_stock"
private const val KEY_ITEMS = "items"

/** Statut commercial d'une pierre en stock. */
enum class StockStatus {
    EN_STOCK, RESERVE, VENDU, CONSIGNATION
}

/**
 * Une pierre du stock professionnel : nom en saisie libre (éventuellement
 * pré-rempli depuis le catalogue via [catalogGemId], mais toujours
 * modifiable — une pierre réelle en stock ne correspond pas toujours
 * exactement à une fiche du catalogue), informations d'achat/vente et statut
 * commercial. Indépendant du Carnet de terrain ([LabNotebookRepository]),
 * qui consigne des observations, pas une gestion commerciale.
 */
data class StockItem(
    val id: String,
    val nom: String,
    val reference: String,
    val catalogGemId: String?,
    val poidsCarats: Double?,
    val taille: String,
    val couleur: String,
    val purete: String,
    val traitement: String,
    val certificatNumero: String,
    val laboratoire: String,
    val fournisseur: String,
    val dateAchatMillis: Long?,
    val coutAchat: Double?,
    val prixVente: Double?,
    val statut: StockStatus,
    val notes: String,
    val photoFileName: String?,
    val createdAtMillis: Long,
    val acheteurNom: String = "",
    val venteDateMillis: Long? = null,
    val factureNotes: String = ""
)

private fun JSONObject.stringOrEmpty(key: String): String = optString(key, "")
private fun JSONObject.doubleOrNull(key: String): Double? = if (has(key) && !isNull(key)) getDouble(key) else null
private fun JSONObject.longOrNull(key: String): Long? = if (has(key) && !isNull(key)) getLong(key) else null

private fun StockItem.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("nom", nom)
    put("reference", reference)
    catalogGemId?.let { put("catalogGemId", it) }
    poidsCarats?.let { put("poidsCarats", it) }
    put("taille", taille)
    put("couleur", couleur)
    put("purete", purete)
    put("traitement", traitement)
    put("certificatNumero", certificatNumero)
    put("laboratoire", laboratoire)
    put("fournisseur", fournisseur)
    dateAchatMillis?.let { put("dateAchatMillis", it) }
    coutAchat?.let { put("coutAchat", it) }
    prixVente?.let { put("prixVente", it) }
    put("statut", statut.name)
    put("notes", notes)
    photoFileName?.let { put("photoFileName", it) }
    put("createdAtMillis", createdAtMillis)
    put("acheteurNom", acheteurNom)
    venteDateMillis?.let { put("venteDateMillis", it) }
    put("factureNotes", factureNotes)
}

private fun itemFromJson(obj: JSONObject): StockItem = StockItem(
    id = obj.getString("id"),
    nom = obj.stringOrEmpty("nom"),
    reference = obj.stringOrEmpty("reference"),
    catalogGemId = if (obj.has("catalogGemId")) obj.getString("catalogGemId") else null,
    poidsCarats = obj.doubleOrNull("poidsCarats"),
    taille = obj.stringOrEmpty("taille"),
    couleur = obj.stringOrEmpty("couleur"),
    purete = obj.stringOrEmpty("purete"),
    traitement = obj.stringOrEmpty("traitement"),
    certificatNumero = obj.stringOrEmpty("certificatNumero"),
    laboratoire = obj.stringOrEmpty("laboratoire"),
    fournisseur = obj.stringOrEmpty("fournisseur"),
    dateAchatMillis = obj.longOrNull("dateAchatMillis"),
    coutAchat = obj.doubleOrNull("coutAchat"),
    prixVente = obj.doubleOrNull("prixVente"),
    statut = obj.stringOrEmpty("statut").let { name -> runCatching { StockStatus.valueOf(name) }.getOrDefault(StockStatus.EN_STOCK) },
    notes = obj.stringOrEmpty("notes"),
    photoFileName = if (obj.has("photoFileName")) obj.getString("photoFileName") else null,
    createdAtMillis = if (obj.has("createdAtMillis")) obj.getLong("createdAtMillis") else 0L,
    acheteurNom = obj.stringOrEmpty("acheteurNom"),
    venteDateMillis = obj.longOrNull("venteDateMillis"),
    factureNotes = obj.stringOrEmpty("factureNotes")
)

/**
 * Stock professionnel, persisté localement en JSON (SharedPreferences) et
 * exposé comme état observable par Compose — même principe que
 * [LabNotebookRepository]. Les photos ne sont pas stockées ici : seul le nom
 * de fichier est conservé, l'image elle-même vit dans le stockage interne de
 * l'app (voir [StockPhotoStorage]).
 */
object StockRepository {
    private var prefs: SharedPreferences? = null
    private val items = mutableStateListOf<StockItem>()

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        val raw = sharedPrefs.getString(KEY_ITEMS, null)
        if (raw != null) {
            runCatching {
                val array = JSONArray(raw)
                for (i in 0 until array.length()) {
                    items.add(itemFromJson(array.getJSONObject(i)))
                }
            }
        }
    }

    /** Fiches de stock, de la plus récemment ajoutée à la plus ancienne. */
    fun allItems(): List<StockItem> = items.sortedByDescending { it.createdAtMillis }

    fun itemById(id: String): StockItem? = items.find { it.id == id }

    fun addItem(item: StockItem): String {
        val id = UUID.randomUUID().toString()
        items.add(item.copy(id = id, createdAtMillis = System.currentTimeMillis()))
        persist()
        return id
    }

    fun updateItem(item: StockItem) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index == -1) return
        items[index] = item
        persist()
    }

    fun deleteItem(id: String) {
        items.removeAll { it.id == id }
        persist()
    }

    private fun persist() {
        val array = JSONArray()
        items.forEach { array.put(it.toJson()) }
        prefs?.edit()?.putString(KEY_ITEMS, array.toString())?.apply()
    }
}
