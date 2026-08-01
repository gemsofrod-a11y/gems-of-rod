package fr.gemsofrod.encyclopedie.data

import androidx.compose.ui.graphics.Color

/**
 * Catégories de rangement de l'encyclopédie : chaque gemme est classée
 * dans la couleur qui la caractérise le plus communément en joaillerie.
 */
enum class GemColorCategory(val label: String, val swatch: Color) {
    ROUGE("Rouge", Color(0xFFB3122E)),
    ORANGE("Orange", Color(0xFFE07A2F)),
    JAUNE("Jaune", Color(0xFFD4AF37)),
    VERT("Vert", Color(0xFF1E7A46)),
    BLEU("Bleu", Color(0xFF1A5FA8)),
    VIOLET("Violet", Color(0xFF6C4E9C)),
    ROSE("Rose", Color(0xFFD988A6)),
    INCOLORE("Incolore & blanc", Color(0xFFB9C4CC)),
    NOIR("Noir", Color(0xFF2B2B2E)),
    MULTICOLORE("Multicolore", Color(0xFF8E6FB0))
}
