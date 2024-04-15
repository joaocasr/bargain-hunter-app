package com.example.salesapp.model

import com.google.firebase.database.PropertyName

data class ProductList (

    @get:PropertyName("getSales")
    @set:PropertyName("setSales")
    var sales : List<Product> = emptyList()
)