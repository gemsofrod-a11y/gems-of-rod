package fr.gemsofrod.gestion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Client
import fr.gemsofrod.gestion.data.Order
import fr.gemsofrod.gestion.data.Product
import fr.gemsofrod.gestion.data.SampleData
import fr.gemsofrod.gestion.data.Store
import fr.gemsofrod.gestion.data.SumUpAttempt
import fr.gemsofrod.gestion.data.SumUpItem
import fr.gemsofrod.gestion.data.SumUpPayment
import fr.gemsofrod.gestion.data.normalizeName
import fr.gemsofrod.gestion.data.withShippingCodes
import fr.gemsofrod.gestion.sumup.SumUpCheckout
import fr.gemsofrod.gestion.sumup.SumUpClient
import fr.gemsofrod.gestion.sumup.SumUpSettings
import fr.gemsofrod.gestion.sumup.SumUpTransaction
import fr.gemsofrod.gestion.ui.orderRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/** État de la connexion SumUp affiché à l'écran. */
data class SumUpUi(
    val configured: Boolean = false,
    val merchantCode: String = "",
    val busy: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
    val lastSync: Long = 0L,
)

class GestionViewModel(app: Application) : AndroidViewModel(app) {

    private val store = Store(app)
    private val saveLock = Mutex()
    private val _data = MutableStateFlow(store.load())
    val data: StateFlow<AppData> = _data.asStateFlow()

    private fun update(transform: (AppData) -> AppData) {
        // Fidélité : chaque nouveau client à 3 commandes reçoit son code de livraison offerte.
        val next = transform(_data.value).withShippingCodes()
        _data.value = next
        viewModelScope.launch(Dispatchers.IO) { saveLock.withLock { store.save(_data.value) } }
    }

    // --- Stock ---

    fun saveProduct(product: Product) = update { d ->
        val exists = d.products.any { it.id == product.id }
        d.copy(products = if (exists) d.products.map { if (it.id == product.id) product else it } else d.products + product)
    }

    fun deleteProduct(id: String) = update { d -> d.copy(products = d.products.filterNot { it.id == id }) }

    fun adjustStock(id: String, delta: Double) = update { d ->
        d.copy(products = d.products.map { if (it.id == id) it.copy(quantity = it.quantity + delta) else it })
    }

    // --- Clients ---

    fun saveClient(client: Client) = update { d ->
        val exists = d.clients.any { it.id == client.id }
        // Le nom affiché sur les commandes suit le nom du client.
        val orders = d.orders.map { if (it.clientId == client.id) it.copy(clientName = client.name) else it }
        d.copy(
            clients = if (exists) d.clients.map { if (it.id == client.id) client else it } else d.clients + client,
            orders = orders,
        )
    }

    /** Les commandes du client sont conservées, avec son nom en clair. */
    fun deleteClient(id: String) = update { d ->
        d.copy(
            clients = d.clients.filterNot { it.id == id },
            orders = d.orders.map { if (it.clientId == id) it.copy(clientId = null) else it },
        )
    }

    // --- Commandes ---

    /**
     * Enregistre la commande et met le stock à jour : les quantités réservées
     * par l'ancienne version sont rendues, celles de la nouvelle sont sorties.
     */
    fun saveOrder(input: Order) = update { d ->
        val old = d.orders.find { it.id == input.id }
        val order = withPaidDate(input)
        val products = applyStock(d.products, old, order)
        val orders = if (old != null) d.orders.map { if (it.id == order.id) order else it } else d.orders + order
        d.copy(
            products = products,
            orders = orders,
            nextOrderNumber = maxOf(d.nextOrderNumber, order.number + 1),
        )
    }

    /** Encaisse tout le solde restant aujourd'hui. */
    fun markPaid(id: String) {
        val order = _data.value.orders.find { it.id == id } ?: return
        saveOrder(order.copy(deposit = order.total))
    }

    fun deleteOrder(id: String) = update { d ->
        val old = d.orders.find { it.id == id } ?: return@update d
        d.copy(products = applyStock(d.products, old, null), orders = d.orders.filterNot { it.id == id })
    }

    /** Date de règlement : posée quand le solde tombe à zéro, retirée sinon. */
    private fun withPaidDate(o: Order): Order = when {
        o.isPaid -> o.copy(paidDate = o.paidDate ?: LocalDate.now().toEpochDay())
        else -> o.copy(paidDate = null)
    }

    private fun applyStock(products: List<Product>, old: Order?, new: Order?): List<Product> {
        val delta = HashMap<String, Double>()
        old?.takeIf { it.status.reservesStock }?.lines?.forEach { l ->
            l.productId?.let { delta[it] = (delta[it] ?: 0.0) + l.quantity }
        }
        new?.takeIf { it.status.reservesStock }?.lines?.forEach { l ->
            l.productId?.let { delta[it] = (delta[it] ?: 0.0) - l.quantity }
        }
        if (delta.isEmpty()) return products
        return products.map { p -> delta[p.id]?.let { p.copy(quantity = p.quantity + it) } ?: p }
    }

