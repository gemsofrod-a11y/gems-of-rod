package fr.gemsofrod.encyclopedie.data

/**
 * Fiche gemmologique d'une pierre de l'encyclopédie.
 */
data class Gem(
    val id: String,
    val nom: String,
    val nomLatin: String,
    val couleur: GemColorCategory,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val formuleChimique: String,
    val systemeCristallin: String,
    val durete: String,
    val origines: List<String>,
    val particularites: String
)
