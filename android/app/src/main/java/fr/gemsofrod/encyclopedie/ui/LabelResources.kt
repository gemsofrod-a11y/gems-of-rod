package fr.gemsofrod.encyclopedie.ui

import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.CoquillageFamille
import fr.gemsofrod.encyclopedie.data.FossileFamille
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemRarete
import fr.gemsofrod.encyclopedie.data.MeteoriteFamille
import fr.gemsofrod.encyclopedie.data.RingCutShape
import fr.gemsofrod.encyclopedie.data.RingGemSpecies
import fr.gemsofrod.encyclopedie.data.RingMetal
import fr.gemsofrod.encyclopedie.data.RingSertissage

/**
 * Résout la ressource de chaîne localisée Android correspondant à
 * `labelKey` (ex. [GemColorCategory.labelKey], [GemRarete.labelKey],
 * [MeteoriteFamille.labelKey], [FossileFamille.labelKey]) — ces enums
 * vivent en commonMain et ne peuvent donc pas référencer directement
 * `R.string`.
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

    "fossile_famille_ammonite" -> R.string.fossile_famille_ammonite
    "fossile_famille_trilobite" -> R.string.fossile_famille_trilobite
    "fossile_famille_vertebre" -> R.string.fossile_famille_vertebre
    "fossile_famille_vegetal" -> R.string.fossile_famille_vegetal
    "fossile_famille_ambre" -> R.string.fossile_famille_ambre
    "fossile_famille_corail" -> R.string.fossile_famille_corail

    "coquillage_famille_gastropode" -> R.string.coquillage_famille_gastropode
    "coquillage_famille_bivalve" -> R.string.coquillage_famille_bivalve
    "coquillage_famille_cephalopode" -> R.string.coquillage_famille_cephalopode

    "ring_metal_or_jaune" -> R.string.ring_metal_or_jaune
    "ring_metal_or_blanc" -> R.string.ring_metal_or_blanc
    "ring_metal_or_rose" -> R.string.ring_metal_or_rose
    "ring_metal_platine" -> R.string.ring_metal_platine

    "ring_species_diamant" -> R.string.ring_species_diamant
    "ring_species_rubis" -> R.string.ring_species_rubis
    "ring_species_emeraude" -> R.string.ring_species_emeraude
    "ring_species_saphir" -> R.string.ring_species_saphir
    "ring_species_alexandrite" -> R.string.ring_species_alexandrite
    "ring_species_tanzanite" -> R.string.ring_species_tanzanite
    "ring_species_spinelle" -> R.string.ring_species_spinelle
    "ring_species_tourmaline" -> R.string.ring_species_tourmaline
    "ring_species_topaze" -> R.string.ring_species_topaze
    "ring_species_grenat" -> R.string.ring_species_grenat
    "ring_species_kunzite" -> R.string.ring_species_kunzite
    "ring_species_morganite" -> R.string.ring_species_morganite

    "ring_cut_ronde" -> R.string.ring_cut_ronde
    "ring_cut_ovale" -> R.string.ring_cut_ovale
    "ring_cut_poire" -> R.string.ring_cut_poire
    "ring_cut_marquise" -> R.string.ring_cut_marquise
    "ring_cut_briolette" -> R.string.ring_cut_briolette
    "ring_cut_asscher" -> R.string.ring_cut_asscher
    "ring_cut_baguette" -> R.string.ring_cut_baguette
    "ring_cut_princesse" -> R.string.ring_cut_princesse
    "ring_cut_coussin" -> R.string.ring_cut_coussin
    "ring_cut_kite" -> R.string.ring_cut_kite
    "ring_cut_coeur" -> R.string.ring_cut_coeur
    "ring_cut_trillion" -> R.string.ring_cut_trillion
    "ring_cut_emeraude" -> R.string.ring_cut_emeraude
    "ring_cut_radiant" -> R.string.ring_cut_radiant

    "ring_sertissage_griffes" -> R.string.ring_sertissage_griffes
    "ring_sertissage_clos" -> R.string.ring_sertissage_clos
    "ring_sertissage_demiclos" -> R.string.ring_sertissage_demiclos
    "ring_sertissage_pave" -> R.string.ring_sertissage_pave
    "ring_sertissage_rail" -> R.string.ring_sertissage_rail
    "ring_sertissage_ras" -> R.string.ring_sertissage_ras
    "ring_sertissage_brightcut" -> R.string.ring_sertissage_brightcut
    "ring_sertissage_barrette" -> R.string.ring_sertissage_barrette
    "ring_sertissage_cluster" -> R.string.ring_sertissage_cluster
    "ring_sertissage_grain" -> R.string.ring_sertissage_grain

    else -> error("Clé de libellé inconnue : $labelKey")
}

val GemRarete.labelRes: Int get() = resolveLabelStringRes(labelKey)
val GemColorCategory.labelRes: Int get() = resolveLabelStringRes(labelKey)
val MeteoriteFamille.labelRes: Int get() = resolveLabelStringRes(labelKey)
val FossileFamille.labelRes: Int get() = resolveLabelStringRes(labelKey)
val CoquillageFamille.labelRes: Int get() = resolveLabelStringRes(labelKey)
val RingMetal.labelRes: Int get() = resolveLabelStringRes(labelKey)
val RingGemSpecies.labelRes: Int get() = resolveLabelStringRes(labelKey)
val RingCutShape.labelRes: Int get() = resolveLabelStringRes(labelKey)
val RingSertissage.labelRes: Int get() = resolveLabelStringRes(labelKey)
