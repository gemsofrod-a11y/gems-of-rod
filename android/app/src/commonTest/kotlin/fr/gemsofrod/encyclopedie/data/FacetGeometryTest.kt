package fr.gemsofrod.encyclopedie.data

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Atelier de facettage : la pierre calculée à partir des angles et index doit être cohérente. */
class FacetGeometryTest {
    private val brilliant = FacetPresets.brilliantRound

    @Test
    fun brilliantRoundHasAllItsFacetsAndClosedShape() {
        val stone = FacetGeometry.build(brilliant)
        assertTrue(stone.closed)
        assertTrue(stone.hiddenTiers.isEmpty())
        // 57 facettes + 32 facettes de rondiste
        assertEquals(89, stone.faces.size)
    }

    @Test
    fun brilliantRoundProportionsAreClassic() {
        val m = assertNotNull(FacetGeometry.metrics(brilliant, FacetGeometry.build(brilliant)))
        assertTrue(abs(m.tablePercent - 57.0) < 0.5, "table ${m.tablePercent}")
        assertTrue(m.crownPercent in 12.0..17.0, "couronne ${m.crownPercent}")
        assertTrue(m.pavilionPercent in 41.0..45.0, "pavillon ${m.pavilionPercent}")
        assertTrue(abs(m.lengthToWidth - 1.0) < 0.01)
        // un brillant de 6,5 mm pèse environ 1 carat
        assertTrue(m.carats in 0.9..1.1, "poids ${m.carats}")
    }

    @Test
    fun steeperPavilionMakesADeeperStone() {
        val steeper = brilliant.copy(tiers = brilliant.tiers.map { if (it.name == "P1") it.copy(angleDeg = 43.0) else it })
        val a = FacetGeometry.metrics(brilliant, FacetGeometry.build(brilliant))!!
        val b = FacetGeometry.metrics(steeper, FacetGeometry.build(steeper))!!
        assertTrue(b.pavilionPercent > a.pavilionPercent)
    }

    @Test
    fun missingPavilionLeavesTheStoneOpen() {
        val open = brilliant.copy(tiers = brilliant.tiers.filter { it.side != FacetSide.PAVILION })
        val stone = FacetGeometry.build(open)
        assertFalse(stone.closed)
        assertEquals(null, FacetGeometry.metrics(open, stone))
    }

    @Test
    fun symmetryAndMirrorGiveTheExpectedIndexes() {
        assertEquals(listOf(96, 12, 24, 36, 48, 60, 72, 84), FacetWheels.indicesFor(0, 8, 96, false))
        assertEquals(16, FacetWheels.indicesFor(3, 8, 96, true).size)
        assertEquals(listOf(6, 18, 30), FacetWheels.parse("06-18 30", 96))
        assertEquals("96", FacetWheels.display(0, 96))
    }

    @Test
    fun changingWheelKeepsFacetPositions() {
        val on64 = FacetWheels.convert(brilliant, 64)
        val mains = on64.tiers.first { it.name == "P1" }.indices
        assertEquals(listOf(64, 8, 16, 24, 32, 40, 48, 56), mains)
    }

    @Test
    fun designSurvivesSaveAndLoad() {
        assertEquals(brilliant, FacetDesignCodec.decode(FacetDesignCodec.encode(brilliant)))
    }
}
