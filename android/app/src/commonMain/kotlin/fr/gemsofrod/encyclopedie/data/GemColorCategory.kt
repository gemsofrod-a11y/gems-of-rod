package fr.gemsofrod.encyclopedie.data

import androidx.compose.ui.graphics.Color

/**
 * Catégories de rangement de l'encyclopédie : chaque gemme est classée
 * dans la couleur qui la caractérise le plus communément en joaillerie.
 *
 * `label` est le nom en français (utilisé en interne, ex. `search()`) ;
 * `labelKey` identifie la chaîne localisée à afficher dans l'UI (résolue
 * côté plateforme — sur Android via
 * [fr.gemsofrod.encyclopedie.ui.resolveLabelStringRes] vers la ressource
 * `R.string` de même nom).
 */
enum class GemColorCategory(val label: String, val swatch: Color, val labelKey: String) {
    ROUGE("Rouge", Color(0xFFB3122E), "color_rouge"),
    ORANGE("Orange", Color(0xFFE07A2F), "color_orange"),
    JAUNE("Jaune", Color(0xFFD4AF37), "color_jaune"),
    VERT("Vert", Color(0xFF1E7A46), "color_vert"),
    BLEU("Bleu", Color(0xFF1A5FA8), "color_bleu"),
    VIOLET("Violet", Color(0xFF6C4E9C), "color_violet"),
    ROSE("Rose", Color(0xFFD988A6), "color_rose"),
    INCOLORE("Incolore & blanc", Color(0xFFB9C4CC), "color_incolore"),
    BRUN("Brun & marron", Color(0xFF6F4E37), "color_brun"),
    NOIR("Noir", Color(0xFF2B2B2E), "color_noir"),
    MULTICOLORE("Multicolore", Color(0xFF8E6FB0), "color_multicolore")
}
