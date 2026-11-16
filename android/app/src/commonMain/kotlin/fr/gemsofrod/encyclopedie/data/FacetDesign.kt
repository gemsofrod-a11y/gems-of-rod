package fr.gemsofrod.encyclopedie.data

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Atelier de facettage (demandes de Sébastien des 16 et 17/11/2026). Une taille
 * se décrit comme sur une machine à facetter : une suite de passes, chacune avec
 * un angle, des index sur la roue et une règle qui dit jusqu'où couper (au
 * centre, largeur, niveau du rondiste, point de rencontre d'autres passes,
 * largeur de table, ou profondeur réglée au curseur).
 *
 * On part d'un cube brut et chaque passe le découpe, dans l'ordre : la pierre
 * est ce qui reste. Ce fichier en tire la géométrie (vue 3D), les proportions,
 * les dimensions et le poids. Code commun pur, testé dans commonTest
 * (FacetGeometryTest) — mêmes vérifications que le prototype validé.
 *
 * Repère : y vers le haut. Cube de demi-côté [CUBE_HALF] posé sur y = 0, centré
 * sur l'axe. Une passe « au centre » du pavillon passe par le milieu de la face
 * du bas (la colette). L'index 0 (affiché comme la taille de la roue, ex. 96)
 * pointe vers +x.
 */

const val CUBE_HALF = 1.2

enum class FacetSide { PAVILION, GIRDLE, CROWN }

/** Rôle d'une passe des tailles de départ : sert seulement à son libellé (traduit dans l'app). */
enum class FacetRole { NONE, PAVILION_MAIN, PAVILION_HALF, GIRDLE, CROWN_MAIN, STAR, UPPER_HALF, TABLE }

/**
 * Jusqu'où couper une passe :
 * - [CENTER] : jusqu'à l'axe (colette au milieu de la face du bas, ou pointe en haut) ;
 * - [WIDTH] : rondiste à la distance [FacetTier.value] de l'axe (1 = largeur de référence) ;
 * - [LEVEL] : niveler le rondiste (la facette arrive à la hauteur du rondiste déjà taillé) ;
 * - [GIRDLE_TOP] : couronne posée au-dessus du rondiste, à l'épaisseur de rondiste voulue ;
 * - [MEET] : jusqu'au point où se rencontrent les passes [FacetTier.meet] ;
 * - [TABLE] : table horizontale dont la largeur vaut [FacetTier.value] (fraction de la largeur) ;
 * - [VALUE] : profondeur réglée au curseur ([FacetTier.value] = distance du plan au repère).
 */
enum class FacetRule { CENTER, WIDTH, LEVEL, GIRDLE_TOP, MEET, TABLE, VALUE }

/** Matières proposées : indice de réfraction, densité (poids) et dispersion (feu, pour le rendu). */
enum class FacetMaterial(val ri: Double, val sg: Double, val dispersion: Double) {
    DIAMANT(2.417, 3.52, 0.044),
    MOISSANITE(2.65, 3.22, 0.104),
    ZIRCONE_CUBIQUE(2.15, 5.80, 0.060),
    ZIRCON(1.95, 4.65, 0.039),
    GRENAT(1.79, 4.05, 0.024),
    CORINDON(1.762, 4.00, 0.018),
    CHRYSOBERYL(1.75, 3.72, 0.015),
    SPINELLE(1.718, 3.60, 0.020),
    TANZANITE(1.70, 3.35, 0.021),
    PERIDOT(1.67, 3.34, 0.020),
    TOURMALINE(1.624, 3.06, 0.017),
    TOPAZE(1.619, 3.53, 0.014),
    BERYL(1.577, 2.72, 0.014),
    QUARTZ(1.544, 2.65, 0.013);

    /** Angle critique (degrés) : sous cet angle d'incidence, la lumière sort de la pierre. */
    val criticalAngleDeg: Double get() = asin(1.0 / ri) * 180.0 / PI
}

data class FacetTier(
    val name: String,
    val side: FacetSide,
    val angleDeg: Double,
    val indices: List<Int>,
    val rule: FacetRule,
    /** Paramètre de la règle : largeur (WIDTH), fraction de table (TABLE) ou profondeur (VALUE). */
    val value: Double = 1.0,
    /** Passes à rencontrer (règle MEET), par leur nom. */
    val meet: List<String> = emptyList(),
    val role: FacetRole = FacetRole.NONE,
    /** Passe masquée : gardée dans la liste mais pas coupée (pour voir son effet). */
    val hidden: Boolean = false
)

