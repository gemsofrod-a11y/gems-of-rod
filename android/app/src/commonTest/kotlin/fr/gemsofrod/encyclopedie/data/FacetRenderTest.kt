package fr.gemsofrod.encyclopedie.data

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Rendu réaliste de l'atelier : la physique de la lumière doit être juste. */
class FacetRenderTest {
    private val brilliant = FacetGeometry.build(FacetPresets.brilliantRound)

    @Test
    fun refractionFollowsSnellDescartes() {
        var deg = 0.0
        while (deg < 89.0) {
            val i = deg * PI / 180
            val r = assertNotNull(FacetRenderer.refract(sin(i), -cos(i), 0.0, 0.0, 1.0, 0.0, 1 / 2.417))
            assertTrue(abs(sin(i) - 2.417 * hypot(r[0], r[2])) < 1e-12, "Snell à $deg°")
            deg += 1.3
        }
    }

    @Test
    fun totalInternalReflectionStartsAtTheCriticalAngle() {
        val crit = asin(1 / 2.417) * 180 / PI
        fun out(deg: Double) = FacetRenderer.refract(sin(deg * PI / 180), cos(deg * PI / 180), 0.0, 0.0, -1.0, 0.0, 2.417)
        assertNotNull(out(crit - 0.01))
        assertNull(out(crit + 0.01))
        assertTrue(abs(FacetMaterial.DIAMANT.criticalAngleDeg - crit) < 1e-12)
    }

    @Test
    fun diamondFaceReflectionIs17Percent() {
        assertTrue(abs(FacetRenderer.fresnel(1.0, 2.417) - 0.1720) < 1e-3)
    }

    @Test
    fun energyIsConservedUnderUniformLight() {
        // sous une lumière uniforme, une pierre incolore ne crée ni ne perd de lumière
        val n = 24
        val lin = DoubleArray(n * n * 3)
        FacetRenderer.render(
            brilliant, FacetRenderSettings(ri = 2.417, dispersion = 0.0, bounces = 40), n, samples = 1,
            linearOut = lin, envOverride = { _, _, _ -> 1.0 }
        )
        val inside = lin.toList().chunked(3).map { it[1] }.filter { it > 0 }
        assertTrue(inside.isNotEmpty())
        assertTrue(inside.max() <= 1 + 1e-9, "énergie ≤ 100 % (${inside.max()})")
        assertTrue(inside.average() > 0.97, "énergie retrouvée ${inside.average()}")
    }

    @Test
    fun colourOnlyAbsorbs() {
        val n = 20
        val a = DoubleArray(n * n * 3); val c = DoubleArray(n * n * 3)
        val base = FacetRenderSettings(ri = 1.544, dispersion = 0.0)
        FacetRenderer.render(brilliant, base, n, samples = 1, linearOut = a)
        FacetRenderer.render(brilliant, base.copy(hue = 44.0, saturation = 0.6, lightness = 0.6), n, samples = 1, linearOut = c)
        for (i in a.indices) assertTrue(c[i] <= a[i] + 1e-12)
    }

    @Test
    fun pavilionBelowTheCriticalAngleLeaksLight() {
        // quartz (angle critique 40,4°) : pavillon à 43° contre pavillon à 37°
        fun lr(angle: Double): Double {
            val d = FacetPresets.brilliantRound.let { b -> b.copy(tiers = b.tiers.map { if (it.name == "P1") it.copy(angleDeg = angle) else if (it.name == "P2") it.copy(angleDeg = angle + 1) else it }) }
            return FacetRenderer.render(FacetGeometry.build(d), FacetRenderSettings(ri = 1.544, dispersion = 0.0), 40, samples = 1).lightReturn
        }
        val good = lr(43.0); val leaky = lr(37.0)
        assertTrue(good > leaky + 0.2, "pavillon 43° : $good, 37° : $leaky")
    }

    @Test
    fun lightReturnStaysBetweenZeroAndOne() {
        for (m in FacetMaterial.entries) {
            val r = FacetRenderer.render(brilliant, FacetRenderSettings.forMaterial(m), 24, samples = 1).lightReturn
            assertTrue(r in 0.0..1.0, "${m.name} : $r")
        }
    }

    @Test
    fun imageHasTheRequestedSizeAndBackgroundOutsideTheStone() {
        val r = FacetRenderer.render(brilliant, FacetRenderSettings.forMaterial(FacetMaterial.DIAMANT), 32, samples = 1, background = 0xFF000000.toInt())
        assertEquals(32 * 32, r.pixels.size)
        assertEquals(0xFF000000.toInt(), r.pixels[0]) // coin : hors de la pierre
    }

    @Test
    fun colourlessMaterialHasNeutralTransmission() {
        assertTrue(FacetRenderSettings.forMaterial(FacetMaterial.DIAMANT).transmission().all { it == 1.0 })
        val q = FacetRenderSettings.forMaterial(FacetMaterial.QUARTZ).transmission()
        assertTrue(q[0] > q[2], "citrine : plus de rouge que de bleu")
    }
}
