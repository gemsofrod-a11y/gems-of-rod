package fr.gemsofrod.encyclopedie.data

/**
 * Valeurs numériques brutes extraites des fiches fossile/coquillage/météorite
 * pour l'outil de comparaison (graphique radar). Contrairement aux gemmes
 * (voir [GemComparisonProfile]), ces catégories n'ont pas de propriétés
 * optiques mesurées (indice de réfraction, fluorescence, pléochroïsme) dans
 * ce catalogue : seules dureté, densité et rareté leur sont communes.
 */
data class CatalogComparisonProfile(
    val dureteValue: Double?,
    val densiteValue: Double?,
    val rarete: GemRarete
) {
    val dureteNorm: Float get() = normalize(dureteValue, 0.0, 10.0)
    val densiteNorm: Float get() = normalize(densiteValue, 1.8, 6.6)
    val rareteNorm: Float get() = (rarete.ordinal.toFloat() / (GemRarete.entries.size - 1)).coerceIn(0f, 1f)

    private fun normalize(value: Double?, min: Double, max: Double): Float {
        if (value == null) return 0f
        return ((value - min) / (max - min)).toFloat().coerceIn(0f, 1f)
    }
}

/**
 * Calcule le profil comparatif (dureté, densité, rareté) d'un fossile, d'un
 * coquillage ou d'une météorite à partir de ses champs texte, pour alimenter
 * le même graphique radar que [GemComparison] mais restreint aux trois
 * grandeurs communes à ces catégories.
 */
object CatalogComparison {
    fun profile(durete: String, densite: String, rarete: GemRarete): CatalogComparisonProfile =
        CatalogComparisonProfile(
            dureteValue = parseNumericAverage(durete),
            densiteValue = parseNumericAverage(densite),
            rarete = rarete
        )
}
