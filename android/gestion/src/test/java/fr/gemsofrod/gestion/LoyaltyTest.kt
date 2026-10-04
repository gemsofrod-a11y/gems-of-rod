package fr.gemsofrod.gestion

import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Client
import fr.gemsofrod.gestion.data.Order
import fr.gemsofrod.gestion.data.OrderLine
import fr.gemsofrod.gestion.data.OrderStatus
import fr.gemsofrod.gestion.data.Segment
import fr.gemsofrod.gestion.data.LOYALTY_DISCOUNT_LABEL
import fr.gemsofrod.gestion.data.loyaltyDiscountAvailable
import fr.gemsofrod.gestion.data.segmentOf
import fr.gemsofrod.gestion.data.withShippingCodes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/** Règles de fidélité de Sébastien : VIP, Régulier, Occasionnel, code de livraison offerte. */
class LoyaltyTest {

    private val today = LocalDate.of(2026, 10, 4)
    private val client = Client(id = "c", name = "Élodie Exemple")

    private fun order(daysAgo: Long, amount: Double, status: OrderStatus = OrderStatus.LIVREE, n: Int = 1) = Order(
        number = n, clientId = "c", clientName = client.name, date = today.minusDays(daysAgo).toEpochDay(), status = status,
        lines = listOf(OrderLine(null, "Pierre", 1.0, amount, 0.0)),
    )

    private fun data(vararg orders: Order) = AppData(clients = listOf(client), orders = orders.toList())

    @Test
    fun occasionnelPuisRegulierPuisVip() {
        assertEquals(Segment.OCCASIONNEL, data(order(10, 100.0)).segmentOf(client, today))
        assertEquals(Segment.REGULIER, data(order(10, 100.0), order(20, 100.0), order(30, 100.0)).segmentOf(client, today))
        assertEquals(Segment.VIP, data(order(10, 600.0)).segmentOf(client, today))
    }

    @Test
    fun devisEtAnnulationsNeComptentPas() {
        val d = data(order(10, 900.0, OrderStatus.DEVIS), order(20, 900.0, OrderStatus.ANNULEE), order(30, 100.0))
        assertEquals(Segment.OCCASIONNEL, d.segmentOf(client, today))
    }

    @Test
    fun ancienVipRetombeEnRegulier() {
        // 2 900 € il y a 14 mois, rien depuis : plus VIP, mais Régulier (pas Occasionnel).
        assertEquals(Segment.REGULIER, data(order(425, 2900.0)).segmentOf(client, today))
    }

    @Test
    fun codeOffertALaTroisiemeCommande() {
        val two = data(order(10, 100.0, n = 1), order(20, 100.0, n = 2)).withShippingCodes(today)
        assertNull(two.clients.single().shippingCode)
        val three = data(order(10, 100.0, n = 1), order(20, 100.0, n = 2), order(30, 100.0, n = 3)).withShippingCodes(today)
        val code = three.clients.single().shippingCode
        assertNotNull(code)
        assertTrue(code!!, Regex("GEMS-ELODIE-[A-Z2-9]{3}").matches(code))
        // Le code reste le même ensuite.
        assertEquals(code, three.withShippingCodes(today).clients.single().shippingCode)
    }

    @Test
    fun remiseCinqPourCentApresCinqCommandesUneFoisParAn() {
        val four = data(*(1..4).map { order(it * 10L, 100.0, n = it) }.toTypedArray())
        assertFalse(four.loyaltyDiscountAvailable(client, today))
        val five = data(*(1..5).map { order(it * 10L, 100.0, n = it) }.toTypedArray())
        assertTrue(five.loyaltyDiscountAvailable(client, today))
        // Remise utilisée sur la 6e commande : plus disponible avant 12 mois.
        val used = order(1, 95.0, n = 6).let { it.copy(lines = it.lines + OrderLine(null, LOYALTY_DISCOUNT_LABEL, 1.0, -5.0, 0.0)) }
        val after = five.copy(orders = five.orders + used)
        assertFalse(after.loyaltyDiscountAvailable(client, today))
        // En modifiant la commande qui porte la remise, elle reste proposée.
        assertTrue(after.loyaltyDiscountAvailable(client, today, excludeOrderId = used.id))
    }
}
