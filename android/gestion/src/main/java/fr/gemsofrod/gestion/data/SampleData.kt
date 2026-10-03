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

        val c1 = Client(name = "Client exemple A", email = "client.a@exemple.fr", segment = Segment.VIP)
        val c2 = Client(name = "Client exemple B", phone = "06 00 00 00 00", segment = Segment.REGULIER)
        val c3 = Client(name = "Client exemple C", segment = Segment.REGULIER)
        val c4 = Client(name = "Client exemple D", segment = Segment.PROSPECT)
        val clients = listOf(c1, c2, c3, c4)

        fun line(p: Product, q: Double) = OrderLine(p.id, p.name, q, p.price, p.cost)
        var n = 1
        fun order(c: Client, daysAgo: Long, status: OrderStatus, vararg lines: OrderLine) = Order(
            number = n++, clientId = c.id, clientName = c.name,
            date = today.minusDays(daysAgo).toEpochDay(), status = status,
            lines = lines.toList(), deposit = if (status == OrderStatus.DEVIS) 0.0 else lines.sumOf { it.total } * 0.3,
        )

        val orders = listOf(
            order(c1, 160, OrderStatus.LIVREE, line(saphir, 1.0)),
            order(c2, 140, OrderStatus.LIVREE, line(tourmaline, 4.0), line(or18, 12.0)),
            order(c3, 115, OrderStatus.LIVREE, line(tanzanite, 1.0)),
            order(c1, 95, OrderStatus.LIVREE, line(emeraude, 1.0), line(or18, 8.0)),
            order(c2, 70, OrderStatus.LIVREE, line(spinelle, 2.5)),
            order(c3, 52, OrderStatus.ANNULEE, line(rubis, 1.0)),
            order(c1, 38, OrderStatus.LIVREE, line(bague, 1.0)),
            order(c2, 21, OrderStatus.LIVREE, line(tanzanite, 2.0)),
            order(c3, 9, OrderStatus.EN_COURS, line(saphir, 1.0), line(or18, 6.0)),
            order(c1, 4, OrderStatus.CONFIRMEE, line(rubis, 1.0)),
            order(c4, 1, OrderStatus.DEVIS, line(emeraude, 1.0)),
        )
        return AppData(products, clients, orders, n)
    }
}
