package fr.gemsofrod.gestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Order
import fr.gemsofrod.gestion.data.OrderStatus
import fr.gemsofrod.gestion.data.Stats
import java.time.LocalDate

/** Titre de page : grand titre, sous-titre gris, bouton d'action à droite. */
@Composable
fun PageHeader(title: String, subtitle: String, action: String?, onAction: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, fontFamily = Display, fontSize = 32.sp, fontWeight = FontWeight.Medium, color = Palette.Ink)
            Text(subtitle, fontSize = 13.sp, color = Palette.Muted)
        }
        if (action != null) PillButton(action, Icons.Outlined.Add, onAction)
    }
}

private enum class DashTab(val label: String) { IMPAYEES("Impayées"), EN_RETARD("En retard"), DEVIS("Devis") }

@Composable
fun DashboardScreen(
    data: AppData,
    onOpenOrder: (String) -> Unit,
    onOpenProduct: (String) -> Unit,
    onNewOrder: () -> Unit,
    onNewProduct: () -> Unit,
    onLoadSample: () -> Unit,
    onSeeOrders: () -> Unit,
    /** Ouvre la page de détail d'une carte. */
    onOpenInsight: (Insight) -> Unit = {},
) {
    val stats = remember(data) { Stats(data) }
    val today = LocalDate.now().toEpochDay()
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        PageHeader("Aperçu", "Ventes, encaissements et stock en un coup d'œil.", "Commande", onNewOrder)

        if (data.products.isEmpty() && data.orders.isEmpty()) {
            SoftCard(Modifier.fillMaxWidth()) {
                Text("Bienvenue", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Palette.Ink)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Ajoutez vos pierres et bijoux dans le stock, puis vos commandes : " +
                        "les chiffres et les graphiques se remplissent tout seuls.",
                    fontSize = 14.sp, color = Palette.Muted,
                )
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PillButton("Ajouter un produit", null, onNewProduct)
                    Text(
                        "Voir un exemple",
                        color = Palette.Accent, fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                        modifier = Modifier.clip(RoundedCornerShape(50)).clickable(onClick = onLoadSample)
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                    )
                }
            }
        }

        // Deux petites tuiles : retards et délai moyen de paiement.
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KpiTile(
                label = "En retard", icon = Icons.Outlined.ErrorOutline, iconTint = Palette.Red,
                value = eurosRound(stats.overdueAmount),
                sub = when (val n = stats.overdueOrders.size) {
                    0 -> "Aucun retard"
                    1 -> "1 commande"
                    else -> "$n commandes"
                },
                subColor = if (stats.overdueOrders.isEmpty()) Palette.Green else Palette.Red,
                modifier = Modifier.weight(1f),
                onClick = { onOpenInsight(Insight.OVERDUE) },
            )
            KpiTile(
                label = "Délai de paiement", icon = Icons.Outlined.Schedule, iconTint = Color(0xFF4A6585),
                value = stats.avgDaysToPay?.let { "$it j" } ?: "–",
                sub = stats.avgDaysTrend?.let { d ->
                    if (d == 0) "Stable" else "${if (d < 0) "↓" else "↑"} ${kotlin.math.abs(d)} j vs avant"
                } ?: "Moyenne 90 jours",
                subColor = when {
                    (stats.avgDaysTrend ?: 0) < 0 -> Palette.Green
                    (stats.avgDaysTrend ?: 0) > 0 -> Palette.Red
                    else -> Palette.Muted
                },
                modifier = Modifier.weight(1f),
                onClick = { onOpenInsight(Insight.PAY_DELAY) },
            )
        }

        SectionCard("Chiffre d'affaires du mois", icon = Icons.Outlined.CalendarMonth, onClick = { onOpenInsight(Insight.REVENUE) }) {
            Text(euros(stats.revenueThisMonth), style = AmountStyle)
            TrendText(stats.revenueTrend, "vs mois dernier", fallback = "Marge : ${eurosRound(stats.marginThisMonth)}")
            Spacer(Modifier.height(16.dp))
            MiniBarChart(stats.revenueByMonth)
        }

        SectionCard("Encaissé ce mois", icon = Icons.AutoMirrored.Outlined.TrendingUp, iconTint = Palette.Green, onClick = { onOpenInsight(Insight.COLLECTED) }) {
            Text(euros(stats.collectedThisMonth), style = AmountStyle)
            TrendText(stats.collectedTrend, "vs mois dernier", fallback = "Commandes réglées en totalité")
            Spacer(Modifier.height(16.dp))
            LineAreaChart(stats.collectedByMonth)
        }

        CollectCard(stats) { onOpenInsight(Insight.TO_COLLECT) }

        UnpaidPanel(data, stats, today, onOpenOrder, onSeeOrders)

        if (stats.salesByCategory.isNotEmpty()) {
            SectionCard("Ventes par catégorie", icon = Icons.Outlined.PieChart, onClick = { onOpenInsight(Insight.CATEGORIES) }) {
                DonutChart(stats.salesByCategory)
            }
        }

        SectionCard("Stock", icon = Icons.Outlined.Inventory2, onClick = { onOpenInsight(Insight.STOCK) }) {
            Row {
                Column(Modifier.weight(1f)) {
                    Text("Valeur d'achat", fontSize = 12.sp, color = Palette.Muted)
                    Text(eurosRound(stats.stockValueAtCost), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Palette.Ink)
                }
                Column(Modifier.weight(1f)) {
                    Text("Valeur de vente", fontSize = 12.sp, color = Palette.Muted)
                    Text(eurosRound(stats.stockValueAtPrice), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Palette.Ink)
                }
            }
            if (stats.lowStock.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                stats.lowStock.take(5).forEach { p ->
                    HorizontalDivider(color = Palette.Line)
                    Row(
                        Modifier.fillMaxWidth().clickable { onOpenProduct(p.id) }.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(p.name, fontSize = 14.sp, color = Palette.Ink, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                        StatusChip(
                            if (p.quantity <= 0) "Rupture" else qty(p.quantity, p.unit),
                            if (p.quantity <= 0) ChipKind.BAD else ChipKind.WARN,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(110.dp))
    }
}

@Composable
private fun KpiTile(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    value: String,
    sub: String,
    subColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    SoftCard(modifier, onClick = onClick, padding = 16.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label.uppercase(), fontSize = 10.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Medium, color = Palette.Muted, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            IconBadge(icon, iconTint, 26.dp)
        }
        Spacer(Modifier.height(10.dp))
        Text(value, fontFamily = Display, fontSize = 24.sp, fontWeight = FontWeight.Medium, color = Palette.Ink, maxLines = 1)
        Spacer(Modifier.height(2.dp))
        Text(sub, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = subColor, maxLines = 1)
    }
}

/** Carte dégradée violette « À encaisser » (équivalent du « payout » de la maquette). */
@Composable
private fun CollectCard(stats: Stats, onClick: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(CardShape).background(Palette.AccentGradient).clickable(onClick = onClick).padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("RESTE À ENCAISSER", fontSize = 10.sp, letterSpacing = 1.6.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.85f), modifier = Modifier.weight(1f))
            Icon(Icons.Outlined.AccountBalanceWallet, null, tint = Color.White)
        }
        Spacer(Modifier.height(6.dp))
        Text(euros(stats.toCollect), fontFamily = Display, fontSize = 32.sp, fontWeight = FontWeight.Medium, color = Color.White)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CollectTile("30 prochains jours", eurosShort(stats.dueNext30), Modifier.weight(1f))
            CollectTile("En cours", stats.openOrders.toString(), Modifier.weight(1f))
            CollectTile("Devis", stats.quotes.toString(), Modifier.weight(1f))
        }
    }
}

