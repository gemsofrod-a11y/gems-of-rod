package fr.gemsofrod.encyclopedie.data

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Rendu réaliste de l'Atelier de facettage (étape 2, validée par Sébastien le
 * 17/11/2026) : la lumière est suivie rayon par rayon dans la pierre — réfraction
 * (Snell-Descartes), reflets (Fresnel), réflexion totale, dispersion (feu, un rayon
 * par couleur rouge / vert / bleu) et absorption de la couleur (Beer-Lambert).
 * Code commun pur, testé dans commonTest (FacetRenderTest).
 */

/** Éclairage de la scène autour de la pierre. */
enum class FacetLighting { STUDIO, SUN, SOFT }

data class FacetRenderSettings(
    val ri: Double,
    val dispersion: Double,
    /** Couleur de la pierre en teinte (0..360), saturation et clarté (0..1). Saturation 0 = incolore. */
    val hue: Double = 0.0,
    val saturation: Double = 0.0,
    val lightness: Double = 1.0,
    /** Densité de la couleur : plus elle est grande, plus la teinte se fonce sur un long trajet. */
    val density: Double = 1.0,
    /** Inclinaison de la pierre (degrés) : avant/arrière et gauche/droite. */
    val tiltX: Double = 0.0,
    val tiltY: Double = 0.0,
    val lighting: FacetLighting = FacetLighting.STUDIO,
    val bounces: Int = 8
) {
    companion object {
        /** Réglages de départ d'une matière (couleur typique de la gemme). */
        fun forMaterial(m: FacetMaterial): FacetRenderSettings {
            return FacetRenderSettings(ri = m.ri, dispersion = m.dispersion, hue = m.hue, saturation = m.saturation, lightness = m.lightness, density = 0.9)
        }
    }

    /** Transmission (0..1) par unité de trajet, pour le rouge, le vert et le bleu. */
    fun transmission(): DoubleArray {
        if (saturation <= 1e-6) return doubleArrayOf(1.0, 1.0, 1.0)
        val c = hslToRgb(hue, saturation, lightness)
        // une couleur très sombre n'éteint pas tout : on garde un minimum de passage
        return DoubleArray(3) { (0.04 + 0.96 * c[it]).coerceIn(0.04, 1.0) }
    }
}

fun hslToRgb(h: Double, s: Double, l: Double): DoubleArray {
    val hh = ((h % 360) + 360) % 360 / 60.0
    val c = (1 - abs(2 * l - 1)) * s
    val x = c * (1 - abs(hh % 2 - 1))
    val (r, g, b) = when (hh.toInt()) {
        0 -> Triple(c, x, 0.0); 1 -> Triple(x, c, 0.0); 2 -> Triple(0.0, c, x)
        3 -> Triple(0.0, x, c); 4 -> Triple(x, 0.0, c); else -> Triple(c, 0.0, x)
    }
    val m = l - c / 2
    return doubleArrayOf(r + m, g + m, b + m)
}

class FacetRenderResult(
    val size: Int,
    /** Pixels ARGB (fond compris). */
    val pixels: IntArray,
    /** Part de la lumière reçue d'en haut qui repart vers l'observateur, à moins de 60° de la verticale (reflet de surface compris), canal vert, 0..1. */
    val lightReturn: Double,
    /** Sommes brutes, pour assembler un calcul réparti par lignes sur plusieurs cœurs. */
    val returnedSum: Double = 0.0,
    val entered: Int = 0
)

object FacetRenderer {

    private class Plane(val nx: Double, val ny: Double, val nz: Double, val d: Double)

    fun refract(dx: Double, dy: Double, dz: Double, nx: Double, ny: Double, nz: Double, eta: Double): DoubleArray? {
        val cosi = -(nx * dx + ny * dy + nz * dz)
        val k = 1 - eta * eta * (1 - cosi * cosi)
        if (k < 0) return null
        val f = eta * cosi - sqrt(k)
        val rx = dx * eta + nx * f; val ry = dy * eta + ny * f; val rz = dz * eta + nz * f
        val l = sqrt(rx * rx + ry * ry + rz * rz)
        return doubleArrayOf(rx / l, ry / l, rz / l)
    }

    /** Reflet de Fresnel (approximation de Schlick). */
    fun fresnel(cosi: Double, eta: Double): Double {
        val r0 = ((1 - eta) / (1 + eta)).let { it * it }
        return r0 + (1 - r0) * (1 - cosi).pow(5)
    }

