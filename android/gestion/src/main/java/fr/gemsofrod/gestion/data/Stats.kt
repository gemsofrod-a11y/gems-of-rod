package fr.gemsofrod.gestion.data

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/** Indicateurs du tableau de bord, calculés à partir des commandes et du stock. */
class Stats(data: AppData, today: LocalDate = LocalDate.now()) {

    private val todayDay = today.toEpochDay()
    private val orders = data.orders
    /** Ventes directes SumUp (non rattachées à une commande) : CA et encaissé. */
    private val directSales = data.sumupPayments.filter { it.orderId == null }
    private fun directIn(month: YearMonth) =
        directSales.filter { YearMonth.from(it.localDate) == month }.sumOf { it.amount }
    private val sales = data.orders.filter { it.status.countsAsSale }
    private val thisMonth = YearMonth.from(today)
    private val months = (5 downTo 0).map { thisMonth.minusMonths(it.toLong()) }

    // --- Chiffre d'affaires ---

    val revenueThisMonth = revenueIn(thisMonth)
    val revenueLastMonth = revenueIn(thisMonth.minusMonths(1))
    val marginThisMonth = sales.filter { YearMonth.from(it.localDate) == thisMonth }.sumOf { it.margin }

    /** Évolution en % par rapport au mois précédent, null s'il n'y a rien à comparer. */
    val revenueTrend: Double? = percent(revenueThisMonth, revenueLastMonth)

    /** Chiffre d'affaires des 6 derniers mois (le plus ancien en premier). */
    val revenueByMonth: List<Pair<String, Double>> = months.map { label(it) to revenueIn(it) }

    // --- Encaissements ---

    private val unpaid = data.orders.filter { it.isUnpaid }
    val unpaidOrders: List<Order> = unpaid.sortedWith(compareBy<Order> { it.dueDate ?: Long.MAX_VALUE }.thenBy { it.number })
    val overdueOrders: List<Order> = unpaid.filter { it.isOverdue(todayDay) }
    val overdueAmount = overdueOrders.sumOf { it.balance }
    val toCollect = unpaid.sumOf { it.balance }

    /** Reste dû arrivant à échéance dans les 30 prochains jours (ou sans échéance). */
    val dueNext30 = unpaid.filter { o -> o.dueDate.let { it == null || it in todayDay..todayDay + 30 } }.sumOf { it.balance }

    /** Délai moyen (jours) entre commande et règlement complet, sur 90 jours glissants. */
    val avgDaysToPay: Int? = avgDays(todayDay - 90, todayDay)
    private val avgDaysToPayBefore: Int? = avgDays(todayDay - 180, todayDay - 91)
    /** Différence en jours vs la période précédente (négatif = paiements plus rapides). */
    val avgDaysTrend: Int? = if (avgDaysToPay != null && avgDaysToPayBefore != null) avgDaysToPay - avgDaysToPayBefore else null

    /** Commandes entièrement réglées par mois de règlement (6 mois). */
    val collectedByMonth: List<Pair<String, Double>> = months.map { m ->
        label(m) to data.orders.filter { o -> o.paidDate?.let { YearMonth.from(LocalDate.ofEpochDay(it)) == m } == true }
            .sumOf { it.total } + directIn(m)
    }
    val collectedThisMonth = collectedByMonth.last().second
    val collectedTrend: Double? = percent(collectedThisMonth, collectedByMonth[collectedByMonth.size - 2].second)

    // --- Commandes et stock ---

    val openOrders = data.orders.count { it.isOpen }
    val quotes = data.orders.count { it.status == OrderStatus.DEVIS }

    val stockValueAtCost = data.products.sumOf { it.quantity.coerceAtLeast(0.0) * it.cost }
    val stockValueAtPrice = data.products.sumOf { it.quantity.coerceAtLeast(0.0) * it.price }
    val lowStock = data.products.filter { it.isLow }.sortedBy { it.quantity }

    /** Valeur de vente du stock par catégorie (la plus forte en premier). */
    val stockByCategoryAtPrice: List<Pair<String, Double>> = Category.entries.map { cat ->
        cat.label to data.products.filter { it.category == cat }.sumOf { it.quantity.coerceAtLeast(0.0) * it.price }
    }.filter { it.second > 0 }.sortedByDescending { it.second }

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
        val direct = directSales.filter { !it.localDate.isBefore(since) }.sumOf { it.amount }
        if (direct > 0) totals["Ventes SumUp directes"] = direct
        totals.filterValues { it > 0 }.toList().sortedByDescending { it.second }
    }

    /** Chiffre d'affaires mois par mois sur [n] mois (le plus ancien en premier). */
    fun revenueSeries(n: Int): List<Pair<String, Double>> =
        (n - 1 downTo 0).map { thisMonth.minusMonths(it.toLong()) }.map { label(it) to revenueIn(it) }

    /** Encaissé mois par mois sur [n] mois (le plus ancien en premier). */
    fun collectedSeries(n: Int): List<Pair<String, Double>> =
        (n - 1 downTo 0).map { thisMonth.minusMonths(it.toLong()) }.map { m ->
            label(m) to orders.filter { o -> o.paidDate?.let { YearMonth.from(LocalDate.ofEpochDay(it)) == m } == true }
                .sumOf { it.total } + directIn(m)
        }

    private fun revenueIn(month: YearMonth) =
        sales.filter { YearMonth.from(it.localDate) == month }.sumOf { it.total } + directIn(month)

    private fun avgDays(from: Long, to: Long): Int? {
        val paid = orders.filter { o -> o.paidDate?.let { it in from..to } == true }
        if (paid.isEmpty()) return null
        return paid.map { it.paidDate!! - it.date }.average().toInt()
    }

    private fun label(m: YearMonth) = m.month.getDisplayName(TextStyle.SHORT, Locale.FRANCE).trimEnd('.')

    private fun percent(now: Double, before: Double): Double? = if (before > 0) (now - before) / before * 100 else null
}

/** Total des achats (commandes vendues) par client. */
fun AppData.spentByClient(): Map<String, Double> =
    orders.filter { it.status.countsAsSale && it.clientId != null }
        .groupBy { it.clientId!! }
        .mapValues { (_, list) -> list.sumOf { it.total } }
