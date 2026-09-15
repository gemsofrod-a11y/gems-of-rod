@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package fr.gemsofrod.assistant.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.gemsofrod.assistant.network.PendingItem

@Composable
fun PendingScreen(
    pendingItems: List<PendingItem>,
    onBack: () -> Unit,
    onApprove: (id: String, editedReply: String?) -> Unit,
    onReject: (id: String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emails en attente") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
            )
        },
    ) { padding ->
        if (pendingItems.isEmpty()) {
            Column(modifier = Modifier.padding(padding).padding(24.dp)) {
                Text("Aucun email complexe en attente pour le moment.")
            }
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(pendingItems, key = { it.id }) { item ->
                PendingCard(item, onApprove = onApprove, onReject = onReject)
            }
        }
    }
}

@Composable
private fun PendingCard(
    item: PendingItem,
    onApprove: (id: String, editedReply: String?) -> Unit,
    onReject: (id: String) -> Unit,
) {
    var replyText by remember(item.id) { mutableStateOf(item.suggested_reply ?: "") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.subject.ifBlank { "(sans objet)" }, style = MaterialTheme.typography.titleMedium)
            Text(item.from_addr, style = MaterialTheme.typography.bodySmall)
            Text(item.snippet, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            Text("Pourquoi c'est en attente : ${item.reasoning}", style = MaterialTheme.typography.bodySmall)
            if (item.suggested_reply != null) {
                OutlinedTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    label = { Text("Réponse proposée") },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    minLines = 3,
                )
            }
            Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onApprove(item.id, replyText.takeIf { it.isNotBlank() }) }) {
                    Text("Approuver et envoyer")
                }
                OutlinedButton(onClick = { onReject(item.id) }) {
                    Text("Rejeter")
                }
            }
        }
    }
}
