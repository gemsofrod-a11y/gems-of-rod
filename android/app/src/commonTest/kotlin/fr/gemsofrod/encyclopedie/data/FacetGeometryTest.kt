package fr.gemsofrod.encyclopedie.data

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.tan
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Atelier de facettage : vérifications chiffrées sur la pierre finie (pas
 * seulement sur les réglages), les mêmes que celles du prototype validé par
 * Sébastien le 17/11/2026.
 */
class FacetGeometryTest {

    private fun checkSolid(design: FacetDesign) {
        val st = FacetGeometry.build(design)
        assertTrue(st.closed, "${design.name} : plus aucune face brute du cube")
        assertTrue(st.issues.isEmpty(), "${design.name} : aucune passe en défaut (${st.issues})")
        // Euler V − A + F = 2
        val verts = ArrayList<Vec3>()
        val edges = HashSet<Pair<Int, Int>>()
        fun key(p: Vec3): Int { val i = verts.indexOfFirst { (it - p).length() < 1e-6 }; if (i >= 0) return i; verts += p; return verts.lastIndex }
        for (f in st.faces) {
            val ids = f.points.map(::key)
            for (i in ids.indices) { val a = ids[i]; val b = ids[(i + 1) % ids.size]; edges += min(a, b) to max(a, b) }
        }
        assertEquals(2, verts.size - edges.size + st.faces.size, "${design.name} : formule d'Euler")
        // convexité et planéité
        for (f in st.faces) {
            val pl = f.plane!!
            for (p in f.points) assertTrue(abs((pl.n dot p) - pl.d) < 1e-9, "facette plane")
            for (v in verts) assertTrue((pl.n dot v) - pl.d < 1e-9, "sommet du bon côté de chaque facette")
        }
        // angle et index mesurés
        for (f in st.faces) {
            val pl = f.plane!!
            val t = design.tiers[pl.tier]
            val measured = if (t.side == FacetSide.GIRDLE) 90.0 else acos(abs(pl.n.y)) * 180 / PI
            assertTrue(abs(measured - t.angleDeg) < 1e-9, "${t.name} : angle mesuré $measured")
            if (hypot(pl.n.x, pl.n.z) > 1e-9) {
                val az = ((atan2(pl.n.z, pl.n.x) / (2 * PI)) * design.wheel + design.wheel) % design.wheel
                val target = (pl.index % design.wheel).toDouble()
                val err = min(abs(az - target), design.wheel - abs(az - target))
                assertTrue(err < 1e-9, "${t.name} : index mesuré $az au lieu de $target")
            }
        }
        // nombre de facettes et symétrie
        design.tiers.forEachIndexed { ti, t ->
            val faces = st.faces.filter { it.plane?.tier == ti }
            assertEquals(t.indices.size, faces.size, "${design.name} ${t.name} : nombre de facettes")
            if (faces.size > 1) {
                val a = faces.map { FacetGeometry.polygonArea(it.points) }
                assertTrue((a.max() - a.min()) / a.max() < 1e-6, "${t.name} : facettes identiques")
            }
        }
        // volume exact = comptage de points
        val all = st.faces.flatMap { it.points }
        val lo = Vec3(all.minOf { it.x }, all.minOf { it.y }, all.minOf { it.z })
        val hi = Vec3(all.maxOf { it.x }, all.maxOf { it.y }, all.maxOf { it.z })
        var seed = 12345L
        fun rnd(): Double { seed = (seed * 16807) % 2147483647; return seed / 2147483647.0 }
        val n = 120_000
        var inside = 0
        repeat(n) {
            val p = Vec3(lo.x + rnd() * (hi.x - lo.x), lo.y + rnd() * (hi.y - lo.y), lo.z + rnd() * (hi.z - lo.z))
            if (st.faces.all { (it.plane!!.n dot p) <= it.plane.d }) inside++
        }
        val mc = inside.toDouble() / n * (hi.x - lo.x) * (hi.y - lo.y) * (hi.z - lo.z)
        val exact = FacetGeometry.volume(st)
        assertTrue(abs(mc - exact) / exact < 0.01, "${design.name} : volume $exact contre comptage $mc")
    }

    @Test
    fun brilliantRoundIsAValidSolid() = checkSolid(FacetPresets.brilliantRound)

    @Test
    fun cushionIsAValidSolid() = checkSolid(FacetPresets.cushionGor)

    @Test
    fun brilliantRoundProportionsAndWeight() {
        val d = FacetPresets.brilliantRound
        val m = assertNotNull(FacetGeometry.metrics(d, FacetGeometry.build(d)))
        assertTrue(m.complete)
        assertEquals(57, m.facetCount - 32, "57 facettes hors rondiste")
        assertTrue(abs(m.lengthToWidth - 1.0) < 1e-9)
        // pavillon coupé au centre : profondeur = tan(41°) × rayon
        assertTrue(abs(m.pavilionPercent - tan(41 * PI / 180) * 50) < 1e-6, "pavillon ${m.pavilionPercent}")
        assertTrue(m.tablePercent in 55.0..65.0, "table ${m.tablePercent}")
        assertTrue(m.crownPercent in 12.0..17.0, "couronne ${m.crownPercent}")
        // poids proche de la formule des laboratoires pour un rond : D² × profondeur × 0,0061
        val lab = m.widthMm * m.widthMm * m.heightMm * 0.0061
        assertTrue(abs(m.carats - lab) / lab < 0.04, "poids ${m.carats} ct contre $lab ct")
    }