    /** Lumière de l'environnement dans la direction (x, y, z) (sortant de la pierre). */
    fun environment(lighting: FacetLighting, x: Double, y: Double, z: Double): Double {
        // ombre douce de l'observateur, juste au-dessus de la pierre
        val head = 1 - 0.55 * ((y - 0.94) / 0.05).coerceIn(0.0, 1.0)
        return when (lighting) {
            FacetLighting.STUDIO -> {
                // studio de photo de joaillerie : pièce sombre, couronne de boîtes à lumière
                // séparées par des intervalles noirs (le contraste qui dessine les facettes),
                // petites lampes vives (le feu), et l'observateur qui masque le zénith
                if (y < 0.0) return 0.02
                var v = 0.10 + 0.22 * y
                val az = atan2(z, x)
                // boîtes à lumière : de 15° à 75° au-dessus de l'horizon, plafond diffus au-dessus
                val ring = smooth((y - 0.26) / 0.1) * smooth((0.97 - y) / 0.04)
                val panel = smooth((cos(az * 4 + 0.4) + 0.1) / 0.3)
                v += 1.3 * ring * panel + 0.55 * smooth((y - 0.95) / 0.03)
                for (sp in SPOTS) if (x * sp[0] + y * sp[1] + z * sp[2] > 0.99945) v += 14.0
                // l'observateur (tête et appareil) masque une petite zone au zénith
                val viewer = 1 - 0.8 * smooth((y - 0.982) / 0.008)
                v * viewer
            }
            FacetLighting.SUN -> {
                // ciel sombre, un soleil franc : beaucoup de contraste et de feu
                val sx = -0.45; val sy = 0.82; val sz = 0.36
                val dot = x * sx + y * sy + z * sz
                val sun = if (dot > 0.975) 5.0 else 0.0
                (0.14 + 0.42 * max(0.0, y) + sun) * head
            }
            FacetLighting.SOFT -> ((0.45 + 0.55 * max(0.0, y)) * head).coerceAtLeast(0.15)
        }
    }

    private fun smooth(t: Double): Double { val c = t.coerceIn(0.0, 1.0); return c * c * (3 - 2 * c) }

    /** Petites lampes du studio (azimut, élévation en degrés), sources du feu. */
    private val SPOTS: List<DoubleArray> = listOf(
        20.0 to 52.0, 95.0 to 34.0, 150.0 to 63.0, 215.0 to 44.0, 290.0 to 57.0, 335.0 to 31.0
    ).map { (a, e) ->
        val ar = a * PI / 180; val er = e * PI / 180
        doubleArrayOf(cos(er) * cos(ar), sin(er), cos(er) * sin(ar))
    }

    /** Lumière qui ressort vers l'observateur : à moins de 60° de la verticale. */
    private const val VIEW_Y = 0.5

