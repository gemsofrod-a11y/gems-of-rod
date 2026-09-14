package fr.gemsofrod.assistant.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.gemsofrod.assistant.AssistantUiState
import fr.gemsofrod.assistant.ChatLine

@Composable
fun HomeScreen(
    state: AssistantUiState,
    onMicClick: () -> Unit,
    onOpenPending: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gems of Rod — Assistant") },
                actions = {
                    BadgedBox(
                        badge = { if (state.pendingCount > 0) Badge { Text("${state.pendingCount}") } },
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(
                            "En attente",
                            modifier = Modifier.padding(8.dp).clickable(onClick = onOpenPending),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Réglages")
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onMicClick,
                icon = { Icon(Icons.Filled.Mic, contentDescription = null) },
                text = { Text(if (state.isBusy) "Écoute en cours..." else "Parler") },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (!state.isConfigured) {
                Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Configurez d'abord l'adresse de votre serveur assistant dans Réglages.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
            if (state.pendingCount > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "${state.pendingCount} email(s) complexe(s) attendent votre décision.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            "Ouvrez l'onglet « En attente » pour y répondre.",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    ExtendedFloatingActionButton(onClick = onOpenPending, text = { Text("Voir les emails en attente") })
                }
            }

            Transcript(state.transcript, modifier = Modifier.weight(1f))

            if (state.isBusy) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun Transcript(lines: List<ChatLine>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(lines) { line ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        if (line.fromUser) "Vous" else "Assistant",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(line.text, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
