package fr.gemsofrod.encyclopedie.data

/**
 * Rubriques approfondies d'une fiche pierre (demande de Sébastien du
 * 08/11/2026) : formation géologique, cristallographie détaillée, gisements
 * région par région et lithothérapie approfondie (usages traditionnels,
 * purification, rechargement — croyances, jamais allégations médicales).
 *
 * Le contenu vit dans des fichiers par lot (`GemDetailsLot01.kt`, …), chaque
 * lot portant les 9 langues de l'app pour ses pierres : une pierre n'est
 * ajoutée à un lot qu'une fois traduite partout (règle CLAUDE.md). Une pierre
 * encore absente de tous les lots n'affiche simplement pas ces rubriques.
 */
data class GemDetails(
    val formation: String,
    val cristallographie: String,
    val gisements: String,
    val lithotherapie: String
)

object GemDetailsRepository {
    private val byGem: Map<String, Map<String, GemDetails>> = buildMap {
        putAll(GemDetailsLot01.data)
        putAll(GemDetailsLot02.data)
        putAll(GemDetailsLot03.data)
        putAll(GemDetailsLot04.data)
        putAll(GemDetailsLot05.data)
        putAll(GemDetailsLot06.data)
        putAll(GemDetailsLot07.data)
        putAll(GemDetailsLot08.data)
        putAll(GemDetailsLot09.data)
        putAll(GemDetailsLot10.data)
        putAll(GemDetailsLot11.data)
        putAll(GemDetailsLot12.data)
        putAll(GemDetailsLot13.data)
        putAll(GemDetailsLot14.data)
        putAll(GemDetailsLot15.data)
        putAll(GemDetailsLot16.data)
        putAll(GemDetailsLot17.data)
        putAll(GemDetailsLot18.data)
        putAll(GemDetailsLot19.data)
        putAll(GemDetailsLot20.data)
        putAll(GemDetailsLot21.data)
        putAll(GemDetailsLot22.data)
        putAll(GemDetailsLot23.data)
        putAll(GemDetailsLot24.data)
        putAll(GemDetailsLot25.data)
        putAll(GemDetailsLot26.data)
        putAll(GemDetailsLot27.data)
        putAll(GemDetailsLot28.data)
        putAll(GemDetailsLot29.data)
        putAll(GemDetailsLot30.data)
        putAll(GemDetailsLot31.data)
        putAll(GemDetailsLot32.data)
        putAll(GemDetailsLot33.data)
        putAll(GemDetailsLot34.data)
    }

    /** Rubriques dans la langue demandée, en français à défaut, ou null. */
    fun detailsFor(gemId: String, languageCode: String): GemDetails? {
        val languages = byGem[gemId] ?: return null
        return languages[languageCode] ?: languages["fr"]
    }

    fun hasDetails(gemId: String): Boolean = gemId in byGem
}