data class FacetDesign(
    val name: String,
    val wheel: Int,
    val tiers: List<FacetTier>,
    /** Épaisseur du rondiste (règle GIRDLE_TOP), en % de la largeur de référence. */
    val girdlePercent: Double = 1.5,
    val material: FacetMaterial = FacetMaterial.DIAMANT,
    /** Côté du cube brut de départ, en mm (donne l'échelle réelle : poids, dimensions). */
    val cubeMm: Double = 8.0
)

object FacetWheels {
    val sizes = listOf(64, 72, 80, 96, 120)

    /** Affichage d'un index : 0 s'écrit comme la taille de la roue (convention des machines). */
    fun display(index: Int, wheel: Int): String {
        val v = normalize(index, wheel)
        return if (v < 10) "0$v" else v.toString()
    }

    fun normalize(index: Int, wheel: Int): Int {
        val i = ((index % wheel) + wheel) % wheel
        return if (i == 0) wheel else i
    }

    /**
     * Index d'une passe : [symmetry] facettes réparties sur le tour à partir
     * de [start] ; avec un [mirror] non nul, chaque facette a sa jumelle de
     * l'autre côté de l'axe (start ± mirror), comme sur une machine.
     */
    fun indicesFor(start: Int, symmetry: Int, wheel: Int, mirror: Int = 0): List<Int> {
        val n = symmetry.coerceIn(1, wheel)
        val step = wheel.toDouble() / n
        val out = LinkedHashSet<Int>()
        for (k in 0 until n) {
            val base = start + (k * step).roundToInt()
            if (mirror == 0) out += normalize(base, wheel)
            else { out += normalize(base - mirror, wheel); out += normalize(base + mirror, wheel) }
        }
        return out.sortedBy { it % wheel }
    }

    /** Symétries qui tombent juste sur la roue donnée. */
    fun symmetriesFor(wheel: Int): List<Int> =
        listOf(1, 2, 3, 4, 5, 6, 8, 10, 12, 16, 24, 32).filter { wheel % it == 0 }

    /** Lit une liste d'index tapée à la main (« 06 18 30 », « 6-18-30 », « 6,18,30 »…). */
    fun parse(text: String, wheel: Int): List<Int> =
        text.split(' ', ',', ';', '-', '·', '/', '\n', '\t')
            .mapNotNull { it.trim().toIntOrNull() }
            .filter { it in 0..wheel }
            .map { normalize(it, wheel) }
            .distinct()

    /** Change de roue en gardant les facettes à la même position angulaire (arrondie). */
    fun convert(design: FacetDesign, newWheel: Int): FacetDesign {
        if (newWheel == design.wheel) return design
        val tiers = design.tiers.map { t ->
            t.copy(indices = t.indices.map { normalize((it * newWheel.toDouble() / design.wheel).roundToInt(), newWheel) }.distinct())
        }
        return design.copy(wheel = newWheel, tiers = tiers)
    }
}

/** Tailles de départ, entièrement décrites par des règles (comme une vraie fiche de taille). */
object FacetPresets {
    private fun every(start: Int, step: Int, wheel: Int = 96) =
        (0 until wheel / step).map { FacetWheels.normalize(start + it * step, wheel) }

    val brilliantRound = FacetDesign(
        name = "Brillant rond",
        wheel = 96,
        girdlePercent = 1.5,
        material = FacetMaterial.DIAMANT,
        cubeMm = 8.0,
        tiers = listOf(
            FacetTier("P1", FacetSide.PAVILION, 41.0, every(0, 12), FacetRule.CENTER, role = FacetRole.PAVILION_MAIN),
            FacetTier("G1", FacetSide.GIRDLE, 90.0, every(0, 12), FacetRule.WIDTH, 1.0, role = FacetRole.GIRDLE),
            FacetTier("G2", FacetSide.GIRDLE, 90.0, every(6, 12), FacetRule.WIDTH, 1.0, role = FacetRole.GIRDLE),
            FacetTier("G3", FacetSide.GIRDLE, 90.0, every(3, 6), FacetRule.WIDTH, 1.0, role = FacetRole.GIRDLE),
            FacetTier("P2", FacetSide.PAVILION, 42.0, every(3, 6), FacetRule.LEVEL, role = FacetRole.PAVILION_HALF),
            FacetTier("C1", FacetSide.CROWN, 34.5, every(0, 12), FacetRule.GIRDLE_TOP, role = FacetRole.CROWN_MAIN),
            FacetTier("C2", FacetSide.CROWN, 40.0, every(3, 6), FacetRule.LEVEL, role = FacetRole.UPPER_HALF),
            FacetTier("C3", FacetSide.CROWN, 20.0, every(6, 12), FacetRule.MEET, meet = listOf("C1", "C2"), role = FacetRole.STAR),
            FacetTier("T", FacetSide.CROWN, 0.0, listOf(96), FacetRule.MEET, meet = listOf("C1", "C3"), role = FacetRole.TABLE)
        )
    )

