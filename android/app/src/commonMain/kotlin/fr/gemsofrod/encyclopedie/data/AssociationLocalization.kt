package fr.gemsofrod.encyclopedie.data

/** Champs de [Association] traduits pour une langue donnée. */
data class AssociationTranslation(
    val titre: String,
    val intention: String,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val conseilUtilisation: String
)

/**
 * Contenu localisé des fiches d'association de pierres. Les identifiants de
 * gemmes restent en français (données techniques, gérées séparément) ; seuls
 * les textes descriptifs sont traduits.
 */
object AssociationLocalization {
    private val byLanguage: Map<String, Map<String, AssociationTranslation>> = emptyMap()

    fun localize(association: Association, languageCode: String): Association {
        val translation = byLanguage[languageCode]?.get(association.id) ?: return association
        return association.copy(
            titre = translation.titre,
            intention = translation.intention,
            descriptionCourte = translation.descriptionCourte,
            descriptionLongue = translation.descriptionLongue,
            conseilUtilisation = translation.conseilUtilisation
        )
    }
}
