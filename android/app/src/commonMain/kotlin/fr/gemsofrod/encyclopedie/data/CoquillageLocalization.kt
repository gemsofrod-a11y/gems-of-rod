package fr.gemsofrod.encyclopedie.data

/** Champs de [Coquillage] traduits pour une langue donnée. */
data class CoquillageTranslation(
    val nom: String,
    val descriptionCourte: String,
    val descriptionLongue: String
)

/**
 * Contenu localisé des fiches de coquillage. Les identifiants et les champs
 * techniques (composition, dureté, densité...) restent en français, gérés
 * séparément dans [CoquillagesRepository] ; seuls le nom d'usage et les
 * textes descriptifs sont traduits. Aucune traduction n'est encore
 * disponible : [localize] retombe systématiquement sur le contenu français
 * d'origine, comme [AssociationLocalization] avant sa traduction.
 */
object CoquillageLocalization {
    private val byLanguage: Map<String, Map<String, CoquillageTranslation>> = emptyMap()

    fun localize(coquillage: Coquillage, languageCode: String): Coquillage {
        val translation = byLanguage[languageCode]?.get(coquillage.id) ?: return coquillage
        return coquillage.copy(
            nom = translation.nom,
            descriptionCourte = translation.descriptionCourte,
            descriptionLongue = translation.descriptionLongue
        )
    }
}