    /** Taille coussin carré créée pour Gems of Rod (validée sur simulations le 17/11/2026). */
    val cushionGor = FacetDesign(
        name = "Coussin Gems of Rod",
        wheel = 96,
        girdlePercent = 2.0,
        material = FacetMaterial.QUARTZ,
        cubeMm = 8.0,
        tiers = listOf(
            FacetTier("P1", FacetSide.PAVILION, 43.0, every(0, 24), FacetRule.CENTER, role = FacetRole.PAVILION_MAIN),
            FacetTier("G1", FacetSide.GIRDLE, 90.0, every(0, 24), FacetRule.WIDTH, 1.0, role = FacetRole.GIRDLE),
            FacetTier("G2", FacetSide.GIRDLE, 90.0, every(12, 24), FacetRule.WIDTH, 1.12, role = FacetRole.GIRDLE),
            FacetTier("G3", FacetSide.GIRDLE, 90.0, every(6, 12), FacetRule.WIDTH, 1.07, role = FacetRole.GIRDLE),
            // coins nivelés sur le rondiste (et non coupés au centre) : rondiste régulier tout autour
            FacetTier("P2", FacetSide.PAVILION, 44.5, every(12, 24), FacetRule.LEVEL, role = FacetRole.PAVILION_MAIN),
            FacetTier("P3", FacetSide.PAVILION, 46.0, listOf(3, 21, 27, 45, 51, 69, 75, 93), FacetRule.LEVEL, role = FacetRole.PAVILION_HALF),
            FacetTier("C1", FacetSide.CROWN, 34.0, every(12, 24), FacetRule.GIRDLE_TOP, role = FacetRole.CROWN_MAIN),
            FacetTier("C2", FacetSide.CROWN, 32.0, every(0, 24), FacetRule.LEVEL, role = FacetRole.CROWN_MAIN),
            FacetTier("C3", FacetSide.CROWN, 38.0, every(6, 12), FacetRule.LEVEL, role = FacetRole.UPPER_HALF),
            FacetTier("C4", FacetSide.CROWN, 24.0, every(6, 12), FacetRule.MEET, meet = listOf("C1", "C2", "C3"), role = FacetRole.STAR),
            FacetTier("T", FacetSide.CROWN, 0.0, listOf(96), FacetRule.TABLE, 0.58, role = FacetRole.TABLE)
        )
    )

    /** Pierre vierge : le cube brut, aucune passe. */
    fun blank(name: String, wheel: Int = 96, material: FacetMaterial = FacetMaterial.DIAMANT) =
        FacetDesign(name = name, wheel = wheel, tiers = emptyList(), material = material)

    val all = listOf(brilliantRound, cushionGor)

    /** Nom de la prochaine passe d'un côté : P3, C4… */
    fun nextName(design: FacetDesign, side: FacetSide): String {
        val prefix = when (side) { FacetSide.PAVILION -> "P"; FacetSide.GIRDLE -> "G"; FacetSide.CROWN -> "C" }
        val used = design.tiers.map { it.name }.toSet()
        var i = 1
        while ("$prefix$i" in used) i++
        return "$prefix$i"
    }
}

// ---------- Géométrie ----------

data class Vec3(val x: Double, val y: Double, val z: Double) {
    operator fun plus(o: Vec3) = Vec3(x + o.x, y + o.y, z + o.z)
    operator fun minus(o: Vec3) = Vec3(x - o.x, y - o.y, z - o.z)
    operator fun times(k: Double) = Vec3(x * k, y * k, z * k)
    infix fun dot(o: Vec3) = x * o.x + y * o.y + z * o.z
    infix fun cross(o: Vec3) = Vec3(y * o.z - z * o.y, z * o.x - x * o.z, x * o.y - y * o.x)
    fun length() = sqrt(this dot this)
    fun normalized(): Vec3 { val l = length(); return if (l < 1e-12) this else this * (1.0 / l) }
}

