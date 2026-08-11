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

    val PLEOCHROISME = listOf(
        "Aucun",
        "Faible",
        "Faible à modéré",
        "Modéré",
        "Modéré à fort",
        "Fort"
    )

    val FLUORESCENCE = listOf(
        "Aucune",
        "Aucune à faible",
        "Faible",
        "Faible à modérée",
        "Modérée",
        "Modérée à forte",
        "Forte",
        "Variable"
    )

    /** Type d'inclusion dominant observé dans la pierre, dérivé de la
     * description éditoriale de [GemDiagnostic.inclusions]. Rempli
     * uniquement pour les gemmes possédant un degré de transparence,
     * comme [GemDiagnostic.inclusions] lui-même. */
    val TYPE_INCLUSION = listOf(
        "Adularescence",
        "Agrégat botryoïdal",
        "Astérisme",
        "Aventurescence",
        "Biréfringence",
        "Chatoyance",
        "Chiastolite",
        "Coloration diffuse",
        "Cristal accessoire",
        "Dispersion",
        "Fracture interne",
        "Halo de tension",
        "Inclusion fossile",
        "Inclusion liquide",
        "Inclusion métallique",
        "Jardin",
        "Labradorescence",
        "Macle",
        "Pierre généralement limpide",
        "Soie",
        "Structure fibreuse",
        "Trichite",
        "Ténébrescence",
        "Zonage de couleur"
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
    val indiceRefraction: Double? = null,
    val pleochroisme: String? = null,
    val fluorescence: String? = null,
    val typeInclusion: String? = null
) {
    val totalCriteria: Int
        get() = listOfNotNull(
            couleur, transparence, eclat, clivage, systemeCristallin, durete, densite, indiceRefraction,
            pleochroisme, fluorescence, typeInclusion
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
 *
 * Le texte du catalogue (dureté, indice de réfraction, densité) est de la
 * prose éditoriale, pas un format numérique garanti ; [parseRange] et
 * [matchesNumeric] sont donc écrits pour ne jamais lever d'exception, quelle
 * que soit la chaîne reçue, et [analyze] isole chaque gemme dans son propre
 * bloc protégé pour qu'une seule entrée imprévue ne puisse pas faire
 * échouer l'analyse entière.
 */
object GemAnalyzer {
    /** Extrait un intervalle numérique d'une chaîne éditoriale du type
     * "6,5 - 7" ou "3,95 – 4,05 (rarement plus)". Ignore tout texte qui ne
     * ressemble pas à un nombre plutôt que d'échouer ; renvoie null si
     * aucun nombre exploitable n'est trouvé. */
    private fun parseRange(raw: String): ClosedFloatingPointRange<Double>? {
        val numberPattern = Regex("""-?\d+(?:[.,]\d+)?""")
        val values = numberPattern.findAll(raw)
            .mapNotNull { it.value.replace(',', '.').toDoubleOrNull() }
            .toList()

        return when {
            values.isEmpty() -> null
            values.size == 1 -> values[0]..values[0]
            else -> values.min()..values.max()
        }
    }

    private fun matchesNumeric(userValue: Double?, catalogRaw: String?, tolerance: Double): Boolean {
        if (userValue == null || catalogRaw == null) return false
        val range = parseRange(catalogRaw) ?: return false
        return userValue in (range.start - tolerance)..(range.endInclusive + tolerance)
    }

    /** Nom de base d'un système cristallin, sans précision entre parenthèses
     * ni variante après "/" (ex. "Trigonal (microcristallin)" -> "Trigonal"). */
    fun baseSystemeCristallin(value: String): String =
        value.substringBefore(" (").substringBefore("/").trim()

    fun systemesCristallins(): List<String> =
        GemsRepository.gems
            .mapNotNull { it.systemeCristallin.takeIf { s -> s.isNotBlank() } }
            .map { baseSystemeCristallin(it) }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

    /** Compare une gemme du catalogue aux critères saisis ; renvoie le
     * nombre de critères correspondants. Protégée individuellement pour
     * qu'une donnée inattendue sur une gemme n'empêche pas l'analyse des
     * autres. */
    private fun matchCount(gem: Gem, diag: GemDiagnostic?, criteria: AnalysisCriteria): Int {
        var matched = 0

        if (criteria.couleur != null && gem.couleur == criteria.couleur) matched++
        if (criteria.transparence != null && diag?.transparence == criteria.transparence) matched++
        if (criteria.eclat != null && diag?.eclat == criteria.eclat) matched++
        if (criteria.clivage != null && diag?.clivage == criteria.clivage) matched++
        if (criteria.systemeCristallin != null &&
            baseSystemeCristallin(gem.systemeCristallin) == criteria.systemeCristallin
        ) matched++
        if (matchesNumeric(criteria.durete, gem.durete, tolerance = 0.5)) matched++
        if (matchesNumeric(criteria.densite, diag?.densite, tolerance = 0.15)) matched++
        if (matchesNumeric(criteria.indiceRefraction, gem.indiceRefraction, tolerance = 0.02)) matched++
        if (criteria.pleochroisme != null && diag?.pleochroisme == criteria.pleochroisme) matched++
        if (criteria.fluorescence != null && diag?.fluorescence == criteria.fluorescence) matched++
        if (criteria.typeInclusion != null && diag?.typeInclusion == criteria.typeInclusion) matched++

        return matched
    }

    fun analyze(criteria: AnalysisCriteria): List<AnalysisMatch> {
        if (criteria.isEmpty) return emptyList()
        val total = criteria.totalCriteria

        return GemsRepository.gems
            .mapNotNull { gem ->
                val matched = runCatching {
                    matchCount(gem, GemDiagnostics.data[gem.id], criteria)
                }.getOrDefault(0)
                if (matched == 0) null else AnalysisMatch(gem, matched, total)
            }
            .sortedWith(compareByDescending<AnalysisMatch> { it.matched }.thenBy { it.gem.nom })
    }
}
