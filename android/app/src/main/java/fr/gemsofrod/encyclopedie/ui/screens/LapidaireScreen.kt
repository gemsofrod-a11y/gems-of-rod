package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.LapidaireAngles
import fr.gemsofrod.encyclopedie.data.LapidaireComponent
import fr.gemsofrod.encyclopedie.data.LapidaireDiagram
import fr.gemsofrod.encyclopedie.data.LapidaireDiagrams
import fr.gemsofrod.encyclopedie.data.LapidaireDisc
import fr.gemsofrod.encyclopedie.data.LapidaireInfo
import fr.gemsofrod.encyclopedie.data.LapidaireTip
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField
import fr.gemsofrod.encyclopedie.ui.rememberSampledDrawablePainter

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

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            CatalogSearchField(
                query = query,
                onQueryChange = { query = it },
                placeholder = stringResource(R.string.catalog_search_placeholder)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                        SectionHeader(page.machinesTitle)
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
                        SectionHeader(page.disquesTitle)
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
                    SectionHeader(page.anglesTitle)
                    Text(
                        text = page.anglesIntro,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    page.angles.forEach { AnglesCard(it) }

                    SectionHeader(page.diagrammesTitle)
                    page.diagrammes.forEach { DiagramCard(it) }

                    SectionHeader(page.tipsTitle)
                    page.tips.forEach { TipCard(it) }

                    LapidaireDisclaimer(title = page.disclaimerTitle, body = page.disclaimerBody)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
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
