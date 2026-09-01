package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.CrystalSystemEntry
import fr.gemsofrod.encyclopedie.data.CrystalSystemShape
import fr.gemsofrod.encyclopedie.data.CrystalSystemsInfo
import kotlin.math.cos
import kotlin.math.sin

/**
 * Les six systèmes cristallins (classification traditionnelle regroupant
 * hexagonal et rhomboédrique), chacun accompagné d'une esquisse filaire
 * simplifiée : traits pleins pour les arêtes visibles, pointillés pour les
 * arêtes cachées — la convention usuelle des schémas de cristallographie.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrystalSystemsScreen(onBackClick: () -> Unit) {
    val languageCode = LocalConfiguration.current.locales[0].language
    val page = CrystalSystemsInfo.page(languageCode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(page.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = page.intro,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            page.systems.forEach { entry ->
                CrystalSystemCard(entry = entry, examplesLabel = page.examplesLabel)
            }

            CrystalSystemsDisclaimer(title = page.disclaimerTitle, body = page.disclaimerBody)
        }
    }
}

@Composable
private fun CrystalSystemCard(entry: CrystalSystemEntry, examplesLabel: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CrystalSketch(
                shape = entry.shape,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
            Text(
                text = entry.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$examplesLabel : ${entry.examples}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CrystalSystemsDisclaimer(title: String, body: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

private data class ParallelepipedPreset(
    val aAngleDeg: Float,
    val bAngleDeg: Float,
    val cAngleDeg: Float,
    val aLen: Float,
    val bLen: Float,
    val cLen: Float
)

private fun presetFor(shape: CrystalSystemShape): ParallelepipedPreset = when (shape) {
    CrystalSystemShape.CUBIQUE -> ParallelepipedPreset(
        aAngleDeg = 0f, bAngleDeg = 135f, cAngleDeg = 90f,
        aLen = 1f, bLen = 0.75f, cLen = 1f
    )
    CrystalSystemShape.QUADRATIQUE -> ParallelepipedPreset(
        aAngleDeg = 0f, bAngleDeg = 135f, cAngleDeg = 90f,
        aLen = 0.8f, bLen = 0.6f, cLen = 1.3f
    )
    CrystalSystemShape.ORTHORHOMBIQUE -> ParallelepipedPreset(
        aAngleDeg = 0f, bAngleDeg = 135f, cAngleDeg = 90f,
        aLen = 0.7f, bLen = 0.6f, cLen = 1.15f
    )
    CrystalSystemShape.MONOCLINIQUE -> ParallelepipedPreset(
        aAngleDeg = 0f, bAngleDeg = 115f, cAngleDeg = 90f,
        aLen = 0.9f, bLen = 0.7f, cLen = 1.1f
    )
    CrystalSystemShape.TRICLINIQUE -> ParallelepipedPreset(
        aAngleDeg = 10f, bAngleDeg = 150f, cAngleDeg = 100f,
        aLen = 0.85f, bLen = 0.75f, cLen = 1f
    )
    CrystalSystemShape.HEXAGONAL -> ParallelepipedPreset(0f, 0f, 0f, 0f, 0f, 0f) // non utilisé
}

@Composable
private fun CrystalSketch(shape: CrystalSystemShape, modifier: Modifier = Modifier) {
    val strokeColor = MaterialTheme.colorScheme.primary
    Canvas(modifier = modifier) {
        if (shape == CrystalSystemShape.HEXAGONAL) {
            drawHexagonalPrism(strokeColor)
        } else {
            drawParallelepiped(presetFor(shape), strokeColor)
        }
    }
}

/** Vecteur 2D pour un angle (0° = droite, 90° = haut) et une longueur, en repère écran (y vers le bas). */
private fun axisVector(angleDeg: Float, length: Float): Offset {
    val rad = Math.toRadians(angleDeg.toDouble())
    return Offset((cos(rad) * length).toFloat(), (-sin(rad) * length).toFloat())
}

