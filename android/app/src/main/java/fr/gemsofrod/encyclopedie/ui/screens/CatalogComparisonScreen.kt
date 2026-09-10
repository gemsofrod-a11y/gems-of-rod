package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.CatalogComparison
import fr.gemsofrod.encyclopedie.data.CoquillagesRepository
import fr.gemsofrod.encyclopedie.data.FossilesRepository
import fr.gemsofrod.encyclopedie.data.GemRarete
import fr.gemsofrod.encyclopedie.data.MeteoritesRepository
import fr.gemsofrod.encyclopedie.ui.components.ComparisonDetailRow
import fr.gemsofrod.encyclopedie.ui.components.ComparisonRadarChart
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.labelRes

/** Catégorie du catalogue (hors gemmes) comparable sur le graphique radar. */
enum class CatalogComparisonCategory { FOSSILE, COQUILLAGE, METEORITE }

/** Fiche réduite aux champs communs aux 3 catégories comparables (dureté, densité, rareté). */
private data class ComparableItem(
    val id: String,
    val nom: String,
    val durete: String,
    val densite: String,
    val rarete: GemRarete
)

@Composable
private fun itemsFor(category: CatalogComparisonCategory): List<ComparableItem> = when (category) {
    CatalogComparisonCategory.FOSSILE -> FossilesRepository.all().map { it.localized() }
        .map { ComparableItem(it.id, it.nom, it.durete, it.densite, it.rarete) }
    CatalogComparisonCategory.COQUILLAGE -> CoquillagesRepository.all().map { it.localized() }
        .map { ComparableItem(it.id, it.nom, it.durete, it.densite, it.rarete) }
    CatalogComparisonCategory.METEORITE -> MeteoritesRepository.all().map { it.localized() }
        .map { ComparableItem(it.id, it.nom, it.durete, it.densite, it.rarete) }
}

/**
 * Compare deux fossiles, deux coquillages ou deux météorites (selon
 * [category]) sur un graphique radar à 3 axes (dureté, densité, rareté) —
 * les seules grandeurs communes à ces catégories dans ce catalogue,
 * contrairement aux gemmes qui ont aussi des propriétés optiques mesurées
 * (voir [GemComparisonScreen]). Même patron d'écran, réutilise
 * [ComparisonRadarChart] et [ComparisonDetailRow].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogComparisonScreen(
    category: CatalogComparisonCategory,
    onBackClick: () -> Unit
) {
    val items = itemsFor(category).sortedBy { it.nom }

    var itemA by remember { mutableStateOf<ComparableItem?>(null) }
    var itemB by remember { mutableStateOf<ComparableItem?>(null) }

    val accentA = MaterialTheme.colorScheme.primary
    val accentB = MaterialTheme.colorScheme.tertiary

    val screenTitle = when (category) {
        CatalogComparisonCategory.FOSSILE -> stringResource(R.string.fossile_comparer_title)
        CatalogComparisonCategory.COQUILLAGE -> stringResource(R.string.coquillage_comparer_title)
        CatalogComparisonCategory.METEORITE -> stringResource(R.string.meteorite_comparer_title)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
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
            ComparableItemPickerField(
                label = stringResource(R.string.comparer_gem_a_label),
                items = items,
                excluding = itemB,
                selected = itemA,
                accentColor = accentA,
                onSelected = { itemA = it }
            )
            ComparableItemPickerField(
                label = stringResource(R.string.comparer_gem_b_label),
                items = items,
                excluding = itemA,
                selected = itemB,
                accentColor = accentB,
                onSelected = { itemB = it }
            )

            val currentA = itemA
            val currentB = itemB
            if (currentA != null && currentB != null) {
                val profileA = remember(currentA.id) { CatalogComparison.profile(currentA.durete, currentA.densite, currentA.rarete) }
                val profileB = remember(currentB.id) { CatalogComparison.profile(currentB.durete, currentB.densite, currentB.rarete) }
                val axisLabels = listOf(
                    stringResource(R.string.comparer_axis_durete_short),
                    stringResource(R.string.comparer_axis_densite_short),
                    stringResource(R.string.comparer_axis_rarete_short)
                )
                ComparisonRadarChart(
                    axisLabels = axisLabels,
                    valuesA = listOf(profileA.dureteNorm, profileA.densiteNorm, profileA.rareteNorm),
                    valuesB = listOf(profileB.dureteNorm, profileB.densiteNorm, profileB.rareteNorm),
                    nameA = currentA.nom,
                    nameB = currentB.nom,
                    accentA = accentA,
                    accentB = accentB
                )
                ComparisonDetailsCard(itemA = currentA, itemB = currentB, accentA = accentA, accentB = accentB)
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
private fun ComparableItemPickerField(
    label: String,
    items: List<ComparableItem>,
    excluding: ComparableItem?,
    selected: ComparableItem?,
    accentColor: Color,
    onSelected: (ComparableItem?) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val results = if (query.isBlank()) {
        emptyList()
    } else {
        items.filter { it.id != excluding?.id && it.nom.contains(query, ignoreCase = true) }.take(6)
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
                placeholder = { Text(stringResource(R.string.catalog_search_placeholder)) },
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
                    results.forEach { item ->
                        Text(
                            text = item.nom,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onSelected(item)
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

@Composable
private fun ComparisonDetailsCard(itemA: ComparableItem, itemB: ComparableItem, accentA: Color, accentB: Color) {
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
                valueA = itemA.durete,
                valueB = itemB.durete,
                accentA = accentA,
                accentB = accentB
            )
            ComparisonDetailRow(
                label = stringResource(R.string.fiche_densite),
                valueA = itemA.densite,
                valueB = itemB.densite,
                accentA = accentA,
                accentB = accentB
            )
            ComparisonDetailRow(
                label = stringResource(R.string.fiche_rarete),
                valueA = stringResource(itemA.rarete.labelRes),
                valueB = stringResource(itemB.rarete.labelRes),
                accentA = accentA,
                accentB = accentB
            )
        }
    }
}
