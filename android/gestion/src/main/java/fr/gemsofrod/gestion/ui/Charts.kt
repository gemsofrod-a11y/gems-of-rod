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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

/**
 * Histogramme en barres arrondies dégradées : la dernière barre (mois en
 * cours) en violet plein, les autres en violet pâle.
 */
@Composable
fun MiniBarChart(values: List<Pair<String, Double>>, modifier: Modifier = Modifier, height: Dp = 110.dp) {
    val max = values.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1.0
    val strong = Brush.verticalGradient(listOf(Color(0xFF7466F7), Palette.Accent))
    val soft = Brush.verticalGradient(listOf(Palette.AccentSoft, Palette.AccentPale))
    Column(modifier) {
        Canvas(Modifier.fillMaxWidth().height(height)) {
            val slot = size.width / values.size
            val barWidth = min(slot * 0.42f, 22.dp.toPx())
            val radius = CornerRadius(barWidth / 2)
            values.forEachIndexed { i, (_, v) ->
                val left = slot * i + (slot - barWidth) / 2
                // Fond de barre discret pour garder le rythme même à zéro.
                drawRoundRect(Palette.Background, Offset(left, 0f), Size(barWidth, size.height), radius)
                val h = ((v / max).toFloat() * size.height).coerceAtLeast(if (v > 0) barWidth else 0f)
                if (h > 0f) {
                    drawRoundRect(
                        brush = if (i == values.lastIndex) strong else soft,
                        topLeft = Offset(left, size.height - h),
                        size = Size(barWidth, h),
                        cornerRadius = radius,
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            values.forEachIndexed { i, (label, _) ->
                Text(
                    label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    fontWeight = if (i == values.lastIndex) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (i == values.lastIndex) Palette.Ink else Palette.Muted,
                )
            }
        }
    }
}

/** Courbe avec points et zone dégradée sous la ligne. */
@Composable
fun LineAreaChart(values: List<Pair<String, Double>>, modifier: Modifier = Modifier, height: Dp = 110.dp) {
    val max = values.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1.0
    Column(modifier) {
        Canvas(Modifier.fillMaxWidth().height(height)) {
            if (values.isEmpty()) return@Canvas
            val pad = 6.dp.toPx()
            val step = if (values.size > 1) (size.width - 2 * pad) / (values.size - 1) else 0f
            val points = values.mapIndexed { i, (_, v) ->
                Offset(pad + step * i, pad + (size.height - 2 * pad) * (1f - (v / max).toFloat()))
            }
            for (k in 1..3) {
                val y = size.height * k / 4f
                drawLine(Palette.Line, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
            }
            val line = Path().apply {
                points.forEachIndexed { i, p ->
                    if (i == 0) moveTo(p.x, p.y)
                    else {
                        // Courbe lissée entre deux points (contrôles horizontaux).
                        val prev = points[i - 1]
                        val midX = (prev.x + p.x) / 2
                        cubicTo(midX, prev.y, midX, p.y, p.x, p.y)
                    }
                }
            }
            val area = Path().apply {
                addPath(line)
                lineTo(points.last().x, size.height)
                lineTo(points.first().x, size.height)
                close()
            }
            drawPath(area, Brush.verticalGradient(listOf(Palette.Accent.copy(alpha = 0.22f), Color.Transparent)))
            drawPath(line, Palette.Accent, style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round))
            points.forEach { p ->
                drawCircle(Color.White, radius = 4.5.dp.toPx(), center = p)
                drawCircle(Palette.Accent, radius = 4.5.dp.toPx(), center = p, style = Stroke(2.dp.toPx()))
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            values.forEach { (label, _) -> Text(label, fontSize = 11.sp, color = Palette.Muted) }
        }
    }
}

/** Anneau de répartition avec légende (libellé, part en %, montant). */
@Composable
fun DonutChart(slices: List<Pair<String, Double>>, modifier: Modifier = Modifier) {
    val total = slices.sumOf { it.second }.takeIf { it > 0 } ?: return
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(120.dp)) {
                val stroke = 16.dp.toPx()
                val arcSize = Size(size.width - stroke, size.height - stroke)
                val topLeft = Offset(stroke / 2, stroke / 2)
                drawArc(Palette.Background, 0f, 360f, false, topLeft, arcSize, style = Stroke(stroke))
                var start = -90f
                slices.forEachIndexed { i, (_, v) ->
                    val sweep = (v / total * 360).toFloat()
                    val gap = if (slices.size > 1) 4f else 0f
                    drawArc(
                        ChartColors[i % ChartColors.size], start + gap / 2, (sweep - gap).coerceAtLeast(0.5f),
                        false, topLeft, arcSize, style = Stroke(stroke, cap = StrokeCap.Round),
                    )
                    start += sweep
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(eurosShort(total), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Palette.Ink)
                Text("12 mois", fontSize = 10.sp, color = Palette.Muted)
            }
        }
        Spacer(Modifier.width(18.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            slices.forEachIndexed { i, (label, v) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).clip(CircleShape).background(ChartColors[i % ChartColors.size]))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(label, fontSize = 13.sp, color = Palette.Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${(v / total * 100).toInt()} % · ${eurosShort(v)}", fontSize = 11.sp, color = Palette.Muted)
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
    Column(modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        values.forEachIndexed { i, (label, v) ->
            Column {
                Row(Modifier.fillMaxWidth()) {
                    Text(label, fontSize = 13.sp, color = Palette.Ink, modifier = Modifier.weight(1f))
                    Text(eurosRound(v), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Palette.Ink)
                }
                Spacer(Modifier.height(6.dp))
                Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Palette.Background)) {
                    Box(
                        Modifier.fillMaxWidth((v / max).toFloat().coerceIn(0.03f, 1f)).height(8.dp)
                            .clip(RoundedCornerShape(4.dp)).background(ChartColors[i % ChartColors.size]),
                    )
                }
            }
        }
    }
}
