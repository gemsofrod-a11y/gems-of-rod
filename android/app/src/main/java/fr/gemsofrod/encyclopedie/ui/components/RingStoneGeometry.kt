package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.ui.geometry.Offset
import fr.gemsofrod.encyclopedie.data.RingCutShape
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Géométrie des tailles de pierre sous forme de polygones, dans le même
 * repère normalisé -1..1 que [RingStoneShapes] (x vers la droite, y vers le
 * bas). Les contours sont échantillonnés une fois pour toutes : le rendu
 * facetté, les griffes et tous les sertissages s'appuient sur ce même
 * contour, ce qui leur permet de suivre exactement la forme et l'orientation
 * de la pierre (demande de Sébastien du 14/11/2026 : griffes qui tournent
 * avec la pierre, sertissages qui épousent sa forme).
 */
internal object RingStoneGeometry {

    /** Famille de facettage : brillant (étoile, table octogonale) ou à gradins. */
    enum class FacetKind { BRILLIANT, STEP }

    /** Griffe : direction (repère normalisé) vers laquelle elle se pose sur le contour. */
    data class Prong(val dx: Float, val dy: Float, val style: ProngStyle = ProngStyle.ROUND)

    enum class ProngStyle { ROUND, V_TIP, CENTER_NOTCH }

    private fun cubic(p0: Offset, p1: Offset, p2: Offset, p3: Offset, n: Int, out: MutableList<Offset>) {
        for (i in 1..n) {
            val t = i.toFloat() / n
            val mt = 1f - t
            out += Offset(
                mt * mt * mt * p0.x + 3 * mt * mt * t * p1.x + 3 * mt * t * t * p2.x + t * t * t * p3.x,
                mt * mt * mt * p0.y + 3 * mt * mt * t * p1.y + 3 * mt * t * t * p2.y + t * t * t * p3.y
            )
        }
    }

    private fun quad(p0: Offset, p1: Offset, p2: Offset, n: Int, out: MutableList<Offset>) {
        for (i in 1..n) {
            val t = i.toFloat() / n
            val mt = 1f - t
            out += Offset(
                mt * mt * p0.x + 2 * mt * t * p1.x + t * t * p2.x,
                mt * mt * p0.y + 2 * mt * t * p1.y + t * t * p2.y
            )
        }
    }

    private fun ellipse(): List<Offset> = List(96) { i ->
        val a = -PI / 2 + i / 96.0 * 2 * PI
        Offset(cos(a).toFloat(), sin(a).toFloat())
    }

    private fun roundedRect(c: Float): List<Offset> {
        val o = mutableListOf(Offset(-1 + c, -1f), Offset(1 - c, -1f))
        quad(Offset(1 - c, -1f), Offset(1f, -1f), Offset(1f, -1 + c), 10, o)
        o += Offset(1f, 1 - c)
        quad(Offset(1f, 1 - c), Offset(1f, 1f), Offset(1 - c, 1f), 10, o)
        o += Offset(-1 + c, 1f)
        quad(Offset(-1 + c, 1f), Offset(-1f, 1f), Offset(-1f, 1 - c), 10, o)
        o += Offset(-1f, -1 + c)
        quad(Offset(-1f, -1 + c), Offset(-1f, -1f), Offset(-1 + c, -1f), 10, o)
        o.removeAt(o.lastIndex)
        return o
    }

    private fun stepRect(c: Float): List<Offset> = listOf(
        Offset(-1 + c, -1f), Offset(1 - c, -1f), Offset(1f, -1 + c), Offset(1f, 1 - c),
        Offset(1 - c, 1f), Offset(-1 + c, 1f), Offset(-1f, 1 - c), Offset(-1f, -1 + c)
    )

    private fun plainRect(): List<Offset> = listOf(Offset(-1f, -1f), Offset(1f, -1f), Offset(1f, 1f), Offset(-1f, 1f))

