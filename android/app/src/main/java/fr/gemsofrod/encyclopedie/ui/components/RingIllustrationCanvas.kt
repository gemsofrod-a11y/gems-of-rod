package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import fr.gemsofrod.encyclopedie.data.RingConfiguration
import fr.gemsofrod.encyclopedie.data.RingSertissage
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/** Portée en mm que représente le diamètre extérieur du jonc dessiné — sert
 * d'ancrage pour convertir les dimensions en mm des pierres vers des pixels
 * à la même échelle relative que le jonc, quelle que soit la taille réelle
 * du canevas. */
private const val RING_OUTER_DIAMETER_MM = 22f

/**
 * Aperçu schématique (vue de dessus) d'une bague composée : jonc en
 * anneau (épaisseur liée au grammage de métal), pierre centrale et
 * pierres annexes disposées sur les épaules, avec leur sertissage.
 * Pas un rendu photoréaliste — un schéma pour valider la composition,
 * dans le même esprit que les croquis de [CrystalSystemsScreen] et le
 * dégradé du jonc.
 */
@Composable
fun RingIllustrationCanvas(config: RingConfiguration, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxWidth().aspectRatio(1.15f)) {
        val outerRx = min(size.width, size.height) * 0.40f
        val outerRy = outerRx * 0.46f
        val center = Offset(size.width / 2f, size.height / 2f + outerRy * 0.18f)
        val pxPerMm = (outerRx * 2f) / RING_OUTER_DIAMETER_MM

        val gramsT = (((config.gramsMetal - 1.0) / 14.0).coerceIn(0.0, 1.0)).toFloat()
        val innerRx = outerRx * (0.85f - gramsT * 0.28f)
        val innerRy = outerRy * (0.70f - gramsT * 0.23f)

        drawBand(center, outerRx, outerRy, innerRx, innerRy, config.metal.gradientStart, config.metal.gradientEnd)

        val central = config.central
        val centralWidthPx = (central.widthMm * pxPerMm).toFloat()
        val centralHeightPx = (central.heightMm * pxPerMm).toFloat()
        val centralCenter = Offset(center.x, center.y - outerRy - centralHeightPx * 0.36f)
        val centralRect = Rect(
            centralCenter.x - centralWidthPx / 2f,
            centralCenter.y - centralHeightPx / 2f,
            centralCenter.x + centralWidthPx / 2f,
            centralCenter.y + centralHeightPx / 2f
        )
        drawStoneShape(central.cut, centralRect, SolidColor(central.color), central.orientationDeg)
        drawSertissage(central.sertissage, centralRect, config.metal.gradientEnd)

        val nombre = config.cote.nombre
        if (nombre > 0) {
            val cote = config.cote.stone
            val coteWidthPx = (cote.widthMm * pxPerMm).toFloat()
            val coteHeightPx = (cote.heightMm * pxPerMm).toFloat()
            val gapAngle = (centralWidthPx * 0.42f + coteWidthPx * 0.75f) / outerRx
            val stepAngle = (coteWidthPx * 1.15f) / outerRx * (0.8f + config.cote.espacement)
            val maxAngle = 1.15f
            for (sign in intArrayOf(-1, 1)) {
                for (i in 0 until nombre) {
                    val angle = (gapAngle + i * stepAngle).coerceAtMost(maxAngle) * sign
                    val stoneCenter = Offset(
                        center.x + outerRx * sin(angle),
                        center.y - outerRy * cos(angle)
                    )
                    val rect = Rect(
                        stoneCenter.x - coteWidthPx / 2f,
                        stoneCenter.y - coteHeightPx / 2f,
                        stoneCenter.x + coteWidthPx / 2f,
                        stoneCenter.y + coteHeightPx / 2f
                    )
                    val rotation = angle * (180f / Math.PI.toFloat()) + config.cote.orientationOffsetDeg
                    drawStoneShape(cote.cut, rect, SolidColor(cote.color), rotation)
                    drawSertissage(cote.sertissage, rect, config.metal.gradientEnd)
                }
            }
        }
    }
}

