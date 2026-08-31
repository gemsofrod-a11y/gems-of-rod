package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.LapidaireAngles
import fr.gemsofrod.encyclopedie.data.LapidaireComponent
import fr.gemsofrod.encyclopedie.data.LapidaireDefaut
import fr.gemsofrod.encyclopedie.data.LapidaireDiagram
import fr.gemsofrod.encyclopedie.data.LapidaireDiagrams
import fr.gemsofrod.encyclopedie.data.LapidaireDisc
import fr.gemsofrod.encyclopedie.data.LapidaireIndexEntry
import fr.gemsofrod.encyclopedie.data.LapidaireInfo
import fr.gemsofrod.encyclopedie.data.LapidaireOptiqueEntry
import fr.gemsofrod.encyclopedie.data.LapidaireTip
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField
import fr.gemsofrod.encyclopedie.ui.rememberSampledDrawablePainter
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Section éditoriale dédiée au métier de lapidaire (taille des facettes) :
 * composants d'une machine à facettes, progression des disques de mise en
 * forme et de polissage, angles de référence, diagrammes réels sourcés via
 * [LapidaireDiagrams], et conseils pratiques. Même patron que
 * [TreatmentsScreen] et [InstrumentsScreen] ; la recherche filtre les
 * sections nommées (machines, disques) et masque le contenu supplémentaire
 * (angles, diagrammes, conseils, avertissement) pendant une recherche active,
 * comme sur les autres pages de référence.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LapidaireScreen(onBackClick: () -> Unit) {
    val languageCode = LocalConfiguration.current.locales[0].language
    val page = LapidaireInfo.page(languageCode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.lapidaire_title)) },
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
        var query by remember { mutableStateOf("") }
        val displayedMachines = if (query.isBlank()) page.machines else page.machines.filter { it.nom.contains(query, ignoreCase = true) }
        val displayedDisques = if (query.isBlank()) page.disques else page.disques.filter { it.nom.contains(query, ignoreCase = true) }
        val isSearching = query.isNotBlank()
        val noResults = isSearching && displayedMachines.isEmpty() && displayedDisques.isEmpty()

        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        // Position de chaque en-tête de section dans la colonne défilante,
        // capturée à la composition via onGloballyPositioned sur SectionHeader ;
        // la table des matières s'en sert pour faire défiler jusqu'à la section
        // choisie plutôt que de dérouler tout l'écran manuellement.
        val sectionOffsets = remember { mutableStateMapOf<String, Int>() }

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            CatalogSearchField(
                query = query,
                onQueryChange = { query = it },
                placeholder = stringResource(R.string.catalog_search_placeholder)
            )
            if (!isSearching) {
                LapidaireToc(
                    entries = listOf(
                        "machines" to page.machinesTitle,
                        "disques" to page.disquesTitle,
                        "index" to page.indexTitle,
                        "angles" to page.anglesTitle,
                        "optique" to page.optiqueTitle,
                        "defauts" to page.defautsTitle,
                        "diagrammes" to page.diagrammesTitle,
                        "conseils" to page.tipsTitle
                    ),
                    onEntryClick = { key ->
                        val target = sectionOffsets[key] ?: return@LapidaireToc
                        coroutineScope.launch { scrollState.animateScrollTo(target) }
                    }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (!isSearching) {
                    Text(
                        text = page.intro,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (noResults) {
                    Text(
                        text = stringResource(R.string.catalog_search_no_results, query),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    if (displayedMachines.isNotEmpty()) {
                        SectionHeader(
                            title = page.machinesTitle,
                            modifier = Modifier.onGloballyPositioned {
                                sectionOffsets["machines"] = it.positionInParent().y.roundToInt()
                            }
                        )
                        if (!isSearching) {
                            Text(
                                text = page.machinesIntro,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        displayedMachines.forEach { ComponentCard(it) }
                    }

                    if (displayedDisques.isNotEmpty()) {
                        SectionHeader(
                            title = page.disquesTitle,
                            modifier = Modifier.onGloballyPositioned {
                                sectionOffsets["disques"] = it.positionInParent().y.roundToInt()
                            }
                        )
                        if (!isSearching) {
                            Text(
                                text = page.disquesIntro,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        displayedDisques.forEach { DiscCard(it) }
                    }
                }

                if (!isSearching) {
                    SectionHeader(
                        title = page.indexTitle,
                        modifier = Modifier.onGloballyPositioned {
                            sectionOffsets["index"] = it.positionInParent().y.roundToInt()
                        }
                    )
                    Text(
                        text = page.indexIntro,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    page.indexTable.forEach { IndexCard(it) }

                    SectionHeader(
                        title = page.anglesTitle,
                        modifier = Modifier.onGloballyPositioned {
                            sectionOffsets["angles"] = it.positionInParent().y.roundToInt()
                        }
                    )
                    Text(
                        text = page.anglesIntro,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    page.angles.forEach { AnglesCard(it) }

                    SectionHeader(
                        title = page.optiqueTitle,
                        modifier = Modifier.onGloballyPositioned {
                            sectionOffsets["optique"] = it.positionInParent().y.roundToInt()
                        }
                    )
                    Text(
                        text = page.optiqueIntro,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    page.optiqueTable.forEach { OptiqueCard(it) }

                    SectionHeader(
                        title = page.defautsTitle,
                        modifier = Modifier.onGloballyPositioned {
                            sectionOffsets["defauts"] = it.positionInParent().y.roundToInt()
                        }
                    )
                    Text(
                        text = page.defautsIntro,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    page.defauts.forEach { DefautCard(it) }

                    SectionHeader(
                        title = page.diagrammesTitle,
                        modifier = Modifier.onGloballyPositioned {
                            sectionOffsets["diagrammes"] = it.positionInParent().y.roundToInt()
                        }
                    )
                    page.diagrammes.forEach { DiagramCard(it) }

                    SectionHeader(
                        title = page.tipsTitle,
                        modifier = Modifier.onGloballyPositioned {
                            sectionOffsets["conseils"] = it.positionInParent().y.roundToInt()
                        }
                    )
                    page.tips.forEach { TipCard(it) }

                    LapidaireDisclaimer(title = page.disclaimerTitle, body = page.disclaimerBody)
                }
            }
        }
    }
}

@Composable
private fun LapidaireToc(entries: List<Pair<String, String>>, onEntryClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        entries.forEach { (key, label) ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { onEntryClick(key) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
private fun ComponentCard(component: LapidaireComponent) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = component.nom,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = component.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DiscCard(disc: LapidaireDisc) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = disc.nom,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.lapidaire_disc_grain_label, disc.grain),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = disc.usage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AnglesCard(angles: LapidaireAngles) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = angles.coupe,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            AnglesRow(stringResource(R.string.lapidaire_angle_crown_label), angles.couronne)
            AnglesRow(stringResource(R.string.lapidaire_angle_pavilion_label), angles.pavillon)
            AnglesRow(stringResource(R.string.lapidaire_angle_table_label), angles.table)
            AnglesRow(stringResource(R.string.lapidaire_angle_facets_label), angles.facettes)
            Text(
                text = stringResource(R.string.lapidaire_angle_source_label, angles.source),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AnglesRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun IndexCard(entry: LapidaireIndexEntry) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.lapidaire_index_title_format, entry.index),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            AnglesRow(stringResource(R.string.lapidaire_index_rotation_label), entry.rotationParCran)
            Text(
                text = entry.cotesTaillables,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OptiqueCard(entry: LapidaireOptiqueEntry) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = entry.pierre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            AnglesRow(stringResource(R.string.lapidaire_angle_critique_label), entry.angleCritique)
            AnglesRow(stringResource(R.string.lapidaire_angle_extinction_label), entry.angleExtinction)
        }
    }
}

@Composable
private fun DefautCard(defaut: LapidaireDefaut) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = defaut.probleme,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.lapidaire_defaut_cause_label, defaut.cause),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.lapidaire_defaut_remede_label, defaut.remede),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DiagramCard(diagram: LapidaireDiagram) {
    val credit = LapidaireDiagrams.creditFor(diagram.id)
    val painter = rememberSampledDrawablePainter(credit?.drawableName, 220.dp)
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (painter != null) {
                Image(
                    painter = painter,
                    contentDescription = diagram.legende,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
            }
            Text(
                text = diagram.legende,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (credit != null) {
                Text(
                    text = stringResource(R.string.lapidaire_diagram_credit_format, credit.author, credit.license),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TipCard(tip: LapidaireTip) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Filled.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(
                text = tip.texte,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LapidaireDisclaimer(title: String, body: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
}
