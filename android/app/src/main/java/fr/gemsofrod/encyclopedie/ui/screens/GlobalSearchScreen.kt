package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.ClientRepository
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.data.SupplierRepository
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField
import fr.gemsofrod.encyclopedie.ui.localized

/**
 * Recherche unique couvrant l'encyclopédie, le stock, le carnet clients et
 * le répertoire fournisseurs — plutôt que la recherche propre à chaque
 * écran ([StockListScreen], [ClientListScreen], [SupplierListScreen]...),
 * pour retrouver rapidement une pierre ou un contact sans savoir d'avance
 * dans quelle section elle se trouve.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchScreen(
    onGemClick: (String) -> Unit,
    onStockItemClick: (String) -> Unit,
    onClientClick: (String) -> Unit,
    onSupplierClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    val gemResults = if (query.isBlank()) {
        emptyList()
    } else {
        GemsRepository.gems.map { it.localized() }.filter {
            it.nom.contains(query, ignoreCase = true) || it.famille.contains(query, ignoreCase = true)
        }
    }
    val stockResults = if (query.isBlank()) {
        emptyList()
    } else {
        StockRepository.allItems().filter {
            it.nom.contains(query, ignoreCase = true) || it.reference.contains(query, ignoreCase = true)
        }
    }
    val clientResults = if (query.isBlank()) emptyList() else ClientRepository.allClients().filter { it.nom.contains(query, ignoreCase = true) }
    val supplierResults = if (query.isBlank()) emptyList() else SupplierRepository.allSuppliers().filter { it.nom.contains(query, ignoreCase = true) }
    val hasResults = gemResults.isNotEmpty() || stockResults.isNotEmpty() || clientResults.isNotEmpty() || supplierResults.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.global_search_title)) },
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
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            CatalogSearchField(
                query = query,
                onQueryChange = { query = it },
                placeholder = stringResource(R.string.global_search_placeholder)
            )

            when {
                query.isBlank() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Text(
                        text = stringResource(R.string.global_search_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                !hasResults -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Text(
                        text = stringResource(R.string.catalog_search_no_results, query),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (gemResults.isNotEmpty()) {
                        SearchSectionHeader(stringResource(R.string.global_search_section_encyclopedie))
                        gemResults.forEach { gem ->
                            GlobalSearchRow(title = gem.nom, subtitle = gem.famille, onClick = { onGemClick(gem.id) })
                        }
                    }
                    if (stockResults.isNotEmpty()) {
                        SearchSectionHeader(stringResource(R.string.global_search_section_stock))
                        stockResults.forEach { item ->
                            GlobalSearchRow(title = item.nom, subtitle = item.reference, onClick = { onStockItemClick(item.id) })
                        }
                    }
                    if (clientResults.isNotEmpty()) {
                        SearchSectionHeader(stringResource(R.string.global_search_section_clients))
                        clientResults.forEach { client ->
                            GlobalSearchRow(title = client.nom, subtitle = client.telephone, onClick = { onClientClick(client.id) })
                        }
                    }
                    if (supplierResults.isNotEmpty()) {
                        SearchSectionHeader(stringResource(R.string.global_search_section_suppliers))
                        supplierResults.forEach { supplier ->
                            GlobalSearchRow(title = supplier.nom, subtitle = stringResource(supplier.type.labelRes), onClick = { onSupplierClick(supplier.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun GlobalSearchRow(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
