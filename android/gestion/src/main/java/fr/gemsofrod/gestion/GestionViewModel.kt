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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GestionViewModel(app: Application) : AndroidViewModel(app) {

    private val store = Store(app)
    private val saveLock = Mutex()
    private val _data = MutableStateFlow(store.load())
    val data: StateFlow<AppData> = _data.asStateFlow()

    private fun update(transform: (AppData) -> AppData) {
        val next = transform(_data.value)
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
    fun saveOrder(order: Order) = update { d ->
        val old = d.orders.find { it.id == order.id }
        val products = applyStock(d.products, old, order)
        val orders = if (old != null) d.orders.map { if (it.id == order.id) order else it } else d.orders + order
        d.copy(
            products = products,
            orders = orders,
            nextOrderNumber = maxOf(d.nextOrderNumber, order.number + 1),
        )
    }

    fun deleteOrder(id: String) = update { d ->
        val old = d.orders.find { it.id == id } ?: return@update d
        d.copy(products = applyStock(d.products, old, null), orders = d.orders.filterNot { it.id == id })
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
