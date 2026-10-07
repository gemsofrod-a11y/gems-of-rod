package fr.gemsofrod.gestion

import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Category
import fr.gemsofrod.gestion.data.Product
import fr.gemsofrod.gestion.data.StockUnit
import fr.gemsofrod.gestion.data.SumUpItem
import fr.gemsofrod.gestion.data.SumUpPayment
import fr.gemsofrod.gestion.data.withSalePrices
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Prix repris des ventes SumUp : seulement après deux ventes au même prix. */
class SalePriceTest {

    private val saphir = Product(id = "s", name = "Saphir 1,1 ct", category = Category.PRECIEUSE, unit = StockUnit.PIECE,
        quantity = 3.0, cost = 300.0, price = 640.0, threshold = 0.0)

    private fun sale(code: String, day: Long, price: Double) =
        SumUpPayment(code, price, day, "12:00", "POS", items = listOf(SumUpItem("Saphir 1,1 ct", 1.0, "s", price)))

    private fun data(vararg sales: SumUpPayment) = AppData(products = listOf(saphir), sumupPayments = sales.toList())

    @Test
    fun uneSeuleVenteAuNouveauPrixNeChangeRien() {
        val (d, changes) = data(sale("A", 10, 640.0), sale("B", 11, 680.0)).withSalePrices(setOf("s"))
        assertEquals(640.0, d.products.single().price, 0.0)
        assertTrue(changes.isEmpty())
    }

    @Test
    fun deuxVentesAuMemePrixMettentLePrixAJour() {
        val (d, changes) = data(sale("A", 10, 640.0), sale("B", 11, 680.0), sale("C", 12, 680.0)).withSalePrices(setOf("s"))
        assertEquals(680.0, d.products.single().price, 0.0)
        assertEquals(1, changes.size)
        assertEquals(640.0, changes.single().before, 0.0)
    }

    @Test
    fun remiseIsoleeIgnoree() {
        // Remise à 600 € sur la dernière vente seulement : le prix reste 640 €.
        val (d, _) = data(sale("A", 10, 640.0), sale("B", 11, 640.0), sale("C", 12, 600.0)).withSalePrices(setOf("s"))
        assertEquals(640.0, d.products.single().price, 0.0)
    }

    @Test
    fun produitNonVenduDansLaSynchronisationIntouche() {
        val (d, _) = data(sale("B", 11, 680.0), sale("C", 12, 680.0)).withSalePrices(emptySet())
        assertEquals(640.0, d.products.single().price, 0.0)
    }
}
