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
    val deposit: Double = 0.0,
    val note: String = "",
) {
    val total: Double get() = lines.sumOf { it.total }
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
