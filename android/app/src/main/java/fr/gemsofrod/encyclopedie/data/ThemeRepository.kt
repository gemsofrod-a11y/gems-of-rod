package fr.gemsofrod.encyclopedie.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

private const val PREFS_NAME = "gems_of_rod_theme"
private const val KEY_THEME_MODE = "theme_mode"

/**
 * Thème d'affichage choisi par l'utilisateur, persisté localement
 * (SharedPreferences) et exposé comme état observable par Compose — au
 * contraire de [LanguageRepository] (qui nécessite un redémarrage
 * d'activité pour recharger les ressources), un changement de thème
 * s'applique immédiatement à tout l'arbre Compose via [mode].
 */
object ThemeRepository {
    var mode by mutableStateOf(ThemeMode.SYSTEME)
        private set

    fun init(context: Context) {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEME.name)
        mode = runCatching { ThemeMode.valueOf(name ?: ThemeMode.SYSTEME.name) }.getOrDefault(ThemeMode.SYSTEME)
    }

    fun setMode(context: Context, newMode: ThemeMode) {
        mode = newMode
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME_MODE, newMode.name)
            .apply()
    }
}