/** Plan d'une facette : n·p ≤ d à l'intérieur de la pierre. [tier] = rang de la passe dans la taille. */
data class FacetPlane(val n: Vec3, val d: Double, val tier: Int, val index: Int)

/** Face de la pierre : polygone convexe, sommets dans l'ordre (sens direct vu de l'extérieur). plane == null : face brute du cube. */
class StoneFace(val plane: FacetPlane?, val points: List<Vec3>)

/** Ce qui cloche dans une passe, pour l'afficher à côté d'elle. */
enum class TierIssue {
    /** Aucune matière retirée : la passe ne touche pas la pierre (trop peu profonde). */
    NO_CUT,
    /** Certaines facettes demandées sont entièrement recoupées par les autres passes. */
    SOME_HIDDEN,
    /** Toutes les facettes de la passe ont disparu sous les passes suivantes. */
    ALL_HIDDEN,
    /** Rencontre impossible : aucun sommet commun aux passes citées. */
    NO_MEET_POINT,
    /** La règle a besoin d'un rondiste ou d'un pavillon déjà taillé. */
    NEEDS_GIRDLE
}

class FacetStone(
    val faces: List<StoneFace>,
    /** Distance calculée du plan de chaque passe (première facette), pour l'afficher au curseur. */
    val depths: Map<Int, Double>,
    val issues: Map<Int, TierIssue>,
    /** Nombre de facettes réellement visibles par passe. */
    val visibleCount: Map<Int, Int>,
    /** Hauteurs de référence du rondiste (bas et haut), celles qu'utilisent les règles « niveler » et « fixer le rondiste ». */
    val girdleBottom: Double? = null,
    val girdleTop: Double? = null
) {
    /** Plus aucune face brute du cube : la pierre est entièrement taillée. */
    val closed: Boolean get() = faces.none { it.plane == null }
}

data class FacetMetrics(
    val widthMm: Double,
    val lengthMm: Double,
    val heightMm: Double,
    /** Longueur / largeur (1,00 pour un rond). */
    val lengthToWidth: Double,
    val tablePercent: Double,
    val crownPercent: Double,
    val pavilionPercent: Double,
    val totalPercent: Double,
    val carats: Double,
    val facetCount: Int,
    /** Facettes du rondiste (comptées à part, comme pour le « brillant 57 facettes »). */
    val girdleFacetCount: Int,
    /** Proportions mesurables (pierre fermée, rondiste présent). */
    val complete: Boolean
)

object FacetGeometry {
    private const val EPS = 1e-9

    private fun rad(deg: Double) = deg * PI / 180.0

    fun normal(side: FacetSide, angleDeg: Double, index: Int, wheel: Int): Vec3 {
        val a = rad(angleDeg.coerceIn(0.0, 90.0))
        val phi = (index % wheel).toDouble() / wheel * 2 * PI
        val cx = cos(phi); val cz = sin(phi)
        return when (side) {
            FacetSide.GIRDLE -> Vec3(cx, 0.0, cz)
            FacetSide.PAVILION -> Vec3(sin(a) * cx, -cos(a), sin(a) * cz)
            FacetSide.CROWN -> Vec3(sin(a) * cx, cos(a), sin(a) * cz)
        }
    }

    fun cube(half: Double = CUBE_HALF): List<StoneFace> {
        val b = half; val y0 = 0.0; val y1 = 2 * half
        fun p(x: Double, y: Double, z: Double) = Vec3(x, y, z)
        return listOf(
            StoneFace(null, listOf(p(b, y0, -b), p(b, y1, -b), p(b, y1, b), p(b, y0, b))),
            StoneFace(null, listOf(p(-b, y0, -b), p(-b, y0, b), p(-b, y1, b), p(-b, y1, -b))),
            StoneFace(null, listOf(p(-b, y1, -b), p(-b, y1, b), p(b, y1, b), p(b, y1, -b))),
            StoneFace(null, listOf(p(-b, y0, -b), p(b, y0, -b), p(b, y0, b), p(-b, y0, b))),
            StoneFace(null, listOf(p(-b, y0, b), p(b, y0, b), p(b, y1, b), p(-b, y1, b))),
            StoneFace(null, listOf(p(-b, y0, -b), p(-b, y1, -b), p(b, y1, -b), p(b, y0, -b)))
        )
    }

