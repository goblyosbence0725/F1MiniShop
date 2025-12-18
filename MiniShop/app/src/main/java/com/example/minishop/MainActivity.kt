package com.example.minishop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.minishop.model.CartManager
import com.example.minishop.model.Product
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var productsContainer: LinearLayout

    private val products: List<Product> by lazy {
        listOf(
            Product(
                id = 1,
                name = "F1 Sapka",
                description = "Hivatalos Forma-1 rajongói sapka.",
                price = 7990,
                imageResId = R.drawable.ic_product_cap
            ),
            Product(
                id = 2,
                name = "F1 Póló",
                description = "Kényelmes F1 póló, több méretben.",
                price = 9990,
                imageResId = R.drawable.ic_product_tshirt
            ),
            Product(
                id = 3,
                name = "F1 Bögre",
                description = "Kerámia bögre F1 logóval.",
                price = 3990,
                imageResId = R.drawable.ic_product_mug
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportActionBar?.hide()

        productsContainer = findViewById(R.id.productsContainer)
        val btnOpenCart = findViewById<Button>(R.id.btnOpenCart)

        inflateProducts()

        btnOpenCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun inflateProducts() {
        val inflater = LayoutInflater.from(this)
        productsContainer.removeAllViews()

        for (product in products) {
            val itemView = inflater.inflate(R.layout.item_product, productsContainer, false)

            val ivProductImage = itemView.findViewById<ImageView>(R.id.ivProductImage)
            val tvProductName = itemView.findViewById<TextView>(R.id.tvProductName)
            val tvProductPrice = itemView.findViewById<TextView>(R.id.tvProductPrice)
            val btnDetails = itemView.findViewById<Button>(R.id.btnDetails)

            ivProductImage.setImageResource(product.imageResId)
            tvProductName.text = product.name
            tvProductPrice.text = "${product.price} Ft"

            btnDetails.setOnClickListener {
                showProductDetailDialog(product)
            }

            productsContainer.addView(itemView)
        }
    }

    private fun showProductDetailDialog(product: Product) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_product_detail, null)
        val ivDetailImage = dialogView.findViewById<ImageView>(R.id.ivDetailImage)
        val tvDetailName = dialogView.findViewById<TextView>(R.id.tvDetailName)
        val tvDetailDescription = dialogView.findViewById<TextView>(R.id.tvDetailDescription)
        val tvDetailPrice = dialogView.findViewById<TextView>(R.id.tvDetailPrice)
        val btnDetailAddToCart = dialogView.findViewById<Button>(R.id.btnDetailAddToCart)

        ivDetailImage.setImageResource(product.imageResId)
        tvDetailName.text = product.name
        tvDetailDescription.text = product.description
        tvDetailPrice.text = "${product.price} Ft"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        btnDetailAddToCart.setOnClickListener {
            CartManager.addProduct(product)

            Snackbar
                .make(
                    dialogView,
                    "${product.name} hozzáadva a kosárhoz",
                    Snackbar.LENGTH_LONG
                )
                .show()

            dialog.dismiss()
        }

        dialog.show()
    }
}
