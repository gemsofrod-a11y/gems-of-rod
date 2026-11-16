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
import kotlin.math.tan

/**
 * Atelier de facettage (demande de Sébastien du 16/11/2026) : une taille se
 * décrit comme sur une machine à facetter. C'est une suite de passes, et
 * chaque passe a un angle, une liste d'index sur la roue et une règle de
 * profondeur. La pierre est l'intersection des demi-espaces des facettes.
 * Ce fichier en tire la géométrie (pour la vue 3D), les proportions et le
 * poids estimé. Code commun pur, testé dans commonTest.
 *
 * Repère : y vers le haut (table), rondiste centré sur y = 0, demi-largeur
 * de référence R = 1. L'index 0 (affiché comme l'index max de la roue,
 * ex. « 96 ») pointe vers +x.
 */

enum class FacetSide { PAVILION, GIRDLE, CROWN }

/** Rôle d'une passe : sert seulement à son libellé (traduit dans l'app). */
enum class FacetRole { NONE, PAVILION_MAIN, PAVILION_HALF, GIRDLE, CROWN_MAIN, STAR, UPPER_HALF, TABLE }

/**
 * Règle de profondeur d'une passe :
 * - [GIRDLE] : la facette passe par le bord du rondiste (R, ±rondiste/2) ;
 * - [TABLE] : (couronne) la facette touche le bord de la table ;
 * - [VALUE] : distance au centre imposée, en % de R (utile pour un rondiste
 *   non rond ou pour une passe qu'on veut plus ou moins profonde).
 */
enum class FacetDepth { GIRDLE, TABLE, VALUE }

/** Matières proposées : indice de réfraction et densité (pour le poids). */
enum class FacetMaterial(val ri: Double, val sg: Double) {
    DIAMANT(2.417, 3.52),
    MOISSANITE(2.65, 3.22),
    ZIRCONE_CUBIQUE(2.15, 5.80),
    ZIRCON(1.95, 4.65),
    GRENAT(1.79, 4.05),
    CORINDON(1.762, 4.00),
    CHRYSOBERYL(1.75, 3.72),
    SPINELLE(1.718, 3.60),
    TANZANITE(1.70, 3.35),
    PERIDOT(1.67, 3.34),
    TOURMALINE(1.624, 3.06),
    TOPAZE(1.619, 3.53),
    BERYL(1.577, 2.72),
    QUARTZ(1.544, 2.65);

    /** Angle critique (degrés) : sous cet angle d'incidence, la lumière sort de la pierre. */
    val criticalAngleDeg: Double get() = asin(1.0 / ri) * 180.0 / PI
}

data class FacetTier(
    val name: String,
    val side: FacetSide,
    val angleDeg: Double,
    val indices: List<Int>,
    val depth: FacetDepth = FacetDepth.GIRDLE,
    /** Distance au centre en % de R, pour [FacetDepth.VALUE]. */
    val depthValue: Double = 100.0,
    val role: FacetRole = FacetRole.NONE
)

data class FacetDesign(
    val name: String,
    val wheel: Int,
    /** Largeur de table voulue, en % du diamètre (sert aux passes « jusqu'à la table »). */
    val tablePercent: Double,
    /** Épaisseur du rondiste, en % du diamètre. */
    val girdlePercent: Double,
    val tiers: List<FacetTier>,
    val material: FacetMaterial = FacetMaterial.DIAMANT,
    val diameterMm: Double = 6.5
)

object FacetWheels {
    val sizes = listOf(64, 72, 80, 96, 120)

    /** Affichage d'un index : 0 s'écrit comme la taille de la roue (convention des machines). */
    fun display(index: Int, wheel: Int): String {
        val i = ((index % wheel) + wheel) % wheel
        val v = if (i == 0) wheel else i
        return if (v < 10) "0$v" else v.toString()
    }

    fun normalize(index: Int, wheel: Int): Int {
        val i = ((index % wheel) + wheel) % wheel
        return if (i == 0) wheel else i
    }

    /**
     * Index d'une passe à partir d'un index de départ et d'une symétrie de
     * rotation [symmetry] (8 = huit facettes réparties sur le tour). Avec
     * [mirror], chaque facette a sa jumelle symétrique par rapport à l'axe
     * (ex. 03 et 93 sur une roue de 96) — sans doublon si l'index est sur l'axe.
     */
    fun indicesFor(start: Int, symmetry: Int, wheel: Int, mirror: Boolean): List<Int> {
        val n = symmetry.coerceIn(1, wheel)
        val step = wheel.toDouble() / n
        val out = LinkedHashSet<Int>()
        for (k in 0 until n) {
            val base = (k * step).roundToInt()
            out += normalize(base + start, wheel)
            if (mirror) out += normalize(base - start, wheel)
        }
        return out.sortedBy { it % wheel }
    }

