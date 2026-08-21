package fr.gemsofrod.encyclopedie.data

/**
 * Fiche d'une association traditionnelle de pierres en lithothérapie : deux
 * ou trois gemmes du catalogue ([GemsRepository]) réputées se compléter pour
 * une intention donnée. Les champs techniques (identifiants des gemmes)
 * restent stables ; les textes sont traduits via [AssociationLocalization].
 */
data class Association(
    val id: String,
    val gemIds: List<String>,
    val titre: String,
    val intention: String,
    val descriptionCourte: String,
    val descriptionLongue: String,
    val conseilUtilisation: String
)
