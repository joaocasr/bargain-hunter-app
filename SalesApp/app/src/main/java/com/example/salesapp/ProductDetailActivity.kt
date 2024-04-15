package com.example.salesapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {
    lateinit var nome: TextView
    lateinit var imagem: ImageView
    lateinit var rua: TextView
    lateinit var localizacao: TextView
    lateinit var preco: TextView
    lateinit var paybtn : CardView
    lateinit var goback : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        paybtn = findViewById(R.id.cardviewpay)
        goback = findViewById(R.id.gobackbutton)

        nome = findViewById(R.id.name_product)
        imagem = findViewById(R.id.img_product)
        rua = findViewById(R.id.street_product)
        localizacao = findViewById(R.id.location_product)
        preco = findViewById(R.id.price_product)

        nome.text = intent.getStringExtra("name_product")
        val strt = "Street\n  "+intent.getStringExtra("street_product")
        rua.text = strt
        val lat = intent.getDoubleExtra("latitude_product",0.0)
        val lon = intent.getDoubleExtra("longitude_product",0.0)
        val loca = "Longitude: $lon º\nLatitude: $lat º"
        localizacao.text = loca
        preco.text = intent.getStringExtra("preco_product")
        val url : String? = intent.getStringExtra("imagem_product")
        Picasso.get().load(url).into(imagem)

        paybtn.setOnClickListener (View.OnClickListener{
            showDialog()
            }
        )
        goback.setOnClickListener(View.OnClickListener {
            val intent:Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        })
    }

    fun showDialog(){
        val dialog : Dialog = Dialog(this,R.style.DialogStyle)
        dialog.setContentView(R.layout.costum_dialog)
        dialog.window?.setBackgroundDrawableResource(R.drawable.background_dialog)
        val btnclose : ImageView = dialog.findViewById(R.id.btn_close)
        btnclose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }
}