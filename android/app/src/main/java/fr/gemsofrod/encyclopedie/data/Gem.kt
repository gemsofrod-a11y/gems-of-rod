package fr.gemsofrod.encyclopedie.data

/**
 * Niveau de rareté indicatif d'une gemme sur le marché de la joaillerie.
 */
enum class GemRarete(val label: String) {
    COURANTE("Courante"),
    PEU_COMMUNE("Peu commune"),
    RARE("Rare"),
    EXCEPTIONNELLE("Exceptionnelle")
}

/**
 * Fiche gemmologique d'une pierre de l'encyclopédie.
 *
 * `prixCaratEur` est une fourchette indicative en euros par carat pour une
 * pierre naturelle de qualité commerciale à belle qualité : la valeur réelle
 * d'une pierre précise dépend énormément de son origine, sa pureté, sa taille
 * et l'existence ou non de traitements — ce n'est pas une cotation.
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
    val origines: List<String>,
    val particularites: String,
    val prixCaratEur: String,
    val rarete: GemRarete
)
