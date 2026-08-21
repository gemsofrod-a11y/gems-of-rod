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
 */
object FossileLocalization {
    private val byLanguage: Map<String, Map<String, FossileTranslation>> = mapOf(
        AppLanguage.EN.code to FossileTranslationsEn.data,
        AppLanguage.ES.code to FossileTranslationsEs.data,
        AppLanguage.IT.code to FossileTranslationsIt.data,
        AppLanguage.DE.code to FossileTranslationsDe.data,
        AppLanguage.PT.code to FossileTranslationsPt.data,
        AppLanguage.ZH.code to FossileTranslationsZh.data,
        AppLanguage.RU.code to FossileTranslationsRu.data,
        AppLanguage.NL.code to FossileTranslationsNl.data
    )

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
