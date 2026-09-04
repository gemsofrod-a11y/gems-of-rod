package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.StockCsvExporter
import fr.gemsofrod.encyclopedie.data.StockItem
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.data.StockStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private enum class SalesPeriod { THIS_MONTH, LAST_3_MONTHS, THIS_YEAR, ALL }

private fun SalesPeriod.cutoffMillis(): Long {
    val cal = Calendar.getInstance()
    return when (this) {
        SalesPeriod.THIS_MONTH -> {
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
        SalesPeriod.LAST_3_MONTHS -> {
            cal.add(Calendar.MONTH, -3)
            cal.timeInMillis
        }
        SalesPeriod.THIS_YEAR -> {
            cal.set(Calendar.DAY_OF_YEAR, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
        SalesPeriod.ALL -> 0L
    }
}

private fun SalesPeriod.labelRes(): Int = when (this) {
    SalesPeriod.THIS_MONTH -> R.string.stock_sales_period_this_month
    SalesPeriod.LAST_3_MONTHS -> R.string.stock_sales_period_last_3_months
    SalesPeriod.THIS_YEAR -> R.string.stock_sales_period_this_year
    SalesPeriod.ALL -> R.string.stock_sales_period_all
}

/**
 * Historique des pierres vendues (statut [StockStatus.VENDU]), filtrable par
 * période, avec le nombre de ventes et le chiffre d'affaires correspondant —
 * complète le [StockDashboardScreen], qui ne montre qu'un instantané du
 * stock actuel, pas une évolution dans le temps.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockSalesHistoryScreen(onBackClick: () -> Unit, onItemClick: (String) -> Unit) {
    var selectedPeriod by remember { mutableStateOf(SalesPeriod.THIS_MONTH) }
    val cutoff = remember(selectedPeriod) { selectedPeriod.cutoffMillis() }

    val soldItems = StockRepository.allItems()
        .filter { it.statut == StockStatus.VENDU }
        .filter { (it.venteDateMillis ?: it.createdAtMillis) >= cutoff }
        .sortedByDescending { it.venteDateMillis ?: it.createdAtMillis }

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.FRANCE) }
    val total = soldItems.sumOf { it.prixVente ?: 0.0 }
    val itemsWithMargin = soldItems.filter { it.coutAchat != null && it.prixVente != null }
    val totalMargin = itemsWithMargin.sumOf { (it.prixVente ?: 0.0) - (it.coutAchat ?: 0.0) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isExporting by remember { mutableStateOf(false) }
    val exportChooserTitle = stringResource(R.string.stock_sales_export_chooser_title)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.stock_sales_history_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(
                        enabled = !isExporting && soldItems.isNotEmpty(),
                        onClick = {
                            isExporting = true
                            scope.launch {
                                val file = withContext(Dispatchers.IO) {
                                    StockCsvExporter.exportSalesCsvFile(context, soldItems)
                                }
                                isExporting = false
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/csv"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(sendIntent, exportChooserTitle))
                            }
                        }
                    ) {
                        if (isExporting) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Filled.IosShare, contentDescription = stringResource(R.string.stock_sales_export_button))
                        }
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
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SalesPeriod.entries.forEach { period ->
                    FilterChip(
                        selected = period == selectedPeriod,
                        onClick = { selectedPeriod = period },
                        label = { Text(stringResource(period.labelRes())) }
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.stock_sales_history_total_format, soldItems.size, currencyFormat.format(total)),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (itemsWithMargin.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.stock_sales_history_margin_format, currencyFormat.format(totalMargin)),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = stringResource(R.string.stock_sales_history_margin_hint),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (soldItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.stock_sales_history_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    soldItems.forEach { item ->
                        SoldItemRow(item = item, currencyFormat = currencyFormat, onClick = { onItemClick(item.id) })
                    }
                }
            }
        }
    }
}

private val historyDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

@Composable
private fun SoldItemRow(item: StockItem, currencyFormat: NumberFormat, onClick: () -> Unit) {
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
                val details = listOfNotNull(
                    item.acheteurNom.takeIf { it.isNotBlank() },
                    item.venteDateMillis?.let { historyDateFormat.format(Date(it)) }
                ).joinToString(" · ")
                if (details.isNotBlank()) {
                    Text(
                        text = details,
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
