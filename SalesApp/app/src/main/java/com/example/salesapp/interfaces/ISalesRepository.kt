package com.example.salesapp.interfaces

import com.example.salesapp.model.Product
import com.example.salesapp.model.ProductList
import com.example.salesapp.model.ProductsDay

interface ISalesRepository {
    fun getAllSales(callback: (MutableList<ProductList>?) -> Unit);
    fun getTodaysClosestSales(mylatitude:Double,mylongitude:Double,callback: (MutableList<Product>?) -> Unit);
    fun getSalesByDay(callback: (MutableList<ProductsDay>?) -> Unit)
}