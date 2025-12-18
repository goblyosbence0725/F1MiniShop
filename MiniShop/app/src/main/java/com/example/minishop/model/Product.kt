package com.example.minishop.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,      // Ft
    val imageResId: Int  // drawable resource
)