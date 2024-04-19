package com.example.salesapp.repository

import com.example.salesapp.interfaces.ISalesRepository
import com.example.salesapp.model.Product
import com.example.salesapp.model.ProductList
import com.example.salesapp.model.ProductsDay
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.math.abs

class SalesRepository(private val firestore: FirebaseFirestore):ISalesRepository {
    private val allSalesRef: FirebaseFirestore = firestore;


    override fun getAllSales(callback: (MutableList<ProductList>?) -> Unit) {
        allSalesRef.collection("allsales").get().addOnSuccessListener{ querySnapshot ->
            val productList = mutableListOf<ProductList>()
                for (document in querySnapshot.documents) {
                    val products = document.toObject(ProductList::class.java)?.sales
                    products?.let {
                        productList.add(ProductList(it))
                    }
                }
                callback(productList)
            } .addOnFailureListener {
                exception ->
                // Handle any errors
                callback(null)
        }
    }

    override fun getSalesByDay(callback: (MutableList<ProductsDay>?) -> Unit) {
        allSalesRef.collection("allsales").get().addOnSuccessListener{ querySnapshot ->
            val daysList = mutableListOf<ProductsDay>()
            for (document in querySnapshot.documents) {
                val productlist = document.toObject(ProductList::class.java)
                productlist?.let {
                    daysList.add(ProductsDay(document.id, productlist))
                }
            }
            callback(daysList)
        } .addOnFailureListener {
                exception ->
            // Handle any errors
            callback(null)
        }
    }

    override fun getTodaysClosestSales(mylatitude:Double,mylongitude:Double,callback: (MutableList<Product>?) -> Unit) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        println(currentDate)
        val productList = ArrayList<Product>()
        allSalesRef.collection("allsales").document(currentDate).get().addOnSuccessListener{ querySnapshot ->
            if (querySnapshot.exists()) {
                val products = querySnapshot.toObject(ProductList::class.java)?.sales
                for(p in products!!){
                    products.let {
                        productList.add(p)
                    }
                }
            }
            callback(getClosest(mylatitude,mylongitude,productList))
        } .addOnFailureListener {
                exception ->
            // Handle any errors
            callback(null)
        }
    }

    fun getClosest(mylatitude:Double,mylongitude:Double,produtos:ArrayList<Product>): MutableList<Product> {
        val closest = ArrayList<Product>()
        val c: Comparator<Product> = Comparator<Product> { p1, p2 ->
            val lat1: Double? = p1.latitude
            val lon1: Double? = p1.longitude
            val lat2: Double? = p2.latitude
            val lon2: Double? = p2.longitude
            var d1 =0.0
            var d2 =0.0
            if(lat1!=null && lon1!=null && lon2!=null && lat2!=null){
                d1 = abs(lat1-mylatitude) + abs(lon1-mylongitude)
                d2 = abs(lat2-mylatitude) + abs(lon2-mylongitude)
            }
            return@Comparator d1.compareTo(d2)
        }
        produtos.stream().sorted(c).collect(Collectors.toList())
        //get 5 closest
        var i =0
        for(p in produtos){
            closest.add(p)
            if(i==5){ break }
            i++
        }
        return closest
    }



}