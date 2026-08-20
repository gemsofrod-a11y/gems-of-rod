package fr.gemsofrod.encyclopedie.data

/**
 * Grande famille de fossiles utilisée pour le classement du catalogue,
 * proche d'une classification paléontologique usuelle mais adaptée à
 * l'activité de lapidaire : elle distingue notamment les fossiles qui se
 * taillent et se polissent en bijouterie (ammonite, bois fossile, ambre...)
 * de ceux qui restent essentiellement des pièces de collection.
 *
 * `labelKey` identifie la chaîne localisée à afficher dans l'UI (résolue
 * côté plateforme — sur Android via
 * [fr.gemsofrod.encyclopedie.ui.resolveLabelStringRes] vers la ressource
 * `R.string` de même nom).
 */
enum class FossileFamille(val labelKey: String) {
    AMMONITE_MOLLUSQUE("fossile_famille_ammonite"),
    TRILOBITE_ARTHROPODE("fossile_famille_trilobite"),
    VERTEBRE("fossile_famille_vertebre"),
    VEGETAL("fossile_famille_vegetal"),
    AMBRE_INSECTE("fossile_famille_ambre"),
    CORAIL_ECHINODERME("fossile_famille_corail")
}

/**
 * Fiche d'un fossile individuel, pensée pour l'activité de lapidaire : au-delà
 * de la classification paléontologique, elle documente si la pièce se taille
 * et se polit, sa qualité gemme éventuelle et son intérêt en joaillerie — à
 * l'inverse d'un fossile de collection scientifique pure.
 *
 * `ageApprox` reste une fourchette d'âge géologique volontairement large
 * (l'estimation précise dépend du gisement) et `prixApproxGramme` une
 * fourchette indicative en euros par gramme : elle varie énormément selon la
 * taille de la pièce, sa provenance certifiée et l'état du marché — ce n'est
 * pas une cotation.
 */
data class Fossile(
    val id: String,
    val nom: String,
    val origine: String,
    val periodeGeologique: String,
    val ageApprox: String,
    val classification: String,
    val famille: FossileFamille,
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
