package com.example.minishop.model

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)