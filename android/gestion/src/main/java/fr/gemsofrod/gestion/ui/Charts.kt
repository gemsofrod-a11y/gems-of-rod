package fr.gemsofrod.gestion.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * Histogramme vertical : une barre par libellé, la dernière (période en
 * cours) mise en valeur, les autres atténuées.
 */
@Composable
fun BarChart(values: List<Pair<String, Double>>, modifier: Modifier = Modifier) {
    val accent = MaterialTheme.colorScheme.secondary
    val muted = accent.copy(alpha = 0.35f)
    val grid = MaterialTheme.colorScheme.outlineVariant
    val max = values.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1.0

    Column(modifier) {
        Canvas(Modifier.fillMaxWidth().height(130.dp)) {
            val slot = size.width / values.size
            val barWidth = min(slot * 0.5f, 28.dp.toPx())
            for (i in 1..3) {
                val y = size.height * i / 4f
                drawLine(grid, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
            }
            values.forEachIndexed { i, (_, v) ->
                val h = (v / max).toFloat() * size.height
                val left = slot * i + (slot - barWidth) / 2
                if (h > 0f) {
                    drawRoundRect(
                        color = if (i == values.lastIndex) accent else muted,
                        topLeft = Offset(left, size.height - h),
                        size = Size(barWidth, h),
                        cornerRadius = CornerRadius(6.dp.toPx()),
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(Modifier.fillMaxWidth()) {
            values.forEachIndexed { i, (label, v) ->
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (i == values.lastIndex) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        if (v > 0) eurosShort(v) else "–",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

/** Anneau de répartition avec légende (libellé, part en %, montant). */
@Composable
fun DonutChart(slices: List<Pair<String, Double>>, modifier: Modifier = Modifier) {
    val total = slices.sumOf { it.second }.takeIf { it > 0 } ?: return
    val track = MaterialTheme.colorScheme.surfaceVariant
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.size(120.dp)) {
            val stroke = 18.dp.toPx()
            val arcSize = Size(size.width - stroke, size.height - stroke)
            val topLeft = Offset(stroke / 2, stroke / 2)
            drawArc(track, 0f, 360f, false, topLeft, arcSize, style = Stroke(stroke))
            var start = -90f
            slices.forEachIndexed { i, (_, v) ->
                val sweep = (v / total * 360).toFloat()
                // Petit espace entre les segments, sauf s'il n'y en a qu'un.
                val gap = if (slices.size > 1) 2f else 0f
                drawArc(
                    ChartColors[i % ChartColors.size], start + gap / 2, (sweep - gap).coerceAtLeast(0.5f),
                    false, topLeft, arcSize, style = Stroke(stroke),
                )
                start += sweep
            }
        }
        Spacer(Modifier.width(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            slices.forEachIndexed { i, (label, v) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).clip(CircleShape).background(ChartColors[i % ChartColors.size]))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(label, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(
                            "${(v / total * 100).toInt()} % · ${eurosShort(v)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

/** Barres horizontales proportionnelles (une ligne par libellé). */
@Composable
fun HorizontalBars(values: List<Pair<String, Double>>, modifier: Modifier = Modifier) {
    val max = values.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: return
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        values.forEachIndexed { i, (label, v) ->
            Column {
                Row(Modifier.fillMaxWidth()) {
                    Text(label, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    Text(eurosRound(v), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(4.dp))
                Box(
                    Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Box(
                        Modifier.fillMaxWidth((v / max).toFloat().coerceIn(0.02f, 1f)).height(8.dp)
                            .clip(RoundedCornerShape(4.dp)).background(ChartColors[i % ChartColors.size]),
                    )
                }
            }
        }
    }
}

/** Utilisé dans les listes : petite pastille de statut. */
@Composable
fun Pill(text: String, modifier: Modifier = Modifier, strong: Boolean = false) {
    val bg = if (strong) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
    Text(
        text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.clip(RoundedCornerShape(50)).background(bg).padding(horizontal = 8.dp, vertical = 3.dp),
    )
}
