package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import fr.gemsofrod.encyclopedie.data.RingCutShape
import fr.gemsofrod.encyclopedie.data.RingSertissage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Rendu « facetté » des pierres du compositeur de bague et de leurs
 * sertissages. Tout est calculé dans le repère de la pierre (contour de
 * [RingStoneGeometry], mis à l'échelle de sa largeur/hauteur puis pivoté de
 * son orientation) : griffes, clos, rails… suivent donc la forme réelle et
 * tournent avec elle. La lumière, elle, reste fixe à l'écran (en haut à
 * gauche), si bien que les facettes changent de teinte quand on fait
 * tourner la pierre, comme une vraie gemme.
 */

/** Passage du repère normalisé de la pierre vers le canevas. */
internal class StoneFrame(
    val center: Offset,
    val widthPx: Float,
    val heightPx: Float,
    rotationDeg: Float
) {
    private val c = cos(rotationDeg * PI / 180.0).toFloat()
    private val s = sin(rotationDeg * PI / 180.0).toFloat()
    val minSide: Float get() = min(widthPx, heightPx)

    fun map(p: Offset): Offset {
        val x = p.x * widthPx / 2f
        val y = p.y * heightPx / 2f
        return Offset(center.x + x * c - y * s, center.y + x * s + y * c)
    }
}

private val LIGHT = Offset(-0.6f, -0.8f)

private fun polygonPath(points: List<Offset>): Path = Path().apply {
    points.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
    close()
}

private fun shade(base: Color, nx: Float, ny: Float, bias: Float): Color {
    val l = hypot(nx, ny).takeIf { it > 0f } ?: 1f
    val d = (nx * LIGHT.x + ny * LIGHT.y) / l
    val k = d * 0.45f + bias
    return if (k >= 0f) lerp(base, Color.White, min(k, 0.85f)) else lerp(base, Color.Black, min(-k, 0.7f))
}

private fun meanOf(points: List<Offset>): Offset {
    var x = 0f
    var y = 0f
    points.forEach { x += it.x; y += it.y }
    return Offset(x / points.size, y / points.size)
}

/**
 * Pierre facettée : couronne d'un brillant (table, étoiles, losanges,
 * demi-facettes) ou gradins d'une taille à degrés, avec ombre portée,
 * rondiste souligné et un éclat.
 */
