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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

/** Pointillés fins des lignes de repère. */
private val Dotted = PathEffect.dashPathEffect(floatArrayOf(2f, 6f))

/**
 * Histogramme en barres fines : le mois en cours en or patiné avec sa
 * valeur au-dessus, les autres en champagne pâle.
 */
@Composable
fun MiniBarChart(values: List<Pair<String, Double>>, modifier: Modifier = Modifier, height: Dp = 120.dp) {
    val max = values.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1.0
    val strong = Brush.verticalGradient(listOf(Palette.AccentSoft, Palette.Accent))
    val soft = Brush.verticalGradient(listOf(Palette.Accent.copy(alpha = 0.34f), Palette.Accent.copy(alpha = 0.18f)))
    Column(modifier) {
        Canvas(Modifier.fillMaxWidth().height(height)) {
            val slot = size.width / values.size
            val barWidth = min(slot * 0.26f, 14.dp.toPx())
            val radius = CornerRadius(barWidth / 2)
            val top = 18.dp.toPx()
            // Ligne de base discrète.
            drawLine(Palette.Line, Offset(0f, size.height), Offset(size.width, size.height), strokeWidth = 1.dp.toPx())
            values.forEachIndexed { i, (_, v) ->
                val left = slot * i + (slot - barWidth) / 2
                val h = ((v / max).toFloat() * (size.height - top)).coerceAtLeast(if (v > 0) barWidth else 0f)
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
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth()) {
            values.forEachIndexed { i, (label, v) ->
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    // Sur 12 mois, l'initiale suffit (« N D J F… ») pour que rien ne se chevauche.
                    val dense = values.size > 8
                    Text(
                        if (dense) label.take(1).uppercase() else label.uppercase(),
                        fontSize = 9.sp,
                        letterSpacing = if (dense) 0.sp else 1.sp,
                        fontWeight = if (i == values.lastIndex) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (i == values.lastIndex) Palette.Accent else Palette.Muted,
                        maxLines = 1,
                        softWrap = false,
                    )
                    if (i == values.lastIndex && v > 0 && !dense) {
                        Text(eurosShort(v), fontSize = 10.sp, color = Palette.Ink, maxLines = 1, softWrap = false)
                    }
                }
            }
        }
    }
}

/**
 * Courbe dorée fine et lissée, reflet dégradé dessous, repères en
 * pointillés, un seul point lumineux sur le dernier mois.
 */
@Composable
fun LineAreaChart(values: List<Pair<String, Double>>, modifier: Modifier = Modifier, height: Dp = 120.dp) {
    val max = values.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1.0
    val gold = Palette.Accent
    Column(modifier) {
        Canvas(Modifier.fillMaxWidth().height(height)) {
            if (values.isEmpty()) return@Canvas
            val padX = 8.dp.toPx()
            val padTop = 14.dp.toPx()
            val bottom = size.height - 2.dp.toPx()
            val step = if (values.size > 1) (size.width - 2 * padX) / (values.size - 1) else 0f
            val points = values.mapIndexed { i, (_, v) ->
                Offset(padX + step * i, padTop + (bottom - padTop) * (1f - (v / max).toFloat()))
            }
            for (k in 0..2) {
                val y = padTop + (bottom - padTop) * k / 2f
                drawLine(Palette.Line, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx(), pathEffect = Dotted)
            }
            val line = Path().apply {
                points.forEachIndexed { i, p ->
                    if (i == 0) moveTo(p.x, p.y)
                    else {
                        // Courbe lissée (contrôles horizontaux à mi-chemin).
                        val prev = points[i - 1]
                        val midX = (prev.x + p.x) / 2
                        cubicTo(midX, prev.y, midX, p.y, p.x, p.y)
                    }
                }
            }
            val area = Path().apply {
                addPath(line)
                lineTo(points.last().x, bottom)
                lineTo(points.first().x, bottom)
                close()
            }
            drawPath(area, Brush.verticalGradient(listOf(gold.copy(alpha = 0.20f), gold.copy(alpha = 0.02f)), startY = padTop, endY = bottom))
            drawPath(line, gold, style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round))
            val last = points.last()
            drawCircle(gold.copy(alpha = 0.18f), radius = 9.dp.toPx(), center = last)
            drawCircle(Palette.Card, radius = 4.dp.toPx(), center = last)
            drawCircle(gold, radius = 2.6.dp.toPx(), center = last)
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            values.forEachIndexed { i, (label, _) ->
                Text(
                    if (values.size > 8) label.take(1).uppercase() else label.uppercase(),
                    fontSize = 9.sp, letterSpacing = if (values.size > 8) 0.sp else 1.sp, maxLines = 1, softWrap = false,
                    color = if (i == values.lastIndex) Palette.Accent else Palette.Muted,
                    fontWeight = if (i == values.lastIndex) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
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
                val stroke = 10.dp.toPx()
                val arcSize = Size(size.width - stroke, size.height - stroke)
                val topLeft = Offset(stroke / 2, stroke / 2)
                drawArc(Palette.Background, 0f, 360f, false, topLeft, arcSize, style = Stroke(stroke))
                var start = -90f
                slices.forEachIndexed { i, (_, v) ->
                    val sweep = (v / total * 360).toFloat()
                    val gap = if (slices.size > 1) 3f else 0f
                    drawArc(
                        ChartColors[i % ChartColors.size], start + gap / 2, (sweep - gap).coerceAtLeast(0.5f),
                        false, topLeft, arcSize, style = Stroke(stroke, cap = StrokeCap.Round),
                    )
                    start += sweep
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(eurosShort(total), fontFamily = Display, fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Palette.Ink)
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