    /**
     * Calcule l'image vue de dessus (pierre centrée, [size] × [size] pixels,
     * [samples] × [samples] rayons par pixel). [rows] permet de ne calculer qu'une
     * partie des lignes (calcul réparti sur plusieurs cœurs).
     */
    fun render(
        stone: FacetStone,
        s: FacetRenderSettings,
        size: Int,
        samples: Int = 2,
        background: Int = 0xFF141D17.toInt(),
        rows: IntRange = 0 until size,
        linearOut: DoubleArray? = null,
        envOverride: ((Double, Double, Double) -> Double)? = null
    ): FacetRenderResult {
        val ax = s.tiltX * PI / 180; val ay = s.tiltY * PI / 180
        val cax = cos(ax); val sax = sin(ax); val cay = cos(ay); val say = sin(ay)
        fun rot(x: Double, y: Double, z: Double): DoubleArray {
            val y1 = y * cax - z * sax; val z1 = y * sax + z * cax
            return doubleArrayOf(x * cay + z1 * say, y1, -x * say + z1 * cay)
        }
        val all = stone.faces.flatMap { it.points }
        val pixels = IntArray(size * size) { background }
        if (all.isEmpty()) return FacetRenderResult(size, pixels, 0.0)
        val cx = (all.maxOf { it.x } + all.minOf { it.x }) / 2
        val cy = (all.maxOf { it.y } + all.minOf { it.y }) / 2
        val cz = (all.maxOf { it.z } + all.minOf { it.z }) / 2
        val planes = stone.faces.map { f ->
            // plan de chaque face, recentré puis incliné
            val p0 = f.points[0]
            val n = (f.points[1] - p0).cross(f.points[2] - p0).normalized()
            val c = f.points.fold(Vec3(0.0, 0.0, 0.0)) { a, p -> a + p } * (1.0 / f.points.size)
            val o = Vec3(cx, cy, cz)
            // orientation vers l'extérieur : le centre de la pierre doit être du côté intérieur
            val nn = if (n dot (c - o) < 0) n * -1.0 else n
            val r = rot(nn.x, nn.y, nn.z)
            val q = rot(p0.x - cx, p0.y - cy, p0.z - cz)
            Plane(r[0], r[1], r[2], r[0] * q[0] + r[1] * q[1] + r[2] * q[2])
        }
        val radius = all.maxOf { sqrt((it.x - cx).let { a -> a * a } + (it.y - cy).let { a -> a * a } + (it.z - cz).let { a -> a * a }) }
        val ext = radius * 1.08
        val trans = s.transmission()
        val ris = doubleArrayOf(s.ri - s.dispersion / 2, s.ri, s.ri + s.dispersion / 2)
        val env: (Double, Double, Double) -> Double = envOverride ?: { x, y, z -> environment(s.lighting, x, y, z) }
        var returned = 0.0; var entered = 0
        val bgR = (background shr 16 and 0xFF) / 255.0; val bgG = (background shr 8 and 0xFF) / 255.0; val bgB = (background and 0xFF) / 255.0
        val ns = samples * samples
        for (py in rows) for (px in 0 until size) {
            var ar = 0.0; var ag = 0.0; var ab = 0.0
            for (sy in 0 until samples) for (sx in 0 until samples) {
                val x = ((px + (sx + 0.5) / samples) / size * 2 - 1) * ext
                val z = ((py + (sy + 0.5) / samples) / size * 2 - 1) * ext
                // rayon vertical venant du haut
                var tIn = Double.NEGATIVE_INFINITY; var tOut = Double.POSITIVE_INFINITY; var hit: Plane? = null; var miss = false
                val oy = radius * 4
                for (pl in planes) {
                    val den = -pl.ny
                    val num = pl.d - (pl.nx * x + pl.ny * oy + pl.nz * z)
                    if (abs(den) < 1e-12) { if (num < 0) { miss = true; break }; continue }
                    val t = num / den
                    if (den < 0) { if (t > tIn) { tIn = t; hit = pl } } else if (t < tOut) tOut = t
                }
                if (miss || hit == null || tIn > tOut) { ar += bgR; ag += bgG; ab += bgB; continue }
                val p0x = x; val p0y = oy - tIn; val p0z = z
                val cosi = hit.ny // rayon (0,-1,0) : -n·d = ny
                val out = DoubleArray(3)
                for (ch in 0 until 3) {
                    val eta = ris[ch]
                    val f = fresnel(cosi, eta)
                    // reflet de surface
                    val rdx = 0 - 2 * (-hit.ny) * hit.nx; val rdy = -1 - 2 * (-hit.ny) * hit.ny; val rdz = 0 - 2 * (-hit.ny) * hit.nz
                    var col = f * env(rdx, rdy, rdz)
                    if (ch == 1 && rdy > VIEW_Y) returned += f
                    var dir = refract(0.0, -1.0, 0.0, hit.nx, hit.ny, hit.nz, 1 / eta)
                    var px0 = p0x; var py0 = p0y; var pz0 = p0z
                    var k = 1 - f
                    var path = 0.0
                    var b = 0
                    while (dir != null && b < s.bounces) {
                        val dx = dir[0]; val dy = dir[1]; val dz = dir[2]
                        var tMin = Double.POSITIVE_INFINITY; var pl: Plane? = null
                        for (q in planes) {
                            val den = q.nx * dx + q.ny * dy + q.nz * dz
                            if (den > 1e-9) {
                                val t = (q.d - (q.nx * px0 + q.ny * py0 + q.nz * pz0)) / den
                                if (t > 1e-9 && t < tMin) { tMin = t; pl = q }
                            }
                        }
                        if (pl == null) break
                        px0 += dx * tMin; py0 += dy * tMin; pz0 += dz * tMin; path += tMin
                        val ci = pl.nx * dx + pl.ny * dy + pl.nz * dz // cos incidence (normale sortante)
                        val ex = refract(dx, dy, dz, -pl.nx, -pl.ny, -pl.nz, eta)
                        val att = trans[ch].pow(path * s.density / radius)
                        if (ex != null) {
                            val fo = fresnel(ci, 1 / eta)
                            col += k * (1 - fo) * att * env(ex[0], ex[1], ex[2])
                            if (ch == 1 && ex[1] > VIEW_Y) returned += k * (1 - fo) // mesure de la taille : sans l'absorption de la couleur
                            k *= fo
                        }
                        // la part réfléchie continue dans la pierre
                        val dd = 2 * ci
                        dir = doubleArrayOf(dx - dd * pl.nx, dy - dd * pl.ny, dz - dd * pl.nz)
                        b++
                    }
                    out[ch] = col
                }
                if (linearOut != null) {
                    val i = (py * size + px) * 3
                    linearOut[i] += out[0] / ns; linearOut[i + 1] += out[1] / ns; linearOut[i + 2] += out[2] / ns
                }
                entered++
                ar += tone(out[0]); ag += tone(out[1]); ab += tone(out[2])
            }
            val r = (ar / ns * 255).roundToInt().coerceIn(0, 255)
            val g = (ag / ns * 255).roundToInt().coerceIn(0, 255)
            val bl = (ab / ns * 255).roundToInt().coerceIn(0, 255)
            pixels[py * size + px] = (0xFF shl 24) or (r shl 16) or (g shl 8) or bl
        }
        return FacetRenderResult(size, pixels, if (entered > 0) returned / entered else 0.0, returned, entered)
    }

    /** Courbe d'affichage : douce dans les hautes lumières. */
    private fun tone(v: Double) = min(1.0, v.coerceAtLeast(0.0).pow(0.85))
}