    /** Découpe le cube passe après passe. [upTo] : ne couper que les passes d'indice < upTo (aperçu étape par étape). */
    fun build(design: FacetDesign, upTo: Int = design.tiers.size): FacetStone {
        var faces = cube()
        var yBottom: Double? = null
        var yTop: Double? = null
        val depths = HashMap<Int, Double>()
        val issues = HashMap<Int, TierIssue>()
        val wheel = design.wheel
        for (ti in 0 until min(upTo, design.tiers.size)) {
            val t = design.tiers[ti]
            if (t.hidden || t.indices.isEmpty()) continue
            val planes = ArrayList<FacetPlane>()
            for (idx in t.indices) {
                val n = normal(t.side, t.angleDeg, idx, wheel)
                val phi = (idx % wheel).toDouble() / wheel * 2 * PI
                val dir = Vec3(cos(phi), 0.0, sin(phi))
                val d: Double? = when (t.rule) {
                    FacetRule.CENTER -> if (t.side == FacetSide.CROWN) n dot Vec3(0.0, 2 * CUBE_HALF, 0.0) else 0.0
                    FacetRule.WIDTH -> if (t.side == FacetSide.GIRDLE) t.value else null
                    FacetRule.VALUE -> t.value
                    FacetRule.LEVEL -> when (t.side) {
                        FacetSide.GIRDLE -> {
                            val ref = yBottom
                            if (ref == null) null else {
                                // le bas de la nouvelle facette de rondiste au niveau du rondiste déjà taillé
                                var lo = 0.02; var hi = 3.0
                                repeat(60) {
                                    val m = (lo + hi) / 2
                                    if (yBottomAt(faces, n * m) > ref) hi = m else lo = m
                                }
                                (lo + hi) / 2
                            }
                        }
                        FacetSide.PAVILION -> yBottom?.let { y -> girdleRadius(faces, dir)?.let { r -> n dot Vec3(dir.x * r, y, dir.z * r) } }
                        FacetSide.CROWN -> (yTop ?: yBottom?.let { it + girdleThickness(design) })?.let { y ->
                            girdleRadius(faces, dir)?.let { r -> n dot Vec3(dir.x * r, y, dir.z * r) }
                        }
                    }
                    FacetRule.GIRDLE_TOP -> {
                        val r = girdleRadius(faces, dir)
                        if (r == null) null else {
                            val q = Vec3(dir.x * r, 0.0, dir.z * r)
                            // épaisseur prise depuis la référence du rondiste (la même que pour « niveler »),
                            // pour un rondiste régulier tout autour de la pierre
                            val yb = yBottom ?: yBottomAt(faces, q).takeIf { it.isFinite() } ?: 0.0
                            val top = yb + girdleThickness(design)
                            if (yTop == null) yTop = top
                            n dot Vec3(q.x, yTop!!, q.z)
                        }
                    }
                    FacetRule.TABLE -> tableDepth(faces, n, t.value, yTop ?: yBottom ?: 0.0)
                    FacetRule.MEET -> meetDepth(faces, n, t.meet, design)
                }
                if (d == null) {
                    issues[ti] = if (t.rule == FacetRule.MEET) TierIssue.NO_MEET_POINT else TierIssue.NEEDS_GIRDLE
                    continue
                }
                planes += FacetPlane(n, d, ti, idx)
            }
            if (planes.isEmpty()) continue
            depths[ti] = planes.first().d
            var cutAny = false
            for (pl in planes) {
                val r = clip(faces, pl)
                if (r != null) { faces = r; cutAny = true }
            }
            if (!cutAny && ti !in issues) issues[ti] = TierIssue.NO_CUT
            if (t.side == FacetSide.GIRDLE && yBottom == null) {
                val pl = planes.first()
                val y = yBottomAt(faces, pl.n * pl.d)
                yBottom = if (y.isFinite()) y else 0.0
            }
        }
        faces = faces.filter { it.points.size >= 3 && polygonArea(it.points) > 1e-12 }
        val visible = HashMap<Int, Int>()
        faces.forEach { f -> f.plane?.let { visible[it.tier] = (visible[it.tier] ?: 0) + 1 } }
        for (ti in 0 until min(upTo, design.tiers.size)) {
            val t = design.tiers[ti]
            if (t.hidden || ti in issues || ti !in depths) continue
            val v = visible[ti] ?: 0
            if (v == 0) issues[ti] = TierIssue.ALL_HIDDEN
            else if (v < t.indices.size) issues[ti] = TierIssue.SOME_HIDDEN
        }
        return FacetStone(faces, depths, issues, visible, yBottom, yTop)
    }