internal fun DrawScope.drawFacetedStone(shape: RingCutShape, frame: StoneFrame, color: Color) {
    val raw = RingStoneGeometry.outline(shape)
    val c0 = RingStoneGeometry.centroid(raw)
    val poly = raw.map { Offset(it.x - c0.x, it.y - c0.y) }
    val map = { p: Offset -> frame.map(Offset(p.x + c0.x, p.y + c0.y)) }
    val center = map(Offset.Zero)
    val outline = poly.map(map)
    val edgeColor = lerp(color, Color.Black, 0.45f).copy(alpha = 0.55f)
    val edgeWidth = max(0.6f, frame.minSide * 0.012f)

    // ombre portée (deux voiles décalés, sans flou natif)
    val outlinePath = polygonPath(outline)
    val drop = frame.minSide * 0.05f
    drawPath(polygonPath(outline.map { Offset(it.x, it.y + drop * 1.4f) }), Color.Black.copy(alpha = 0.18f))
    drawPath(polygonPath(outline.map { Offset(it.x, it.y + drop * 0.7f) }), Color.Black.copy(alpha = 0.22f))
    drawPath(outlinePath, lerp(color, Color.Black, 0.35f))

    fun facet(points: List<Offset>, bias: Float) {
        val n = meanOf(points) - center
        val path = polygonPath(points)
        drawPath(path, shade(color, n.x, n.y, bias))
        drawPath(path, edgeColor, style = Stroke(width = edgeWidth))
    }

    val table: List<Offset>
    if (RingStoneGeometry.facetKind(shape) == RingStoneGeometry.FacetKind.STEP) {
        val ringBase = if (poly.size <= 8) poly else RingStoneGeometry.byAngle(poly, 16)
        val levels = floatArrayOf(1f, 0.84f, 0.68f, 0.54f)
        val rings = levels.map { k -> ringBase.map { map(Offset(it.x * k, it.y * k)) } }
        for (l in 0 until levels.size - 1) {
            val a = rings[l]
            val b = rings[l + 1]
            val bias = when (l) { 1 -> 0.12f; 2 -> -0.05f; else -> 0f }
            for (i in a.indices) {
                val j = (i + 1) % a.size
                facet(listOf(a[i], a[j], b[j], b[i]), bias)
            }
        }
        table = rings.last()
    } else {
        val n = 16
        val half = n / 2
        val girdleNorm = RingStoneGeometry.byAngle(poly, n)
        val g = girdleNorm.map(map)
        val t = RingStoneGeometry.byAngle(poly, half).map { map(Offset(it.x * 0.55f, it.y * 0.55f)) }
        val p = List(half) { k -> val q = girdleNorm[2 * k + 1]; map(Offset(q.x * 0.8f, q.y * 0.8f)) }
        for (k in 0 until half) {
            val k1 = (k + 1) % half
            val km = (k - 1 + half) % half
            facet(listOf(t[k], t[k1], p[k]), 0.22f)
            facet(listOf(t[k], p[km], g[2 * k], p[k]), if (k % 2 == 1) 0.06f else -0.06f)
            facet(listOf(p[k], g[2 * k], g[2 * k + 1]), -0.18f)
            facet(listOf(p[k], g[2 * k + 1], g[(2 * k + 2) % n]), 0.02f)
        }
        table = t
    }
    // table : dégradé de lumière
    val tablePath = polygonPath(table)
    val tc = meanOf(table)
    val span = frame.minSide * 0.35f
    drawPath(
        tablePath,
        Brush.linearGradient(
            colors = listOf(lerp(color, Color.White, 0.45f), color, lerp(color, Color.Black, 0.25f)),
            start = Offset(tc.x - span, tc.y - span),
            end = Offset(tc.x + span, tc.y + span)
        )
    )
    drawPath(tablePath, edgeColor, style = Stroke(width = edgeWidth))
    // rondiste
    drawPath(outlinePath, lerp(color, Color.Black, 0.55f).copy(alpha = 0.9f), style = Stroke(width = max(0.8f, frame.minSide * 0.02f)))
    // éclat
    val sp = map(Offset(-0.32f, -0.38f))
    val r = frame.minSide * 0.13f
    val star = Path().apply {
        moveTo(sp.x, sp.y - r)
        quadraticBezierTo(sp.x, sp.y, sp.x + r * 0.35f, sp.y)
        quadraticBezierTo(sp.x, sp.y, sp.x, sp.y + r)
        quadraticBezierTo(sp.x, sp.y, sp.x - r * 0.35f, sp.y)
        quadraticBezierTo(sp.x, sp.y, sp.x, sp.y - r)
        close()
    }
    drawPath(star, Color.White.copy(alpha = 0.9f))
}

/** Petite bille de métal avec reflet (griffe ronde, grain, pavé). */
internal fun DrawScope.drawMetalBead(center: Offset, radius: Float, metalStart: Color, metalEnd: Color) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(lerp(metalEnd, Color.White, 0.6f), metalEnd, metalStart),
            center = Offset(center.x - radius * 0.35f, center.y - radius * 0.35f),
            radius = radius * 1.3f
        ),
        radius = radius,
        center = center
    )
}

/**
 * Sertissage dessiné sur le contour réel de la pierre, dans son repère :
 * il suit la forme et l'orientation.
 */