    private fun marquise(): List<Offset> {
        val o = mutableListOf(Offset(0f, -1f))
        cubic(Offset(0f, -1f), Offset(0.55f, -1f), Offset(1f, -0.45f), Offset(1f, 0f), 16, o)
        cubic(Offset(1f, 0f), Offset(1f, 0.45f), Offset(0.55f, 1f), Offset(0f, 1f), 16, o)
        cubic(Offset(0f, 1f), Offset(-0.55f, 1f), Offset(-1f, 0.45f), Offset(-1f, 0f), 16, o)
        cubic(Offset(-1f, 0f), Offset(-1f, -0.45f), Offset(-0.55f, -1f), Offset(0f, -1f), 16, o)
        o.removeAt(o.lastIndex)
        return o
    }

    private fun poire(): List<Offset> {
        val o = mutableListOf(Offset(0f, -1f))
        cubic(Offset(0f, -1f), Offset(0.62f, -0.82f), Offset(1f, -0.1f), Offset(1f, 0.32f), 16, o)
        cubic(Offset(1f, 0.32f), Offset(1f, 0.74f), Offset(0.5f, 1f), Offset(0f, 1f), 16, o)
        cubic(Offset(0f, 1f), Offset(-0.5f, 1f), Offset(-1f, 0.74f), Offset(-1f, 0.32f), 16, o)
        cubic(Offset(-1f, 0.32f), Offset(-1f, -0.1f), Offset(-0.62f, -0.82f), Offset(0f, -1f), 16, o)
        o.removeAt(o.lastIndex)
        return o
    }

    private fun briolette(): List<Offset> {
        val o = mutableListOf(Offset(0f, -1f))
        cubic(Offset(0f, -1f), Offset(0.42f, -1.05f), Offset(1.05f, -0.28f), Offset(0.72f, 0.42f), 16, o)
        cubic(Offset(0.72f, 0.42f), Offset(0.5f, 0.9f), Offset(-0.5f, 0.9f), Offset(-0.72f, 0.42f), 16, o)
        cubic(Offset(-0.72f, 0.42f), Offset(-1.05f, -0.28f), Offset(-0.42f, -1.05f), Offset(0f, -1f), 16, o)
        o.removeAt(o.lastIndex)
        return o
    }

    private fun coeur(): List<Offset> {
        val o = mutableListOf(Offset(0f, 0.78f))
        cubic(Offset(0f, 0.78f), Offset(-1f, 0.12f), Offset(-1f, -0.68f), Offset(0f, -0.28f), 24, o)
        cubic(Offset(0f, -0.28f), Offset(1f, -0.68f), Offset(1f, 0.12f), Offset(0f, 0.78f), 24, o)
        o.removeAt(o.lastIndex)
        return o
    }

    private fun kite(): List<Offset> = listOf(Offset(0f, -1f), Offset(0.85f, -0.1f), Offset(0f, 1f), Offset(-0.85f, -0.1f))

    private fun trillion(): List<Offset> {
        val o = mutableListOf(Offset(0f, -1f))
        quad(Offset(0f, -1f), Offset(1.05f, 0.35f), Offset(1f, 0.75f), 14, o)
        quad(Offset(1f, 0.75f), Offset(0f, 0.55f), Offset(-1f, 0.75f), 14, o)
        quad(Offset(-1f, 0.75f), Offset(-1.05f, 0.35f), Offset(0f, -1f), 14, o)
        o.removeAt(o.lastIndex)
        return o
    }

    private val outlines: Map<RingCutShape, List<Offset>> = RingCutShape.entries.associateWith { shape ->
        when (shape) {
            RingCutShape.RONDE, RingCutShape.OVALE -> ellipse()
            RingCutShape.COUSSIN -> roundedRect(0.42f)
            RingCutShape.PRINCESSE, RingCutShape.BAGUETTE -> plainRect()
            RingCutShape.ASSCHER -> stepRect(0.16f)
            RingCutShape.EMERAUDE_CUT -> stepRect(0.24f)
            RingCutShape.RADIANT -> stepRect(0.12f)
            RingCutShape.MARQUISE -> marquise()
            RingCutShape.POIRE -> poire()
            RingCutShape.BRIOLETTE -> briolette()
            RingCutShape.COEUR -> coeur()
            RingCutShape.KITE -> kite()
            RingCutShape.TRILLION -> trillion()
        }
    }

    fun outline(shape: RingCutShape): List<Offset> = outlines.getValue(shape)

