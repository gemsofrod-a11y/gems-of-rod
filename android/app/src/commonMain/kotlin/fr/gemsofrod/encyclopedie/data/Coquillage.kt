package fr.gemsofrod.encyclopedie.data

/**
 * Grande famille de mollusques utilisée pour le classement du catalogue des
 * coquillages : gastéropodes (escargots de mer à coquille unique), bivalves
 * (coquilles à deux valves) et céphalopodes à coquille externe (nautile).
 *
 * `labelKey` identifie la chaîne localisée à afficher dans l'UI (résolue
 * côté plateforme — sur Android via
 * [fr.gemsofrod.encyclopedie.ui.resolveLabelStringRes] vers la ressource
 * `R.string` de même nom).
 */
enum class CoquillageFamille(val labelKey: String) {
    GASTROPODE("coquillage_famille_gastropode"),
    BIVALVE("coquillage_famille_bivalve"),
    CEPHALOPODE("coquillage_famille_cephalopode")
}

/**
 * Fiche d'un coquillage individuel, pensée pour l'activité de lapidaire :
 * au-delà de la classification malacologique, elle documente si la nacre ou
 * la coquille se taille et se polit, sa qualité gemme éventuelle et son
 * intérêt en joaillerie et décoration — à l'inverse d'un coquillage de
 * collection naturaliste pure.
 *
 * `prixApprox` reste une fourchette indicative par pièce (et non par gramme,
 * contrairement aux fossiles et météorites) : la taille et l'état d'un
 * coquillage entier comptent bien davantage que son poids brut. Ce n'est pas
 * une cotation.
 */
data class Coquillage(
    val id: String,
    val nom: String,
    val nomLatin: String,
    val origine: String,
    val famille: CoquillageFamille,
    val compositionMinerale: String,
    val durete: String,
    val densite: String,
    val couleur: String,
    val taillePossible: String,
    val qualiteGemme: String,
    val interetJoaillerie: String,
    val rarete: GemRarete,
    val prixApprox: String,
    val descriptionCourte: String,
    val descriptionLongue: String
)
