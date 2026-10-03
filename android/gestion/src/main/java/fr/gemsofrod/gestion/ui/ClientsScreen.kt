package fr.gemsofrod.gestion.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Client
import fr.gemsofrod.gestion.data.Segment
import fr.gemsofrod.gestion.data.newId
import fr.gemsofrod.gestion.data.spentByClient

@Composable
fun ClientsScreen(data: AppData, onOpen: (String) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf<Segment?>(null) }
    val spent = remember(data) { data.spentByClient() }
    val list = data.clients
        .filter { filter == null || it.segment == filter }
        .filter { query.isBlank() || it.name.contains(query.trim(), ignoreCase = true) || it.email.contains(query.trim(), ignoreCase = true) }
        .sortedByDescending { spent[it.id] ?: 0.0 }

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Rechercher") },
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            ChoiceChips(null, listOf<Segment?>(null) + Segment.entries, filter, { it?.label ?: "Tous" }) { filter = it }
        }
        if (list.isEmpty()) {
            EmptyState(if (data.clients.isEmpty()) "Aucun client.\nAjoutez-en avec le bouton +." else "Aucun résultat.")
        } else {
            LazyColumn(contentPadding = ListPadding) {
                items(list, key = { it.id }) { c ->
                    val count = data.orders.count { it.clientId == c.id }
                    ListRow(
                        title = c.name,
                        subtitle = "$count commande${if (count > 1) "s" else ""}" + (c.email.takeIf { it.isNotBlank() }?.let { " · $it" } ?: ""),
                        trailing = spent[c.id]?.let { eurosRound(it) },
                        onClick = { onOpen(c.id) },
                        badge = { Pill(c.segment.label, strong = c.segment == Segment.VIP) },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Composable
fun ClientEditor(
    client: Client?,
    data: AppData,
    onSave: (Client) -> Unit,
    onDelete: (String) -> Unit,
    onOpenOrder: (String) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(client?.name ?: "") }
    var email by remember { mutableStateOf(client?.email ?: "") }
    var phone by remember { mutableStateOf(client?.phone ?: "") }
    var segment by remember { mutableStateOf(client?.segment ?: Segment.PROSPECT) }
    var note by remember { mutableStateOf(client?.note ?: "") }

    EditorScaffold(
        title = if (client == null) "Nouveau client" else "Client",
        onBack = onBack,
        onSave = if (name.isNotBlank()) {
            {
                onSave(
                    Client(
                        id = client?.id ?: newId(), name = name.trim(), email = email.trim(),
                        phone = phone.trim(), segment = segment, note = note.trim(),
                    ),
                )
            }
        } else null,
        onDelete = client?.let { c -> { onDelete(c.id) } },
        deleteMessage = "Supprimer ce client ? Ses commandes sont conservées.",
    ) {
        Field("Nom", name, { name = it })
        Field("Email", email, { email = it }, keyboard = KeyboardType.Email)
        Field("Téléphone", phone, { phone = it }, keyboard = KeyboardType.Phone)
        if (client != null && (client.phone.isNotBlank() || client.email.isNotBlank())) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (client.phone.isNotBlank()) {
                    OutlinedButton(onClick = {
                        runCatching {
                            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${client.phone}")))
                        }
                    }) {
                        Icon(Icons.Outlined.Call, null)
                        Text("  Appeler")
                    }
                }
                if (client.email.isNotBlank()) {
                    OutlinedButton(onClick = {
                        runCatching {
                            context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${client.email}")))
                        }
                    }) {
                        Icon(Icons.Outlined.Email, null)
                        Text("  Écrire")
                    }
                }
            }
        }
        ChoiceChips("Segment", Segment.entries, segment, { it.label }) { segment = it }
        Field("Notes (goûts, tailles, occasions…)", note, { note = it }, singleLine = false)

        if (client != null) {
            val orders = data.orders.filter { it.clientId == client.id }.sortedByDescending { it.date }
            if (orders.isNotEmpty()) {
                Text("Commandes", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                orders.forEach { o ->
                    ListRow(
                        title = "N° ${o.number} · ${date(o.localDate)}",
                        subtitle = o.lines.joinToString(", ") { it.label },
                        trailing = euros(o.total),
                        onClick = { onOpenOrder(o.id) },
                        badge = { Pill(o.status.label) },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}
