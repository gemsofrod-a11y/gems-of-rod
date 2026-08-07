package fr.gemsofrod.encyclopedie.data

/** Champs de [Gem] traduits pour une langue donnée. */
data class GemTranslation(
    val nom: String,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val particularites: String,
    val lithotherapie: String
)

/**
 * Contenu localisé des fiches gemmes (nom et textes descriptifs). Chaque
 * langue non-française expose une Map<gemId, GemTranslation> ; tant qu'une
 * gemme n'a pas de traduction disponible pour la langue demandée, on retombe
 * sur le contenu français d'origine — aucune langue n'a besoin d'une
 * couverture à 100 % pour être utilisable.
 *
 * Les champs non listés dans [GemTranslation] (formule chimique, système
 * cristallin, dureté, indice de réfraction, origines, famille, prix, rareté)
 * restent en français : ce sont des données techniques/proper nouns qui ne
 * nécessitent pas de traduction, ou dont la traduction est gérée séparément
 * (ex. la rareté et la couleur, déjà localisées via des ressources de
 * chaînes).
 */
object GemLocalization {
    private val byLanguage: Map<String, Map<String, GemTranslation>> = mapOf(
        AppLanguage.EN.code to GemTranslationsEn.data,
        AppLanguage.ES.code to GemTranslationsEs.data
    )

    fun localize(gem: Gem, languageCode: String): Gem {
        val translation = byLanguage[languageCode]?.get(gem.id) ?: return gem
        return gem.copy(
            nom = translation.nom,
            descriptionCourte = translation.descriptionCourte,
            descriptionLongue = translation.descriptionLongue,
            particularites = translation.particularites,
            lithotherapie = translation.lithotherapie
        )
    }
}
