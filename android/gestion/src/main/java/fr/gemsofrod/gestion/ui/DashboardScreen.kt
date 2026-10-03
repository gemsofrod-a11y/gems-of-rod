package fr.gemsofrod.gestion.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Stats

@Composable
fun DashboardScreen(
    data: AppData,
    onOpenOrder: (String) -> Unit,
    onOpenProduct: (String) -> Unit,
    onNewProduct: () -> Unit,
    onLoadSample: () -> Unit,
) {
    val stats = remember(data) { Stats(data) }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (data.products.isEmpty() && data.orders.isEmpty()) {
            SectionCard("Bienvenue") {
                Text(
                    "Ajoutez vos pierres et bijoux dans le stock, puis enregistrez vos commandes : " +
                        "le tableau de bord et les graphiques se remplissent tout seuls.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onNewProduct) { Text("Ajouter un produit") }
                    OutlinedButton(onClick = onLoadSample) { Text("Voir un exemple") }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KpiTile(
                "CA du mois", eurosRound(stats.revenueThisMonth),
                stats.revenueTrend?.let { "${if (it >= 0) "+" else ""}${it.toInt()} % vs mois dernier" }
                    ?: "Mois dernier : ${eurosRound(stats.revenueLastMonth)}",
                Modifier.weight(1f),
            )
            KpiTile("Marge du mois", eurosRound(stats.marginThisMonth), "Vente − coût d'achat", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KpiTile(
                "Commandes en cours", stats.openOrders.toString(),
                if (stats.quotes > 0) "${stats.quotes} devis en attente" else "Aucun devis en attente",
                Modifier.weight(1f),
            )
            KpiTile("Valeur du stock", eurosRound(stats.stockValueAtCost), "Prix de vente : ${eurosShort(stats.stockValueAtPrice)}", Modifier.weight(1f))
        }

        SectionCard("Chiffre d'affaires · 6 derniers mois") {
            BarChart(stats.revenueByMonth)
        }

        if (stats.salesByCategory.isNotEmpty()) {
            SectionCard("Ventes par catégorie · 12 mois") {
                DonutChart(stats.salesByCategory)
            }
        }

        if (stats.stockByCategory.isNotEmpty()) {
            SectionCard("Stock par catégorie · prix d'achat") {
                HorizontalBars(stats.stockByCategory)
            }
        }

        val open = data.orders.filter { it.isOpen }.sortedBy { it.date }
        if (open.isNotEmpty()) {
            SectionCard("À traiter") {
                open.take(5).forEachIndexed { i, order ->
                    if (i > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ListRow(
                        title = "N° ${order.number} · ${order.clientName.ifBlank { "Sans client" }}",
                        subtitle = "${order.status.label} · ${date(order.localDate)}",
                        trailing = euros(order.total),
                        onClick = { onOpenOrder(order.id) },
                    )
                }
                if (stats.depositsToCollect > 0) {
                    Text(
                        "Reste à encaisser : ${euros(stats.depositsToCollect)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }

        if (stats.lowStock.isNotEmpty()) {
            SectionCard("Stock bas") {
                stats.lowStock.take(6).forEachIndexed { i, p ->
                    if (i > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ListRow(
                        title = p.name,
                        subtitle = "Seuil d'alerte : ${qty(p.threshold, p.unit)}",
                        trailing = qty(p.quantity, p.unit),
                        trailingAlert = p.quantity <= 0,
                        onClick = { onOpenProduct(p.id) },
                    )
                }
            }
        }
        androidx.compose.foundation.layout.Spacer(Modifier.padding(bottom = 16.dp))
    }
}

@Composable
private fun KpiTile(label: String, value: String, sub: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 4.dp),
            )
            Text(sub, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
        }
    }
}

/** Ligne de liste sobre : titre, sous-titre, valeur à droite. */
@Composable
fun ListRow(
    title: String,
    subtitle: String?,
    trailing: String?,
    onClick: () -> Unit,
    trailingAlert: Boolean = false,
    badge: (@Composable () -> Unit)? = null,
) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 12.dp)) {
            if (trailing != null) {
                Text(
                    trailing,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (trailingAlert) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                )
            }
            if (badge != null) {
                androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 4.dp))
                badge()
            }
        }
    }
}
