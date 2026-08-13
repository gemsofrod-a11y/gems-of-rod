package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private const val PREFS_NAME = "gems_of_rod_lab_notebook"
private const val KEY_SAMPLES = "samples"

/**
 * Un échantillon consigné dans le carnet de terrain : les mesures relevées
 * sur une pierre réelle avec de vrais instruments (loupe, duromètre,
 * réfractomètre...), indépendamment de tout résultat d'identification.
 */
data class LabSample(
    val id: String,
    val label: String,
    val notes: String,
    val timestamp: Long,
    val criteria: AnalysisCriteria
)

private fun AnalysisCriteria.toJson(): JSONObject = JSONObject().apply {
    couleur?.let { put("couleur", it.name) }
    transparence?.let { put("transparence", it) }
    eclat?.let { put("eclat", it) }
    clivage?.let { put("clivage", it) }
    systemeCristallin?.let { put("systemeCristallin", it) }
    durete?.let { put("durete", it) }
    densite?.let { put("densite", it) }
    indiceRefraction?.let { put("indiceRefraction", it) }
    pleochroisme?.let { put("pleochroisme", it) }
    fluorescence?.let { put("fluorescence", it) }
    typeInclusion?.let { put("typeInclusion", it) }
}

private fun JSONObject.stringOrNull(key: String): String? = if (has(key)) getString(key) else null
private fun JSONObject.doubleOrNull(key: String): Double? = if (has(key)) getDouble(key) else null

private fun criteriaFromJson(obj: JSONObject): AnalysisCriteria = AnalysisCriteria(
    couleur = obj.stringOrNull("couleur")?.let { name -> runCatching { GemColorCategory.valueOf(name) }.getOrNull() },
    transparence = obj.stringOrNull("transparence"),
    eclat = obj.stringOrNull("eclat"),
    clivage = obj.stringOrNull("clivage"),
    systemeCristallin = obj.stringOrNull("systemeCristallin"),
    durete = obj.doubleOrNull("durete"),
    densite = obj.doubleOrNull("densite"),
    indiceRefraction = obj.doubleOrNull("indiceRefraction"),
    pleochroisme = obj.stringOrNull("pleochroisme"),
    fluorescence = obj.stringOrNull("fluorescence"),
    typeInclusion = obj.stringOrNull("typeInclusion")
)

private fun LabSample.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("label", label)
    put("notes", notes)
    put("timestamp", timestamp)
    put("criteria", criteria.toJson())
}

private fun sampleFromJson(obj: JSONObject): LabSample = LabSample(
    id = obj.getString("id"),
    label = obj.getString("label"),
    notes = obj.optString("notes", ""),
    timestamp = obj.getLong("timestamp"),
    criteria = criteriaFromJson(obj.getJSONObject("criteria"))
)

/**
 * Carnet de terrain : échantillons mesurés par l'utilisateur avec ses
 * propres instruments, persistés localement en JSON (SharedPreferences) et
 * exposés comme état observable par Compose — même principe que
 * [FavoritesRepository] et [AchievementsRepository]. Contrairement à l'outil
 * d'analyse, ces fiches ne sont pas comparées automatiquement au catalogue :
 * la comparaison est une action explicite depuis la fiche détail.
 */
object LabNotebookRepository {
    private var prefs: SharedPreferences? = null
    private val samples = mutableStateListOf<LabSample>()

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        val raw = sharedPrefs.getString(KEY_SAMPLES, null)
        if (raw != null) {
            runCatching {
                val array = JSONArray(raw)
                for (i in 0 until array.length()) {
                    samples.add(sampleFromJson(array.getJSONObject(i)))
                }
            }
        }
    }

    /** Échantillons du carnet, du plus récemment ajouté au plus ancien. */
    fun allSamples(): List<LabSample> = samples.sortedByDescending { it.timestamp }

    fun sampleById(id: String): LabSample? = samples.find { it.id == id }

    fun addSample(label: String, notes: String, criteria: AnalysisCriteria): String {
        val id = UUID.randomUUID().toString()
        samples.add(LabSample(id, label, notes, System.currentTimeMillis(), criteria))
        persist()
        return id
    }

    fun updateSample(id: String, label: String, notes: String, criteria: AnalysisCriteria) {
        val index = samples.indexOfFirst { it.id == id }
        if (index == -1) return
        samples[index] = samples[index].copy(label = label, notes = notes, criteria = criteria)
        persist()
    }

    fun deleteSample(id: String) {
        samples.removeAll { it.id == id }
        persist()
    }

    private fun persist() {
        val array = JSONArray()
        samples.forEach { array.put(it.toJson()) }
        prefs?.edit()?.putString(KEY_SAMPLES, array.toString())?.apply()
    }
}