private fun DrawScope.drawBand(
    center: Offset,
    outerRx: Float,
    outerRy: Float,
    innerRx: Float,
    innerRy: Float,
    gradientStart: Color,
    gradientEnd: Color
) {
    val path = Path().apply {
        addOval(Rect(center.x - outerRx, center.y - outerRy, center.x + outerRx, center.y + outerRy))
        addOval(Rect(center.x - innerRx, center.y - innerRy, center.x + innerRx, center.y + innerRy))
        fillType = PathFillType.EvenOdd
    }
    val brush = Brush.linearGradient(
        colors = listOf(gradientStart, gradientEnd, gradientStart),
        start = Offset(center.x - outerRx, center.y - outerRy),
        end = Offset(center.x + outerRx, center.y + outerRy)
    )
    drawPath(path, brush)
}

/** Distribue le rendu du sertissage vers l'extension dédiée à chaque type. */
private fun DrawScope.drawSertissage(sertissage: RingSertissage, rect: Rect, metalColor: Color) {
    when (sertissage) {
        RingSertissage.GRIFFES -> drawProngs(rect, metalColor)
        RingSertissage.CLOS -> drawBezel(rect, metalColor)
        RingSertissage.DEMI_CLOS -> drawHalfBezel(rect, metalColor)
        RingSertissage.PAVE -> drawPaveDots(rect, metalColor)
        RingSertissage.RAIL -> drawChannelRail(rect, metalColor)
        RingSertissage.RAS -> drawFlush(rect, metalColor)
        RingSertissage.BRIGHT_CUT -> drawBrightCut(rect, metalColor)
        RingSertissage.BARRETTE -> drawBarrette(rect, metalColor)
        RingSertissage.CLUSTER -> drawCluster(rect, metalColor)
        RingSertissage.GRAIN -> drawGrain(rect, metalColor)
    }
}

/** Griffes : quatre petites griffes aux points cardinaux du contour de la pierre. */
private fun DrawScope.drawProngs(rect: Rect, metalColor: Color) {
    val r = min(rect.width, rect.height) * 0.09f
    val points = listOf(
        Offset(rect.center.x, rect.top + r * 0.6f),
        Offset(rect.center.x, rect.bottom - r * 0.6f),
        Offset(rect.left + r * 0.6f, rect.center.y),
        Offset(rect.right - r * 0.6f, rect.center.y)
    )
    points.forEach { drawCircle(metalColor, radius = r, center = it) }
}

/** Clos : liseré métallique continu tout autour de la pierre. */
private fun DrawScope.drawBezel(rect: Rect, metalColor: Color) {
    val pad = min(rect.width, rect.height) * 0.08f
    drawOval(
        color = metalColor,
        topLeft = Offset(rect.left - pad, rect.top - pad),
        size = Size(rect.width + pad * 2, rect.height + pad * 2),
        style = Stroke(width = pad * 1.4f)
    )
}

/** Demi-clos : le même liseré, mais seulement sur la moitié haute. */
private fun DrawScope.drawHalfBezel(rect: Rect, metalColor: Color) {
    val pad = min(rect.width, rect.height) * 0.08f
    val path = Path().apply {
        addArc(
            Rect(rect.left - pad, rect.top - pad, rect.right + pad, rect.bottom + pad),
            startAngleDegrees = 200f,
            sweepAngleDegrees = 140f
        )
    }
    drawPath(path, metalColor, style = Stroke(width = pad * 1.4f))
}

