package fr.gemsofrod.encyclopedie.data

/**
 * Grande famille de météorites, au sens de la classification traditionnelle
 * par composition (sidérites, sidérolithes, aérolithes), complétée ici par
 * deux catégories d'usage pour un lapidaire : les pièces de prestige
 * scientifique (non taillées) et les météorites martiennes/lunaires
 * (rareté et prix hors norme).
 *
 * `labelKey` identifie la chaîne localisée à afficher dans l'UI (résolue
 * côté plateforme — sur Android via
 * [fr.gemsofrod.encyclopedie.ui.resolveLabelStringRes] vers la ressource
 * `R.string` de même nom).
 */
enum class MeteoriteFamille(val labelKey: String) {
    FERREUSE("meteorite_famille_ferreuse"),
    PALLASITE("meteorite_famille_pallasite"),
    CHONDRITE("meteorite_famille_chondrite"),
    PRESTIGE("meteorite_famille_prestige"),
    MARTIENNE_LUNAIRE("meteorite_famille_martienne_lunaire")
}

/**
 * Fiche d'une météorite individuelle, pensée pour l'activité de lapidaire :
 * au-delà de la classification scientifique, elle documente si la pièce se
 * taille et se polit, sa qualité gemme éventuelle et son intérêt en
 * joaillerie — à l'inverse d'une météorite de collection scientifique pure.
 *
 * `prixApproxGramme` est une fourchette indicative en euros par gramme :
 * elle varie énormément selon la taille du fragment, sa provenance certifiée
 * et l'état du marché — ce n'est pas une cotation.
 */
data class Meteorite(
    val id: String,
    val nom: String,
    val origine: String,
    val classification: String,
    val famille: MeteoriteFamille,
    val compositionMinerale: String,
    val durete: String,
    val densite: String,
    val couleur: String,
    val taillePossible: String,
    val qualiteGemme: String,
    val interetJoaillerie: String,
    val rarete: GemRarete,
    val prixApproxGramme: String,
    val descriptionCourte: String,
    val descriptionLongue: String
)
