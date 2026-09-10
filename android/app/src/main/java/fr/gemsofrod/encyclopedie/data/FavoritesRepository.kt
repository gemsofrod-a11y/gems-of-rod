package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf

private const val PREFS_NAME = "gems_of_rod_favorites"
private const val KEY_GEM_IDS = "gem_ids"
private const val KEY_FOSSILE_IDS = "fossile_ids"
private const val KEY_COQUILLAGE_IDS = "coquillage_ids"
private const val KEY_METEORITE_IDS = "meteorite_ids"

/** Catégorie d'une fiche pouvant être mise en favori. */
enum class FavoriteCategory { GEM, FOSSILE, COQUILLAGE, METEORITE }

/**
 * Liste des fiches marquées en favori par l'utilisateur (gemmes, fossiles,
 * coquillages, météorites), persistée localement (SharedPreferences) et
 * exposée comme état observable par Compose. Chaque catégorie a son propre
 * espace de stockage : un fossile et une gemme peuvent partager le même id
 * sans se marcher dessus.
 */
object FavoritesRepository {
    private var prefs: SharedPreferences? = null
    private val gemIds = mutableStateListOf<String>()
    private val fossileIds = mutableStateListOf<String>()
    private val coquillageIds = mutableStateListOf<String>()
    private val meteoriteIds = mutableStateListOf<String>()

    private fun listFor(category: FavoriteCategory): MutableList<String> = when (category) {
        FavoriteCategory.GEM -> gemIds
        FavoriteCategory.FOSSILE -> fossileIds
        FavoriteCategory.COQUILLAGE -> coquillageIds
        FavoriteCategory.METEORITE -> meteoriteIds
    }

    private fun keyFor(category: FavoriteCategory): String = when (category) {
        FavoriteCategory.GEM -> KEY_GEM_IDS
        FavoriteCategory.FOSSILE -> KEY_FOSSILE_IDS
        FavoriteCategory.COQUILLAGE -> KEY_COQUILLAGE_IDS
        FavoriteCategory.METEORITE -> KEY_METEORITE_IDS
    }

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        FavoriteCategory.entries.forEach { category ->
            listFor(category).addAll(sharedPrefs.getStringSet(keyFor(category), emptySet()).orEmpty())
        }
    }

    /** Ids des favoris d'une catégorie, du plus récemment ajouté au plus ancien. */
    fun favoriteIds(category: FavoriteCategory = FavoriteCategory.GEM): List<String> = listFor(category).asReversed()

    fun isFavorite(id: String, category: FavoriteCategory = FavoriteCategory.GEM): Boolean = listFor(category).contains(id)

    fun toggle(id: String, category: FavoriteCategory = FavoriteCategory.GEM) {
        val before = AchievementsRepository.stats()
        val ids = listFor(category)
        if (ids.contains(id)) {
            ids.remove(id)
        } else {
            ids.add(id)
        }
        persist(category)
        AchievementsRepository.checkNewlyUnlocked(before, AchievementsRepository.stats())
    }

    private fun persist(category: FavoriteCategory) {
        prefs?.edit()?.putStringSet(keyFor(category), listFor(category).toSet())?.apply()
    }
}
