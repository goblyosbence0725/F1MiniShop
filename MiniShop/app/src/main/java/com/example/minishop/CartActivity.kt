package com.example.minishop

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.minishop.model.CartItem
import com.example.minishop.model.CartManager

class CartActivity : AppCompatActivity() {

    private lateinit var cartItemsContainer: LinearLayout
    private lateinit var tvTotal: TextView
    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var spinnerPayment: Spinner
    private lateinit var btnPlaceOrder: Button
    private lateinit var btnBackToShop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartItemsContainer = findViewById(R.id.cartItemsContainer)
        tvTotal = findViewById(R.id.tvTotal)
        etName = findViewById(R.id.etName)
        etAddress = findViewById(R.id.etAddress)
        spinnerPayment = findViewById(R.id.spinnerPayment)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        btnBackToShop = findViewById(R.id.btnBackToShop)

        setupPaymentSpinner()
        loadCart()

        btnPlaceOrder.setOnClickListener {
            placeOrder()
        }


        btnBackToShop.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCart()
    }

    private fun setupPaymentSpinner() {
        val methods = listOf("Válassz fizetési módot", "Utánvét", "Bankkártya", "Átutalás")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            methods
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPayment.adapter = adapter
    }

    private fun loadCart() {
        cartItemsContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for (item: CartItem in CartManager.items) {
            val itemView = inflater.inflate(R.layout.item_cart, cartItemsContainer, false)

            val ivCartImage = itemView.findViewById<ImageView>(R.id.ivCartImage)
            val tvCartProductName = itemView.findViewById<TextView>(R.id.tvCartProductName)
            val tvCartProductPrice = itemView.findViewById<TextView>(R.id.tvCartProductPrice)
            val tvQuantity = itemView.findViewById<TextView>(R.id.tvQuantity)
            val btnMinus = itemView.findViewById<Button>(R.id.btnMinus)
            val btnPlus = itemView.findViewById<Button>(R.id.btnPlus)
            val btnRemove = itemView.findViewById<Button>(R.id.btnRemove)

            ivCartImage.setImageResource(item.product.imageResId)
            tvCartProductName.text = item.product.name
            tvCartProductPrice.text = "${item.product.price} Ft / db"
            tvQuantity.text = item.quantity.toString()

            btnMinus.setOnClickListener {
                CartManager.decreaseQuantity(item.product)
                loadCart()
            }

            btnPlus.setOnClickListener {
                CartManager.increaseQuantity(item.product)
                loadCart()
            }

            btnRemove.setOnClickListener {
                CartManager.removeProduct(item.product)
                loadCart()
            }

            cartItemsContainer.addView(itemView)
        }

        val total = CartManager.getTotal()
        tvTotal.text = "Összesen: $total Ft"
    }

    private fun placeOrder() {
        val name = etName.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val paymentIndex = spinnerPayment.selectedItemPosition

        if (CartManager.items.isEmpty()) {
            Toast.makeText(this, "A kosár üres.", Toast.LENGTH_SHORT).show()
            return
        }
        if (name.isEmpty()) {
            etName.error = "Kötelező mező"
            return
        }
        if (address.isEmpty()) {
            etAddress.error = "Kötelező mező"
            return
        }
        if (paymentIndex == 0) {
            Toast.makeText(this, "Válassz fizetési módot!", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Rendelés sikeres")
            .setMessage("Köszönjük a rendelést, $name!\n\nA kosár most kiürül.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                CartManager.clearCart()
                loadCart()
                etName.setText("")
                etAddress.setText("")
                spinnerPayment.setSelection(0)
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
