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

/** Segments calculés par [segmentOf] ; Prospect : ancienne valeur, plus utilisée. */
enum class Segment(val label: String) {
    VIP("VIP"),
    REGULIER("Régulier"),
    OCCASIONNEL("Occasionnel"),
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
    /** Ancienne saisie manuelle, ignorée : le segment est calculé par [segmentOf]. */
    val segment: Segment = Segment.OCCASIONNEL,
    val note: String = "",
    /** Code personnel de livraison offerte, créé à la 3e commande. */
    val shippingCode: String? = null,
    /** Quand le message avec le code a été préparé pour envoi (millisecondes). */
    val shippingCodeSentAt: Long? = null,
    val shippingCodeUsed: Boolean = false,
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
/**
 * Paiement en ligne SumUp qui n'a pas abouti (carte refusée, page fermée…) :
 * le seul « panier en cours » que SumUp laisse voir — un panier abandonné
 * avant le paiement n'est pas exposé par l'API.
 */
data class SumUpAttempt(
    val code: String,
    val amount: Double,
    val date: Long,
    val time: String,
    val paymentType: String,
    /** « FAILED » ou « CANCELLED ». */
    val status: String,
    /** Articles, tels que résumés par SumUp (peut être vide). */
    val summary: String = "",
    /** Écarté à la main par Sébastien (déjà relancé, sans suite…). */
    val dismissed: Boolean = false,
) {
    val localDate: LocalDate get() = LocalDate.ofEpochDay(date)
    val statusLabel: String get() = if (status == "CANCELLED") "Annulé" else "Échoué"
}

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
    val sumupAttempts: List<SumUpAttempt> = emptyList(),
)

/** Le client a finalement payé : paiement réussi du même montant dans les 3 jours. */
fun AppData.attemptPaidLater(a: SumUpAttempt): Boolean =
    sumupPayments.any { kotlin.math.abs(it.amount - a.amount) < 0.01 && it.date in a.date..(a.date + 3) }

/** Paiements non aboutis à relancer : ni écartés, ni payés ensuite. */
fun AppData.openAttempts(): List<SumUpAttempt> = sumupAttempts.filter { !it.dismissed && !attemptPaidLater(it) }

/** Article acheté par un client : libellé, quantité et montant cumulés. */
data class BasketLine(val label: String, val quantity: Double, val total: Double)

/**
 * « Panier » d'un client : tout ce qu'il a acheté (commandes hors devis et
 * annulations), regroupé par article, du plus gros montant au plus petit.
 */
fun AppData.basketOf(c: Client): List<BasketLine> =
    orders.filter { it.clientId == c.id && it.status.countsAsSale }
        .flatMap { it.lines }.filterNot { it.isLoyaltyDiscount() }
        .groupBy { it.productId ?: ("#" + normalizeName(it.label)) }
        .map { (_, l) -> BasketLine(l.first().label, l.sumOf { it.quantity }, l.sumOf { it.total }) }
        .sortedByDescending { it.total }

/** Achats minimum sur 12 mois pour qu'un client soit « VIP » (règle de Sébastien, 04/10/2026). */
const val VIP_MIN_SPENT = 500.0

/** Commandes minimum sur 12 mois pour qu'un client soit « Régulier ». */
const val REGULAR_MIN_ORDERS = 3

/**
 * Segment d'un client, calculé sur les 12 derniers mois (hors devis et
 * annulations) : VIP dès [VIP_MIN_SPENT] € d'achats ; sinon Régulier dès
 * [REGULAR_MIN_ORDERS] commandes, ou s'il a déjà été VIP (un ancien VIP
 * retombe en Régulier, jamais en Occasionnel) ; sinon Occasionnel.
 */
fun AppData.segmentOf(c: Client, today: LocalDate = LocalDate.now()): Segment = when {
    spentLastYear(c, today) >= VIP_MIN_SPENT -> Segment.VIP
    ordersLastYear(c, today) >= REGULAR_MIN_ORDERS || wasVip(c) -> Segment.REGULIER
    else -> Segment.OCCASIONNEL
}

