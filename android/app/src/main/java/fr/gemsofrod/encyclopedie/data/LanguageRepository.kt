package fr.gemsofrod.encyclopedie.data

import android.content.Context

private const val PREFS_NAME = "gems_of_rod_language"
private const val KEY_LANGUAGE_CODE = "language_code"

/**
 * Langue d'interface choisie par l'utilisateur, persistée localement
 * (SharedPreferences). Lue très tôt (dans `Activity.attachBaseContext`, avant
 * `onCreate`), donc implémentée en accès direct plutôt qu'avec un état
 * observable global comme [FavoritesRepository].
 */
object LanguageRepository {
    fun getLanguage(context: Context): AppLanguage {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANGUAGE_CODE, AppLanguage.FR.code) ?: AppLanguage.FR.code
        return AppLanguage.fromCode(code)
    }

    fun setLanguage(context: Context, language: AppLanguage) {
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE_CODE, language.code)
            .apply()
    }
}
