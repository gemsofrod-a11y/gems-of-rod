package fr.gemsofrod.encyclopedie.ui.screens

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Client
import fr.gemsofrod.encyclopedie.data.ClientRepository
import fr.gemsofrod.encyclopedie.data.StockItem
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.data.StockStatus
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val clientDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

/**
 * Fiche client : coordonnées et historique d'achats. L'historique se
 * calcule par correspondance de nom (insensible à la casse) entre
 * [Client.nom] et [StockItem.acheteurNom] des pierres vendues — il n'existe
 * pas de lien direct (pas de clientId sur [StockItem]) : un nom mal
 * orthographié lors d'une vente n'apparaîtra pas ici. Voir le disclaimer
 * affiché à l'écran.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    clientId: String,
    onEditClick: (String) -> Unit,
    onDeleted: () -> Unit,
    onBackClick: () -> Unit,
    onStockItemClick: (String) -> Unit
) {
    val client = remember(clientId) { ClientRepository.clientById(clientId) }

    if (client == null) {
        LaunchedEffect(Unit) { onBackClick() }
        return
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.FRANCE) }

    val purchases = remember(client) {
        StockRepository.allItems()
            .filter { it.statut == StockStatus.VENDU && it.acheteurNom.isNotBlank() && it.acheteurNom.equals(client.nom, ignoreCase = true) }
            .sortedByDescending { it.venteDateMillis ?: it.createdAtMillis }
    }
    val totalPurchases = purchases.sumOf { it.prixVente ?: 0.0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(client.nom) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(client.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.client_edit_button))
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.client_delete_button))
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ClientDetailRow(stringResource(R.string.client_field_telephone_label), client.telephone)
                    ClientDetailRow(stringResource(R.string.client_field_email_label), client.email)
                    ClientDetailRow(stringResource(R.string.client_field_adresse_label), client.adresse)
                }
            }

            if (client.notes.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.client_field_notes_label),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = client.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.client_purchases_section_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.client_purchases_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (purchases.isEmpty()) {
                Text(
                    text = stringResource(R.string.client_purchases_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = stringResource(R.string.client_purchases_total_format, purchases.size, currencyFormat.format(totalPurchases)),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                purchases.forEach { item ->
                    ClientPurchaseRow(item = item, currencyFormat = currencyFormat, onClick = { onStockItemClick(item.id) })
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.client_delete_confirm_title)) },
            text = { Text(stringResource(R.string.client_delete_confirm_message)) },
            confirmButton = {
                TextButton(onClick = {
                    ClientRepository.deleteClient(client.id)
                    showDeleteConfirm = false
                    onDeleted()
                }) {
                    Text(stringResource(R.string.client_delete_confirm_action))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text(stringResource(R.string.stock_cancel_button))
                }
            }
        )
    }
}

@Composable
private fun ClientDetailRow(label: String, value: String) {
    if (value.isBlank()) return
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ClientPurchaseRow(item: StockItem, currencyFormat: NumberFormat, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                item.venteDateMillis?.let {
                    Text(
                        text = clientDateFormat.format(Date(it)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item.prixVente?.let { price ->
                Text(
                    text = currencyFormat.format(price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
