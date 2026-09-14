package fr.gemsofrod.encyclopedie.data

import androidx.compose.ui.graphics.Color
import kotlin.math.round

/** Description d'une pierre montée (centrale ou une pierre annexe type). */
data class RingStoneSelection(
    val species: RingGemSpecies,
    val color: Color,
    val sertissage: RingSertissage,
    val cut: RingCutShape,
    val widthMm: Double,
    val heightMm: Double,
    val orientationDeg: Float
)

/** Rangée de pierres annexes : une pierre type répétée [nombre] fois de chaque côté. */
data class RingSideStoneSelection(
    val stone: RingStoneSelection,
    val nombre: Int,
    val espacement: Float,
    val orientationOffsetDeg: Float
)

/** Composition complète d'une bague, telle que saisie dans le formulaire. */
data class RingConfiguration(
    val metal: RingMetal,
    val gramsMetal: Double,
    val central: RingStoneSelection,
    val cote: RingSideStoneSelection
)

/** Estimation de prix indicative, à titre de fourchette (pas un devis). */
data class RingPricingResult(
    val metalPrice: Double,
    val centralPrice: Double,
    val cotePrice: Double,
    val faconFee: Double,
    val retail: Double,
    val rangeMinRounded: Double,
    val rangeMaxRounded: Double
)

/**
 * Calcule une estimation de prix indicative pour une [RingConfiguration] :
 * métal (poids × prix au gramme) + pierre centrale + pierres annexes
 * (estimées en carats via [RingCaratEstimator]) + façon (main-d'œuvre),
 * puis une marge de détail et une fourchette autour du résultat — tout
 * dépendra en pratique du type exact de pierre, d'où l'affichage d'une
 * fourchette plutôt qu'un montant unique.
 */
object RingPricingCalculator {

    fun compute(config: RingConfiguration): RingPricingResult {
        val metalPrice = config.gramsMetal * config.metal.pricePerGram

        val centralCarats = RingCaratEstimator.estimateCarats(
            config.central.species.specificGravity,
            config.central.widthMm,
            config.central.heightMm
        )
        val centralPrice = centralCarats * config.central.species.pricePerCarat

        val nombre = config.cote.nombre
        val cotePrice = if (nombre > 0) {
            val coteCarats = RingCaratEstimator.estimateCarats(
                config.cote.stone.species.specificGravity,
                config.cote.stone.widthMm,
                config.cote.stone.heightMm
            )
            coteCarats * config.cote.stone.species.pricePerCarat * nombre * 2
        } else {
            0.0
        }

        val faconFee = 140.0 + nombre * 2 * 6.0
        val retail = (metalPrice + centralPrice + cotePrice + faconFee) * 1.65

        return RingPricingResult(
            metalPrice = metalPrice,
            centralPrice = centralPrice,
            cotePrice = cotePrice,
            faconFee = faconFee,
            retail = retail,
            rangeMinRounded = roundToNearestTen(retail * 0.88),
            rangeMaxRounded = roundToNearestTen(retail * 1.18)
        )
    }

    private fun roundToNearestTen(value: Double): Double = round(value / 10.0) * 10.0
}
