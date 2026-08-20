package fr.gemsofrod.encyclopedie.data

/**
 * Champs de [Gem] traduits pour une langue donnée.
 *
 * [inclusions] traduit [GemDiagnostic.inclusions] ; il reste `null` tant
 * qu'aucune traduction n'existe pour cette gemme (notamment les gemmes
 * translucides à opaques ou opaques, pour lesquelles le texte français
 * source est lui-même vide).
 */
data class GemTranslation(
    val nom: String,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val particularites: String,
    val lithotherapie: String,
    val inclusions: String? = null
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
        AppLanguage.ES.code to GemTranslationsEs.data,
        AppLanguage.IT.code to GemTranslationsIt.data,
        AppLanguage.DE.code to GemTranslationsDe.data,
        AppLanguage.ZH.code to GemTranslationsZh.data
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

    /**
     * Texte des inclusions typiques ([GemDiagnostic.inclusions]) traduit
     * dans la langue demandée, ou le texte français d'origine si aucune
     * traduction n'est disponible pour cette gemme et cette langue.
     */
    fun localizeInclusions(gemId: String, languageCode: String): String? {
        val original = GemDiagnostics.data[gemId]?.inclusions?.takeIf { it.isNotBlank() } ?: return null
        return byLanguage[languageCode]?.get(gemId)?.inclusions ?: original
    }
}
