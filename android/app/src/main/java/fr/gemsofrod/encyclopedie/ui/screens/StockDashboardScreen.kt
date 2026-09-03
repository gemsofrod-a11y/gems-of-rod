package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.StockItem
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.data.StockStatus
import java.text.NumberFormat
import java.util.Locale

/**
 * Vue d'ensemble du stock : valeur totale, coût total d'achat, répartition
 * par statut et alertes simples (fiches sans prix ou sans photo) — calculées
 * à la volée depuis [StockRepository.allItems], sans état persisté propre.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDashboardScreen(onBackClick: () -> Unit) {
    val items = StockRepository.allItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.stock_dashboard_title)) },
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
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.stock_dashboard_empty_state),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.FRANCE) }
            val totalValue = items.sumOf { it.prixVente ?: 0.0 }
            val totalCost = items.sumOf { it.coutAchat ?: 0.0 }
            val noPriceCount = items.count { it.prixVente == null }
            val noPhotoCount = items.count { it.photoFileName == null }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    totalValueText = currencyFormat.format(totalValue),
                    totalCostText = currencyFormat.format(totalCost),
                    itemCount = items.size
                )
                StatusBreakdownCard(items = items)
                AlertsCard(noPriceCount = noPriceCount, noPhotoCount = noPhotoCount)
            }
        }
    }
}

@Composable
private fun SummaryCard(totalValueText: String, totalCostText: String, itemCount: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.stock_dashboard_item_count_format, itemCount),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            SummaryRow(label = stringResource(R.string.stock_dashboard_total_value_label), valueText = totalValueText)
            SummaryRow(label = stringResource(R.string.stock_dashboard_total_cost_label), valueText = totalCostText)
        }
    }
}

@Composable
private fun SummaryRow(label: String, valueText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = valueText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

private fun statusColor(status: StockStatus): Color = when (status) {
    StockStatus.EN_STOCK -> Color(0xFF4C8C4A)
    StockStatus.RESERVE -> Color(0xFFC69B3A)
    StockStatus.VENDU -> Color(0xFF8A8A8A)
    StockStatus.CONSIGNATION -> Color(0xFF5C7CBE)
}

private fun statusLabelRes(status: StockStatus): Int = when (status) {
    StockStatus.EN_STOCK -> R.string.stock_status_en_stock
    StockStatus.RESERVE -> R.string.stock_status_reserve
    StockStatus.VENDU -> R.string.stock_status_vendu
    StockStatus.CONSIGNATION -> R.string.stock_status_consignation
}

@Composable
private fun StatusBreakdownCard(items: List<StockItem>) {
    val counts = StockStatus.entries.associateWith { status -> items.count { it.statut == status } }
    val maxCount = (counts.values.maxOrNull() ?: 0).coerceAtLeast(1)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.stock_dashboard_status_section_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            StockStatus.entries.forEach { status ->
                val count = counts[status] ?: 0
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(statusLabelRes(status)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = count.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(count / maxCount.toFloat())
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(statusColor(status))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertsCard(noPriceCount: Int, noPhotoCount: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = stringResource(R.string.stock_dashboard_alerts_section_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            if (noPriceCount == 0 && noPhotoCount == 0) {
                Text(
                    text = stringResource(R.string.stock_dashboard_no_alerts),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            } else {
                if (noPriceCount > 0) {
                    Text(
                        text = stringResource(R.string.stock_dashboard_alert_no_price_format, noPriceCount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                if (noPhotoCount > 0) {
                    Text(
                        text = stringResource(R.string.stock_dashboard_alert_no_photo_format, noPhotoCount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}
