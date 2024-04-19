package com.example.salesapp.model

import com.google.firebase.database.PropertyName

data class ProductList (

    @get:PropertyName("getSales")
    @set:PropertyName("setSales")
    var sales : List<Product> = emptyList()
){
    fun getMediaDiscount(): Double {
        var d = 0
        var n = 0
        for (p in this.sales){
            d+=p.desconto!!.substring(0,p.desconto!!.length-1).toInt()
            n+=1;
        }
        return (d/n).toDouble();
    }
    fun groupByStreet():Map<String,List<Product>>{
        return this.sales.groupBy { it.rua!! }
    }

}