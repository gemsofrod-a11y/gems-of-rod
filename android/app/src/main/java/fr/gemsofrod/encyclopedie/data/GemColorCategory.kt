package fr.gemsofrod.encyclopedie.data

import androidx.compose.ui.graphics.Color
import fr.gemsofrod.encyclopedie.R

/**
 * Catégories de rangement de l'encyclopédie : chaque gemme est classée
 * dans la couleur qui la caractérise le plus communément en joaillerie.
 *
 * `label` est le nom en français (utilisé en interne, ex. `search()`) ;
 * `labelRes` est la ressource de chaîne localisée à afficher dans l'UI.
 */
enum class GemColorCategory(val label: String, val swatch: Color, val labelRes: Int) {
    ROUGE("Rouge", Color(0xFFB3122E), R.string.color_rouge),
    ORANGE("Orange", Color(0xFFE07A2F), R.string.color_orange),
    JAUNE("Jaune", Color(0xFFD4AF37), R.string.color_jaune),
    VERT("Vert", Color(0xFF1E7A46), R.string.color_vert),
    BLEU("Bleu", Color(0xFF1A5FA8), R.string.color_bleu),
    VIOLET("Violet", Color(0xFF6C4E9C), R.string.color_violet),
    ROSE("Rose", Color(0xFFD988A6), R.string.color_rose),
    INCOLORE("Incolore & blanc", Color(0xFFB9C4CC), R.string.color_incolore),
    BRUN("Brun & marron", Color(0xFF6F4E37), R.string.color_brun),
    NOIR("Noir", Color(0xFF2B2B2E), R.string.color_noir),
    MULTICOLORE("Multicolore", Color(0xFF8E6FB0), R.string.color_multicolore)
}
