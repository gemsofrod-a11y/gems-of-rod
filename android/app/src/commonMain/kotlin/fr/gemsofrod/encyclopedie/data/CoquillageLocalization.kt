package fr.gemsofrod.encyclopedie.data

/** Champs de [Coquillage] traduits pour une langue donnée. */
data class CoquillageTranslation(
    val nom: String,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val interetJoaillerie: String
)

/**
 * Contenu localisé des fiches de coquillage. Comme pour [FossileLocalization]
 * et [MeteoriteLocalization], les identifiants et les champs techniques
 * (composition, dureté, densité, couleur, possibilité de taille, qualité
 * gemme, prix, famille) restent en français, gérés séparément dans
 * [CoquillagesRepository] ; seuls le nom d'usage, les textes descriptifs et
 * l'intérêt en joaillerie sont traduits.
 */
object CoquillageLocalization {
    private val byLanguage: Map<String, Map<String, CoquillageTranslation>> = mapOf(
        AppLanguage.EN.code to CoquillageTranslationsEn.data,
        AppLanguage.ES.code to CoquillageTranslationsEs.data,
        AppLanguage.IT.code to CoquillageTranslationsIt.data,
        AppLanguage.DE.code to CoquillageTranslationsDe.data,
        AppLanguage.PT.code to CoquillageTranslationsPt.data,
        AppLanguage.ZH.code to CoquillageTranslationsZh.data,
        AppLanguage.RU.code to CoquillageTranslationsRu.data,
        AppLanguage.NL.code to CoquillageTranslationsNl.data
    )

    fun localize(coquillage: Coquillage, languageCode: String): Coquillage {
        val translation = byLanguage[languageCode]?.get(coquillage.id) ?: return coquillage
        return coquillage.copy(
            nom = translation.nom,
            descriptionCourte = translation.descriptionCourte,
            descriptionLongue = translation.descriptionLongue,
            interetJoaillerie = translation.interetJoaillerie
        )
    }
}
