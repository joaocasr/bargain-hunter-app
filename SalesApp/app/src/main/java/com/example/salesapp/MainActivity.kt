package com.example.salesapp

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.salesapp.adapters.SalesAdapter
import com.example.salesapp.model.MyNotificationService
import com.example.salesapp.model.Product
import com.example.salesapp.repository.SalesRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var recyclerview:RecyclerView
    lateinit var salesAdapter:SalesAdapter
    lateinit var loading: LottieAnimationView
    var listproduct = ArrayList<Product>()
    var filteredList = ArrayList<Product>()
    lateinit var navigation : BottomNavigationView
    lateinit var searchbox : EditText
    lateinit var close : ImageView
    lateinit var welcome: TextView
    lateinit var cardplan : CardView
    lateinit var settingsbtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcome = findViewById(R.id.welcome)

        auth = Firebase.auth
        if(auth.currentUser != null){
            val email = auth.currentUser!!.email
            val m = "Hello, "+ email.toString() +"!"
            welcome.text = m
        }else{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        recyclerview = findViewById(R.id.recycler_view1)
        cardplan = findViewById(R.id.mycardview)
        loading = findViewById(R.id.loading)
        navigation = findViewById(R.id.bottom_navigation)
        searchbox = findViewById(R.id.search_view)
        settingsbtn = findViewById(R.id.settingsbtn)
        close = findViewById(R.id.closeimage)

        val firebaseDatabase = FirebaseFirestore.getInstance()
        val productRepo = SalesRepository(firebaseDatabase)

        if(listproduct.size==0) {
            productRepo.getAllSales { allsales ->
                if (allsales != null) {
                    for (products in allsales) {
                        for (p in products.sales) {
                            listproduct.add(p)
                        }
                    }
                    loading.visibility = View.GONE
                    recyclerview.visibility = View.VISIBLE
                    salesAdapter = SalesAdapter(ArrayList(listproduct.shuffled()), this)
                    val layoutManager: RecyclerView.LayoutManager =LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    recyclerview.layoutManager = layoutManager
                    recyclerview.setHasFixedSize(true)
                    recyclerview.adapter = salesAdapter
                    clickItem(salesAdapter)
                    startListener()
                }
            }
        }else{
            salesAdapter = SalesAdapter(ArrayList(listproduct.shuffled()), this)
            val layoutManager: RecyclerView.LayoutManager =LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerview.layoutManager = layoutManager
            recyclerview.setHasFixedSize(true)
            recyclerview.adapter = salesAdapter
            clickItem(salesAdapter)
        }
        navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> true
                R.id.nav_nearest ->{
                    startActivity(Intent(applicationContext,NearestActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.nav_visualization ->{
                    startActivity(Intent(applicationContext,ChartsActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                else -> {true}
            }
        }

        searchbox.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                close.visibility=View.GONE
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                close.visibility=View.VISIBLE
                return
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

        })
        cardplan.setOnClickListener {
            showDialog()
        }
        close.setOnClickListener {
            searchbox.setText("")
        }
        settingsbtn.setOnClickListener {
            val intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
        }
    }

    fun clickItem(recyclerAdapter: SalesAdapter){
        recyclerAdapter.onItemClick = {
            val intent = Intent(this,ProductDetailActivity::class.java)
            intent.putExtra("name_product",it.nome)
            intent.putExtra("discount_product",it.desconto)
            intent.putExtra("imagem_product",it.imagem)
            intent.putExtra("preco_product",it.preco)
            intent.putExtra("latitude_product",it.latitude)
            intent.putExtra("longitude_product",it.longitude)
            intent.putExtra("street_product",it.rua)
            startActivity(intent)
        }

    }

    private fun filter(s: String) {
        filteredList.clear()
        for (p in listproduct) {
            if (p.nome?.lowercase()?.contains(s.lowercase()) == true) {
                filteredList.add(p)
            }
        }
        salesAdapter.filteredProducts(filteredList)
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

    fun startListener() {
        // change to today
        println("hello")
        FirebaseFirestore.getInstance().collection("allsales").document("2024-04-17")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }
                else{
                    if (snapshots != null) {
                        val produtos = snapshots.data!!.get("sales").toString().split("nome=")
                        var newrandomproduct = produtos[produtos.size-1].split(",")[0]

                        val serviceintent = Intent(this, MyNotificationService::class.java)
                        serviceintent.putExtra("randomproduct",newrandomproduct)
                        startService(serviceintent)
                    }
                }
            }
    }
}