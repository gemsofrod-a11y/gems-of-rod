package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import fr.gemsofrod.encyclopedie.data.RingConfiguration
import fr.gemsofrod.encyclopedie.data.RingCutShape
import fr.gemsofrod.encyclopedie.data.RingSertissage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/** Portée en mm que représente le diamètre extérieur du jonc dessiné — sert
 * d'ancrage pour convertir les dimensions en mm des pierres vers des pixels
 * à la même échelle relative que le jonc, quelle que soit la taille réelle
 * du canevas. */
private const val RING_OUTER_DIAMETER_MM = 22f

/**
 * Aperçu de la bague composée, vu de trois quarts : jonc (épaisseur liée au
 * grammage de métal) avec reflet, pierre centrale et pierres annexes
 * facettées sur les épaules, avec des sertissages qui suivent la forme et
 * l'orientation de chaque pierre. Un schéma pour valider la composition,
 * pas un rendu photoréaliste.
 */
@Composable
fun RingIllustrationCanvas(config: RingConfiguration, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxWidth().aspectRatio(1.15f)) {
        drawRingPerspective(config)
    }
}

/** Positions des pierres annexes le long du jonc (angle depuis le sommet, en radians). */
internal fun sideStoneAngles(config: RingConfiguration, centralAlongBandMm: Float, ringRadiusMm: Float): List<Float> {
    val n = config.cote.nombre
    if (n <= 0) return emptyList()
    val side = config.cote.stone
    val gap = (centralAlongBandMm * 0.42f + side.widthMm.toFloat() * 0.75f) / ringRadiusMm
    val step = (side.widthMm.toFloat() * 1.15f) / ringRadiusMm * (0.8f + config.cote.espacement)
    return List(n) { i -> (gap + i * step).coerceAtMost(1.15f) }
}

/** Vue de trois quarts (celle du compositeur et de la vue de dessus de la simulation). */
internal fun DrawScope.drawRingPerspective(config: RingConfiguration) {
    val outerRx = min(size.width, size.height) * 0.40f
    val outerRy = outerRx * 0.46f
    val center = Offset(size.width / 2f, size.height / 2f + outerRy * 0.18f)
    val pxPerMm = (outerRx * 2f) / RING_OUTER_DIAMETER_MM

    val gramsT = (((config.gramsMetal - 1.0) / 14.0).coerceIn(0.0, 1.0)).toFloat()
    val innerRx = outerRx * (0.85f - gramsT * 0.28f)
    val innerRy = outerRy * (0.70f - gramsT * 0.23f)
    val metalStart = config.metal.gradientStart
    val metalEnd = config.metal.gradientEnd

    drawBand(center, outerRx, outerRy, innerRx, innerRy, metalStart, metalEnd)

    val central = config.central
    val centralWidthPx = (central.widthMm * pxPerMm).toFloat()
    val centralHeightPx = (central.heightMm * pxPerMm).toFloat()
    val centralCenter = Offset(center.x, center.y - outerRy - centralHeightPx * 0.36f)

    if (config.cote.nombre > 0) {
        val cote = config.cote.stone
        val coteWidthPx = (cote.widthMm * pxPerMm).toFloat()
        val coteHeightPx = (cote.heightMm * pxPerMm).toFloat()
        val gapAngle = (centralWidthPx * 0.42f + coteWidthPx * 0.75f) / outerRx
        val stepAngle = (coteWidthPx * 1.15f) / outerRx * (0.8f + config.cote.espacement)
        for (sign in intArrayOf(-1, 1)) {
            for (i in 0 until config.cote.nombre) {
                val angle = (gapAngle + i * stepAngle).coerceAtMost(1.15f) * sign
                val stoneCenter = Offset(center.x + outerRx * sin(angle), center.y - outerRy * cos(angle))
                val rotation = angle * (180f / PI.toFloat()) + config.cote.orientationOffsetDeg
                val frame = StoneFrame(stoneCenter, coteWidthPx, coteHeightPx, rotation)
                drawFacetedStone(cote.cut, frame, cote.color)
                drawSettingOnStone(cote.sertissage, cote.cut, frame, metalStart, metalEnd)
            }
        }
    }

    val frame = StoneFrame(centralCenter, centralWidthPx, centralHeightPx, central.orientationDeg)
    drawFacetedStone(central.cut, frame, central.color)
    drawSettingOnStone(central.sertissage, central.cut, frame, metalStart, metalEnd)
}

/**
 * Aperçu isolé d'une seule taille de pierre, sans jonc ni sertissage —
 * utilisé par le quiz pour illustrer « Quelle est cette taille de
 * pierre ? ». Même rendu facetté que le configurateur, sur un fond neutre.
 */
@Composable
fun RingCutPreview(shape: RingCutShape, modifier: Modifier = Modifier) {
    val stoneColor = MaterialTheme.colorScheme.primary
    Canvas(modifier = modifier.fillMaxWidth().aspectRatio(1.4f)) {
        val frame = StoneFrame(Offset(size.width / 2f, size.height / 2f), size.width * 0.5f, size.height * 0.62f, 0f)
        drawFacetedStone(shape, frame, stoneColor)
    }
}

/**
 * Aperçu isolé d'un type de sertissage sur une taille ronde fixe (pour
 * ne pas ajouter une variable de forme à la question) — utilisé par le
 * quiz pour illustrer « Quel est ce type de sertissage ? ».
 */
@Composable
fun RingSertissagePreview(sertissage: RingSertissage, modifier: Modifier = Modifier) {
    val stoneColor = MaterialTheme.colorScheme.primary
    val metalColor = MaterialTheme.colorScheme.onSurfaceVariant
    Canvas(modifier = modifier.fillMaxWidth().aspectRatio(1.4f)) {
        val widthPx = size.width * 0.4f
        val frame = StoneFrame(Offset(size.width / 2f, size.height / 2f), widthPx, widthPx, 0f)
        drawFacetedStone(RingCutShape.RONDE, frame, stoneColor)
        drawSettingOnStone(sertissage, RingCutShape.RONDE, frame, lerp(metalColor, Color.Black, 0.3f), metalColor)
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
    // reflet sur le dessus du jonc et ombre douce sur l'avant
    val midRx = (outerRx + innerRx) / 2f
    val midRy = (outerRy + innerRy) / 2f
    val bandW = outerRx - innerRx
    drawArc(
        color = Color.White.copy(alpha = 0.4f),
        startAngle = 194f, sweepAngle = 152f, useCenter = false,
        topLeft = Offset(center.x - midRx, center.y - midRy),
        size = androidx.compose.ui.geometry.Size(midRx * 2f, midRy * 2f),
        style = Stroke(width = bandW * 0.18f, cap = StrokeCap.Round)
    )
    drawArc(
        color = Color.Black.copy(alpha = 0.22f),
        startAngle = 6f, sweepAngle = 168f, useCenter = false,
        topLeft = Offset(center.x - outerRx, center.y - outerRy),
        size = androidx.compose.ui.geometry.Size(outerRx * 2f, outerRy * 2f),
        style = Stroke(width = bandW * 0.12f)
    )
}
