package fr.gemsofrod.gestion.data

import java.time.LocalDate

/**
 * Jeu d'exemple entièrement fictif, pour découvrir l'app et ses graphiques.
 * Il remplace les données existantes et s'efface depuis le menu.
 */
object SampleData {

    fun build(today: LocalDate = LocalDate.now()): AppData {
        val saphir = Product(name = "Saphir bleu de Ceylan 2,1 ct", category = Category.PRECIEUSE, unit = StockUnit.PIECE, quantity = 3.0, cost = 1800.0, price = 2900.0, threshold = 1.0)
        val rubis = Product(name = "Rubis du Mozambique 1,4 ct", category = Category.PRECIEUSE, unit = StockUnit.PIECE, quantity = 1.0, cost = 2100.0, price = 3400.0, threshold = 1.0)
        val emeraude = Product(name = "Émeraude de Colombie 1,0 ct", category = Category.PRECIEUSE, unit = StockUnit.PIECE, quantity = 2.0, cost = 1500.0, price = 2500.0, threshold = 1.0)
        val tanzanite = Product(name = "Tanzanite 3,2 ct", category = Category.FINE, unit = StockUnit.PIECE, quantity = 4.0, cost = 420.0, price = 780.0, threshold = 2.0)
        val spinelle = Product(name = "Spinelle rose", category = Category.FINE, unit = StockUnit.CARAT, quantity = 6.5, cost = 160.0, price = 290.0, threshold = 3.0)
        val tourmaline = Product(name = "Tourmaline verte", category = Category.FINE, unit = StockUnit.CARAT, quantity = 12.0, cost = 45.0, price = 95.0, threshold = 5.0)
        val or18 = Product(name = "Or 18 carats jaune", category = Category.METAL, unit = StockUnit.GRAMME, quantity = 85.0, cost = 62.0, price = 78.0, threshold = 30.0)
        val bague = Product(name = "Bague solitaire or blanc", category = Category.BIJOU, unit = StockUnit.PIECE, quantity = 1.0, cost = 950.0, price = 1650.0, threshold = 1.0)
        val products = listOf(saphir, rubis, emeraude, tanzanite, spinelle, tourmaline, or18, bague)

        val c1 = Client(name = "Claire Exemple", email = "claire@exemple.fr", segment = Segment.VIP)
        val c2 = Client(name = "Marc Exemple", phone = "06 00 00 00 00", segment = Segment.REGULIER)
        val c3 = Client(name = "Inès Exemple", segment = Segment.REGULIER)
        val c4 = Client(name = "Paul Exemple", segment = Segment.PROSPECT)
        val clients = listOf(c1, c2, c3, c4)

        fun line(p: Product, q: Double) = OrderLine(p.id, p.name, q, p.price, p.cost)
        var n = 1

        /**
         * [paidAfter] : réglée intégralement N jours après la commande ;
         * null = seul l'acompte de 30 % est reçu (rien pour un devis).
         */
        // Commandes « du mois » : jamais avant le 1er du mois en cours, pour que
        // le CA et l'encaissé du mois soient toujours remplis, quel que soit le jour.
        fun thisMonth(daysAgo: Long): Long = maxOf(0L, minOf(daysAgo, today.dayOfMonth - 1L))

        fun order(c: Client, daysAgo: Long, status: OrderStatus, paidAfter: Long?, vararg lines: OrderLine): Order {
            val date = today.minusDays(daysAgo)
            val total = lines.sumOf { it.total }
            return Order(
                number = n++, clientId = c.id, clientName = c.name,
                date = date.toEpochDay(), status = status, lines = lines.toList(),
                deposit = when {
                    paidAfter != null -> total
                    status == OrderStatus.DEVIS || status == OrderStatus.ANNULEE -> 0.0
                    else -> Math.round(total * 30) / 100.0
                },
                dueDate = date.plusDays(15).toEpochDay(),
                paidDate = paidAfter?.let { date.plusDays(it).toEpochDay() },
            )
        }

        val orders = listOf(
            order(c1, 160, OrderStatus.LIVREE, 12, line(saphir, 1.0)),
            order(c2, 140, OrderStatus.LIVREE, 20, line(tourmaline, 4.0), line(or18, 12.0)),
            order(c3, 115, OrderStatus.LIVREE, 18, line(tanzanite, 1.0)),
            order(c1, 95, OrderStatus.LIVREE, 9, line(emeraude, 1.0), line(or18, 8.0)),
            order(c2, 70, OrderStatus.LIVREE, 14, line(spinelle, 2.5)),
            order(c3, 52, OrderStatus.ANNULEE, null, line(rubis, 1.0)),
            order(c1, 38, OrderStatus.LIVREE, 7, line(bague, 1.0)),
            order(c2, 33, OrderStatus.LIVREE, null, line(tanzanite, 2.0)),
            order(c3, 24, OrderStatus.LIVREE, 10, line(tourmaline, 3.0)),
            order(c3, 9, OrderStatus.EN_COURS, null, line(saphir, 1.0), line(or18, 6.0)),
            // Ventes du mois en cours (dont une déjà réglée).
            order(c2, thisMonth(3), OrderStatus.LIVREE, 0, line(tanzanite, 1.0), line(tourmaline, 2.0)),
            order(c1, thisMonth(2), OrderStatus.CONFIRMEE, null, line(rubis, 1.0)),
            order(c4, thisMonth(1), OrderStatus.LIVREE, 0, line(spinelle, 1.5)),
            order(c4, 0, OrderStatus.DEVIS, null, line(emeraude, 1.0)),
        )
        // Paiements SumUp fictifs : ventes directes au Tap to Pay, plus le
        // règlement d'une commande du mois rattaché à celle-ci.
        fun pay(code: String, amount: Double, daysAgo: Long, time: String, type: String, orderId: String? = null, items: List<SumUpItem> = emptyList()) =
            SumUpPayment(code, amount, today.minusDays(daysAgo).toEpochDay(), time, type, orderId, items)
        val paidThisMonth = orders.last { it.status == OrderStatus.LIVREE && it.isPaid }
        val payments = listOf(
            pay("EXEMPLE01", 190.0, thisMonth(0), "11:42", "POS", items = listOf(SumUpItem("Tourmaline verte", 2.0, tourmaline.id))),
            pay("EXEMPLE02", paidThisMonth.total, (today.toEpochDay() - paidThisMonth.date), "15:10", "ECOM", paidThisMonth.id),
            pay("EXEMPLE03", 95.0, thisMonth(2), "17:05", "POS", items = listOf(SumUpItem("Pendentif goutte argent", 1.0))),
            pay("EXEMPLE04", 350.0, 12, "10:20", "POS"),
            pay("EXEMPLE05", 120.0, 40, "16:35", "POS"),
        )
        return AppData(products, clients, orders, n, payments)
    }
}