    private fun girdleThickness(design: FacetDesign) = design.girdlePercent / 100.0 * 2.0

    /** Coupe [faces] par le plan ; null si le plan ne retire rien. */
    private fun clip(faces: List<StoneFace>, pl: FacetPlane): List<StoneFace>? {
        val next = ArrayList<StoneFace>(faces.size + 1)
        val cap = ArrayList<Vec3>()
        var cut = false
        for (f in faces) {
            val pts = f.points
            val out = ArrayList<Vec3>(pts.size + 2)
            for (i in pts.indices) {
                val a = pts[i]; val b = pts[(i + 1) % pts.size]
                val da = (pl.n dot a) - pl.d; val db = (pl.n dot b) - pl.d
                val aIn = da <= EPS; val bIn = db <= EPS
                if (aIn) out += a
                if (aIn != bIn) {
                    val p = a + (b - a) * (da / (da - db))
                    out += p; cap += p
                }
                if (!aIn) cut = true
                if (abs(da) <= EPS) cap += a
            }
            val clean = dedupe(out)
            if (clean.size >= 3) next += StoneFace(f.plane, clean)
        }
        if (!cut) return null
        val capPts = dedupe(cap)
        if (capPts.size >= 3) next += StoneFace(pl, sortAround(capPts, pl.n))
        return next
    }

    /** Hauteur du bas de la pierre (pavillon) sur la verticale passant par q. */
    private fun yBottomAt(faces: List<StoneFace>, q: Vec3): Double {
        var y = Double.NEGATIVE_INFINITY
        for (f in faces) {
            val pl = f.plane ?: continue
            if (pl.n.y > -1e-6) continue
            y = max(y, (pl.d - pl.n.x * q.x - pl.n.z * q.z) / pl.n.y)
        }
        return y
    }

    /** Distance au bord du rondiste dans la direction horizontale dir ; null sans rondiste. */
    private fun girdleRadius(faces: List<StoneFace>, dir: Vec3): Double? {
        var r = Double.POSITIVE_INFINITY
        for (f in faces) {
            val pl = f.plane ?: continue
            if (abs(pl.n.y) > 1e-6) continue
            val k = pl.n dot dir
            if (k > 1e-6) r = min(r, pl.d / k)
        }
        return if (r.isFinite()) r else null
    }

    private fun widthOf(points: List<Vec3>): Double {
        if (points.isEmpty()) return 0.0
        return min(points.maxOf { it.x } - points.minOf { it.x }, points.maxOf { it.z } - points.minOf { it.z })
    }

    /** Table horizontale dont la largeur vaut [fraction] de la largeur actuelle. */
    private fun tableDepth(faces: List<StoneFace>, n: Vec3, fraction: Double, floor: Double): Double? {
        if (abs(n.y - 1.0) > 1e-9) return null // une table est horizontale
        val all = faces.flatMap { it.points }
        val w0 = widthOf(all)
        var lo = floor; var hi = all.maxOf { it.y }
        if (hi <= lo) return null
        repeat(48) {
            val m = (lo + hi) / 2
            val cut = clip(faces, FacetPlane(Vec3(0.0, 1.0, 0.0), m, -1, 0))
            val top = cut?.filter { it.plane?.tier == -1 }?.flatMap { it.points }.orEmpty()
            if (widthOf(top) > fraction * w0) lo = m else hi = m
        }
        return (lo + hi) / 2
    }

    /**
     * Point de rencontre : un sommet de la pierre qui touche toutes les passes
     * citées et le moins possible d'autres ; parmi eux, le premier atteint en
     * descendant la facette (le plus avancé dans la direction de sa normale).
     */
    private fun meetDepth(faces: List<StoneFace>, n: Vec3, names: List<String>, design: FacetDesign): Double? {
        if (names.isEmpty()) return null
        val wanted = names.mapNotNull { name -> design.tiers.indexOfFirst { it.name == name }.takeIf { it >= 0 } }.toSet()
        if (wanted.size != names.size) return null
        val verts = ArrayList<Pair<Vec3, HashSet<Int>>>()
        for (f in faces) for (p in f.points) {
            var v = verts.firstOrNull { (it.first - p).length() < 1e-6 }
            if (v == null) { v = p to HashSet(); verts += v }
            f.plane?.let { v.second += it.tier }
        }
        val touching = verts.filter { v -> wanted.all { it in v.second } }
        if (touching.isEmpty()) return null
        val fewest = touching.minOf { it.second.size }
        return touching.filter { it.second.size == fewest }.maxOf { n dot it.first }
    }

