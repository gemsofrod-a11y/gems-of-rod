package com.gemsofrod.app.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)

object CartManager {

    private val _items = MutableLiveData<List<CartItem>>(emptyList())
    val items: LiveData<List<CartItem>> get() = _items

    val itemCount: Int get() = _items.value?.sumOf { it.quantity } ?: 0

    val total: Double get() = _items.value?.sumOf { it.product.price * it.quantity } ?: 0.0

    val formattedTotal: String get() {
        val nf = java.text.NumberFormat.getNumberInstance(java.util.Locale.FRANCE)
        nf.maximumFractionDigits = 0
        return "${nf.format(total)} €"
    }

    fun add(product: Product) {
        val current = _items.value.orEmpty().toMutableList()
        val existing = current.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity++
        } else {
            current.add(CartItem(product))
        }
        _items.value = current
    }

    fun remove(productId: String) {
        _items.value = _items.value.orEmpty().filter { it.product.id != productId }
    }

    fun clear() {
        _items.value = emptyList()
    }
}
