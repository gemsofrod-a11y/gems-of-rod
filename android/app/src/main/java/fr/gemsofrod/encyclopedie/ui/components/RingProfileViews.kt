package fr.gemsofrod.encyclopedie.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import fr.gemsofrod.encyclopedie.data.RingConfiguration
import fr.gemsofrod.encyclopedie.data.RingSertissage
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Vues de profil de la bague pour la fiche de simulation (demande de
 * Sébastien du 14/11/2026) : vue de face (dans l'axe du doigt, l'anneau
 * entier et la pierre de profil) et vue de côté (le jonc par la tranche,
 * les pierres annexes alignées sur l'épaule), avec cotes en millimètres
 * calculées depuis la composition. Schémas cotés, pas un rendu 3D.
 */
private const val INNER_DIAMETER_MM = 17.3f

private fun shankThicknessMm(grams: Double) = 1.5f + grams.toFloat() * 0.06f
private fun bandWidthMm(grams: Double) = 1.8f + grams.toFloat() * 0.12f

private fun formatMm(value: Float): String = String.format(Locale.FRANCE, "%.1f mm", value)

private fun metalBrush(x0: Float, y0: Float, x1: Float, y1: Float, start: Color, end: Color) =
    Brush.linearGradient(listOf(start, end, start), start = Offset(x0, y0), end = Offset(x1, y1))

/** Profil d'une pierre (couronne au-dessus du rondiste, pavillon en dessous), centrée en x, rondiste en y. */
private fun DrawScope.drawStoneProfile(x: Float, y: Float, width: Float, color: Color, step: Boolean): Pair<Float, Float> {
    val crown = width * (if (step) 0.12f else 0.16f)
    val pav = width * (if (step) 0.38f else 0.43f)
    val table = width * (if (step) 0.62f else 0.55f)
    val edge = lerp(color, Color.Black, 0.55f)
    val pavilion = Path().apply {
        moveTo(x - width / 2f, y)
        lineTo(x + width / 2f, y)
        if (step) {
            lineTo(x + width * 0.18f, y + pav)
            lineTo(x - width * 0.18f, y + pav)
        } else {
            lineTo(x, y + pav)
        }
        close()
    }
    drawPath(
        pavilion,
        Brush.linearGradient(
            listOf(lerp(color, Color.Black, 0.15f), lerp(color, Color.White, 0.25f), lerp(color, Color.Black, 0.4f)),
            start = Offset(x - width / 2f, y), end = Offset(x + width / 2f, y + pav)
        )
    )
    drawPath(pavilion, edge, style = Stroke(width = 1f))
    for (f in floatArrayOf(-0.3f, -0.12f, 0.12f, 0.3f)) {
        val endX = if (step) x + width * 0.18f * kotlin.math.sign(f) else x
        drawLine(edge.copy(alpha = 0.6f), Offset(x + width * f, y), Offset(endX, y + pav), strokeWidth = 1f)
    }
    val crownPath = Path().apply {
        moveTo(x - width / 2f, y)
        lineTo(x - table / 2f, y - crown)
        lineTo(x + table / 2f, y - crown)
        lineTo(x + width / 2f, y)
        close()
    }
    drawPath(
        crownPath,
        Brush.linearGradient(
            listOf(lerp(color, Color.White, 0.55f), lerp(color, Color.Black, 0.15f)),
            start = Offset(x - width / 2f, y - crown), end = Offset(x + width / 2f, y)
        )
    )
    drawPath(crownPath, edge, style = Stroke(width = 1f))
    for (f in floatArrayOf(-0.42f, -0.2f, 0.2f, 0.42f)) {
        drawLine(edge.copy(alpha = 0.6f), Offset(x + width * f * 0.62f, y - crown), Offset(x + width * f * 1.1f, y), strokeWidth = 1f)
    }
    drawRect(lerp(color, Color.Black, 0.3f), topLeft = Offset(x - width / 2f, y - 1f), size = androidx.compose.ui.geometry.Size(width, 2f))
    return crown to pav
}

/** Les deux griffes visibles de profil, qui montent du panier et recourbent sur la couronne. */
private fun DrawScope.drawProfileProngs(x: Float, y: Float, width: Float, crown: Float, pav: Float, start: Color, end: Color) {
    val lw = max(2.5f, width * 0.07f)
    for (f in floatArrayOf(-0.62f, 0.62f)) {
        val px = x + f * width / 2f
        val path = Path().apply {
            moveTo(px, y + pav * 1.05f)
            lineTo(px, y - crown * 0.15f)
            quadraticBezierTo(px, y - crown * 0.55f, px - kotlin.math.sign(f) * width * 0.06f, y - crown * 0.55f)
        }
        drawPath(path, metalBrush(px - lw, 0f, px + lw, 0f, start, end), style = Stroke(width = lw, cap = StrokeCap.Round))
    }
}

/** Panier (corbeille) sous la pierre, qui la relie au jonc. */
private fun DrawScope.drawBasket(x: Float, girdleY: Float, width: Float, baseHalf: Float, baseY: Float, start: Color, end: Color) {
    val path = Path().apply {
        moveTo(x - width * 0.42f, girdleY + width * 0.12f)
        lineTo(x + width * 0.42f, girdleY + width * 0.12f)
        lineTo(x + baseHalf, baseY)
        lineTo(x - baseHalf, baseY)
        close()
    }
    drawPath(path, metalBrush(x - width / 2f, 0f, x + width / 2f, 0f, start, end), style = Stroke(width = max(2f, width * 0.05f)))
}

private fun DrawScope.drawCentralSetting(
    config: RingConfiguration, x: Float, girdleY: Float, width: Float, crown: Float, pav: Float
) {
    val start = config.metal.gradientStart
    val end = config.metal.gradientEnd
    when (config.central.sertissage) {
        RingSertissage.GRIFFES -> drawProfileProngs(x, girdleY, width, crown, pav, start, end)
        RingSertissage.CLOS, RingSertissage.DEMI_CLOS, RingSertissage.RAS, RingSertissage.RAIL -> {
            val h = if (config.central.sertissage == RingSertissage.RAS) pav * 0.35f else pav * 0.7f
            drawRect(
                metalBrush(x - width / 2f, 0f, x + width / 2f, 0f, start, end),
                topLeft = Offset(x - width * 0.54f, girdleY - crown * 0.35f),
                size = androidx.compose.ui.geometry.Size(width * 1.08f, h + crown * 0.35f)
            )
        }
        else -> Unit
    }
}

/** Cote : trait pointillé, deux butées et la valeur. */
private fun DrawScope.drawDimension(a: Offset, b: Offset, label: String, vertical: Boolean, color: Color, textSizePx: Float) {
    drawLine(color, a, b, strokeWidth = 1.2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f)))
    for (p in listOf(a, b)) {
        if (vertical) drawLine(color, Offset(p.x - 6f, p.y), Offset(p.x + 6f, p.y), strokeWidth = 1.2f)
        else drawLine(color, Offset(p.x, p.y - 6f), Offset(p.x, p.y + 6f), strokeWidth = 1.2f)
    }
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color.toArgb()
        textSize = textSizePx
        textAlign = Paint.Align.CENTER
        typeface = Typeface.SANS_SERIF
    }
    val canvas = drawContext.canvas.nativeCanvas
    if (vertical) {
        canvas.save()
        canvas.translate(a.x - 10f, (a.y + b.y) / 2f)
        canvas.rotate(-90f)
        canvas.drawText(label, 0f, 0f, paint)
        canvas.restore()
    } else {
        canvas.drawText(label, (a.x + b.x) / 2f, a.y - 10f, paint)
    }
}