    // --- SumUp ---

    private val sumupSettings = SumUpSettings(app)
    private val _sumup = MutableStateFlow(sumupUi())
    val sumup: StateFlow<SumUpUi> = _sumup.asStateFlow()

    private fun sumupUi(busy: Boolean = false, message: String? = null, isError: Boolean = false) = SumUpUi(
        configured = sumupSettings.isConfigured,
        merchantCode = sumupSettings.merchantCode,
        busy = busy,
        message = message,
        isError = isError,
        lastSync = sumupSettings.lastSync,
    )

    /** Vérifie la clé auprès de SumUp, l'enregistre puis lance une synchronisation. */
    fun connectSumUp(apiKey: String, merchantCode: String) {
        val key = apiKey.trim()
        if (key.isBlank()) return
        _sumup.value = sumupUi(busy = true, message = "Connexion à SumUp…")
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    val client = SumUpClient(key)
                    val code = merchantCode.trim().uppercase().ifBlank { client.merchantCode() }
                    client.transactions(code, emptySet(), maxPages = 1) // test de la clé et du code
                    code
                }
            }.onSuccess { code ->
                sumupSettings.apiKey = key
                sumupSettings.merchantCode = code
                sumupSettings.stockSince = System.currentTimeMillis()
                syncSumUp()
            }.onFailure {
                _sumup.value = sumupUi(message = it.message ?: "Connexion impossible.", isError = true)
            }
        }
    }

    fun disconnectSumUp() {
        sumupSettings.clear()
        _sumup.value = sumupUi(message = "SumUp déconnecté. Les paiements déjà récupérés restent dans l'app.")
    }

    /**
     * Récupère les nouveaux paiements SumUp et vérifie les liens de paiement
     * en attente (une commande dont le lien est payé est encaissée d'office).
     */
    fun syncSumUp() {
        if (!sumupSettings.isConfigured || _sumup.value.busy && _sumup.value.message != "Connexion à SumUp…") return
        _sumup.value = sumupUi(busy = true, message = "Synchronisation…")
        val key = sumupSettings.apiKey
        val code = sumupSettings.merchantCode
        // Connexion antérieure à la gestion du stock : on part de maintenant.
        if (sumupSettings.stockSince == 0L) sumupSettings.stockSince = System.currentTimeMillis()
        val stockSince = sumupSettings.stockSince
        viewModelScope.launch {
            val snapshot = _data.value
            runCatching {
                withContext(Dispatchers.IO) {
                    val client = SumUpClient(key)
                    val checkouts = snapshot.orders.filter { it.sumupCheckoutId != null }.mapNotNull { o ->
                        runCatching { o.id to client.checkout(o.sumupCheckoutId!!) }.getOrNull()
                    }
                    val known = (snapshot.sumupPayments.map { it.code } + snapshot.sumupAttempts.map { it.code }).toSet()
                    // Articles vendus : seulement pour les ventes récentes (au plus 40 par synchronisation).
                    val txs = client.transactions(code, known).mapIndexed { i, t ->
                        if (t.status == "SUCCESSFUL" && t.epochMillis >= stockSince && i < 40) {
                            t.copy(items = runCatching { client.transactionItems(code, t.code) }.getOrDefault(emptyList()))
                        } else t
                    }
                    checkouts to txs
                }
            }.onSuccess { (checkouts, txs) ->
                val before = _data.value.sumupPayments.size
                val attemptsBefore = _data.value.sumupAttempts.size
                update { d -> applySumUp(d, checkouts, txs) }
                val added = _data.value.sumupPayments.size - before
                val failed = _data.value.sumupAttempts.size - attemptsBefore
                sumupSettings.lastSync = System.currentTimeMillis()
                _sumup.value = sumupUi(
                    message = when (added) {
                        0 -> "À jour : aucun nouveau paiement."
                        1 -> "1 nouveau paiement récupéré."
                        else -> "$added nouveaux paiements récupérés."
                    } + when (failed) {
                        0 -> ""
                        1 -> " 1 paiement en ligne non abouti."
                        else -> " $failed paiements en ligne non aboutis."
                    },
                )
            }.onFailure {
                _sumup.value = sumupUi(message = it.message ?: "Synchronisation impossible.", isError = true)
            }
        }
    }

    private fun applySumUp(d: AppData, checkouts: List<Pair<String, SumUpCheckout>>, txs: List<SumUpTransaction>): AppData {
        var orders = d.orders
        val payments = d.sumupPayments.toMutableList()
        val known = payments.map { it.code }.toMutableSet()
        val nowTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        for ((orderId, co) in checkouts) {
            when (co.status) {
                "PAID" -> {
                    val codes = co.transactionCodes.ifEmpty { listOf("CO-" + co.id) }
                    if (codes.none { it in known }) {
                        val tx = txs.find { it.code == codes.first() }
                        payments += SumUpPayment(
                            code = codes.first(), amount = co.amount,
                            date = tx?.date ?: LocalDate.now().toEpochDay(), time = tx?.time ?: nowTime,
                            paymentType = "ECOM", orderId = orderId,
                        )
                        orders = orders.map { if (it.id == orderId) withPaidDate(it.copy(deposit = it.deposit + co.amount, sumupCheckoutId = null)) else it }
                    } else {
                        orders = orders.map { if (it.id == orderId) it.copy(sumupCheckoutId = null) else it }
                    }
                    known += codes
                }
                "FAILED", "EXPIRED" -> orders = orders.map { if (it.id == orderId) it.copy(sumupCheckoutId = null) else it }
            }
        }
        val added = mutableListOf<SumUpPayment>()
        val attempts = d.sumupAttempts.toMutableList()
        for (tx in txs) {
            if (tx.code in known || attempts.any { it.code == tx.code }) continue
            if (tx.status != "SUCCESSFUL") {
                attempts += SumUpAttempt(tx.code, tx.amount, tx.date, tx.time, tx.paymentType, tx.status, tx.summary)
                continue
            }
            val items = tx.items.map { (name, q) -> SumUpItem(name, q, matchProduct(d, name)) }
            added += SumUpPayment(tx.code, tx.amount, tx.date, tx.time, tx.paymentType, items = items)
            known += tx.code
        }
        payments += added
        return d.copy(
            orders = orders,
            products = applyEffects(d.products, added.map { it.stockEffect }),
            sumupPayments = payments.sortedWith(compareByDescending<SumUpPayment> { it.date }.thenByDescending { it.time }),
            sumupAttempts = attempts.sortedWith(compareByDescending<SumUpAttempt> { it.date }.thenByDescending { it.time }),
        )
    }

    /** Produit du stock correspondant à un article SumUp : correspondance apprise, sinon même nom. */
    private fun matchProduct(d: AppData, name: String): String? {
        val key = normalizeName(name)
        d.sumupProductMap[key]?.let { id -> if (d.products.any { it.id == id }) return id }
        return d.products.find { normalizeName(it.name) == key }?.id
    }

    /** Applique des variations de stock (produit → quantité) ; [sign] = -1 pour les annuler. */
    private fun applyEffects(products: List<Product>, effects: List<Map<String, Double>>, sign: Double = 1.0): List<Product> {
        val total = HashMap<String, Double>()
        effects.forEach { e -> e.forEach { (id, q) -> total[id] = (total[id] ?: 0.0) + q * sign } }
        if (total.isEmpty()) return products
        return products.map { p -> total[p.id]?.let { p.copy(quantity = p.quantity + it) } ?: p }
    }

    /**
     * Associe un article SumUp non reconnu à un produit du stock et retient la
     * correspondance : toutes les ventes portant ce nom sont mises à jour et
     * sortent du stock (si ce sont des ventes directes).
     */
    fun mapSumUpItem(name: String, productId: String) = update { d ->
        val key = normalizeName(name)
        val before = d.sumupPayments
        val after = before.map { p ->
            if (p.items.none { normalizeName(it.name) == key }) p
            else p.copy(items = p.items.map { if (normalizeName(it.name) == key) it.copy(productId = productId) else it })
        }
        var products = applyEffects(d.products, before.map { it.stockEffect }, sign = -1.0)
        products = applyEffects(products, after.map { it.stockEffect })
        d.copy(products = products, sumupPayments = after, sumupProductMap = d.sumupProductMap + (key to productId))
    }

    /** Rattache un paiement SumUp à une commande (null = vente directe), en ajustant les montants reçus. */
    fun linkPayment(code: String, orderId: String?) = update { d ->
        val p = d.sumupPayments.find { it.code == code } ?: return@update d
        if (p.orderId == orderId) return@update d
        val linked = p.copy(orderId = orderId)
        // Vente directe ↔ commande : le stock suit (jamais compté deux fois).
        var products = applyEffects(d.products, listOf(p.stockEffect), sign = -1.0)
        products = applyEffects(products, listOf(linked.stockEffect))
        val orders = d.orders.map { o ->
            when (o.id) {
                p.orderId -> withPaidDate(o.copy(deposit = (o.deposit - p.amount).coerceAtLeast(0.0)))
                orderId -> withPaidDate(o.copy(deposit = o.deposit + p.amount))
                else -> o
            }
        }
        d.copy(products = products, orders = orders, sumupPayments = d.sumupPayments.map { if (it.code == code) linked else it })
    }

    /**
     * Crée un lien de paiement SumUp pour le reste dû de la commande, puis
     * renvoie l'adresse (ou un message d'erreur) à [onResult].
     */
    /** Écarte (ou fait revenir) un paiement non abouti de la liste à relancer. */
    fun dismissAttempt(code: String, dismissed: Boolean = true) = update { d ->
        d.copy(sumupAttempts = d.sumupAttempts.map { if (it.code == code) it.copy(dismissed = dismissed) else it })
    }

    /** Nouveau lien de paiement SumUp du montant d'un paiement non abouti, pour relancer le client. */
    fun createAttemptLink(code: String, onResult: (url: String?, error: String?) -> Unit) {
        val attempt = _data.value.sumupAttempts.find { it.code == code } ?: return
        if (!sumupSettings.isConfigured) {
            onResult(null, "Connectez d'abord SumUp : menu ⋮ → SumUp.")
            return
        }
        val key = sumupSettings.apiKey
        val merchant = sumupSettings.merchantCode
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    SumUpClient(key).createCheckout(
                        merchant, "GOR-R-${attempt.code}-${System.currentTimeMillis()}", attempt.amount,
                        "Gems of Rod - ${attempt.summary.ifBlank { "votre commande" }}".take(120),
                    )
                }
            }.onSuccess { co -> onResult(co.url, if (co.url == null) "SumUp n'a pas renvoyé de lien de paiement." else null) }
                .onFailure { onResult(null, it.message ?: "Création du lien impossible.") }
        }
    }

    fun createPaymentLink(orderId: String, onResult: (url: String?, error: String?) -> Unit) {
        val order = _data.value.orders.find { it.id == orderId } ?: return
        if (!sumupSettings.isConfigured) {
            onResult(null, "Connectez d'abord SumUp : menu ⋮ → SumUp.")
            return
        }
        val key = sumupSettings.apiKey
        val code = sumupSettings.merchantCode
        val amount = Math.round(order.balance * 100) / 100.0
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    SumUpClient(key).createCheckout(
                        code, "GOR-${order.number}-${System.currentTimeMillis()}", amount,
                        "Gems of Rod - commande ${orderRef(order.number)}",
                    )
                }
            }.onSuccess { co ->
                if (co.url == null) {
                    onResult(null, "SumUp n'a pas renvoyé de lien de paiement.")
                } else {
                    update { d -> d.copy(orders = d.orders.map { if (it.id == orderId) it.copy(sumupCheckoutId = co.id) else it }) }
                    onResult(co.url, null)
                }
            }.onFailure { onResult(null, it.message ?: "Création du lien impossible.") }
        }
    }

    /**
     * Importe un export Excel du tableau de bord SumUp (articles ou clients)
     * et renvoie un compte rendu à afficher.
     */
    fun importSumUpExport(bytes: ByteArray): String {
        val result = try {
            fr.gemsofrod.gestion.data.SumUpImport.import(bytes, _data.value)
        } catch (e: fr.gemsofrod.gestion.data.SumUpImport.ImportException) {
            return e.message ?: "Import impossible."
        } catch (e: Exception) {
            return "Import impossible : fichier non reconnu."
        }
        update { result.data }
        val what = if (result.kind == fr.gemsofrod.gestion.data.SumUpImport.Kind.ARTICLES) "article" else "client"
        fun n(k: Int) = "$k $what${if (k > 1) "s" else ""}"
        return when {
            result.created + result.updated == 0 -> "Aucun $what trouvé dans ce fichier."
            result.updated == 0 -> "${n(result.created)} importé${if (result.created > 1) "s" else ""} depuis SumUp."
            else -> "${n(result.created)} ajouté${if (result.created > 1) "s" else ""}, ${result.updated} mis à jour depuis SumUp."
        }
    }

    // --- Fidélité ---

    /** Le message avec le code a été préparé pour envoi. */
    fun markShippingCodeSent(clientId: String) = update { d ->
        d.copy(clients = d.clients.map { if (it.id == clientId) it.copy(shippingCodeSentAt = System.currentTimeMillis()) else it })
    }

    fun setShippingCodeUsed(clientId: String, used: Boolean) = update { d ->
        d.copy(clients = d.clients.map { if (it.id == clientId) it.copy(shippingCodeUsed = used) else it })
    }

    // --- Sauvegarde ---

    fun exportJson(): String = Store.toJson(_data.value)

    /** Remplace toutes les données. Renvoie false si le fichier est invalide. */
    fun importJson(text: String): Boolean {
        val imported = runCatching { Store.fromJson(text) }.getOrNull() ?: return false
        update { imported }
        return true
    }

    fun loadSample() = update { SampleData.build() }

    fun clearAll() = update { AppData() }
}
