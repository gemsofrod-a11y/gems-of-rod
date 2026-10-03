package fr.gemsofrod.gestion.data

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/** Indicateurs du tableau de bord, calculés à partir des commandes et du stock. */
class Stats(data: AppData, today: LocalDate = LocalDate.now()) {

    private val sales = data.orders.filter { it.status.countsAsSale }
    private val thisMonth = YearMonth.from(today)

    val revenueThisMonth = revenueIn(thisMonth)
    val revenueLastMonth = revenueIn(thisMonth.minusMonths(1))
    val marginThisMonth = sales.filter { YearMonth.from(it.localDate) == thisMonth }.sumOf { it.margin }

    /** Évolution en % par rapport au mois précédent, null s'il n'y a rien à comparer. */
    val revenueTrend: Double? =
        if (revenueLastMonth > 0) (revenueThisMonth - revenueLastMonth) / revenueLastMonth * 100 else null

    val openOrders = data.orders.count { it.isOpen }
    val quotes = data.orders.count { it.status == OrderStatus.DEVIS }
    val depositsToCollect = data.orders.filter { it.isOpen }.sumOf { (it.total - it.deposit).coerceAtLeast(0.0) }

    val stockValueAtCost = data.products.sumOf { it.quantity.coerceAtLeast(0.0) * it.cost }
    val stockValueAtPrice = data.products.sumOf { it.quantity.coerceAtLeast(0.0) * it.price }
    val lowStock = data.products.filter { it.isLow }.sortedBy { it.quantity }

    /** Chiffre d'affaires des 6 derniers mois (le plus ancien en premier). */
    val revenueByMonth: List<Pair<String, Double>> = (5 downTo 0).map { back ->
        val month = thisMonth.minusMonths(back.toLong())
        month.month.getDisplayName(TextStyle.SHORT, Locale.FRANCE).trimEnd('.') to revenueIn(month)
    }

    /** Ventes des 12 derniers mois par catégorie de produit. */
    val salesByCategory: List<Pair<String, Double>> = run {
        val since = thisMonth.minusMonths(11).atDay(1)
        val byId = data.products.associateBy { it.id }
        val totals = linkedMapOf<String, Double>()
        Category.entries.forEach { totals[it.label] = 0.0 }
        sales.filter { !it.localDate.isBefore(since) }.forEach { order ->
            order.lines.forEach { line ->
                val key = line.productId?.let { byId[it] }?.category?.label ?: "Autre"
                totals[key] = (totals[key] ?: 0.0) + line.total
            }
        }
        totals.filterValues { it > 0 }.toList().sortedByDescending { it.second }
    }

    /** Valeur du stock (prix d'achat) par catégorie. */
    val stockByCategory: List<Pair<String, Double>> = Category.entries.map { cat ->
        cat.label to data.products.filter { it.category == cat }
            .sumOf { it.quantity.coerceAtLeast(0.0) * it.cost }
    }.filter { it.second > 0 }

    private fun revenueIn(month: YearMonth) =
        sales.filter { YearMonth.from(it.localDate) == month }.sumOf { it.total }
}

/** Total des achats (commandes vendues) par client. */
fun AppData.spentByClient(): Map<String, Double> =
    orders.filter { it.status.countsAsSale && it.clientId != null }
        .groupBy { it.clientId!! }
        .mapValues { (_, list) -> list.sumOf { it.total } }