@Composable
private fun CollectTile(label: String, value: String, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha = 0.14f)).padding(12.dp),
    ) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White, maxLines = 1)
        Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.75f), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/** Panneau marine : commandes impayées / en retard / devis, avec onglets en pilule. */
@Composable
private fun UnpaidPanel(data: AppData, stats: Stats, today: Long, onOpenOrder: (String) -> Unit, onSeeOrders: () -> Unit) {
    var tab by rememberSaveable { mutableStateOf(DashTab.IMPAYEES) }
    val quotes = data.orders.filter { it.status == OrderStatus.DEVIS }.sortedByDescending { it.date }
    val list: List<Order> = when (tab) {
        DashTab.IMPAYEES -> stats.unpaidOrders
        DashTab.EN_RETARD -> stats.overdueOrders
        DashTab.DEVIS -> quotes
    }
    Column(Modifier.fillMaxWidth().clip(CardShape).background(Palette.Navy).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("À suivre", fontFamily = Display, fontWeight = FontWeight.Medium, fontSize = 20.sp, color = Color.White, modifier = Modifier.weight(1f))
            Text(
                "Tout voir", fontSize = 13.sp, color = Palette.AccentSoft, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clip(RoundedCornerShape(50)).clickable(onClick = onSeeOrders).padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
        Spacer(Modifier.height(12.dp))
        SegmentedTabs(
            DashTab.entries, tab, { it.label }, { tab = it },
            count = {
                when (it) {
                    DashTab.IMPAYEES -> stats.unpaidOrders.size
                    DashTab.EN_RETARD -> stats.overdueOrders.size
                    DashTab.DEVIS -> quotes.size
                }
            },
            dark = true,
        )
        Spacer(Modifier.height(12.dp))
        if (list.isEmpty()) {
            Text(
                when (tab) {
                    DashTab.IMPAYEES -> "Tout est encaissé."
                    DashTab.EN_RETARD -> "Aucun retard de paiement."
                    DashTab.DEVIS -> "Aucun devis en attente."
                },
                color = Palette.NavyMuted, fontSize = 14.sp, modifier = Modifier.padding(vertical = 12.dp),
            )
        }
        list.take(6).forEach { o ->
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).clickable { onOpenOrder(o.id) }
                    .padding(horizontal = 6.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(o.clientName.ifBlank { "?" }, 40.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(orderRef(o.number), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(
                        "${o.clientName.ifBlank { "Sans client" }} · ${dueLabel(o, today)}",
                        color = Palette.NavyMuted, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    euros(if (o.status.countsAsSale) o.balance else o.total),
                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                )
            }
        }
    }
}
