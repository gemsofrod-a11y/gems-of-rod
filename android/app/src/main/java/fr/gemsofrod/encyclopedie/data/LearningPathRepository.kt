package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf

private const val PREFS_NAME = "gems_of_rod_learning_path"
private const val KEY_COMPLETED_STEPS = "completed_steps"

/**
 * Étapes du parcours d'apprentissage marquées comme vues (l'utilisateur a
 * ouvert l'écran cible depuis le parcours), persistées localement et
 * exposées comme état observable par Compose — même pattern que
 * [FavoritesRepository], un seul espace de stockage ici (pas de catégories).
 */
object LearningPathRepository {
    private var prefs: SharedPreferences? = null
    private val completedSteps = mutableStateListOf<String>()

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        completedSteps.addAll(sharedPrefs.getStringSet(KEY_COMPLETED_STEPS, emptySet()).orEmpty())
    }

    fun isCompleted(stepId: String): Boolean = completedSteps.contains(stepId)

    fun completedCount(): Int = completedSteps.size

    fun markCompleted(stepId: String) {
        if (completedSteps.contains(stepId)) return
        completedSteps.add(stepId)
        prefs?.edit()?.putStringSet(KEY_COMPLETED_STEPS, completedSteps.toSet())?.apply()
    }
}
