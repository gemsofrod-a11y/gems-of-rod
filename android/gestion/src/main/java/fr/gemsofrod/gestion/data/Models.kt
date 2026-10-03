package fr.gemsofrod.gestion.data

import java.time.LocalDate
import java.util.UUID

enum class Category(val label: String) {
    PRECIEUSE("Pierre précieuse"),
    FINE("Pierre fine"),
    METAL("Métal précieux"),
    BIJOU("Bijou"),
}

enum class StockUnit(val label: String) {
    PIECE("pièce"),
    CARAT("ct"),
    GRAMME("g"),
}

enum class Segment(val label: String) {
    VIP("VIP"),
    REGULIER("Régulier"),
    PROSPECT("Prospect"),
}

/**
 * [reservesStock] : une commande dans ce statut sort ses quantités du stock.
 * Un devis ne réserve rien ; une annulation rend les quantités au stock.
 */
enum class OrderStatus(val label: String, val reservesStock: Boolean, val countsAsSale: Boolean) {
    DEVIS("Devis", false, false),
    CONFIRMEE("Confirmée", true, true),
    EN_COURS("En préparation", true, true),
    LIVREE("Livrée", true, true),
    ANNULEE("Annulée", false, false),
}

data class Product(
    val id: String = newId(),
    val name: String,
    val category: Category,
    val unit: StockUnit,
    val quantity: Double,
    val cost: Double,
    val price: Double,
    val threshold: Double,
    val note: String = "",
) {
    val isLow: Boolean get() = quantity <= threshold
}

data class Client(
    val id: String = newId(),
    val name: String,
    val email: String = "",
    val phone: String = "",
    val segment: Segment = Segment.PROSPECT,
    val note: String = "",
)

data class OrderLine(
    val productId: String?,
    val label: String,
    val quantity: Double,
    val unitPrice: Double,
    /** Coût d'achat unitaire au moment de la commande (pour la marge). */
    val unitCost: Double,
) {
    val total: Double get() = quantity * unitPrice
    val margin: Double get() = quantity * (unitPrice - unitCost)
}

data class Order(
    val id: String = newId(),
    val number: Int,
    val clientId: String?,
    val clientName: String,
    /** Jour de la commande, en LocalDate.toEpochDay(). */
    val date: Long = LocalDate.now().toEpochDay(),
    val status: OrderStatus = OrderStatus.DEVIS,
    val lines: List<OrderLine> = emptyList(),
    /** Montant déjà reçu (acompte et paiements). */
    val deposit: Double = 0.0,
    val note: String = "",
    /** Échéance de paiement (epochDay), null = pas d'échéance. */
    val dueDate: Long? = null,
    /** Jour où le solde a été entièrement réglé (epochDay). */
    val paidDate: Long? = null,
) {
    val total: Double get() = lines.sumOf { it.total }
    val balance: Double get() = (total - deposit).coerceAtLeast(0.0)
    val isPaid: Boolean get() = total > 0 && balance < 0.005
    /** Vente (confirmée → livrée) dont il reste quelque chose à encaisser. */
    val isUnpaid: Boolean get() = status.countsAsSale && balance >= 0.005
    fun isOverdue(today: Long): Boolean = isUnpaid && dueDate != null && dueDate < today
    val margin: Double get() = lines.sumOf { it.margin }
    val localDate: LocalDate get() = LocalDate.ofEpochDay(date)
    val isOpen: Boolean get() = status == OrderStatus.CONFIRMEE || status == OrderStatus.EN_COURS
}

data class AppData(
    val products: List<Product> = emptyList(),
    val clients: List<Client> = emptyList(),
    val orders: List<Order> = emptyList(),
    val nextOrderNumber: Int = 1,
)

fun newId(): String = UUID.randomUUID().toString()