    /** Symétries qui tombent juste sur la roue donnée. */
    fun symmetriesFor(wheel: Int): List<Int> =
        listOf(2, 3, 4, 5, 6, 8, 10, 12, 16, 24, 32).filter { wheel % it == 0 }

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

/** Tailles de départ (angles de référence classiques, à reprendre et modifier). */
object FacetPresets {
    private fun every(start: Int, step: Int, wheel: Int = 96) =
        (0 until wheel / step).map { FacetWheels.normalize(start + it * step, wheel) }

    val brilliantRound = FacetDesign(
        name = "Brillant rond",
        wheel = 96,
        tablePercent = 57.0,
        girdlePercent = 1.5,
        tiers = listOf(
            FacetTier("P1", FacetSide.PAVILION, 40.75, every(0, 12), FacetDepth.GIRDLE, role = FacetRole.PAVILION_MAIN),
            FacetTier("P2", FacetSide.PAVILION, 42.0, every(3, 6), FacetDepth.GIRDLE, role = FacetRole.PAVILION_HALF),
            FacetTier("G", FacetSide.GIRDLE, 90.0, every(0, 3), FacetDepth.GIRDLE, role = FacetRole.GIRDLE),
            FacetTier("C1", FacetSide.CROWN, 34.5, every(0, 12), FacetDepth.GIRDLE, role = FacetRole.CROWN_MAIN),
            FacetTier("C2", FacetSide.CROWN, 20.0, every(6, 12), FacetDepth.TABLE, role = FacetRole.STAR),
            FacetTier("C3", FacetSide.CROWN, 40.0, every(3, 6), FacetDepth.GIRDLE, role = FacetRole.UPPER_HALF),
            FacetTier("T", FacetSide.CROWN, 0.0, listOf(96), FacetDepth.TABLE, role = FacetRole.TABLE)
        )
    )

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

/** Plan d'une facette : n·p ≤ d à l'intérieur de la pierre. */
data class FacetPlane(val n: Vec3, val d: Double, val tier: Int, val index: Int)

/** Face de la pierre : polygone convexe, sommets dans l'ordre. plane == null : face de la boîte englobante (pierre ouverte). */
class StoneFace(val plane: FacetPlane?, val points: List<Vec3>)

class FacetStone(
    val faces: List<StoneFace>,
    /** false si une face de la boîte englobante subsiste : il manque une passe pour fermer la pierre. */
    val closed: Boolean,
    /** Index des passes dont aucune facette n'apparaît (coupées entièrement par les autres). */
    val hiddenTiers: Set<Int>
)

data class FacetMetrics(
    val tablePercent: Double,
    val crownPercent: Double,
    val pavilionPercent: Double,
    val totalPercent: Double,
    val girdlePercent: Double,
    /** Longueur / largeur (1,00 pour un rond). */
    val lengthToWidth: Double,
    val carats: Double,
    val facetCount: Int
)

object FacetGeometry {
    private const val EPS = 1e-9
    private const val BOX = 4.0

    private fun rad(deg: Double) = deg * PI / 180.0

    /** Pente des principales de couronne : sert à placer la table. */
    private fun crownMainAngle(design: FacetDesign): Double =
        design.tiers.firstOrNull { it.side == FacetSide.CROWN && it.depth == FacetDepth.GIRDLE && it.angleDeg > 0.5 && it.angleDeg < 89.5 }
            ?.angleDeg ?: 35.0

    /** Hauteur de la table (y) déduite de la largeur de table voulue et des principales de couronne. */
    fun tableHeight(design: FacetDesign): Double {
        val g = design.girdlePercent / 100.0 * 2.0
        val rT = (design.tablePercent / 100.0).coerceIn(0.05, 0.98)
        return g / 2 + (1.0 - rT) * tan(rad(crownMainAngle(design)))
    }

    fun planes(design: FacetDesign): List<FacetPlane> {
        val g = design.girdlePercent / 100.0 * 2.0 // en unités de R (le % est du diamètre = 2R)
        val rT = (design.tablePercent / 100.0).coerceIn(0.05, 0.98)
        val yT = tableHeight(design)
        val out = ArrayList<FacetPlane>()
        design.tiers.forEachIndexed { ti, t ->
            val a = rad(t.angleDeg.coerceIn(0.0, 90.0))
            for (idx in t.indices) {
                val phi = (idx % design.wheel).toDouble() / design.wheel * 2 * PI
                val cx = cos(phi); val cz = sin(phi)
                val value = t.depthValue / 100.0
                val (n, d) = when (t.side) {
                    FacetSide.GIRDLE -> Vec3(cx, 0.0, cz) to (if (t.depth == FacetDepth.VALUE) value else 1.0)
                    FacetSide.PAVILION -> {
                        val n = Vec3(sin(a) * cx, -cos(a), sin(a) * cz)
                        n to when (t.depth) {
                            FacetDepth.VALUE -> value
                            else -> sin(a) + cos(a) * (g / 2)
                        }
                    }
                    FacetSide.CROWN -> {
                        val n = Vec3(sin(a) * cx, cos(a), sin(a) * cz)
                        n to when (t.depth) {
                            FacetDepth.VALUE -> value
                            FacetDepth.TABLE -> sin(a) * rT + cos(a) * yT
                            FacetDepth.GIRDLE -> sin(a) + cos(a) * (g / 2)
                        }
                    }
                }
                out += FacetPlane(n, d, ti, idx)
            }
        }
        return out
    }