/** Le client a-t-il, à un moment, atteint [VIP_MIN_SPENT] € d'achats sur 12 mois ? */
fun AppData.wasVip(c: Client): Boolean {
    val sales = orders.filter { it.clientId == c.id && it.status.countsAsSale }
    return sales.any { end -> sales.filter { it.date in (end.date - 365)..end.date }.sumOf { it.total } >= VIP_MIN_SPENT }
}

private fun AppData.salesLastYear(c: Client, today: LocalDate): List<Order> {
    val since = today.minusYears(1).toEpochDay()
    return orders.filter { it.clientId == c.id && it.status.countsAsSale && it.date >= since }
}

fun AppData.ordersLastYear(c: Client, today: LocalDate = LocalDate.now()): Int = salesLastYear(c, today).size

fun AppData.spentLastYear(c: Client, today: LocalDate = LocalDate.now()): Double = salesLastYear(c, today).sumOf { it.total }

/**
 * Code personnel de livraison offerte : « GEMS-PRENOM-XXX » (prénom sans
 * accents, 3 caractères sans ambiguïté 0/O, 1/I), unique parmi [taken].
 */
fun shippingCodeFor(name: String, taken: Set<String>, random: java.util.Random = java.util.Random()): String {
    val first = normalizeName(name).split(" ").firstOrNull().orEmpty()
        .filter { it in 'a'..'z' }.take(8).uppercase().ifEmpty { "CLIENT" }
    val alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    while (true) {
        val suffix = (1..3).map { alphabet[random.nextInt(alphabet.length)] }.joinToString("")
        val code = "GEMS-$first-$suffix"
        if (code !in taken) return code
    }
}

/** Donne un code de livraison offerte à chaque client qui atteint [REGULAR_MIN_ORDERS] commandes sur 12 mois. */
fun AppData.withShippingCodes(today: LocalDate = LocalDate.now()): AppData {
    val needs = clients.filter { it.shippingCode == null && ordersLastYear(it, today) >= REGULAR_MIN_ORDERS }
    if (needs.isEmpty()) return this
    val taken = clients.mapNotNull { it.shippingCode }.toMutableSet()
    val codes = needs.associate { c -> c.id to shippingCodeFor(c.name, taken).also { taken += it } }
    return copy(clients = clients.map { c -> codes[c.id]?.let { c.copy(shippingCode = it) } ?: c })
}

/** Commandes sur 12 mois ouvrant droit à la remise fidélité (règle de Sébastien, 04/10/2026). */
const val LOYALTY_DISCOUNT_ORDERS = 5
const val LOYALTY_DISCOUNT_RATE = 0.05
const val LOYALTY_DISCOUNT_LABEL = "Remise fidélité 5 %"

fun OrderLine.isLoyaltyDiscount(): Boolean = productId == null && label == LOYALTY_DISCOUNT_LABEL

/**
 * Remise fidélité de 5 % disponible pour la prochaine commande : au moins
 * [LOYALTY_DISCOUNT_ORDERS] commandes sur 12 mois, et pas de remise
 * fidélité déjà utilisée sur ces 12 mois (une par an). [excludeOrderId] :
 * commande en cours de modification, ignorée dans le calcul.
 */
fun AppData.loyaltyDiscountAvailable(c: Client, today: LocalDate = LocalDate.now(), excludeOrderId: String? = null): Boolean {
    val since = today.minusYears(1).toEpochDay()
    val sales = orders.filter { it.clientId == c.id && it.status.countsAsSale && it.date >= since && it.id != excludeOrderId }
    return sales.size >= LOYALTY_DISCOUNT_ORDERS && sales.none { o -> o.lines.any { it.isLoyaltyDiscount() } }
}

fun newId(): String = UUID.randomUUID().toString()
