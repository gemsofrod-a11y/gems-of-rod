package fr.gemsofrod.encyclopedie.data

/**
 * Formes de taille (cuts) proposées par le compositeur de bague, pour
 * la pierre centrale et les pierres annexes. [isSingleDimension] indique
 * si la forme est symétrique (un seul champ "diamètre" dans le
 * formulaire) ou allongée (longueur + largeur séparées) ; le dessin
 * réel de chaque forme vit dans `RingStoneShapes.kt` (androidMain).
 */
enum class RingCutShape(val label: String, val labelKey: String, val isSingleDimension: Boolean) {
    RONDE("Ronde", "ring_cut_ronde", isSingleDimension = true),
    OVALE("Ovale", "ring_cut_ovale", isSingleDimension = false),
    POIRE("Poire", "ring_cut_poire", isSingleDimension = false),
    MARQUISE("Marquise", "ring_cut_marquise", isSingleDimension = false),
    BRIOLETTE("Briolette", "ring_cut_briolette", isSingleDimension = false),
    ASSCHER("Asscher", "ring_cut_asscher", isSingleDimension = true),
    BAGUETTE("Baguette", "ring_cut_baguette", isSingleDimension = false),
    PRINCESSE("Princesse", "ring_cut_princesse", isSingleDimension = true),
    COUSSIN("Coussin", "ring_cut_coussin", isSingleDimension = true),
    KITE("Kite", "ring_cut_kite", isSingleDimension = true),
    COEUR("Cœur", "ring_cut_coeur", isSingleDimension = true),
    TRILLION("Trillion", "ring_cut_trillion", isSingleDimension = true),
    EMERAUDE_CUT("Émeraude", "ring_cut_emeraude", isSingleDimension = false),
    RADIANT("Radiant", "ring_cut_radiant", isSingleDimension = false)
}
