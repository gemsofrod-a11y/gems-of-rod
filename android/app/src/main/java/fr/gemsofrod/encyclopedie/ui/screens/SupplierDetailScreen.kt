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
import fr.gemsofrod.encyclopedie.data.StockItem
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.data.SupplierRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val supplierDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

/**
 * Fiche fournisseur : coordonnées et historique d'approvisionnement. Ce
 * dernier se calcule par correspondance de nom (insensible à la casse) entre
 * [fr.gemsofrod.encyclopedie.data.Supplier.nom] et [StockItem.fournisseur] —
 * il n'existe pas de lien direct (pas de supplierId sur [StockItem]) : un nom
 * mal orthographié lors d'une entrée de stock n'apparaîtra pas ici. Voir le
 * disclaimer affiché à l'écran — même patron que [ClientDetailScreen].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierDetailScreen(
    supplierId: String,
    onEditClick: (String) -> Unit,
    onDeleted: () -> Unit,
    onBackClick: () -> Unit,
    onStockItemClick: (String) -> Unit
) {
    val supplier = remember(supplierId) { SupplierRepository.supplierById(supplierId) }

    if (supplier == null) {
        LaunchedEffect(Unit) { onBackClick() }
        return
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.FRANCE) }

    val suppliedItems = remember(supplier) {
        StockRepository.allItems()
            .filter { it.fournisseur.isNotBlank() && it.fournisseur.equals(supplier.nom, ignoreCase = true) }
            .sortedByDescending { it.dateAchatMillis ?: it.createdAtMillis }
    }
    val totalCost = suppliedItems.sumOf { it.coutAchat ?: 0.0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(supplier.nom) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(supplier.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.supplier_edit_button))
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.supplier_delete_button))
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
                    SupplierDetailRow(stringResource(R.string.supplier_field_type_label), stringResource(supplier.type.labelRes))
                    SupplierDetailRow(stringResource(R.string.client_field_telephone_label), supplier.telephone)
                    SupplierDetailRow(stringResource(R.string.client_field_email_label), supplier.email)
                    SupplierDetailRow(stringResource(R.string.supplier_field_pays_label), supplier.pays)
                }
            }

            if (supplier.notes.isNotBlank()) {
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
                            text = supplier.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.supplier_supplied_section_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.supplier_supplied_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (suppliedItems.isEmpty()) {
                Text(
                    text = stringResource(R.string.supplier_supplied_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = stringResource(R.string.supplier_supplied_total_format, suppliedItems.size, currencyFormat.format(totalCost)),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                suppliedItems.forEach { item ->
                    SupplierSuppliedRow(item = item, currencyFormat = currencyFormat, onClick = { onStockItemClick(item.id) })
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.supplier_delete_confirm_title)) },
            text = { Text(stringResource(R.string.supplier_delete_confirm_message)) },
            confirmButton = {
                TextButton(onClick = {
                    SupplierRepository.deleteSupplier(supplier.id)
                    showDeleteConfirm = false
                    onDeleted()
                }) {
                    Text(stringResource(R.string.supplier_delete_confirm_action))
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
private fun SupplierDetailRow(label: String, value: String) {
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
private fun SupplierSuppliedRow(item: StockItem, currencyFormat: NumberFormat, onClick: () -> Unit) {
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
                item.dateAchatMillis?.let {
                    Text(
                        text = supplierDateFormat.format(Date(it)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item.coutAchat?.let { cost ->
                Text(
                    text = currencyFormat.format(cost),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
