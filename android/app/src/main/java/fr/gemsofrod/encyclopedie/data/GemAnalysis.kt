package fr.gemsofrod.encyclopedie.data

/** Vocabulaire contrôlé des champs diagnostiques, dans l'ordre d'affichage
 * proposé à l'utilisateur dans l'outil d'analyse. */
object AnalysisVocabulary {
    val TRANSPARENCE = listOf(
        "Transparente",
        "Transparente à translucide",
        "Translucide",
        "Translucide à opaque",
        "Opaque"
    )

    val ECLAT = listOf(
        "Vitreux",
        "Vitreux à résineux",
        "Vitreux à adamantin",
        "Résineux",
        "Gras",
        "Adamantin",
        "Soyeux",
        "Nacré",
        "Métallique",
        "Submétallique",
        "Terne",
        "Cireux"
    )

    val CLIVAGE = listOf(
        "Aucun",
        "Indistinct",
        "Imparfait",
        "Bon",
        "Parfait",
        "Parfait dans une direction",
        "Parfait dans deux directions",
        "Parfait dans trois directions"
    )
}

/** Critères optionnels saisis par l'utilisateur dans l'outil d'analyse de
 * pierre. Un champ non renseigné (null) n'entre pas dans le calcul. */
data class AnalysisCriteria(
    val couleur: GemColorCategory? = null,
    val transparence: String? = null,
    val eclat: String? = null,
    val clivage: String? = null,
    val systemeCristallin: String? = null,
    val durete: Double? = null,
    val densite: Double? = null,
    val indiceRefraction: Double? = null
) {
    val totalCriteria: Int
        get() = listOfNotNull(
            couleur, transparence, eclat, clivage, systemeCristallin, durete, densite, indiceRefraction
        ).size

    val isEmpty: Boolean get() = totalCriteria == 0
}

/** Une gemme du catalogue et son nombre de critères correspondants sur le
 * total de critères renseignés par l'utilisateur. */
data class AnalysisMatch(val gem: Gem, val matched: Int, val total: Int)

/**
 * Compare les critères saisis par l'utilisateur au catalogue, façon clé de
 * détermination gemmologique : chaque critère rempli compte pour un point
 * s'il correspond (valeur exacte pour les champs catégoriels, chevauchement
 * de plage avec tolérance pour les mesures physiques). Les gemmes sans
 * aucune correspondance sont écartées ; le reste est trié par nombre de
 * critères correspondants, décroissant.
 */
object GemAnalyzer {
    private fun parseRange(raw: String): ClosedFloatingPointRange<Double>? {
        val normalized = raw.replace(',', '.').replace('–', '-')
        val parts = normalized.split('-').map { it.trim() }.filter { it.isNotEmpty() }
        val values = parts.mapNotNull { it.toDoubleOrNull() }
        return when (values.size) {
            1 -> values[0]..values[0]
            2 -> minOf(values[0], values[1])..maxOf(values[0], values[1])
            else -> null
        }
    }

    private fun matchesNumeric(userValue: Double?, catalogRaw: String, tolerance: Double): Boolean {
        if (userValue == null) return false
        val range = parseRange(catalogRaw) ?: return false
        return userValue in (range.start - tolerance)..(range.endInclusive + tolerance)
    }

    /** Nom de base d'un système cristallin, sans précision entre parenthèses
     * ni variante après "/" (ex. "Trigonal (microcristallin)" -> "Trigonal"). */
    fun baseSystemeCristallin(value: String): String =
        value.substringBefore(" (").substringBefore("/").trim()

    fun systemesCristallins(): List<String> =
        GemsRepository.gems.map { baseSystemeCristallin(it.systemeCristallin) }.distinct().sorted()

    fun analyze(criteria: AnalysisCriteria): List<AnalysisMatch> {
        if (criteria.isEmpty) return emptyList()
        val total = criteria.totalCriteria

        return GemsRepository.gems.mapNotNull { gem ->
            val diag = GemDiagnostics.data[gem.id]
            var matched = 0

            if (criteria.couleur != null && gem.couleur == criteria.couleur) matched++
            if (criteria.transparence != null && diag?.transparence == criteria.transparence) matched++
            if (criteria.eclat != null && diag?.eclat == criteria.eclat) matched++
            if (criteria.clivage != null && diag?.clivage == criteria.clivage) matched++
            if (criteria.systemeCristallin != null &&
                baseSystemeCristallin(gem.systemeCristallin) == criteria.systemeCristallin
            ) matched++
            if (matchesNumeric(criteria.durete, gem.durete, tolerance = 0.5)) matched++
            if (diag != null && matchesNumeric(criteria.densite, diag.densite, tolerance = 0.15)) matched++
            if (matchesNumeric(criteria.indiceRefraction, gem.indiceRefraction, tolerance = 0.02)) matched++

            if (matched == 0) null else AnalysisMatch(gem, matched, total)
        }.sortedWith(compareByDescending<AnalysisMatch> { it.matched }.thenBy { it.gem.nom })
    }
}
