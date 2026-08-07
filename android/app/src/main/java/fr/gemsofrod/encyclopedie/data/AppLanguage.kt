package fr.gemsofrod.encyclopedie.data

/** Langues disponibles pour l'interface de l'application. */
enum class AppLanguage(val code: String, val label: String, val flagEmoji: String) {
    FR("fr", "Français", "🇫🇷"),
    EN("en", "English", "🇬🇧"),
    ES("es", "Español", "🇪🇸"),
    IT("it", "Italiano", "🇮🇹"),
    DE("de", "Deutsch", "🇩🇪");

    companion object {
        fun fromCode(code: String): AppLanguage = entries.firstOrNull { it.code == code } ?: FR
    }
}