/** Pavé : petits grains métalliques réguliers autour du bord de la pierre. */
private fun DrawScope.drawPaveDots(rect: Rect, metalColor: Color) {
    val n = 8
    val rx = rect.width / 2f * 1.12f
    val ry = rect.height / 2f * 1.12f
    val dotR = min(rect.width, rect.height) * 0.045f
    for (i in 0 until n) {
        val angle = (i.toFloat() / n) * (2 * Math.PI.toFloat())
        drawCircle(
            metalColor,
            radius = dotR,
            center = Offset(rect.center.x + rx * cos(angle), rect.center.y + ry * sin(angle))
        )
    }
}

/** Rail (chenal) : deux rails métalliques parallèles encadrant la pierre. */
private fun DrawScope.drawChannelRail(rect: Rect, metalColor: Color) {
    val w = rect.width * 0.06f
    drawLine(metalColor, Offset(rect.left - w, rect.top), Offset(rect.left - w, rect.bottom), strokeWidth = w)
    drawLine(metalColor, Offset(rect.right + w, rect.top), Offset(rect.right + w, rect.bottom), strokeWidth = w)
}

/** Ras (affleurant) : aucune griffe visible, juste un fin trait de tension. */
private fun DrawScope.drawFlush(rect: Rect, metalColor: Color) {
    val pad = min(rect.width, rect.height) * 0.03f
    drawOval(
        color = metalColor,
        topLeft = Offset(rect.left - pad, rect.top - pad),
        size = Size(rect.width + pad * 2, rect.height + pad * 2),
        style = Stroke(width = pad)
    )
}

/** Bright-cut : petites entailles rayonnantes autour de la pierre. */
private fun DrawScope.drawBrightCut(rect: Rect, metalColor: Color) {
    val n = 6
    val rInner = min(rect.width, rect.height) / 2f * 1.05f
    val rOuter = rInner * 1.35f
    for (i in 0 until n) {
        val angle = (i.toFloat() / n) * (2 * Math.PI.toFloat())
        drawLine(
            metalColor,
            Offset(rect.center.x + rInner * cos(angle), rect.center.y + rInner * sin(angle)),
            Offset(rect.center.x + rOuter * cos(angle), rect.center.y + rOuter * sin(angle)),
            strokeWidth = min(rect.width, rect.height) * 0.035f
        )
    }
}

/** Barrette : une fine barre métallique de part et d'autre de la pierre. */
private fun DrawScope.drawBarrette(rect: Rect, metalColor: Color) {
    val barW = rect.width * 0.16f
    drawRect(
        metalColor,
        topLeft = Offset(rect.left - barW * 1.3f, rect.top),
        size = Size(barW, rect.height)
    )
    drawRect(
        metalColor,
        topLeft = Offset(rect.right + barW * 0.3f, rect.top),
        size = Size(barW, rect.height)
    )
}

/** Grappe (cluster) : une petite rosette de grains autour de la pierre. */
private fun DrawScope.drawCluster(rect: Rect, metalColor: Color) {
    val dotR = min(rect.width, rect.height) * 0.14f
    val rx = rect.width / 2f * 0.95f
    val ry = rect.height / 2f * 0.95f
    for (i in 0 until 3) {
        val angle = (i.toFloat() / 3f) * (2 * Math.PI.toFloat()) - Math.PI.toFloat() / 2f
        drawCircle(
            metalColor,
            radius = dotR,
            center = Offset(rect.center.x + rx * cos(angle), rect.center.y + ry * sin(angle))
        )
    }
}

/** Grain : minuscules perles métalliques serrées contre la pierre. */
private fun DrawScope.drawGrain(rect: Rect, metalColor: Color) {
    val n = 5
    val rx = rect.width / 2f * 1.05f
    val ry = rect.height / 2f * 1.05f
    val dotR = min(rect.width, rect.height) * 0.025f
    for (i in 0 until n) {
        val angle = (i.toFloat() / n) * (2 * Math.PI.toFloat())
        drawCircle(
            metalColor,
            radius = dotR,
            center = Offset(rect.center.x + rx * cos(angle), rect.center.y + ry * sin(angle))
        )
    }
}