    private fun dedupe(pts: List<Vec3>): List<Vec3> {
        val out = ArrayList<Vec3>(pts.size)
        for (p in pts) if (out.none { (it - p).length() < 1e-9 }) out += p
        return out
    }

    /** Ordonne les sommets d'une face dans le sens direct autour de sa normale (vue de l'extérieur). */
    private fun sortAround(pts: List<Vec3>, n: Vec3): List<Vec3> {
        val c = pts.fold(Vec3(0.0, 0.0, 0.0)) { s, p -> s + p } * (1.0 / pts.size)
        val ref = if (abs(n.y) < 0.9) Vec3(0.0, 1.0, 0.0) else Vec3(1.0, 0.0, 0.0)
        val u = (ref cross n).normalized(); val w = n cross u
        return pts.sortedBy { val q = it - c; atan2(q dot w, q dot u) }
    }

    fun polygonArea(pts: List<Vec3>): Double {
        var s = Vec3(0.0, 0.0, 0.0)
        for (i in 1 until pts.size - 1) s += (pts[i] - pts[0]) cross (pts[i + 1] - pts[0])
        return s.length() / 2
    }

    fun volume(stone: FacetStone): Double {
        val all = stone.faces.flatMap { it.points }
        if (all.isEmpty()) return 0.0
        val o = all.fold(Vec3(0.0, 0.0, 0.0)) { s, p -> s + p } * (1.0 / all.size)
        var vol = 0.0
        for (f in stone.faces) for (i in 1 until f.points.size - 1) {
            val a = f.points[0] - o; val b = f.points[i] - o; val c = f.points[i + 1] - o
            vol += abs(a dot (b cross c)) / 6.0
        }
        return vol
    }

    /** mm par unité du repère : le cube fait [FacetDesign.cubeMm] de côté. */
    fun mmPerUnit(design: FacetDesign) = design.cubeMm / (2 * CUBE_HALF)

    fun metrics(design: FacetDesign, stone: FacetStone): FacetMetrics? {
        val all = stone.faces.flatMap { it.points }
        if (all.isEmpty()) return null
        val k = mmPerUnit(design)
        val ex = all.maxOf { it.x } - all.minOf { it.x }
        val ez = all.maxOf { it.z } - all.minOf { it.z }
        val width = min(ex, ez); val length = max(ex, ez)
        val top = all.maxOf { it.y }; val bottom = all.minOf { it.y }
        val carats = volume(stone) * k * k * k / 1000.0 * design.material.sg * 5.0
        val girdlePts = stone.faces.filter { f -> f.plane?.let { abs(it.n.y) < 1e-6 } == true }.flatMap { it.points }
        val tablePts = stone.faces.filter { f -> f.plane?.let { abs(it.n.y - 1.0) < 1e-9 } == true }.flatMap { it.points }
        val complete = stone.closed && girdlePts.isNotEmpty() && width > 1e-6
        // proportions mesurées depuis la référence du rondiste (comme les règles de coupe)
        val gBottom = stone.girdleBottom ?: girdlePts.minOfOrNull { it.y } ?: bottom
        val gTop = stone.girdleTop ?: girdlePts.maxOfOrNull { it.y } ?: top
        val tableW = if (tablePts.isEmpty()) 0.0 else
            if (ex <= ez) tablePts.maxOf { it.x } - tablePts.minOf { it.x } else tablePts.maxOf { it.z } - tablePts.minOf { it.z }
        return FacetMetrics(
            widthMm = width * k,
            lengthMm = length * k,
            heightMm = (top - bottom) * k,
            lengthToWidth = if (width > 1e-9) length / width else 1.0,
            tablePercent = if (width > 1e-9) tableW / width * 100 else 0.0,
            crownPercent = if (width > 1e-9) (top - gTop) / width * 100 else 0.0,
            pavilionPercent = if (width > 1e-9) (gBottom - bottom) / width * 100 else 0.0,
            totalPercent = if (width > 1e-9) (top - bottom) / width * 100 else 0.0,
            carats = carats,
            facetCount = stone.faces.count { it.plane != null },
            girdleFacetCount = stone.faces.count { f -> f.plane?.let { abs(it.n.y) < 1e-6 } == true },
            complete = complete
        )
    }