    fun facetKind(shape: RingCutShape): FacetKind = when (shape) {
        RingCutShape.BAGUETTE, RingCutShape.ASSCHER, RingCutShape.EMERAUDE_CUT, RingCutShape.KITE -> FacetKind.STEP
        else -> FacetKind.BRILLIANT
    }

    /** Griffes rondes sur les formes arrondies, en V sur les pointes et les coins. */
    fun prongs(shape: RingCutShape): List<Prong> {
        val diag = listOf(Prong(1f, 1f), Prong(-1f, 1f), Prong(1f, -1f), Prong(-1f, -1f))
        val diagV = diag.map { it.copy(style = ProngStyle.V_TIP) }
        return when (shape) {
            RingCutShape.RONDE, RingCutShape.OVALE, RingCutShape.COUSSIN,
            RingCutShape.ASSCHER, RingCutShape.EMERAUDE_CUT, RingCutShape.RADIANT -> diag
            RingCutShape.PRINCESSE, RingCutShape.BAGUETTE -> diagV
            RingCutShape.MARQUISE -> listOf(
                Prong(0f, -1f, ProngStyle.V_TIP), Prong(0f, 1f, ProngStyle.V_TIP),
                Prong(1f, 0.35f), Prong(-1f, 0.35f), Prong(1f, -0.35f), Prong(-1f, -0.35f)
            )
            RingCutShape.POIRE -> listOf(
                Prong(0f, -1f, ProngStyle.V_TIP), Prong(1f, 0.35f), Prong(-1f, 0.35f), Prong(0.55f, 1f), Prong(-0.55f, 1f)
            )
            RingCutShape.BRIOLETTE -> listOf(Prong(0f, -1f, ProngStyle.V_TIP), Prong(1f, 0.4f), Prong(-1f, 0.4f))
            RingCutShape.COEUR -> listOf(
                Prong(0f, 1f, ProngStyle.V_TIP), Prong(1f, -0.2f), Prong(-1f, -0.2f), Prong(0f, -0.3f, ProngStyle.CENTER_NOTCH)
            )
            RingCutShape.KITE -> listOf(
                Prong(0f, -1f, ProngStyle.V_TIP), Prong(1f, -0.1f, ProngStyle.V_TIP),
                Prong(0f, 1f, ProngStyle.V_TIP), Prong(-1f, -0.1f, ProngStyle.V_TIP)
            )
            RingCutShape.TRILLION -> listOf(
                Prong(0f, -1f, ProngStyle.V_TIP), Prong(1f, 0.8f, ProngStyle.V_TIP), Prong(-1f, 0.8f, ProngStyle.V_TIP)
            )
        }
    }

    /** Point du contour le plus avancé dans la direction (dx, dy). */
    fun supportPoint(poly: List<Offset>, dx: Float, dy: Float): Offset =
        poly.maxBy { it.x * dx + it.y * dy }

    /** Centre de gravité approché (sert de centre aux formes non centrées : cœur, trillion…). */
    fun centroid(poly: List<Offset>): Offset {
        var x = 0f
        var y = 0f
        poly.forEach { x += it.x; y += it.y }
        return Offset(x / poly.size, y / poly.size)
    }

    /**
     * Rééchantillonne [poly] en [n] points répartis par angle autour de
     * l'origine (intersection rayon / contour), en partant du haut.
     */
    fun byAngle(poly: List<Offset>, n: Int, phase: Double = 0.0): List<Offset> = List(n) { i ->
        val a = -PI / 2 + phase + i.toDouble() / n * 2 * PI
        val dx = cos(a).toFloat()
        val dy = sin(a).toFloat()
        var best = Offset.Zero
        var bestT = Float.MAX_VALUE
        for (j in poly.indices) {
            val p = poly[j]
            val q = poly[(j + 1) % poly.size]
            val ex = q.x - p.x
            val ey = q.y - p.y
            val den = dx * ey - dy * ex
            if (abs(den) < 1e-9f) continue
            val t = (p.x * ey - p.y * ex) / den
            val s = (p.x * dy - p.y * dx) / den
            if (t > 0f && s >= -1e-5f && s <= 1f + 1e-5f && t < bestT) {
                bestT = t
                best = Offset(dx * t, dy * t)
            }
        }
        best
    }
}