    /** Construit la pierre en découpant une boîte par chaque facette. */
    fun build(design: FacetDesign): FacetStone {
        var faces = boxFaces()
        val seen = HashSet<Int>()
        for (pl in planes(design)) {
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
                        val t = da / (da - db)
                        val p = a + (b - a) * t
                        out += p; cap += p
                    }
                    if (!aIn) cut = true
                    if (abs(da) <= EPS) cap += a
                }
                val clean = dedupe(out)
                if (clean.size >= 3) next += StoneFace(f.plane, clean)
            }
            if (!cut) continue // facette qui ne touche pas la pierre
            val capPts = dedupe(cap)
            if (capPts.size >= 3) next += StoneFace(pl, sortAround(capPts, pl.n))
            faces = next
        }
        // une facette peut avoir été recoupée entièrement par les suivantes
        faces = faces.filter { polygonArea(it.points) > 1e-9 }
        faces.forEach { f -> f.plane?.let { seen += it.tier } }
        val hidden = design.tiers.indices.filter { it !in seen }.toSet()
        return FacetStone(faces, closed = faces.none { it.plane == null }, hiddenTiers = hidden)
    }

    private fun boxFaces(): List<StoneFace> {
        val b = BOX
        fun p(x: Double, y: Double, z: Double) = Vec3(x, y, z)
        return listOf(
            StoneFace(null, listOf(p(b, -b, -b), p(b, b, -b), p(b, b, b), p(b, -b, b))),
            StoneFace(null, listOf(p(-b, -b, -b), p(-b, -b, b), p(-b, b, b), p(-b, b, -b))),
            StoneFace(null, listOf(p(-b, b, -b), p(-b, b, b), p(b, b, b), p(b, b, -b))),
            StoneFace(null, listOf(p(-b, -b, -b), p(b, -b, -b), p(b, -b, b), p(-b, -b, b))),
            StoneFace(null, listOf(p(-b, -b, b), p(b, -b, b), p(b, b, b), p(-b, b, b))),
            StoneFace(null, listOf(p(-b, -b, -b), p(-b, b, -b), p(b, b, -b), p(b, -b, -b)))
        )
    }

    private fun dedupe(pts: List<Vec3>): List<Vec3> {
        val out = ArrayList<Vec3>(pts.size)
        for (p in pts) if (out.none { (it - p).length() < 1e-7 }) out += p
        // enlève aussi les points alignés consécutifs (arêtes dégénérées)
        return out
    }

    private fun sortAround(pts: List<Vec3>, n: Vec3): List<Vec3> {
        val c = pts.fold(Vec3(0.0, 0.0, 0.0)) { s, p -> s + p } * (1.0 / pts.size)
        val ref = if (abs(n.y) < 0.9) Vec3(0.0, 1.0, 0.0) else Vec3(1.0, 0.0, 0.0)
        val u = (ref cross n).normalized(); val w = n cross u
        return pts.sortedBy { val q = it - c; atan2(q dot w, q dot u) }
    }

    private fun polygonArea(pts: List<Vec3>): Double {
        var s = Vec3(0.0, 0.0, 0.0)
        for (i in 1 until pts.size - 1) s += (pts[i] - pts[0]) cross (pts[i + 1] - pts[0])
        return s.length() / 2
    }

    fun metrics(design: FacetDesign, stone: FacetStone): FacetMetrics? {
        if (stone.faces.isEmpty() || !stone.closed) return null
        val all = stone.faces.flatMap { it.points }
        val minX = all.minOf { it.x }; val maxX = all.maxOf { it.x }
        val minZ = all.minOf { it.z }; val maxZ = all.maxOf { it.z }
        val top = all.maxOf { it.y }; val bottom = all.minOf { it.y }
        val width = min(maxX - minX, maxZ - minZ)
        val length = max(maxX - minX, maxZ - minZ)
        if (width <= 1e-6) return null
        val girdlePts = stone.faces.filter { f -> f.plane?.let { design.tiers.getOrNull(it.tier)?.side == FacetSide.GIRDLE } == true }.flatMap { it.points }
        val gTop = girdlePts.maxOfOrNull { it.y } ?: 0.0
        val gBottom = girdlePts.minOfOrNull { it.y } ?: 0.0
        val tablePts = stone.faces.filter { f -> f.plane?.let { abs(it.n.y - 1.0) < 1e-6 } == true }.flatMap { it.points }
        val tableW = if (tablePts.isEmpty()) 0.0 else {
            val tx = tablePts.maxOf { it.x } - tablePts.minOf { it.x }
            val tz = tablePts.maxOf { it.z } - tablePts.minOf { it.z }
            if (maxX - minX <= maxZ - minZ) tx else tz
        }
        // volume : tétraèdres depuis un point intérieur (la pierre est convexe)
        val o = all.fold(Vec3(0.0, 0.0, 0.0)) { s, p -> s + p } * (1.0 / all.size)
        var vol = 0.0
        for (f in stone.faces) for (i in 1 until f.points.size - 1) {
            val a = f.points[0] - o; val b = f.points[i] - o; val c = f.points[i + 1] - o
            vol += abs(a dot (b cross c)) / 6.0
        }
        val scale = design.diameterMm / width // unités → mm (le diamètre saisi est la largeur)
        val carats = vol * scale * scale * scale / 1000.0 * design.material.sg * 5.0
        return FacetMetrics(
            tablePercent = tableW / width * 100,
            crownPercent = (top - gTop) / width * 100,
            pavilionPercent = (gBottom - bottom) / width * 100,
            totalPercent = (top - bottom) / width * 100,
            girdlePercent = (gTop - gBottom) / width * 100,
            lengthToWidth = length / width,
            carats = carats,
            facetCount = stone.faces.size
        )
    }

    /** Géométrie pour le moteur 3D : {"v":[x,y,z,…],"f":[[i,j,k,…],…]} à l'échelle de la vue. */
    fun meshJson(stone: FacetStone, scale: Double = 6.0): String {
        val verts = ArrayList<Vec3>()
        val sb = StringBuilder("{\"f\":[")
        stone.faces.forEachIndexed { fi, f ->
            if (fi > 0) sb.append(',')
            sb.append('[')
            f.points.forEachIndexed { k, p ->
                var vi = verts.indexOfFirst { (it - p).length() < 1e-7 }
                if (vi < 0) { vi = verts.size; verts += p }
                if (k > 0) sb.append(',')
                sb.append(vi)
            }
            sb.append(']')
        }
        sb.append("],\"v\":[")
        verts.forEachIndexed { i, p ->
            if (i > 0) sb.append(',')
            sb.append(fmt(p.x * scale)).append(',').append(fmt(p.y * scale)).append(',').append(fmt(p.z * scale))
        }
        sb.append("]}")
        return sb.toString()
    }

    private fun fmt(v: Double): String {
        val r = (v * 10000).roundToInt() / 10000.0
        return r.toString()
    }
}

