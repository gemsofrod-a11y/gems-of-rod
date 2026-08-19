package fr.gemsofrod.encyclopedie.ui

import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemRarete
import fr.gemsofrod.encyclopedie.data.MeteoriteFamille

/**
 * Résout la ressource de chaîne localisée Android correspondant à
 * `labelKey` (ex. [GemColorCategory.labelKey], [GemRarete.labelKey],
 * [MeteoriteFamille.labelKey]) — ces enums vivent en commonMain et ne
 * peuvent donc pas référencer directement `R.string`.
 */
fun resolveLabelStringRes(labelKey: String): Int = when (labelKey) {
    "rarete_courante" -> R.string.rarete_courante
    "rarete_peu_commune" -> R.string.rarete_peu_commune
    "rarete_rare" -> R.string.rarete_rare
    "rarete_exceptionnelle" -> R.string.rarete_exceptionnelle

    "color_rouge" -> R.string.color_rouge
    "color_orange" -> R.string.color_orange
    "color_jaune" -> R.string.color_jaune
    "color_vert" -> R.string.color_vert
    "color_bleu" -> R.string.color_bleu
    "color_violet" -> R.string.color_violet
    "color_rose" -> R.string.color_rose
    "color_incolore" -> R.string.color_incolore
    "color_brun" -> R.string.color_brun
    "color_noir" -> R.string.color_noir
    "color_multicolore" -> R.string.color_multicolore

    "meteorite_famille_ferreuse" -> R.string.meteorite_famille_ferreuse
    "meteorite_famille_pallasite" -> R.string.meteorite_famille_pallasite
    "meteorite_famille_chondrite" -> R.string.meteorite_famille_chondrite
    "meteorite_famille_prestige" -> R.string.meteorite_famille_prestige
    "meteorite_famille_martienne_lunaire" -> R.string.meteorite_famille_martienne_lunaire

    else -> error("Clé de libellé inconnue : $labelKey")
}

val GemRarete.labelRes: Int get() = resolveLabelStringRes(labelKey)
val GemColorCategory.labelRes: Int get() = resolveLabelStringRes(labelKey)
val MeteoriteFamille.labelRes: Int get() = resolveLabelStringRes(labelKey)
