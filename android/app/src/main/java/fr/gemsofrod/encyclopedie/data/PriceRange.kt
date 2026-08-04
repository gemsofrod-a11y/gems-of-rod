package fr.gemsofrod.encyclopedie.data

/**
 * Fourchette de prix indicative au carat (en euros), extraite de
 * [Gem.prixCaratEur]. Seules les fiches dont le prix est exprimé "au carat"
 * (format "X – Y €/ct") sont concernées : les gemmes cotées au gramme, à la
 * pièce ou "non cotées" n'ont pas de fourchette exploitable ici.
 */
data class PriceRangeEur(val minEur: Double, val maxEur: Double)

private val PRICE_RANGE_REGEX = Regex("""(\d[\d\s  ]*)\s*[–-]\s*(\d[\d\s  ]*)\+?\s*€/ct""")
private val NUMBER_SEPARATORS = Regex("""[\s  ]""")

private fun String.toPlainNumber(): Double? = NUMBER_SEPARATORS.replace(this, "").toDoubleOrNull()

fun Gem.priceRangePerCarat(): PriceRangeEur? {
    val match = PRICE_RANGE_REGEX.find(prixCaratEur) ?: return null
    val min = match.groupValues[1].toPlainNumber()
    val max = match.groupValues[2].toPlainNumber()
    if (min == null || max == null || min > max) return null
    return PriceRangeEur(min, max)
}
