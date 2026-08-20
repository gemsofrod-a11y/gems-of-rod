package fr.gemsofrod.encyclopedie.data

/** Champs de [Meteorite] traduits pour une langue donnée. */
data class MeteoriteTranslation(
    val nom: String,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val interetJoaillerie: String
)

/**
 * Contenu localisé des fiches météorites (nom, textes descriptifs et intérêt
 * en joaillerie). Comme pour [GemLocalization], les champs techniques
 * (origine, classification, composition, dureté, densité, couleur,
 * possibilité de taille, qualité gemme, prix, famille) restent en français :
 * ce sont des données techniques/proper nouns qui ne nécessitent pas de
 * traduction, ou dont la traduction est gérée séparément (la famille, via
 * l'enum [MeteoriteFamille] et ses ressources de chaînes localisées).
 */
object MeteoriteLocalization {
    private val byLanguage: Map<String, Map<String, MeteoriteTranslation>> = mapOf(
        AppLanguage.EN.code to MeteoriteTranslationsEn.data,
        AppLanguage.ES.code to MeteoriteTranslationsEs.data,
        AppLanguage.IT.code to MeteoriteTranslationsIt.data,
        AppLanguage.DE.code to MeteoriteTranslationsDe.data
    )

    fun localize(meteorite: Meteorite, languageCode: String): Meteorite {
        val translation = byLanguage[languageCode]?.get(meteorite.id) ?: return meteorite
        return meteorite.copy(
            nom = translation.nom,
            descriptionCourte = translation.descriptionCourte,
            descriptionLongue = translation.descriptionLongue,
            interetJoaillerie = translation.interetJoaillerie
        )
    }
}
