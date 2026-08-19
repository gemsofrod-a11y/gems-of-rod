package fr.gemsofrod.encyclopedie.data

/**
 * Niveau de rareté indicatif d'une gemme sur le marché de la joaillerie.
 *
 * `label` est le nom en français (fallback interne) ; `labelKey` identifie la
 * chaîne localisée à afficher dans l'UI (résolue côté plateforme — sur
 * Android via [fr.gemsofrod.encyclopedie.ui.resolveLabelStringRes] vers la
 * ressource `R.string` de même nom).
 */
enum class GemRarete(val label: String, val labelKey: String) {
    COURANTE("Courante", "rarete_courante"),
    PEU_COMMUNE("Peu commune", "rarete_peu_commune"),
    RARE("Rare", "rarete_rare"),
    EXCEPTIONNELLE("Exceptionnelle", "rarete_exceptionnelle")
}

/**
 * Fiche gemmologique d'une pierre de l'encyclopédie.
 *
 * `prixCaratEur` est une fourchette indicative en euros par carat pour une
 * pierre naturelle de qualité commerciale à belle qualité : la valeur réelle
 * d'une pierre précise dépend énormément de son origine, sa pureté, sa taille
 * et l'existence ou non de traitements — ce n'est pas une cotation.
 *
 * `lithotherapie` décrit des croyances et usages traditionnels (bien-être,
 * symbolique) associés à la gemme. Ce contenu relève de la tradition
 * populaire, pas de faits scientifiquement établis : il ne constitue ni un
 * avis médical, ni une allégation de soin.
 */
data class Gem(
    val id: String,
    val nom: String,
    val nomLatin: String,
    val famille: String,
    val couleur: GemColorCategory,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val formuleChimique: String,
    val systemeCristallin: String,
    val durete: String,
    val indiceRefraction: String,
    val origines: List<String>,
    val particularites: String,
    val lithotherapie: String,
    val prixCaratEur: String,
    val rarete: GemRarete
)