/** Sauvegarde texte d'une taille (préférences de l'app, export futur). */
object FacetDesignCodec {
    fun encode(d: FacetDesign): String = buildString {
        append("v1|").append(d.name.replace("|", " ").replace("\n", " ")).append('|').append(d.wheel).append('|')
            .append(d.tablePercent).append('|').append(d.girdlePercent).append('|').append(d.material.name).append('|').append(d.diameterMm)
        for (t in d.tiers) {
            append('\n').append(t.name.replace(";", " ").replace("|", " ")).append(';').append(t.side.name).append(';').append(t.role.name).append(';')
                .append(t.angleDeg).append(';').append(t.depth.name).append(';').append(t.depthValue).append(';').append(t.indices.joinToString(","))
        }
    }

    fun decode(text: String): FacetDesign? = runCatching {
        val lines = text.split('\n')
        val h = lines.first().split('|')
        require(h[0] == "v1")
        val tiers = lines.drop(1).filter { it.isNotBlank() }.map { l ->
            val p = l.split(';')
            FacetTier(
                name = p[0], side = FacetSide.valueOf(p[1]), role = FacetRole.valueOf(p[2]), angleDeg = p[3].toDouble(),
                depth = FacetDepth.valueOf(p[4]), depthValue = p[5].toDouble(),
                indices = if (p[6].isBlank()) emptyList() else p[6].split(',').map { it.toInt() }
            )
        }
        FacetDesign(
            name = h[1], wheel = h[2].toInt(), tablePercent = h[3].toDouble(), girdlePercent = h[4].toDouble(),
            material = FacetMaterial.valueOf(h[5]), diameterMm = h[6].toDouble(), tiers = tiers
        )
    }.getOrNull()
}
