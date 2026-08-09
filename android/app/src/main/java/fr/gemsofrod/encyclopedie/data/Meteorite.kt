package fr.gemsofrod.encyclopedie.data

import fr.gemsofrod.encyclopedie.R

/**
 * Grande famille de météorites, au sens de la classification traditionnelle
 * par composition (sidérites, sidérolithes, aérolithes), complétée ici par
 * deux catégories d'usage pour un lapidaire : les pièces de prestige
 * scientifique (non taillées) et les météorites martiennes/lunaires
 * (rareté et prix hors norme).
 */
enum class MeteoriteFamille(val labelRes: Int) {
    FERREUSE(R.string.meteorite_famille_ferreuse),
    PALLASITE(R.string.meteorite_famille_pallasite),
    CHONDRITE(R.string.meteorite_famille_chondrite),
    PRESTIGE(R.string.meteorite_famille_prestige),
    MARTIENNE_LUNAIRE(R.string.meteorite_famille_martienne_lunaire)
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
