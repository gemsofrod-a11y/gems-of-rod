package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf

private const val PREFS_NAME = "gems_of_rod_favorites"
private const val KEY_GEM_IDS = "gem_ids"

/**
 * Liste des gemmes marquées en favori par l'utilisateur, persistée localement
 * (SharedPreferences) et exposée comme état observable par Compose.
 */
object FavoritesRepository {
    private var prefs: SharedPreferences? = null
    private val ids = mutableStateListOf<String>()

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        ids.addAll(sharedPrefs.getStringSet(KEY_GEM_IDS, emptySet()).orEmpty())
    }

    /** Ids des favoris, du plus récemment ajouté au plus ancien. */
    fun favoriteIds(): List<String> = ids.asReversed()

    fun isFavorite(gemId: String): Boolean = ids.contains(gemId)

    fun toggle(gemId: String) {
        val before = AchievementsRepository.stats()
        if (ids.contains(gemId)) {
            ids.remove(gemId)
        } else {
            ids.add(gemId)
        }
        persist()
        AchievementsRepository.checkNewlyUnlocked(before, AchievementsRepository.stats())
    }

    private fun persist() {
        prefs?.edit()?.putStringSet(KEY_GEM_IDS, ids.toSet())?.apply()
    }
}
