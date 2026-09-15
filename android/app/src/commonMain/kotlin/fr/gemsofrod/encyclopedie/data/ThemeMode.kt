package fr.gemsofrod.encyclopedie.data

/**
 * Thème d'affichage choisi par l'utilisateur. [SYSTEME] suit le réglage
 * clair/sombre du système d'exploitation ; [CLAIR]/[SOMBRE] forcent un
 * thème indépendamment du système.
 */
enum class ThemeMode(val labelKey: String) {
    SYSTEME("theme_mode_systeme"),
    CLAIR("theme_mode_clair"),
    SOMBRE("theme_mode_sombre")
}
