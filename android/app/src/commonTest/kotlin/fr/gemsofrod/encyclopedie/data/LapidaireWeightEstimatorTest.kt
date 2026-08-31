package fr.gemsofrod.encyclopedie.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LapidaireWeightEstimatorTest {

    @Test
    fun rond_matchesDiameterSquaredFormula() {
        // Diamant rond, 6.5 mm de diamètre, 4.0 mm de hauteur, P.S. 3.52.
        val expected = 6.5 * 6.5 * 4.0 * 3.52 * LapidaireCutShape.ROND.coefficient
        val actual = LapidaireWeightEstimator.estimateWeightCarats(
            shape = LapidaireCutShape.ROND,
            dimension1Mm = 6.5,
            dimension2Mm = null,
            heightMm = 4.0,
            specificGravity = 3.52
        )
        assertEquals(expected, actual, 1e-9)
    }

    @Test
    fun ovale_usesSumSquaredOverTwoFormula() {
        // (largeur + longueur)² / 2, pas largeur × longueur.
        val length = 8.0
        val width = 6.0
        val height = 4.5
        val sg = 4.00 // Corindon
        val expectedBase = (length + width) * (length + width) / 2.0
        val expected = expectedBase * height * sg * LapidaireCutShape.OVALE.coefficient
        val actual = LapidaireWeightEstimator.estimateWeightCarats(
            shape = LapidaireCutShape.OVALE,
            dimension1Mm = length,
            dimension2Mm = width,
            heightMm = height,
            specificGravity = sg
        )
        assertEquals(expected, actual, 1e-9)
    }

    @Test
    fun autresFormes_useLengthTimesWidthFormula() {
        val length = 7.0
        val width = 5.0
        val height = 4.0
        val sg = 2.72 // Béryl
        val expected = length * width * height * sg * LapidaireCutShape.COUSSIN_RECTANGULAIRE.coefficient
        val actual = LapidaireWeightEstimator.estimateWeightCarats(
            shape = LapidaireCutShape.COUSSIN_RECTANGULAIRE,
            dimension1Mm = length,
            dimension2Mm = width,
            heightMm = height,
            specificGravity = sg
        )
        assertEquals(expected, actual, 1e-9)
    }

    @Test
    fun formeSansDeuxiemeDimension_utiliseDimension1CommeLargeur() {
        // Coussin carré : une seule dimension fournie, traitée comme carrée (largeur = longueur).
        val side = 6.0
        val height = 4.0
        val sg = 3.52
        val expected = side * side * height * sg * LapidaireCutShape.COUSSIN_CARRE.coefficient
        val actual = LapidaireWeightEstimator.estimateWeightCarats(
            shape = LapidaireCutShape.COUSSIN_CARRE,
            dimension1Mm = side,
            dimension2Mm = null,
            heightMm = height,
            specificGravity = sg
        )
        assertEquals(expected, actual, 1e-9)
    }

    @Test
    fun rejectsNonPositiveDimensions() {
        assertFailsWith<IllegalArgumentException> {
            LapidaireWeightEstimator.estimateWeightCarats(
                shape = LapidaireCutShape.ROND,
                dimension1Mm = 0.0,
                dimension2Mm = null,
                heightMm = 4.0,
                specificGravity = 3.52
            )
        }
        assertFailsWith<IllegalArgumentException> {
            LapidaireWeightEstimator.estimateWeightCarats(
                shape = LapidaireCutShape.ROND,
                dimension1Mm = 6.5,
                dimension2Mm = null,
                heightMm = -1.0,
                specificGravity = 3.52
            )
        }
        assertFailsWith<IllegalArgumentException> {
            LapidaireWeightEstimator.estimateWeightCarats(
                shape = LapidaireCutShape.ROND,
                dimension1Mm = 6.5,
                dimension2Mm = null,
                heightMm = 4.0,
                specificGravity = 0.0
            )
        }
    }

    @Test
    fun rejectsMissingSecondDimensionWhenRequired() {
        assertFailsWith<IllegalArgumentException> {
            LapidaireWeightEstimator.estimateWeightCarats(
                shape = LapidaireCutShape.OVALE,
                dimension1Mm = 8.0,
                dimension2Mm = null,
                heightMm = 4.5,
                specificGravity = 4.00
            )
        }
    }
}
