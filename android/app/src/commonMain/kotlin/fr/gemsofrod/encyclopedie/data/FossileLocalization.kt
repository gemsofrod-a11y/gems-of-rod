package fr.gemsofrod.encyclopedie.data

/** Champs de [Fossile] traduits pour une langue donnée. */
data class FossileTranslation(
    val nom: String,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val interetJoaillerie: String
)

/**
 * Contenu localisé des fiches fossiles (nom, textes descriptifs et intérêt
 * en joaillerie). Comme pour [MeteoriteLocalization], les champs techniques
 * (origine, période géologique, âge, classification, composition, dureté,
 * densité, couleur, possibilité de taille, qualité gemme, prix, famille)
 * restent en français : ce sont des données techniques/proper nouns qui ne
 * nécessitent pas de traduction, ou dont la traduction est gérée séparément
 * (la famille, via l'enum [FossileFamille] et ses ressources de chaînes
 * localisées).
 *
 * Traductions non françaises à venir (voir [MeteoriteLocalization] pour le
 * patron à suivre) ; en attendant, [localize] retombe sur le contenu
 * français pour toute langue non couverte.
 */
object FossileLocalization {
    private val byLanguage: Map<String, Map<String, FossileTranslation>> = emptyMap()

    fun localize(fossile: Fossile, languageCode: String): Fossile {
        val translation = byLanguage[languageCode]?.get(fossile.id) ?: return fossile
        return fossile.copy(
            nom = translation.nom,
            descriptionCourte = translation.descriptionCourte,
            descriptionLongue = translation.descriptionLongue,
            interetJoaillerie = translation.interetJoaillerie
        )
    }
}
