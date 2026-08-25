package fr.gemsofrod.encyclopedie.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ReflectivityMeterTest {

    @Test
    fun reflectance_knownValues() {
        assertEquals(0.0, Fresnel.reflectance(1.0), 1e-12)
        assertEquals(0.04, Fresnel.reflectance(1.5), 1e-12)
        assertEquals(1.0 / 9.0, Fresnel.reflectance(2.0), 1e-12)
    }

    @Test
    fun reflectance_rejectsIndexBelowOne() {
        assertFailsWith<IllegalArgumentException> { Fresnel.reflectance(0.9) }
    }

    @Test
    fun refractiveIndexFromReflectance_isInverseOfReflectance() {
        val sampleIndices = listOf(1.0, 1.3, 1.5, 1.544, 1.7, 1.762, 2.0, 2.417, 2.9)
        for (n in sampleIndices) {
            val recovered = Fresnel.refractiveIndexFromReflectance(Fresnel.reflectance(n))
            assertEquals(n, recovered, 1e-9, "round-trip a échoué pour n=$n")
        }
    }

    @Test
    fun refractiveIndexFromReflectance_rejectsOutOfRangeReflectance() {
        assertFailsWith<IllegalArgumentException> { Fresnel.refractiveIndexFromReflectance(-0.01) }
        assertFailsWith<IllegalArgumentException> { Fresnel.refractiveIndexFromReflectance(1.0) }
        assertFailsWith<IllegalArgumentException> { Fresnel.refractiveIndexFromReflectance(1.5) }
    }

    /** Modèle synthétique brillance = GAIN * réflectance + OFFSET, pour
     * vérifier que la calibration à deux points retrouve exactement ce
     * modèle (et donc l'indice de réfraction d'une troisième pierre inconnue
     * mesurée dans les mêmes conditions). */
    private object SyntheticDevice {
        const val GAIN = 500.0
        const val OFFSET = 50.0

        fun brightnessFor(knownRefractiveIndex: Double): Double =
            GAIN * Fresnel.reflectance(knownRefractiveIndex) + OFFSET
    }

    @Test
    fun calibration_recoversKnownIndexAtCalibrationPoints() {
        val quartz = 1.544
        val corindon = 1.762

        val calibration = ReflectivityCalibration(
            pointA = CalibrationPoint(quartz, SyntheticDevice.brightnessFor(quartz)),
            pointB = CalibrationPoint(corindon, SyntheticDevice.brightnessFor(corindon))
        )

        assertEquals(
            quartz,
            calibration.estimate(SyntheticDevice.brightnessFor(quartz)).refractiveIndexCenter,
            1e-6
        )
        assertEquals(
            corindon,
            calibration.estimate(SyntheticDevice.brightnessFor(corindon)).refractiveIndexCenter,
            1e-6
        )
    }

    @Test
    fun calibration_recoversUnknownThirdPointUnderSameLinearModel() {
        val quartz = 1.544
        val corindon = 1.762
        val diamant = 2.417

        val calibration = ReflectivityCalibration(
            pointA = CalibrationPoint(quartz, SyntheticDevice.brightnessFor(quartz)),
            pointB = CalibrationPoint(corindon, SyntheticDevice.brightnessFor(corindon))
        )

        val estimate = calibration.estimate(SyntheticDevice.brightnessFor(diamant))
        assertEquals(diamant, estimate.refractiveIndexCenter, 1e-6)
        assertTrue(estimate.refractiveIndexLow <= estimate.refractiveIndexCenter)
        assertTrue(estimate.refractiveIndexCenter <= estimate.refractiveIndexHigh)
    }

    @Test
    fun calibration_rejectsIdenticalBrightnessPoints() {
        assertFailsWith<IllegalArgumentException> {
            ReflectivityCalibration(
                pointA = CalibrationPoint(1.5, 100.0),
                pointB = CalibrationPoint(1.8, 100.0)
            )
        }
    }

    @Test
    fun calibration_rejectsNonPositiveUncertainty() {
        assertFailsWith<IllegalArgumentException> {
            ReflectivityCalibration(
                pointA = CalibrationPoint(1.5, 100.0),
                pointB = CalibrationPoint(1.8, 200.0),
                measurementUncertainty = 0.0
            )
        }
    }

    @Test
    fun estimate_staysWithinBoundsForExtremeBrightness() {
        val calibration = ReflectivityCalibration(
            pointA = CalibrationPoint(1.5, 100.0),
            pointB = CalibrationPoint(1.8, 200.0)
        )

        // Brillance très en dehors de la plage calibrée : ne doit jamais
        // planter (coercition de la réflectance dans [0, 1[), même si le
        // résultat n'est alors plus fiable physiquement.
        val estimateLow = calibration.estimate(-1000.0)
        val estimateHigh = calibration.estimate(1_000_000.0)

        assertTrue(estimateLow.refractiveIndexLow <= estimateLow.refractiveIndexHigh)
        assertTrue(estimateHigh.refractiveIndexLow <= estimateHigh.refractiveIndexHigh)
    }
}