    @Test
    fun girdleThicknessIsTheOneAsked() {
        for (d in FacetPresets.all) {
            val st = FacetGeometry.build(d)
            val m = FacetGeometry.metrics(d, st)!!
            val width = m.widthMm / FacetGeometry.mmPerUnit(d)
            val thickness = (st.girdleTop!! - st.girdleBottom!!) / width * 100
            assertTrue(abs(thickness - d.girdlePercent) < 1e-9, "${d.name} : rondiste $thickness % au lieu de ${d.girdlePercent} %")
        }
    }

    @Test
    fun culetIsOnTheAxis() {
        val st = FacetGeometry.build(FacetPresets.brilliantRound)
        val all = st.faces.flatMap { it.points }
        val bottom = all.minOf { it.y }
        all.filter { abs(it.y - bottom) < 1e-9 }.forEach { assertTrue(hypot(it.x, it.z) < 1e-9) }
    }

    @Test
    fun blankCubeAndFirstCut() {
        val blank = FacetPresets.blank("Nouvelle taille")
        val st = FacetGeometry.build(blank)
        assertEquals(6, st.faces.size)
        assertFalse(st.closed)
        val m = assertNotNull(FacetGeometry.metrics(blank.copy(cubeMm = 10.0, material = FacetMaterial.QUARTZ), st))
        assertTrue(abs(m.carats - 13.25) < 1e-9, "cube de 10 mm en quartz = 13,25 ct (${m.carats})")
        val first = blank.copy(tiers = listOf(FacetPresets.brilliantRound.tiers.first()))
        assertEquals(8, FacetGeometry.build(first).faces.count { it.plane != null })
    }

    @Test
    fun meetPointLiesOnTheNewFacet() {
        val d = FacetPresets.brilliantRound
        val st = FacetGeometry.build(d)
        val c3 = d.tiers.indexOfFirst { it.name == "C3" }
        // chaque étoile touche une main de couronne et une demi-facette supérieure
        for (f in st.faces.filter { it.plane?.tier == c3 }) {
            val touches = st.faces.filter { g -> g !== f && g.points.any { p -> f.points.any { (it - p).length() < 1e-7 } } }
                .mapNotNull { it.plane?.tier }.map { d.tiers[it].name }.toSet()
            assertTrue("C1" in touches && "C2" in touches, "étoile au contact de C1 et C2 : $touches")
        }
    }

    @Test
    fun detectsFacetsCutAwayByOthers() {
        // une passe recoupée par une passe plus profonde au même angle, sur la moitié de ses index
        val base = FacetPresets.blank("Test")
        val p1 = FacetTier("P1", FacetSide.PAVILION, 41.0, FacetWheels.indicesFor(0, 8, 96), FacetRule.CENTER)
        val deeper = FacetTier("P2", FacetSide.PAVILION, 41.0, FacetWheels.indicesFor(0, 4, 96), FacetRule.VALUE, value = -0.1)
        val some = FacetGeometry.build(base.copy(tiers = listOf(p1, deeper)))
        assertEquals(TierIssue.SOME_HIDDEN, some.issues[0])
        assertEquals(4, some.visibleCount[0])
        val all = FacetGeometry.build(base.copy(tiers = listOf(p1, deeper.copy(indices = p1.indices))))
        assertEquals(TierIssue.ALL_HIDDEN, all.issues[0])
        // une passe trop peu profonde ne retire rien
        val shallow = FacetGeometry.build(base.copy(tiers = listOf(p1.copy(rule = FacetRule.VALUE, value = 5.0))))
        assertEquals(TierIssue.NO_CUT, shallow.issues[0])
        // rencontre avec une passe qui n'existe pas
        val noMeet = FacetGeometry.build(base.copy(tiers = listOf(p1, p1.copy(name = "P2", rule = FacetRule.MEET, meet = listOf("C9")))))
        assertEquals(TierIssue.NO_MEET_POINT, noMeet.issues[1])
    }

    @Test
    fun hiddenTierIsNotCut() {
        val d = FacetPresets.brilliantRound
        val hidden = d.copy(tiers = d.tiers.map { if (it.name == "T") it.copy(hidden = true) else it })
        val st = FacetGeometry.build(hidden)
        assertTrue(st.faces.none { it.plane?.tier == d.tiers.lastIndex })
    }

    @Test
    fun symmetryAndMirrorGiveTheExpectedIndexes() {
        assertEquals(listOf(96, 12, 24, 36, 48, 60, 72, 84), FacetWheels.indicesFor(0, 8, 96))
        assertEquals(listOf(3, 21, 27, 45, 51, 69, 75, 93), FacetWheels.indicesFor(0, 4, 96, mirror = 3).sorted())
        assertEquals(listOf(6, 18, 30), FacetWheels.parse("06-18 30", 96))
        assertEquals("96", FacetWheels.display(0, 96))
    }

    @Test
    fun changingWheelKeepsFacetPositions() {
        val on64 = FacetWheels.convert(FacetPresets.brilliantRound, 64)
        assertEquals(listOf(64, 8, 16, 24, 32, 40, 48, 56), on64.tiers.first().indices)
    }

    @Test
    fun designsSurviveSaveAndLoad() {
        val list = FacetPresets.all + FacetPresets.blank("Vierge")
        assertEquals(list, FacetDesignCodec.decodeAll(FacetDesignCodec.encodeAll(list)))
    }
}