internal fun DrawScope.drawSettingOnStone(
    sertissage: RingSertissage,
    shape: RingCutShape,
    frame: StoneFrame,
    metalStart: Color,
    metalEnd: Color
) {
    val poly = RingStoneGeometry.outline(shape)
    val m = frame.minSide
    val center = frame.center
    val metalBrush = Brush.linearGradient(
        colors = listOf(metalEnd, metalStart),
        start = Offset(center.x - frame.widthPx, center.y - frame.heightPx),
        end = Offset(center.x + frame.widthPx, center.y + frame.heightPx)
    )

    fun ring(offsetPx: Float, width: Float, from: Float = 0f, to: Float = 1f) {
        val pts = poly.map(frame::map).map { p ->
            val d = p - center
            val l = d.getDistance().takeIf { it > 0f } ?: 1f
            Offset(p.x + d.x / l * offsetPx, p.y + d.y / l * offsetPx)
        }
        val n = pts.size
        val a = (from * n).toInt()
        val b = kotlin.math.ceil(to * n).toInt()
        val path = Path()
        for (i in a..b) {
            val p = pts[i % n]
            if (i == a) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
        }
        if (from == 0f && to == 1f) path.close()
        drawPath(path, metalBrush, style = Stroke(width = width, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }

    fun along(count: Int, scale: Float): List<Offset> =
        RingStoneGeometry.byAngle(poly, count).map { frame.map(Offset(it.x * scale, it.y * scale)) }

    fun sideBars(offset: Float, extent: Float, width: Float, cap: StrokeCap) {
        for (sx in floatArrayOf(-offset, offset)) {
            val a = frame.map(Offset(sx, -extent))
            val b = frame.map(Offset(sx, extent))
            drawLine(
                brush = Brush.linearGradient(listOf(metalEnd, metalStart), start = a, end = b),
                start = a, end = b, strokeWidth = width, cap = cap
            )
        }
    }

    when (sertissage) {
        RingSertissage.GRIFFES -> {
            val r = m * 0.085f
            for (prong in RingStoneGeometry.prongs(shape)) {
                if (prong.style == RingStoneGeometry.ProngStyle.CENTER_NOTCH) {
                    drawMetalBead(frame.map(Offset(0f, -0.3f)), r * 0.8f, metalStart, metalEnd)
                    continue
                }
                val sp = RingStoneGeometry.supportPoint(poly, prong.dx, prong.dy)
                val p = frame.map(Offset(sp.x * 0.96f, sp.y * 0.96f))
                if (prong.style == RingStoneGeometry.ProngStyle.V_TIP) {
                    val out = p - center
                    val l = out.getDistance().takeIf { it > 0f } ?: 1f
                    val ux = out.x / l
                    val uy = out.y / l
                    val tip = Offset(p.x + ux * r * 0.9f, p.y + uy * r * 0.9f)
                    val back = Offset(p.x - ux * r * 1.1f, p.y - uy * r * 1.1f)
                    val px = -uy
                    val py = ux
                    val path = Path().apply {
                        moveTo(tip.x, tip.y)
                        lineTo(back.x + px * r * 1.1f, back.y + py * r * 1.1f)
                        quadraticBezierTo(p.x - ux * r * 0.2f, p.y - uy * r * 0.2f, back.x - px * r * 1.1f, back.y - py * r * 1.1f)
                        close()
                    }
                    drawPath(
                        path,
                        Brush.radialGradient(
                            listOf(lerp(metalEnd, Color.White, 0.5f), metalEnd, metalStart),
                            center = Offset(p.x - r * 0.3f, p.y - r * 0.3f),
                            radius = r * 1.6f
                        )
                    )
                } else {
                    drawMetalBead(p, r, metalStart, metalEnd)
                }
            }
        }
        RingSertissage.CLOS -> ring(m * 0.07f, m * 0.12f)
        RingSertissage.DEMI_CLOS -> {
            ring(m * 0.07f, m * 0.12f, 0.62f, 0.88f)
            ring(m * 0.07f, m * 0.12f, 0.12f, 0.38f)
        }
        RingSertissage.RAS -> ring(m * 0.025f, m * 0.05f)
        RingSertissage.PAVE -> along(10, 1.13f).forEach { drawMetalBead(it, m * 0.05f, metalStart, metalEnd) }
        RingSertissage.GRAIN -> along(8, 1.06f).forEach { drawMetalBead(it, m * 0.032f, metalStart, metalEnd) }
        RingSertissage.CLUSTER -> along(6, 1.18f).forEach { drawMetalBead(it, m * 0.09f, metalStart, metalEnd) }
        RingSertissage.BRIGHT_CUT -> {
            val inner = along(8, 1.05f)
            val outer = along(8, 1.32f)
            inner.indices.forEach { i ->
                drawLine(metalBrush, inner[i], outer[i], strokeWidth = m * 0.035f, cap = StrokeCap.Round)
            }
        }
        RingSertissage.RAIL -> sideBars(1.16f, 0.95f, m * 0.1f, StrokeCap.Round)
        RingSertissage.BARRETTE -> sideBars(1.22f, 0.7f, m * 0.14f, StrokeCap.Butt)
    }
}

/** Étendue (en mm) d'une pierre vue de dessus, une fois pivotée : (largeur x, longueur y). */
internal fun stoneExtentsMm(shape: RingCutShape, widthMm: Double, heightMm: Double, rotationDeg: Float): Pair<Float, Float> {
    val frame = StoneFrame(Offset.Zero, widthMm.toFloat(), heightMm.toFloat(), rotationDeg)
    val pts = RingStoneGeometry.outline(shape).map(frame::map)
    return (pts.maxOf { it.x } - pts.minOf { it.x }) to (pts.maxOf { it.y } - pts.minOf { it.y })
}
