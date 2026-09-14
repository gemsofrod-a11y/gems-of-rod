package fr.gemsofrod.encyclopedie.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RingCaratEstimatorTest {

    @Test
    fun matchesBoundingBoxVolumeFormula() {
        val width = 8.0
        val height = 6.0
        val sg = 3.52 // Diamant
        val expectedDepth = ((width + height) / 2.0) * 0.62
        val expected = width * height * expectedDepth * 0.48 * sg / 200.0
        val actual = RingCaratEstimator.estimateCarats(sg, width, height)
        assertEquals(expected, actual, 1e-9)
    }

    @Test
    fun singleDimensionShape_widthEqualsHeight() {
        // Une forme symétrique (ronde, princesse, ...) passe le même diamètre
        // pour largeur et hauteur — la profondeur doit alors s'aligner dessus.
        val diameter = 6.5
        val sg = 3.52
        val expectedDepth = diameter * 0.62
        val expected = diameter * diameter * expectedDepth * 0.48 * sg / 200.0
        val actual = RingCaratEstimator.estimateCarats(sg, diameter, diameter)
        assertEquals(expected, actual, 1e-9)
    }

    @Test
    fun rejectsNonPositiveInputs() {
        assertFailsWith<IllegalArgumentException> {
            RingCaratEstimator.estimateCarats(3.52, 0.0, 6.0)
        }
        assertFailsWith<IllegalArgumentException> {
            RingCaratEstimator.estimateCarats(3.52, 8.0, -1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            RingCaratEstimator.estimateCarats(0.0, 8.0, 6.0)
        }
    }
}
