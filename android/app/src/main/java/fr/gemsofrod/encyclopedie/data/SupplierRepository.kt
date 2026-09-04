package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import fr.gemsofrod.encyclopedie.R
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private const val PREFS_NAME = "gems_of_rod_suppliers"
private const val KEY_SUPPLIERS = "suppliers"

/** Type de fournisseur, voir la section « Fournisseurs & Partenaires » de la base de connaissance. */
enum class SupplierType(val labelRes: Int) {
    LAPIDAIRE(R.string.supplier_type_lapidaire),
    NEGOCIANT(R.string.supplier_type_negociant),
    JOAILLIER(R.string.supplier_type_joaillier),
    MINEUR_EXPORTATEUR(R.string.supplier_type_mineur_exportateur)
}

/**
 * Un fournisseur du répertoire professionnel : coordonnées et notes libres —
 * même patron que [Client]. Indépendant de [StockRepository] — aucun lien
 * direct (pas de supplierId sur [StockItem]) : l'historique d'approvisionnement
 * affiché sur la fiche fournisseur se calcule par correspondance de nom avec
 * [StockItem.fournisseur], pas par identifiant, donc sensible aux variantes
 * d'orthographe.
 */
data class Supplier(
    val id: String,
    val nom: String,
    val type: SupplierType,
    val telephone: String,
    val email: String,
    val pays: String,
    val notes: String,
    val createdAtMillis: Long
)

private fun JSONObject.stringOrEmpty(key: String): String = optString(key, "")

private fun Supplier.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("nom", nom)
    put("type", type.name)
    put("telephone", telephone)
    put("email", email)
    put("pays", pays)
    put("notes", notes)
    put("createdAtMillis", createdAtMillis)
}

private fun supplierFromJson(obj: JSONObject): Supplier = Supplier(
    id = obj.getString("id"),
    nom = obj.stringOrEmpty("nom"),
    type = runCatching { SupplierType.valueOf(obj.stringOrEmpty("type")) }.getOrDefault(SupplierType.NEGOCIANT),
    telephone = obj.stringOrEmpty("telephone"),
    email = obj.stringOrEmpty("email"),
    pays = obj.stringOrEmpty("pays"),
    notes = obj.stringOrEmpty("notes"),
    createdAtMillis = if (obj.has("createdAtMillis")) obj.getLong("createdAtMillis") else 0L
)

/**
 * Répertoire fournisseurs, persisté localement en JSON (SharedPreferences) et
 * exposé comme état observable par Compose — même principe que [ClientRepository].
 */
object SupplierRepository {
    private var prefs: SharedPreferences? = null
    private val suppliers = mutableStateListOf<Supplier>()

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        val raw = sharedPrefs.getString(KEY_SUPPLIERS, null)
        if (raw != null) {
            runCatching {
                val array = JSONArray(raw)
                for (i in 0 until array.length()) {
                    suppliers.add(supplierFromJson(array.getJSONObject(i)))
                }
            }
        }
    }

    /** Fournisseurs triés par ordre alphabétique de nom. */
    fun allSuppliers(): List<Supplier> = suppliers.sortedBy { it.nom.lowercase() }

    fun supplierById(id: String): Supplier? = suppliers.find { it.id == id }

    fun addSupplier(supplier: Supplier): String {
        val id = UUID.randomUUID().toString()
        suppliers.add(supplier.copy(id = id, createdAtMillis = System.currentTimeMillis()))
        persist()
        return id
    }

    fun updateSupplier(supplier: Supplier) {
        val index = suppliers.indexOfFirst { it.id == supplier.id }
        if (index == -1) return
        suppliers[index] = supplier
        persist()
    }

    fun deleteSupplier(id: String) {
        suppliers.removeAll { it.id == id }
        persist()
    }

    private fun persist() {
        val array = JSONArray()
        suppliers.forEach { array.put(it.toJson()) }
        prefs?.edit()?.putString(KEY_SUPPLIERS, array.toString())?.apply()
    }
}
