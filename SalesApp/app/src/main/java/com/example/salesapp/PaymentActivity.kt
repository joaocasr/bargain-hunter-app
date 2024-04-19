package com.example.salesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PaymentActivity : AppCompatActivity() {
    lateinit var goback:Button
    lateinit var price : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val preco = intent.getStringExtra("preco_product")
        price = findViewById(R.id.total)
        val m = "Total: $preco"
        price.text = m

        goback = findViewById(R.id.gobackbutton)
        goback.setOnClickListener {
            val newintent= Intent(this,ProductDetailActivity::class.java)
            newintent.putExtra("name_product",intent.getStringExtra("name_product"))
            newintent.putExtra("discount_product",intent.getStringExtra("discount_product"))
            newintent.putExtra("imagem_product",intent.getStringExtra("imagem_product"))
            newintent.putExtra("preco_product",intent.getStringExtra("preco_product"))
            newintent.putExtra("latitude_product",intent.getDoubleExtra("latitude_product",0.0))
            newintent.putExtra("longitude_product",intent.getDoubleExtra("longitude_product",0.0))
            newintent.putExtra("street_product",intent.getStringExtra("street_product"))
            startActivity(newintent)
            overridePendingTransition(0,0)

        }
    }
}