private fun DrawScope.drawParallelepiped(preset: ParallelepipedPreset, color: Color) {
    val a = axisVector(preset.aAngleDeg, preset.aLen)
    val b = axisVector(preset.bAngleDeg, preset.bLen)
    val c = axisVector(preset.cAngleDeg, preset.cLen)

    // 8 sommets du parallélépipède, origine arbitraire avant recentrage.
    val v000 = Offset.Zero
    val v100 = v000 + a
    val v010 = v000 + b
    val v001 = v000 + c
    val v110 = v000 + a + b
    val v101 = v000 + a + c
    val v011 = v000 + b + c
    val v111 = v000 + a + b + c

    val rawVertices = listOf(v000, v100, v010, v001, v110, v101, v011, v111)
    val fitted = fitToCanvas(rawVertices, size)
    val p000 = fitted[0]
    val p100 = fitted[1]
    val p010 = fitted[2]
    val p001 = fitted[3]
    val p110 = fitted[4]
    val p101 = fitted[5]
    val p011 = fitted[6]
    val p111 = fitted[7]

    val strokeWidth = 2.5.dp.toPx()
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)

    // Sommet caché : v010 (origine + b). Ses 3 arêtes sont en pointillés.
    drawLine(color, p010, p000, strokeWidth, pathEffect = dashEffect)
    drawLine(color, p010, p110, strokeWidth, pathEffect = dashEffect)
    drawLine(color, p010, p011, strokeWidth, pathEffect = dashEffect)

    // Les 9 autres arêtes, pleines.
    val solidEdges = listOf(
        p000 to p100, p100 to p110, p100 to p101,
        p001 to p101, p001 to p011, p001 to p000,
        p110 to p111, p101 to p111, p011 to p111
    )
    solidEdges.forEach { (from, to) -> drawLine(color, from, to, strokeWidth) }
}

private fun DrawScope.drawHexagonalPrism(color: Color) {
    val strokeWidth = 2.5.dp.toPx()
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)

    // Hexagone régulier, sommet vers le haut ; angles à 30°, 90°, 150°...
    fun hexVertex(angleDeg: Float) = axisVector(angleDeg, 1f)
    val hexAngles = listOf(90f, 150f, 210f, 270f, 330f, 30f)
    val topRing = hexAngles.map { hexVertex(it) + Offset(0f, -0.65f) }
    val bottomRing = hexAngles.map { hexVertex(it) + Offset(0f, 0.65f) }

    val fitted = fitToCanvas(topRing + bottomRing, size)
    val top = fitted.subList(0, 6)
    val bottom = fitted.subList(6, 12)

    // Sommet du haut : entièrement visible (trait plein).
    for (i in top.indices) {
        drawLine(color, top[i], top[(i + 1) % 6], strokeWidth)
    }
    // Arêtes verticales et hexagone du bas : les 3 sommets arrière (index 1,2,3, côté
    // gauche/arrière de l'hexagone tel qu'orienté ici) sont cachés.
    val hiddenIndices = setOf(1, 2, 3)
    for (i in 0 until 6) {
        val isHiddenEdgeVertical = i in hiddenIndices
        drawLine(
            color, top[i], bottom[i], strokeWidth,
            pathEffect = if (isHiddenEdgeVertical) dashEffect else null
        )
    }
    val hiddenBottomEdges = setOf(0, 1, 2, 3)
    for (i in 0 until 6) {
        val next = (i + 1) % 6
        drawLine(
            color, bottom[i], bottom[next], strokeWidth,
            pathEffect = if (i in hiddenBottomEdges) dashEffect else null
        )
    }
}

/** Met à l'échelle et centre une liste de sommets 2D pour remplir [targetSize] avec une marge. */
private fun fitToCanvas(vertices: List<Offset>, targetSize: Size): List<Offset> {
    val minX = vertices.minOf { it.x }
    val maxX = vertices.maxOf { it.x }
    val minY = vertices.minOf { it.y }
    val maxY = vertices.maxOf { it.y }
    val width = (maxX - minX).coerceAtLeast(0.0001f)
    val height = (maxY - minY).coerceAtLeast(0.0001f)

    val margin = 0.82f
    val scale = minOf(targetSize.width * margin / width, targetSize.height * margin / height)

    val centerX = (minX + maxX) / 2f
    val centerY = (minY + maxY) / 2f
    val targetCenterX = targetSize.width / 2f
    val targetCenterY = targetSize.height / 2f

    return vertices.map { v ->
        Offset(
            targetCenterX + (v.x - centerX) * scale,
            targetCenterY + (v.y - centerY) * scale
        )
    }
}