/** Vue de face : le doigt entre dans la feuille ; l'anneau entier, la pierre de profil. */
internal fun DrawScope.drawRingFrontView(config: RingConfiguration, labelColor: Color, textSizePx: Float) {
    val c = config.central
    val (ex, _) = stoneExtentsMm(c.cut, c.widthMm, c.heightMm, c.orientationDeg)
    val thick = shankThicknessMm(config.gramsMetal)
    val outer = INNER_DIAMETER_MM + 2f * thick
    val totalH = outer + ex * 0.6f + 2.5f
    val s = min(size.width * 0.6f / outer, size.height * 0.74f / totalH)
    val cx = size.width / 2f
    val cy = size.height * 0.56f + ex * 0.15f * s
    val start = config.metal.gradientStart
    val end = config.metal.gradientEnd
    val r = outer / 2f * s
    val ri = INNER_DIAMETER_MM / 2f * s

    val ring = Path().apply {
        addOval(androidx.compose.ui.geometry.Rect(cx - r, cy - r, cx + r, cy + r))
        addOval(androidx.compose.ui.geometry.Rect(cx - ri, cy - ri, cx + ri, cy + ri))
        fillType = androidx.compose.ui.graphics.PathFillType.EvenOdd
    }
    drawPath(ring, metalBrush(cx - r, cy - r, cx + r, cy + r, start, end))
    drawArc(
        Color.White.copy(alpha = 0.35f), startAngle = 202f, sweepAngle = 136f, useCenter = false,
        topLeft = Offset(cx - (r + ri) / 2f, cy - (r + ri) / 2f),
        size = androidx.compose.ui.geometry.Size(r + ri, r + ri),
        style = Stroke(width = (r - ri) * 0.25f, cap = StrokeCap.Round)
    )

    val topY = cy - r
    val width = ex * s
    val girdleY = topY - width * 0.5f
    drawBasket(cx, girdleY, width, width * 0.2f, topY + 2f, start, end)

    // pierres annexes sur les épaules, vues de profil et inclinées avec le jonc
    val side = config.cote
    if (side.nombre > 0) {
        val sideW = side.stone.widthMm.toFloat() * s
        val angles = sideStoneAngles(config, ex, outer / 2f)
        for (sign in intArrayOf(-1, 1)) {
            for (a in angles) {
                val px = cx + sign * sin(a) * r
                val py = cy - cos(a) * r
                translate(px, py) {
                    rotate(sign * a * 180f / PI.toFloat(), pivot = Offset.Zero) {
                        drawStoneProfile(0f, -sideW * 0.12f, sideW, side.stone.color, false)
                    }
                }
            }
        }
    }

    val step = RingStoneGeometry.facetKind(c.cut) == RingStoneGeometry.FacetKind.STEP
    val (crown, pav) = drawStoneProfile(cx, girdleY, width, c.color, step)
    drawCentralSetting(config, cx, girdleY, width, crown, pav)

    drawDimension(Offset(cx - width / 2f, girdleY - crown - 18f), Offset(cx + width / 2f, girdleY - crown - 18f), formatMm(ex), false, labelColor, textSizePx)
    drawDimension(Offset(cx - r - 26f, topY), Offset(cx - r - 26f, cy + r), formatMm(outer), true, labelColor, textSizePx)
    drawDimension(Offset(cx + r + 26f, girdleY - crown), Offset(cx + r + 26f, topY), formatMm((topY - girdleY + crown) / s), true, labelColor, textSizePx)
}

