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
import androidx.compose.material.icons.filled.Add
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
import fr.gemsofrod.encyclopedie.data.Client
import fr.gemsofrod.encyclopedie.data.ClientRepository
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField

/**
 * Liste du carnet clients, triée par ordre alphabétique — même patron que
 * [StockListScreen], mais pour des coordonnées plutôt qu'un inventaire.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(onClientClick: (String) -> Unit, onAddClick: () -> Unit, onBackClick: () -> Unit) {
    val clients = ClientRepository.allClients()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.clients_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.clients_add_button))
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
        if (clients.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.clients_empty_state),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            var query by remember { mutableStateOf("") }
            val displayed = if (query.isBlank()) {
                clients
            } else {
                clients.filter {
                    it.nom.contains(query, ignoreCase = true) ||
                        it.telephone.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true)
                }
            }

            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                CatalogSearchField(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = stringResource(R.string.clients_search_placeholder)
                )
                if (query.isNotBlank() && displayed.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Text(
                            text = stringResource(R.string.catalog_search_no_results, query),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        displayed.forEach { client ->
                            ClientRow(client = client, onClick = { onClientClick(client.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClientRow(client: Client, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = client.nom,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            val details = listOfNotNull(
                client.telephone.takeIf { it.isNotBlank() },
                client.email.takeIf { it.isNotBlank() }
            ).joinToString(" · ")
            if (details.isNotBlank()) {
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