    /**
     * Géométrie pour le moteur 3D : {"v":[x,y,z,…],"f":[[i,j,k,…],…],"raw":[0|1,…],"hl":[0|1,…]}
     * — raw = face brute du cube, hl = facette de la passe [highlightTier] (en or).
     * Le modèle est recentré (rondiste vers y = 0) et mis à l'échelle de la vue.
     */
    fun meshJson(stone: FacetStone, highlightTier: Int? = null, scale: Double = 5.0): String {
        val all = stone.faces.flatMap { it.points }
        val cy = if (all.isEmpty()) 0.0 else (all.maxOf { it.y } + all.minOf { it.y }) / 2
        val verts = ArrayList<Vec3>()
        val f = StringBuilder("[")
        val raw = StringBuilder("[")
        val hl = StringBuilder("[")
        stone.faces.forEachIndexed { fi, face ->
            if (fi > 0) { f.append(','); raw.append(','); hl.append(',') }
            f.append('[')
            face.points.forEachIndexed { k, p ->
                var vi = verts.indexOfFirst { (it - p).length() < 1e-7 }
                if (vi < 0) { vi = verts.size; verts += p }
                if (k > 0) f.append(',')
                f.append(vi)
            }
            f.append(']')
            raw.append(if (face.plane == null) 1 else 0)
            hl.append(if (highlightTier != null && face.plane?.tier == highlightTier) 1 else 0)
        }
        f.append(']'); raw.append(']'); hl.append(']')
        val v = StringBuilder("[")
        verts.forEachIndexed { i, p ->
            if (i > 0) v.append(',')
            v.append(fmt(p.x * scale)).append(',').append(fmt((p.y - cy) * scale)).append(',').append(fmt(p.z * scale))
        }
        v.append(']')
        return "{\"f\":$f,\"raw\":$raw,\"hl\":$hl,\"v\":$v}"
    }

    private fun fmt(v: Double): String = ((v * 10000).roundToInt() / 10000.0).toString()
}

/** Sauvegarde texte d'une taille (préférences de l'app). */
object FacetDesignCodec {
    private const val SEP = "\n~~~\n"

    fun encode(d: FacetDesign): String = buildString {
        append("v2|").append(clean(d.name)).append('|').append(d.wheel).append('|')
            .append(d.girdlePercent).append('|').append(d.material.name).append('|').append(d.cubeMm)
        for (t in d.tiers) {
            append('\n').append(clean(t.name)).append(';').append(t.side.name).append(';').append(t.role.name).append(';')
                .append(t.angleDeg).append(';').append(t.rule.name).append(';').append(t.value).append(';')
                .append(t.indices.joinToString(",")).append(';').append(t.meet.joinToString(",")).append(';').append(if (t.hidden) 1 else 0)
        }
    }

    private fun clean(s: String) = s.replace("|", " ").replace(";", " ").replace(",", " ").replace("\n", " ").replace("~", " ")

    fun decode(text: String): FacetDesign? = runCatching {
        val lines = text.split('\n')
        val h = lines.first().split('|')
        require(h[0] == "v2")
        val tiers = lines.drop(1).filter { it.isNotBlank() }.map { l ->
            val p = l.split(';')
            FacetTier(
                name = p[0], side = FacetSide.valueOf(p[1]), role = FacetRole.valueOf(p[2]), angleDeg = p[3].toDouble(),
                rule = FacetRule.valueOf(p[4]), value = p[5].toDouble(),
                indices = if (p[6].isBlank()) emptyList() else p[6].split(',').map { it.toInt() },
                meet = if (p[7].isBlank()) emptyList() else p[7].split(','),
                hidden = p[8] == "1"
            )
        }
        FacetDesign(
            name = h[1], wheel = h[2].toInt(), girdlePercent = h[3].toDouble(),
            material = FacetMaterial.valueOf(h[4]), cubeMm = h[5].toDouble(), tiers = tiers
        )
    }.getOrNull()

    fun encodeAll(list: List<FacetDesign>): String = list.joinToString(SEP) { encode(it) }
    fun decodeAll(text: String): List<FacetDesign> = text.split(SEP).mapNotNull { decode(it) }
}
