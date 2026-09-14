package fr.gemsofrod.encyclopedie.data

import kotlin.math.round
import kotlin.test.Test
import kotlin.test.assertEquals

class RingPricingCalculatorTest {

    private fun stone(species: RingGemSpecies, sertissage: RingSertissage, width: Double, height: Double) =
        RingStoneSelection(
            species = species,
            color = species.defaultColor,
            sertissage = sertissage,
            cut = RingCutShape.RONDE,
            widthMm = width,
            heightMm = height,
            orientationDeg = 0f
        )

    @Test
    fun soloStone_facconFeeHasNoSideStoneTerm_andNoCotePrice() {
        val config = RingConfiguration(
            metal = RingMetal.PLATINE,
            gramsMetal = 3.5,
            central = stone(RingGemSpecies.DIAMANT, RingSertissage.GRIFFES, 8.0, 6.0),
            cote = RingSideStoneSelection(
                stone = stone(RingGemSpecies.SAPHIR, RingSertissage.PAVE, 1.8, 1.8),
                nombre = 0,
                espacement = 0.15f,
                orientationOffsetDeg = 0f
            )
        )
        val result = RingPricingCalculator.compute(config)

        val expectedMetal = 3.5 * RingMetal.PLATINE.pricePerGram
        val expectedCentralCarats = RingCaratEstimator.estimateCarats(RingGemSpecies.DIAMANT.specificGravity, 8.0, 6.0)
        val expectedCentral = expectedCentralCarats * RingGemSpecies.DIAMANT.pricePerCarat
        val expectedFacon = 140.0
        val expectedRetail = (expectedMetal + expectedCentral + 0.0 + expectedFacon) * 1.65

        assertEquals(expectedMetal, result.metalPrice, 1e-6)
        assertEquals(expectedCentral, result.centralPrice, 1e-6)
        assertEquals(0.0, result.cotePrice, 1e-6)
        assertEquals(expectedFacon, result.faconFee, 1e-6)
        assertEquals(expectedRetail, result.retail, 1e-6)
    }

    @Test
    fun sideStones_priceCountsBothSides_andFacconFeeScalesWithNombre() {
        val nombre = 6
        val config = RingConfiguration(
            metal = RingMetal.OR_JAUNE,
            gramsMetal = 6.0,
            central = stone(RingGemSpecies.EMERAUDE, RingSertissage.CLOS, 7.5, 7.5),
            cote = RingSideStoneSelection(
                stone = stone(RingGemSpecies.DIAMANT, RingSertissage.PAVE, 1.6, 1.6),
                nombre = nombre,
                espacement = 0.15f,
                orientationOffsetDeg = 0f
            )
        )
        val result = RingPricingCalculator.compute(config)

        val coteCarats = RingCaratEstimator.estimateCarats(RingGemSpecies.DIAMANT.specificGravity, 1.6, 1.6)
        val expectedCote = coteCarats * RingGemSpecies.DIAMANT.pricePerCarat * nombre * 2
        val expectedFacon = 140.0 + nombre * 2 * 6.0

        assertEquals(expectedCote, result.cotePrice, 1e-6)
        assertEquals(expectedFacon, result.faconFee, 1e-6)
    }

    @Test
    fun displayedRange_is88to118PercentOfRetail_roundedToNearestTen() {
        val config = RingConfiguration(
            metal = RingMetal.OR_BLANC,
            gramsMetal = 4.5,
            central = stone(RingGemSpecies.DIAMANT, RingSertissage.GRIFFES, 6.5, 6.5),
            cote = RingSideStoneSelection(
                stone = stone(RingGemSpecies.DIAMANT, RingSertissage.GRIFFES, 4.5, 4.5),
                nombre = 1,
                espacement = 0.5f,
                orientationOffsetDeg = 0f
            )
        )
        val result = RingPricingCalculator.compute(config)

        assertEquals(round(result.retail * 0.88 / 10.0) * 10.0, result.rangeMinRounded, 1e-6)
        assertEquals(round(result.retail * 1.18 / 10.0) * 10.0, result.rangeMaxRounded, 1e-6)
    }
}
