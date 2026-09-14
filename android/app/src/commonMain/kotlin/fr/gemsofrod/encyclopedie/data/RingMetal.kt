package fr.gemsofrod.encyclopedie.data

import androidx.compose.ui.graphics.Color

/**
 * Métaux de monture proposés par le compositeur de bague, avec le
 * dégradé (foncé → clair) utilisé pour dessiner le jonc et le prix
 * indicatif au gramme (métal façonné) pour l'estimation de prix.
 */
enum class RingMetal(
    val label: String,
    val labelKey: String,
    val gradientStart: Color,
    val gradientEnd: Color,
    val pricePerGram: Double
) {
    OR_JAUNE("Or jaune 18ct", "ring_metal_or_jaune", Color(0xFF7C5E19), Color(0xFFE8C157), 55.0),
    OR_BLANC("Or blanc 18ct", "ring_metal_or_blanc", Color(0xFF7C7E82), Color(0xFFD8DADE), 56.0),
    OR_ROSE("Or rose 18ct", "ring_metal_or_rose", Color(0xFF7C4A3F), Color(0xFFE3A692), 55.0),
    PLATINE("Platine", "ring_metal_platine", Color(0xFF7F8286), Color(0xFFE2E4E7), 48.0)
}
