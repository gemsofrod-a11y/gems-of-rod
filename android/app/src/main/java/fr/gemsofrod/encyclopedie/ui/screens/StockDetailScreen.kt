package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.StockItem
import fr.gemsofrod.encyclopedie.data.StockPhotoStorage
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.data.StockStatus
import fr.gemsofrod.encyclopedie.ui.rememberStockPhotoBitmap
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

private val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

/**
 * Fiche détail d'une pierre en stock : toutes les informations saisies,
 * édition et suppression — même patron que [LabNotebookDetailScreen], pour
 * une gestion commerciale plutôt qu'un journal d'observations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailScreen(
    itemId: String,
    onEditClick: (String) -> Unit,
    onDeleted: () -> Unit,
    onBackClick: () -> Unit
) {
    val item = remember(itemId) { StockRepository.itemById(itemId) }

    if (item == null) {
        LaunchedEffect(Unit) { onBackClick() }
        return
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val bitmap = rememberStockPhotoBitmap(item.photoFileName, 320.dp)
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.FRANCE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.nom) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(item.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.stock_edit_button))
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.stock_delete_button))
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
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = item.nom,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            StockStatusChip(status = item.statut)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item.poidsCarats?.let { StockDetailRow(stringResource(R.string.stock_field_weight_label), "$it ct") }
                    StockDetailRow(stringResource(R.string.stock_field_cut_label), item.taille)
                    StockDetailRow(stringResource(R.string.stock_field_color_label), item.couleur)
                    StockDetailRow(stringResource(R.string.stock_field_clarity_label), item.purete)
                    StockDetailRow(stringResource(R.string.stock_field_treatment_label), item.traitement)
                    StockDetailRow(stringResource(R.string.stock_field_certificate_number_label), item.certificatNumero)
                    StockDetailRow(stringResource(R.string.stock_field_laboratory_label), item.laboratoire)
                    StockDetailRow(stringResource(R.string.stock_field_supplier_label), item.fournisseur)
                    item.dateAchatMillis?.let {
                        StockDetailRow(stringResource(R.string.stock_field_purchase_date_label), displayDateFormat.format(it))
                    }
                    item.coutAchat?.let { StockDetailRow(stringResource(R.string.stock_field_purchase_cost_label), currencyFormat.format(it)) }
                    item.prixVente?.let { StockDetailRow(stringResource(R.string.stock_field_selling_price_label), currencyFormat.format(it)) }
                }
            }

            if (item.notes.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.stock_field_notes_label),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = item.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.stock_delete_confirm_title)) },
            text = { Text(stringResource(R.string.stock_delete_confirm_message)) },
            confirmButton = {
                TextButton(onClick = {
                    StockPhotoStorage.deletePhoto(context, item.photoFileName)
                    StockRepository.deleteItem(item.id)
                    showDeleteConfirm = false
                    onDeleted()
                }) {
                    Text(stringResource(R.string.stock_delete_confirm_action))
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
private fun StockDetailRow(label: String, value: String) {
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
private fun StockStatusChip(status: StockStatus) {
    val labelRes = when (status) {
        StockStatus.EN_STOCK -> R.string.stock_status_en_stock
        StockStatus.RESERVE -> R.string.stock_status_reserve
        StockStatus.VENDU -> R.string.stock_status_vendu
        StockStatus.CONSIGNATION -> R.string.stock_status_consignation
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
