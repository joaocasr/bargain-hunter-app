package com.example.salesapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.salesapp.adapters.ClosestSalesAdapter
import com.example.salesapp.adapters.SalesAdapter
import com.example.salesapp.model.LocationData
import com.example.salesapp.model.LocationViewModel
import com.example.salesapp.model.Product
import com.example.salesapp.repository.SalesRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class NearestActivity : AppCompatActivity() {
    lateinit var navigation : BottomNavigationView
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var locationManager:LocationManager
    private val locationvm: LocationViewModel by viewModels()
    lateinit var locationSensorListener:LocationSensorListener
    lateinit var radar : LottieAnimationView
    lateinit var coordenadas : TextView
    lateinit var estado : TextView
    lateinit var iconplace : ImageView
    lateinit var recyclerview: RecyclerView
    lateinit var closestadapter: ClosestSalesAdapter
    var produtos : List<Product> = ArrayList()
    var firebaseDatabase = FirebaseFirestore.getInstance()


    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearest)

        radar = findViewById(R.id.radar)
        estado = findViewById(R.id.estadoatual)
        iconplace = findViewById(R.id.placeicon)
        navigation = findViewById(R.id.bottom_navigation2)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationSensorListener = LocationSensorListener()
        locationSensorListener.setSensorManager(locationManager,locationvm)
        coordenadas = findViewById(R.id.mylocation)
        recyclerview = findViewById(R.id.recycler_view2)

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100)
        }else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0.0f,
                locationSensorListener
            )
        }
        val locationObserver = Observer<LocationData> { locSample ->
            latitude = locSample.latitude
            longitude = locSample.longitude
            changeUI()
        }
        locationvm.currentLocationData.observe(this, locationObserver)

        navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.nav_nearest -> true
                R.id.nav_settings ->{
                    startActivity(Intent(applicationContext,SettingsActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                else -> {true}
            }
        }
    }

    fun changeUI() {
        val m = "Latitude: $latitude º\nLongitude: $longitude º"
        iconplace.visibility = View.VISIBLE
        coordenadas.text = m
        val s = "Detecting discounts near you..."
        estado.text = s
        //println("$latitude;$longitude")

        if(produtos.isEmpty()) {
            val productRepo = SalesRepository(firebaseDatabase)
            productRepo.getTodaysClosestSales(latitude, longitude) { closeproducts ->
                if (closeproducts != null) {
                    produtos = ArrayList(closeproducts)
                    closestadapter = ClosestSalesAdapter(produtos as ArrayList<Product>, this)
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    recyclerview.layoutManager = layoutManager
                    recyclerview.setHasFixedSize(true)
                    recyclerview.adapter = closestadapter
                    clickItem(closestadapter)
                }
            }
        }else{
            closestadapter = ClosestSalesAdapter(produtos as ArrayList<Product>, this)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerview.layoutManager = layoutManager
            recyclerview.setHasFixedSize(true)
            recyclerview.adapter = closestadapter
            clickItem(closestadapter)
        }
        estado.visibility = View.GONE
        radar.visibility = View.GONE

    }

    fun clickItem(recyclerAdapter: ClosestSalesAdapter){
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

}