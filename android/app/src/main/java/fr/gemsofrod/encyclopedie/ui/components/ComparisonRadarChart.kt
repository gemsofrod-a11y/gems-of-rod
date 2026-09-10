package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Graphique radar générique comparant deux fiches du catalogue (gemme,
 * fossile, coquillage, météorite...) sur un nombre variable d'axes, chacun
 * normalisé entre 0 et 1 par l'appelant. Extrait de l'outil de comparaison
 * gemmologique pour être réutilisé par les autres catégories, dont le
 * nombre de grandeurs comparables diffère (3 axes contre 5 pour les gemmes).
 */
@Composable
fun ComparisonRadarChart(
    axisLabels: List<String>,
    valuesA: List<Float>,
    valuesB: List<Float>,
    nameA: String,
    nameB: String,
    accentA: Color,
    accentB: Color
) {
    val axisCount = axisLabels.size
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(fontSize = 11.sp, color = labelColor)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                RadarLegendDot(color = accentA, label = nameA)
                RadarLegendDot(color = accentB, label = nameB)
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxRadius = minOf(size.width, size.height) / 2f
                val chartRadius = maxRadius * 0.62f
                val labelRadius = maxRadius * 0.9f
                val angleStep = (2 * PI / axisCount).toFloat()

                fun pointAt(radius: Float, index: Int): Offset {
                    val angle = -PI.toFloat() / 2f + index * angleStep
                    return Offset(center.x + radius * cos(angle), center.y + radius * sin(angle))
                }

                for (ring in 1..4) {
                    val ringPoints = (0 until axisCount).map { pointAt(chartRadius * ring / 4f, it) }
                    for (i in ringPoints.indices) {
                        drawLine(gridColor, ringPoints[i], ringPoints[(i + 1) % ringPoints.size], strokeWidth = 1.dp.toPx())
                    }
                }
                for (i in 0 until axisCount) {
                    drawLine(gridColor, center, pointAt(chartRadius, i), strokeWidth = 1.dp.toPx())
                }

                fun polygonPath(values: List<Float>): Path {
                    val points = (0 until axisCount).map { pointAt(chartRadius * values[it].coerceIn(0f, 1f), it) }
                    return Path().apply {
                        moveTo(points[0].x, points[0].y)
                        for (i in 1 until points.size) lineTo(points[i].x, points[i].y)
                        close()
                    }
                }

                drawPath(polygonPath(valuesA), color = accentA.copy(alpha = 0.25f))
                drawPath(polygonPath(valuesA), color = accentA, style = Stroke(width = 2.dp.toPx()))
                drawPath(polygonPath(valuesB), color = accentB.copy(alpha = 0.25f))
                drawPath(polygonPath(valuesB), color = accentB, style = Stroke(width = 2.dp.toPx()))

                for (i in 0 until axisCount) {
                    val labelPoint = pointAt(labelRadius, i)
                    val measured = textMeasurer.measure(axisLabels[i], style = labelStyle)
                    drawText(
                        textMeasurer = textMeasurer,
                        text = axisLabels[i],
                        topLeft = Offset(labelPoint.x - measured.size.width / 2f, labelPoint.y - measured.size.height / 2f),
                        style = labelStyle
                    )
                }
            }
        }
    }
}

@Composable
private fun RadarLegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

/**
 * Ligne de détail comparant une même caractéristique (dureté, densité...)
 * entre deux fiches, chacune repérée par la couleur d'accent de sa colonne.
 * Partagée par les écrans de comparaison gemmes et catalogue.
 */
@Composable
fun ComparisonDetailRow(label: String, valueA: String, valueB: String, accentA: Color, accentB: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(8.dp)
                        .background(accentA, CircleShape)
                )
                Text(text = valueA, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(8.dp)
                        .background(accentB, CircleShape)
                )
                Text(text = valueB, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