/** Vue de côté : le jonc par la tranche, la pierre dans sa longueur, les pierres annexes sur l'épaule. */
internal fun DrawScope.drawRingSideView(config: RingConfiguration, labelColor: Color, textSizePx: Float) {
    val c = config.central
    val (ex, ey) = stoneExtentsMm(c.cut, c.widthMm, c.heightMm, c.orientationDeg)
    val thick = shankThicknessMm(config.gramsMetal)
    val outer = INNER_DIAMETER_MM + 2f * thick
    val bw = bandWidthMm(config.gramsMetal)
    val s = min(size.width * 0.6f / max(outer * 0.55f, ey * 1.4f), size.height * 0.74f / (outer + ey * 0.6f + 2.5f))
    val cx = size.width / 2f
    val cy = size.height * 0.56f + ey * 0.15f * s
    val start = config.metal.gradientStart
    val end = config.metal.gradientEnd
    val r = outer / 2f * s
    val topY = cy - r
    val botY = cy + r

    val side = config.cote
    val sideW = if (side.nombre > 0) side.stone.widthMm.toFloat() * s else 0f
    val angles = if (side.nombre > 0) sideStoneAngles(config, ex, outer / 2f) else emptyList()
    val shoulderW = if (angles.isNotEmpty()) max(bw * s, sideW * 1.45f) else bw * s
    val shoulderEnd = if (angles.isNotEmpty()) min(PI.toFloat() * 0.42f, angles.last() + 0.25f) else 0.35f
    val yEnd = cy - r * cos(shoulderEnd)
    fun half(y: Float): Float {
        if (y >= yEnd) return bw * s / 2f
        val t = (yEnd - y) / (yEnd - topY)
        return bw * s / 2f + (shoulderW / 2f - bw * s / 2f) * min(1f, t * 1.6f)
    }
    val shank = Path().apply {
        val n = 40
        for (i in 0..n) {
            val y = topY + (botY - topY) * i / n
            if (i == 0) moveTo(cx - half(y), y) else lineTo(cx - half(y), y)
        }
        for (i in n downTo 0) {
            val y = topY + (botY - topY) * i / n
            lineTo(cx + half(y), y)
        }
        close()
    }
    drawPath(shank, metalBrush(cx - shoulderW / 2f, 0f, cx + shoulderW / 2f, 0f, start, end))

    // pierres du côté visible, de la plus basse à la plus haute, vues de biais (aplaties près du sommet)
    for (a in angles.reversed()) {
        val y = cy - r * cos(a)
        val k = max(0.3f, sin(a))
        val frame = StoneFrame(Offset(cx, y), sideW, sideW * k, 0f)
        drawFacetedStone(side.stone.cut, frame, side.stone.color)
        if (side.stone.sertissage == RingSertissage.PAVE || side.stone.sertissage == RingSertissage.GRAIN) {
            for (fx in floatArrayOf(-1f, 1f)) for (fy in floatArrayOf(-1f, 1f)) {
                drawMetalBead(Offset(cx + fx * sideW * 0.62f, y + fy * sideW * k * 0.42f), max(1.2f, sideW * 0.08f), start, end)
            }
        } else {
            drawSettingOnStone(side.stone.sertissage, side.stone.cut, frame, start, end)
        }
    }

    val width = ey * s
    val girdleY = topY - width * 0.5f
    drawBasket(cx, girdleY, width, bw * 0.6f * s, topY + 2f, start, end)
    val step = RingStoneGeometry.facetKind(c.cut) == RingStoneGeometry.FacetKind.STEP
    val (crown, pav) = drawStoneProfile(cx, girdleY, width, c.color, step)
    drawCentralSetting(config, cx, girdleY, width, crown, pav)

    drawDimension(Offset(cx - width / 2f, girdleY - crown - 18f), Offset(cx + width / 2f, girdleY - crown - 18f), formatMm(ey), false, labelColor, textSizePx)
    drawDimension(Offset(cx - bw / 2f * s, botY + 26f), Offset(cx + bw / 2f * s, botY + 26f), formatMm(bw), false, labelColor, textSizePx)
}

/** Dimensions utiles à la fiche : (diamètre extérieur, largeur du jonc) en mm. */
internal fun ringOuterDiameterMm(config: RingConfiguration): Float = INNER_DIAMETER_MM + 2f * shankThicknessMm(config.gramsMetal)
internal fun ringBandWidthMm(config: RingConfiguration): Float = bandWidthMm(config.gramsMetal)
