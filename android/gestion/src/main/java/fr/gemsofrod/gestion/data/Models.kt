package fr.gemsofrod.gestion.data

import java.time.LocalDate
import java.util.UUID

enum class Category(val label: String) {
    PRECIEUSE("Pierre précieuse"),
    FINE("Pierre fine"),
    METAL("Métal précieux"),
    BIJOU("Bijou"),
    AUTRE("Autre"),
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
    /** Identifiant de l'article dans le catalogue SumUp (import), pour les mises à jour. */
    val sumupId: String? = null,
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
    /** Dernier lien de paiement SumUp créé pour cette commande (en attente). */
    val sumupCheckoutId: String? = null,
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

/**
 * Paiement encaissé par SumUp (Tap to Pay, lien de paiement…), récupéré par
 * synchronisation. [orderId] : commande à laquelle il est rattaché ; null =
 * vente directe, comptée telle quelle dans le CA et l'encaissé.
 */
data class SumUpPayment(
    val code: String,
    val amount: Double,
    /** Jour du paiement (epochDay, fuseau du téléphone). */
    val date: Long,
    /** Heure affichée (HH:mm). */
    val time: String,
    /** « POS » (carte, Tap to Pay), « ECOM » (lien de paiement)… */
    val paymentType: String,
    val orderId: String? = null,
    /** Articles du catalogue SumUp vendus dans ce paiement (ventes récentes uniquement). */
    val items: List<SumUpItem> = emptyList(),
) {
    val localDate: LocalDate get() = LocalDate.ofEpochDay(date)

    /**
     * Effet sur le stock de l'app : une vente directe sort ses articles
     * reconnus ; rattachée à une commande, c'est la commande qui gère le stock.
     */
    val stockEffect: Map<String, Double>
        get() = if (orderId != null) emptyMap()
        else items.filter { it.productId != null }.groupBy { it.productId!! }.mapValues { (_, l) -> -l.sumOf { it.quantity } }
    val typeLabel: String
        get() = when (paymentType) {
            "POS" -> "Carte (Tap to Pay)"
            "ECOM" -> "Lien de paiement"
            "CASH" -> "Espèces"
            else -> paymentType.lowercase().replaceFirstChar { it.uppercase() }
        }
}

/** Article d'une vente SumUp ; [productId] = produit du stock reconnu, null = à associer. */
data class SumUpItem(val name: String, val quantity: Double, val productId: String? = null)

/** Nom d'article comparable : sans accents, minuscules, espaces simples. */
fun normalizeName(name: String): String =
    java.text.Normalizer.normalize(name, java.text.Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "").lowercase().trim().replace(Regex("\\s+"), " ")

data class AppData(
    val products: List<Product> = emptyList(),
    val clients: List<Client> = emptyList(),
    val orders: List<Order> = emptyList(),
    val nextOrderNumber: Int = 1,
    val sumupPayments: List<SumUpPayment> = emptyList(),
    /** Correspondances apprises : nom d'article SumUp normalisé → produit du stock. */
    val sumupProductMap: Map<String, String> = emptyMap(),
)

fun newId(): String = UUID.randomUUID().toString()
