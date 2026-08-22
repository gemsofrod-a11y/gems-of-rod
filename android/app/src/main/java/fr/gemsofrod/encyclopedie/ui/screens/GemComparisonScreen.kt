package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Gem
import fr.gemsofrod.encyclopedie.data.GemComparison
import fr.gemsofrod.encyclopedie.data.GemComparisonProfile
import fr.gemsofrod.encyclopedie.data.GemDiagnostics
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.localizedLabel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

/**
 * Compare deux gemmes du catalogue sur un graphique radar (dureté, indice
 * de réfraction, densité, fluorescence, pléochroïsme), à partir des mêmes
 * données que la fiche gemmologique détaillée. Voir [GemComparison] pour
 * l'extraction des valeurs numériques depuis les champs texte.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GemComparisonScreen(
    onBackClick: () -> Unit,
    initialGemAId: String? = null,
    initialGemBId: String? = null
) {
    val allGems = remember { GemsRepository.gems }
    val localizedGems = allGems.map { it.localized() }.sortedBy { it.nom }

    var gemA by remember { mutableStateOf(initialGemAId?.let { id -> localizedGems.find { it.id == id } }) }
    var gemB by remember { mutableStateOf(initialGemBId?.let { id -> localizedGems.find { it.id == id } }) }

    val accentA = MaterialTheme.colorScheme.primary
    val accentB = MaterialTheme.colorScheme.tertiary

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.gemmologie_comparer_title)) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GemPickerField(
                label = stringResource(R.string.comparer_gem_a_label),
                gems = localizedGems,
                excluding = gemB,
                selected = gemA,
                accentColor = accentA,
                onSelected = { gemA = it }
            )
            GemPickerField(
                label = stringResource(R.string.comparer_gem_b_label),
                gems = localizedGems,
                excluding = gemA,
                selected = gemB,
                accentColor = accentB,
                onSelected = { gemB = it }
            )

            val currentA = gemA
            val currentB = gemB
            if (currentA != null && currentB != null) {
                val profileA = remember(currentA.id) { GemComparison.profile(currentA) }
                val profileB = remember(currentB.id) { GemComparison.profile(currentB) }
                ComparisonRadarChart(
                    profileA = profileA,
                    profileB = profileB,
                    nameA = currentA.nom,
                    nameB = currentB.nom,
                    accentA = accentA,
                    accentB = accentB
                )
                ComparisonDetailsCard(gemA = currentA, gemB = currentB, accentA = accentA, accentB = accentB)
            } else {
                Text(
                    text = stringResource(R.string.comparer_select_prompt),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun GemPickerField(
    label: String,
    gems: List<Gem>,
    excluding: Gem?,
    selected: Gem?,
    accentColor: Color,
    onSelected: (Gem?) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val results = if (query.isBlank()) {
        emptyList()
    } else {
        gems.filter { it.id != excluding?.id && it.nom.contains(query, ignoreCase = true) }.take(6)
    }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (selected != null) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(accentColor, CircleShape)
                    )
                    Text(
                        text = selected.nom,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { onSelected(null); query = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.cd_clear))
                    }
                }
            }
        } else {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.search_gems_placeholder)) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            if (results.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    results.forEach { gem ->
                        Text(
                            text = gem.nom,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onSelected(gem)
                                    query = ""
                                }
                                .padding(vertical = 10.dp, horizontal = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

private const val ComparisonAxisCount = 5

@Composable
private fun ComparisonRadarChart(
    profileA: GemComparisonProfile,
    profileB: GemComparisonProfile,
    nameA: String,
    nameB: String,
    accentA: Color,
    accentB: Color
) {
    val axisLabels = listOf(
        stringResource(R.string.comparer_axis_durete_short),
        stringResource(R.string.comparer_axis_indice_short),
        stringResource(R.string.comparer_axis_densite_short),
        stringResource(R.string.comparer_axis_fluorescence_short),
        stringResource(R.string.comparer_axis_pleochroisme_short)
    )
    val valuesA = listOf(profileA.dureteNorm, profileA.indiceNorm, profileA.densiteNorm, profileA.fluorescenceNorm, profileA.pleochroismeNorm)
    val valuesB = listOf(profileB.dureteNorm, profileB.indiceNorm, profileB.densiteNorm, profileB.fluorescenceNorm, profileB.pleochroismeNorm)

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
                LegendDot(color = accentA, label = nameA)
                LegendDot(color = accentB, label = nameB)
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
                val angleStep = (2 * PI / ComparisonAxisCount).toFloat()

                fun pointAt(radius: Float, index: Int): Offset {
                    val angle = -PI.toFloat() / 2f + index * angleStep
                    return Offset(center.x + radius * cos(angle), center.y + radius * sin(angle))
                }

                for (ring in 1..4) {
                    val ringPoints = (0 until ComparisonAxisCount).map { pointAt(chartRadius * ring / 4f, it) }
                    for (i in ringPoints.indices) {
                        drawLine(gridColor, ringPoints[i], ringPoints[(i + 1) % ringPoints.size], strokeWidth = 1.dp.toPx())
                    }
                }
                for (i in 0 until ComparisonAxisCount) {
                    drawLine(gridColor, center, pointAt(chartRadius, i), strokeWidth = 1.dp.toPx())
                }

                fun polygonPath(values: List<Float>): Path {
                    val points = (0 until ComparisonAxisCount).map { pointAt(chartRadius * values[it].coerceIn(0f, 1f), it) }
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

                for (i in 0 until ComparisonAxisCount) {
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
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun ComparisonDetailsCard(gemA: Gem, gemB: Gem, accentA: Color, accentB: Color) {
    val diagnosticA = GemDiagnostics.data[gemA.id]
    val diagnosticB = GemDiagnostics.data[gemB.id]
    val placeholder = "—"

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.comparer_details_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ComparisonDetailRow(
                label = stringResource(R.string.fiche_durete),
                valueA = gemA.durete,
                valueB = gemB.durete,
                accentA = accentA,
                accentB = accentB
            )
            ComparisonDetailRow(
                label = stringResource(R.string.fiche_indice_refraction),
                valueA = gemA.indiceRefraction,
                valueB = gemB.indiceRefraction,
                accentA = accentA,
                accentB = accentB
            )
            ComparisonDetailRow(
                label = stringResource(R.string.fiche_densite),
                valueA = diagnosticA?.densite ?: placeholder,
                valueB = diagnosticB?.densite ?: placeholder,
                accentA = accentA,
                accentB = accentB
            )
            ComparisonDetailRow(
                label = stringResource(R.string.fiche_fluorescence),
                valueA = diagnosticA?.let { localizedLabel(it.fluorescence) } ?: placeholder,
                valueB = diagnosticB?.let { localizedLabel(it.fluorescence) } ?: placeholder,
                accentA = accentA,
                accentB = accentB
            )
            ComparisonDetailRow(
                label = stringResource(R.string.fiche_pleochroisme),
                valueA = diagnosticA?.let { localizedLabel(it.pleochroisme) } ?: placeholder,
                valueB = diagnosticB?.let { localizedLabel(it.pleochroisme) } ?: placeholder,
                accentA = accentA,
                accentB = accentB
            )
        }
    }
}

@Composable
private fun ComparisonDetailRow(label: String, valueA: String, valueB: String, accentA: Color, accentB: Color) {
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
