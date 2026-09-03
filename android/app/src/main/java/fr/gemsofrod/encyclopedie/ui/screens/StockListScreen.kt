package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.StockCsvExporter
import fr.gemsofrod.encyclopedie.data.StockCsvImportResult
import fr.gemsofrod.encyclopedie.data.StockCsvImporter
import fr.gemsofrod.encyclopedie.data.StockItem
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.data.StockStatus
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField
import fr.gemsofrod.encyclopedie.ui.rememberStockPhotoBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

/**
 * Liste du stock professionnel, de la fiche la plus récemment ajoutée à la
 * plus ancienne — même principe que [LabNotebookScreen], mais pour une
 * gestion commerciale (statut, prix) plutôt qu'un journal d'observations.
 * L'export CSV suit le même patron que le partage PDF de [CertificateScreen] :
 * génération sur IO puis partage via [FileProvider].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    onItemClick: (String) -> Unit,
    onAddClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val items = StockRepository.allItems()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isExporting by remember { mutableStateOf(false) }
    var isImporting by remember { mutableStateOf(false) }
    var importResult by remember { mutableStateOf<StockCsvImportResult?>(null) }
    val exportChooserTitle = stringResource(R.string.stock_export_chooser_title)

    val importPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null || isImporting) return@rememberLauncherForActivityResult
        isImporting = true
        scope.launch {
            val result = withContext(Dispatchers.IO) { StockCsvImporter.import(context, uri) }
            isImporting = false
            importResult = result
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.stock_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = onDashboardClick) {
                        Icon(Icons.Filled.Assessment, contentDescription = stringResource(R.string.stock_dashboard_button))
                    }
                    IconButton(
                        onClick = { if (!isImporting) importPicker.launch("*/*") },
                        enabled = !isImporting
                    ) {
                        if (isImporting) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Filled.UploadFile, contentDescription = stringResource(R.string.stock_import_button))
                        }
                    }
                    IconButton(
                        onClick = {
                            if (isExporting || items.isEmpty()) return@IconButton
                            isExporting = true
                            scope.launch {
                                val file = withContext(Dispatchers.IO) {
                                    StockCsvExporter.exportZip(context, items)
                                }
                                isExporting = false
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "application/zip"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(sendIntent, exportChooserTitle))
                            }
                        },
                        enabled = !isExporting && items.isNotEmpty()
                    ) {
                        if (isExporting) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Filled.IosShare, contentDescription = stringResource(R.string.stock_export_button))
                        }
                    }
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.stock_add_button))
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
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.stock_empty_state),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            var query by remember { mutableStateOf("") }
            var selectedStatuses by remember { mutableStateOf(setOf<StockStatus>()) }
            var minPriceText by remember { mutableStateOf("") }
            var maxPriceText by remember { mutableStateOf("") }
            val minPrice = minPriceText.toDoubleOrNull()
            val maxPrice = maxPriceText.toDoubleOrNull()
            val hasActiveFilters = selectedStatuses.isNotEmpty() || minPrice != null || maxPrice != null

            val displayedItems = items.filter { item ->
                val matchesQuery = query.isBlank() ||
                    item.nom.contains(query, ignoreCase = true) ||
                    item.reference.contains(query, ignoreCase = true)
                val matchesStatus = selectedStatuses.isEmpty() || item.statut in selectedStatuses
                val matchesMinPrice = minPrice == null || (item.prixVente != null && item.prixVente >= minPrice)
                val matchesMaxPrice = maxPrice == null || (item.prixVente != null && item.prixVente <= maxPrice)
                matchesQuery && matchesStatus && matchesMinPrice && matchesMaxPrice
            }

            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                CatalogSearchField(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = stringResource(R.string.stock_search_placeholder)
                )
                StockFilterBar(
                    selectedStatuses = selectedStatuses,
                    onToggleStatus = { status ->
                        selectedStatuses = if (status in selectedStatuses) selectedStatuses - status else selectedStatuses + status
                    },
                    minPriceText = minPriceText,
                    onMinPriceChange = { minPriceText = it },
                    maxPriceText = maxPriceText,
                    onMaxPriceChange = { maxPriceText = it },
                    hasActiveFilters = hasActiveFilters,
                    onClearFilters = {
                        selectedStatuses = emptySet()
                        minPriceText = ""
                        maxPriceText = ""
                    }
                )
                if ((query.isNotBlank() || hasActiveFilters) && displayedItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Text(
                            text = if (query.isNotBlank()) {
                                stringResource(R.string.catalog_search_no_results, query)
                            } else {
                                stringResource(R.string.stock_filter_no_results)
                            },
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
                        displayedItems.forEach { item ->
                            StockItemRow(item = item, onClick = { onItemClick(item.id) })
                        }
                    }
                }
            }
        }
    }

    importResult?.let { result ->
        AlertDialog(
            onDismissRequest = { importResult = null },
            title = { Text(stringResource(R.string.stock_import_result_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (result.imported == 0 && result.skipped == 0) {
                        Text(stringResource(R.string.stock_import_empty_error))
                    } else {
                        Text(stringResource(R.string.stock_import_count_format, result.imported))
                        if (result.skipped > 0) {
                            Text(stringResource(R.string.stock_import_skipped_format, result.skipped))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { importResult = null }) {
                    Text(stringResource(R.string.stock_import_ok_button))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockFilterBar(
    selectedStatuses: Set<StockStatus>,
    onToggleStatus: (StockStatus) -> Unit,
    minPriceText: String,
    onMinPriceChange: (String) -> Unit,
    maxPriceText: String,
    onMaxPriceChange: (String) -> Unit,
    hasActiveFilters: Boolean,
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StockStatus.entries.forEach { status ->
                FilterChip(
                    selected = status in selectedStatuses,
                    onClick = { onToggleStatus(status) },
                    label = { Text(stringResource(statusFilterLabelRes(status))) }
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = minPriceText,
                onValueChange = onMinPriceChange,
                label = { Text(stringResource(R.string.stock_filter_price_min_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = maxPriceText,
                onValueChange = onMaxPriceChange,
                label = { Text(stringResource(R.string.stock_filter_price_max_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.weight(1f)
            )
        }
        if (hasActiveFilters) {
            TextButton(onClick = onClearFilters) {
                Text(stringResource(R.string.stock_filter_clear_button))
            }
        }
    }
}

private fun statusFilterLabelRes(status: StockStatus): Int = when (status) {
    StockStatus.EN_STOCK -> R.string.stock_status_en_stock
    StockStatus.RESERVE -> R.string.stock_status_reserve
    StockStatus.VENDU -> R.string.stock_status_vendu
    StockStatus.CONSIGNATION -> R.string.stock_status_consignation
}

@Composable
private fun StockItemRow(item: StockItem, onClick: () -> Unit) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val bitmap = rememberStockPhotoBitmap(item.photoFileName, 48.dp)
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = item.nom,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Diamond, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val details = listOfNotNull(
                    item.poidsCarats?.let { stringResource(R.string.stock_weight_ct_format, it) },
                    item.couleur.takeIf { it.isNotBlank() }
                ).joinToString(" · ")
                if (details.isNotBlank()) {
                    Text(
                        text = details,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                StockStatusBadge(status = item.statut)
                item.prixVente?.let { price ->
                    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.FRANCE) }
                    Text(
                        text = currencyFormat.format(price),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun StockStatusBadge(status: StockStatus) {
    val (labelRes, containerColor) = when (status) {
        StockStatus.EN_STOCK -> R.string.stock_status_en_stock to MaterialTheme.colorScheme.primaryContainer
        StockStatus.RESERVE -> R.string.stock_status_reserve to MaterialTheme.colorScheme.tertiaryContainer
        StockStatus.VENDU -> R.string.stock_status_vendu to MaterialTheme.colorScheme.surfaceVariant
        StockStatus.CONSIGNATION -> R.string.stock_status_consignation to MaterialTheme.colorScheme.secondaryContainer
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
