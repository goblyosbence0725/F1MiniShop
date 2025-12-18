package com.example.minishop.model

object CartManager {

    val items: MutableList<CartItem> = mutableListOf()

    fun addProduct(product: Product) {
        val existing = items.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity++
        } else {
            items.add(CartItem(product, 1))
        }
    }

    fun increaseQuantity(product: Product) {
        val existing = items.find { it.product.id == product.id }
        existing?.let { it.quantity++ }
    }

    fun decreaseQuantity(product: Product) {
        val existing = items.find { it.product.id == product.id }
        existing?.let {
            it.quantity--
            if (it.quantity <= 0) {
                items.remove(it)
            }
        }
    }

    fun removeProduct(product: Product) {
        items.removeAll { it.product.id == product.id }
    }

    fun clearCart() {
        items.clear()
    }

    fun getTotal(): Int {
        return items.sumOf { it.product.price * it.quantity }
    }
}
