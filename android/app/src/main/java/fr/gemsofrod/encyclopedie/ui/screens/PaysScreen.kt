package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Map
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.CountryCoordinates
import fr.gemsofrod.encyclopedie.data.GemOrigins
import fr.gemsofrod.encyclopedie.ui.components.ApproximateLocationCaption
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField
import fr.gemsofrod.encyclopedie.ui.components.OriginsMap
import fr.gemsofrod.encyclopedie.ui.localizedLabel

/**
 * Liste des pays d'origine du catalogue, dérivés automatiquement du champ
 * `origines` de chaque [fr.gemsofrod.encyclopedie.data.Gem].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaysListScreen(onPaysClick: (String) -> Unit, onMapClick: () -> Unit, onBackClick: () -> Unit) {
    val groups = GemOrigins.groups()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.gemmologie_pays_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = onMapClick) {
                        Icon(Icons.Filled.Map, contentDescription = stringResource(R.string.pays_map_action))
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
        val localizedGroups = groups.map { (country, gems) -> Triple(country, localizedLabel(country), gems.size) }
        val displayedGroups = if (query.isBlank()) {
            localizedGroups
        } else {
            localizedGroups.filter { (_, localizedName, _) -> localizedName.contains(query, ignoreCase = true) }
        }

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            CatalogSearchField(
                query = query,
                onQueryChange = { query = it },
                placeholder = stringResource(R.string.catalog_search_placeholder)
            )
            if (query.isNotBlank() && displayedGroups.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Text(
                        text = stringResource(R.string.catalog_search_no_results, query),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayedGroups) { (country, localizedName, count) ->
                        PaysRow(name = localizedName, count = count, onClick = { onPaysClick(country) })
                    }
                }
            }
        }
    }
}

@Composable
private fun PaysRow(name: String, count: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = pluralStringResource(R.plurals.gem_count, count, count),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Vue carte de l'écran Pays : un repère par pays représenté dans le
 * catalogue (centre approximatif, voir [CountryCoordinates]) ; appuyer sur
 * un repère ouvre la liste des gemmes de ce pays, comme une ligne de
 * [PaysListScreen].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaysMapScreen(onPaysClick: (String) -> Unit, onBackClick: () -> Unit) {
    val countries = remember { GemOrigins.groups().map { (country, _) -> country } }
    // GeoPoint.label est forcé au nom de pays non localisé (clé de
    // GemOrigins), pas au nom affiché dans la liste : il sert ici de clé de
    // retour pour la navigation (onPaysClick attend ce même nom de pays).
    val points = remember(countries) {
        countries.mapNotNull { country -> CountryCoordinates.of(country)?.copy(label = country) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.gemmologie_pays_title)) },
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
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OriginsMap(
                points = points,
                mapHeight = 520.dp,
                onMarkerClick = { point -> onPaysClick(point.label) }
            )
            ApproximateLocationCaption()
        }
    }
}
