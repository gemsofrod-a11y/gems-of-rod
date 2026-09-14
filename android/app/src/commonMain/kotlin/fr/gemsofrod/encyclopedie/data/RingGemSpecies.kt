package fr.gemsofrod.encyclopedie.data

import androidx.compose.ui.graphics.Color

/**
 * Espèces de pierres proposées par le compositeur de bague (pierre
 * centrale et pierres annexes), avec les valeurs utilisées pour
 * l'estimation de prix : [specificGravity] (poids spécifique, pour
 * convertir les dimensions en carats via [RingCaratEstimator]) et
 * [pricePerCarat] (prix indicatif au carat).
 *
 * Volontairement distinct de [Gem]/[GemsRepository] (catalogue de
 * l'encyclopédie, 205 fiches sans champs de densité/prix structurés) :
 * reste un petit enum dédié à la joaillerie.
 */
enum class RingGemSpecies(
    val label: String,
    val labelKey: String,
    val defaultColor: Color,
    val specificGravity: Double,
    val pricePerCarat: Double
) {
    DIAMANT("Diamant", "ring_species_diamant", Color(0xFFF4F6FA), 3.52, 4200.0),
    RUBIS("Rubis", "ring_species_rubis", Color(0xFFC21F44), 4.00, 2500.0),
    EMERAUDE("Émeraude", "ring_species_emeraude", Color(0xFF127A47), 2.76, 2200.0),
    SAPHIR("Saphir", "ring_species_saphir", Color(0xFF1E4BAE), 4.00, 1800.0),
    ALEXANDRITE("Alexandrite", "ring_species_alexandrite", Color(0xFF1F8A72), 3.73, 12000.0),
    TANZANITE("Tanzanite", "ring_species_tanzanite", Color(0xFF4B3F9E), 3.35, 600.0),
    SPINELLE("Spinelle", "ring_species_spinelle", Color(0xFFC23368), 3.60, 400.0),
    TOURMALINE_ROSE("Tourmaline rose", "ring_species_tourmaline", Color(0xFFD6367F), 3.06, 300.0),
    TOPAZE_IMPERIALE("Topaze impériale", "ring_species_topaze", Color(0xFFE08A2C), 3.53, 150.0),
    GRENAT_DEMANTOIDE("Grenat démantoïde", "ring_species_grenat", Color(0xFF3B8A3B), 3.85, 900.0),
    KUNZITE("Kunzite", "ring_species_kunzite", Color(0xFFC97FA8), 3.18, 80.0),
    MORGANITE("Morganite", "ring_species_morganite", Color(0xFFE0A08E), 2.80, 120.0)
}
