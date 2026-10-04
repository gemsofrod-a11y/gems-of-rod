package fr.gemsofrod.gestion.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Category
import fr.gemsofrod.gestion.data.Order
import fr.gemsofrod.gestion.data.Stats
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/** Cartes de l'Aperçu qui s'ouvrent en page de détail. */
enum class Insight(val title: String) {
    REVENUE("Chiffre d'affaires"),
    COLLECTED("Encaissements"),
    OVERDUE("Paiements en retard"),
    PAY_DELAY("Délai de paiement"),
    TO_COLLECT("Reste à encaisser"),
    CATEGORIES("Ventes par catégorie"),
    STOCK("Valeur du stock"),
}

/** Page de détail d'un indicateur : chiffre clé, graphique, liste cliquable. */
@Composable
fun InsightScreen(
    insight: Insight,
    data: AppData,
    onOpenOrder: (String) -> Unit,
    onOpenProduct: (String) -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    val stats = remember(data) { Stats(data) }
    val today = LocalDate.now()
    val todayDay = today.toEpochDay()
    val thisMonth = YearMonth.from(today)
    val monthName = thisMonth.month.getDisplayName(TextStyle.FULL, Locale.FRANCE)

    Column(
        Modifier.fillMaxSize().background(Palette.Background).statusBarsPadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RoundIcon(Icons.AutoMirrored.Filled.ArrowBack, "Retour", onBack)
            Text(
                insight.title, fontFamily = Display, fontSize = 24.sp, fontWeight = FontWeight.Medium, color = Palette.Ink,
                modifier = Modifier.weight(1f).padding(start = 12.dp), maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
        }

        when (insight) {
            Insight.REVENUE -> {
                val series = stats.revenueSeries(12)
                Hero("Ce mois-ci", euros(stats.revenueThisMonth)) {
                    TrendText(stats.revenueTrend, "vs mois dernier")
                    Text("Marge du mois : ${euros(stats.marginThisMonth)}", fontSize = 12.sp, color = Palette.Muted)
                }
                SectionCard("12 derniers mois") { MiniBarChart(series, height = 140.dp) }
                MonthTable(series)
                val sales = data.orders.filter { it.status.countsAsSale && YearMonth.from(it.localDate) == thisMonth }
                    .sortedByDescending { it.date }
                val direct = data.sumupPayments.filter { it.orderId == null && YearMonth.from(it.localDate) == thisMonth }
                ListTitle("Ventes de $monthName", sales.size + direct.size)
                sales.forEach { o -> OrderRow(o, todayDay, onOpenOrder) }
                direct.forEach { p ->
                    ItemCard(
                        title = euros(p.amount), subtitle = "${date(p.localDate)} · vente directe SumUp",
                        onClick = {}, leading = { IconBadge(Icons.Outlined.ShoppingBag, Palette.Accent, 40.dp) },
                    )
                }
                if (sales.isEmpty() && direct.isEmpty()) EmptyState("Aucune vente ce mois-ci pour l'instant.")
            }

            Insight.COLLECTED -> {
                val series = stats.collectedSeries(12)
                Hero("Encaissé ce mois-ci", euros(stats.collectedThisMonth)) {
                    TrendText(stats.collectedTrend, "vs mois dernier")
                }
                SectionCard("12 derniers mois") { LineAreaChart(series, height = 140.dp) }
                MonthTable(series)
                val paid = data.orders.filter { o -> o.paidDate?.let { YearMonth.from(LocalDate.ofEpochDay(it)) == thisMonth } == true }
                val direct = data.sumupPayments.filter { it.orderId == null && YearMonth.from(it.localDate) == thisMonth }
                ListTitle("Rentré en $monthName", paid.size + direct.size)
                paid.forEach { o -> OrderRow(o, todayDay, onOpenOrder) }
                direct.forEach { p ->
                    ItemCard(
                        title = euros(p.amount), subtitle = "${date(p.localDate)} à ${p.time} · ${p.typeLabel}",
                        onClick = {}, leading = { IconBadge(Icons.Outlined.CreditCard, Palette.Accent, 40.dp) },
                    )
                }
                if (paid.isEmpty() && direct.isEmpty()) EmptyState("Rien d'encaissé ce mois-ci pour l'instant.")
            }

            Insight.OVERDUE -> {
                val list = stats.overdueOrders.sortedBy { it.dueDate ?: 0L }
                Hero("Total en retard", euros(stats.overdueAmount)) {
                    Text(
                        if (list.isEmpty()) "Aucun client en retard." else "${list.size} commande${if (list.size > 1) "s" else ""} à relancer",
                        fontSize = 12.sp, color = if (list.isEmpty()) Palette.Green else Palette.Red,
                    )
                }
                list.forEach { o -> OrderRow(o, todayDay, onOpenOrder, amount = o.balance) }
                if (list.isNotEmpty()) {
                    Text(
                        "Astuce : ouvrez une commande puis « Lien SumUp » pour envoyer au client un lien de paiement.",
                        fontSize = 12.sp, color = Palette.Muted,
                    )
                }
            }

            Insight.PAY_DELAY -> {
                val paid = data.orders.filter { o -> o.paidDate?.let { it >= todayDay - 90 } == true }
                    .sortedByDescending { it.paidDate }
                Hero("Délai moyen sur 90 jours", stats.avgDaysToPay?.let { "$it jours" } ?: "–") {
                    Text("Temps entre la commande et le paiement complet.", fontSize = 12.sp, color = Palette.Muted)
                }
                ListTitle("Commandes réglées", paid.size)
                paid.forEach { o ->
                    val days = (o.paidDate ?: o.date) - o.date
                    ItemCard(
                        title = "${orderRef(o.number)} · ${o.clientName.ifBlank { "Sans client" }}",
                        subtitle = "Payée en $days jour${if (days > 1) "s" else ""}",
                        onClick = { onOpenOrder(o.id) },
                        leading = { Avatar(o.clientName.ifBlank { "?" }, 40.dp) },
                        trailing = euros(o.total),
                        chip = { StatusChip("$days j", if (days <= 15) ChipKind.GOOD else ChipKind.WARN) },
                    )
                }
                if (paid.isEmpty()) EmptyState("Aucune commande réglée ces 90 derniers jours.")
            }

            Insight.TO_COLLECT -> {
                Hero("Reste à encaisser", euros(stats.toCollect)) {
                    Text("Dont ${euros(stats.dueNext30)} attendus dans les 30 jours.", fontSize = 12.sp, color = Palette.Muted)
                }
                val late = stats.unpaidOrders.filter { it.isOverdue(todayDay) }
                val upcoming = stats.unpaidOrders.filter { !it.isOverdue(todayDay) && it.dueDate != null }
                val noDue = stats.unpaidOrders.filter { it.dueDate == null }
                if (late.isNotEmpty()) { ListTitle("En retard", late.size); late.forEach { OrderRow(it, todayDay, onOpenOrder, amount = it.balance) } }
                if (upcoming.isNotEmpty()) { ListTitle("À venir", upcoming.size); upcoming.forEach { OrderRow(it, todayDay, onOpenOrder, amount = it.balance) } }
                if (noDue.isNotEmpty()) { ListTitle("Sans échéance", noDue.size); noDue.forEach { OrderRow(it, todayDay, onOpenOrder, amount = it.balance) } }
                if (stats.unpaidOrders.isEmpty()) EmptyState("Tout est encaissé.")
            }

            Insight.CATEGORIES -> {
                SectionCard("12 derniers mois") { DonutChart(stats.salesByCategory) }
                val since = thisMonth.minusMonths(11).atDay(1)
                val byId = data.products.associateBy { it.id }
                val lines = data.orders.filter { it.status.countsAsSale && !it.localDate.isBefore(since) }.flatMap { it.lines }
                Category.entries.forEach { cat ->
                    val sold = lines.filter { l -> l.productId?.let { byId[it] }?.category == cat }
                        .groupBy { it.label }.mapValues { (_, l) -> l.sumOf { it.total } }
                        .toList().sortedByDescending { it.second }
                    if (sold.isNotEmpty()) {
                        SectionCard("${cat.label} · ${eurosShort(sold.sumOf { it.second })}") {
                            sold.take(6).forEachIndexed { i, (name, amount) ->
                                if (i > 0) HorizontalDivider(color = Palette.Line)
                                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(name, fontSize = 14.sp, color = Palette.Ink, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(euros(amount), fontSize = 14.sp, color = Palette.Ink, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
                val direct = data.sumupPayments.filter { it.orderId == null && !it.localDate.isBefore(since) }.sumOf { it.amount }
                if (direct > 0) {
                    Text("Plus ${euros(direct)} de ventes directes SumUp (sans détail de pièce).", fontSize = 12.sp, color = Palette.Muted)
                }
                if (lines.isEmpty() && direct == 0.0) EmptyState("Aucune vente sur les 12 derniers mois.")
            }

            Insight.STOCK -> {
                Hero("Valeur d'achat du stock", euros(stats.stockValueAtCost)) {
                    Text("Valeur de vente : ${euros(stats.stockValueAtPrice)}", fontSize = 12.sp, color = Palette.Muted)
                }
                if (stats.stockByCategoryAtPrice.isNotEmpty()) {
                    SectionCard("Valeur de vente par catégorie") { HorizontalBars(stats.stockByCategoryAtPrice) }
                }
                val out = data.products.filter { it.quantity <= 0 }
                if (out.isNotEmpty()) {
                    ListTitle("Épuisés", out.size)
                    out.forEach { p -> ProductRow(p.name, p.category.label, "Épuisé", ChipKind.BAD) { onOpenProduct(p.id) } }
                }
                val top = data.products.filter { it.quantity > 0 }.sortedByDescending { it.quantity * it.price }.take(10)
                if (top.isNotEmpty()) {
                    ListTitle("Pièces les plus précieuses en stock", top.size)
                    top.forEach { p -> ProductRow(p.name, "${qty(p.quantity, p.unit)} · ${p.category.label}", eurosShort(p.quantity * p.price), ChipKind.ACCENT) { onOpenProduct(p.id) } }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

/** Grande carte du chiffre clé. */
@Composable
private fun Hero(label: String, value: String, extra: @Composable ColumnScope.() -> Unit) {
    SoftCard(Modifier.fillMaxWidth(), padding = 20.dp) {
        Text(label.uppercase(), fontSize = 10.sp, letterSpacing = 1.6.sp, fontWeight = FontWeight.Medium, color = Palette.Muted)
        Spacer(Modifier.height(6.dp))
        Text(value, style = AmountStyle.copy(fontSize = 36.sp))
        Spacer(Modifier.height(4.dp))
        extra()
    }
}

/** Tableau mois par mois (le plus récent en premier). */
@Composable
private fun MonthTable(series: List<Pair<String, Double>>) {
    SectionCard("Mois par mois") {
        series.asReversed().forEachIndexed { i, (month, value) ->
            if (i > 0) HorizontalDivider(color = Palette.Line)
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text(month.replaceFirstChar { it.uppercase() }, fontSize = 14.sp, color = if (i == 0) Palette.Accent else Palette.Ink, modifier = Modifier.weight(1f))
                Text(if (value > 0) euros(value) else "–", fontSize = 14.sp, color = Palette.Ink, fontWeight = if (i == 0) FontWeight.SemiBold else FontWeight.Normal)
            }
        }
    }
}

@Composable
private fun ListTitle(title: String, count: Int) {
    Text(
        "${title.uppercase()}  ·  $count", fontSize = 11.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Medium,
        color = Palette.Muted, modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun OrderRow(o: Order, today: Long, onOpen: (String) -> Unit, amount: Double = o.total) {
    ItemCard(
        title = "${orderRef(o.number)} · ${o.clientName.ifBlank { "Sans client" }}",
        subtitle = dueLabel(o, today),
        onClick = { onOpen(o.id) },
        leading = { Avatar(o.clientName.ifBlank { "?" }, 40.dp) },
        trailing = euros(amount),
        chip = { OrderChip(o, today) },
    )
}

@Composable
private fun ProductRow(name: String, subtitle: String, chip: String, kind: ChipKind, onClick: () -> Unit) {
    ItemCard(
        title = name, subtitle = subtitle, onClick = onClick,
        leading = { IconBadge(Icons.Outlined.Diamond, Palette.Accent, 40.dp) },
        chip = { StatusChip(chip, kind) },
    )
}
