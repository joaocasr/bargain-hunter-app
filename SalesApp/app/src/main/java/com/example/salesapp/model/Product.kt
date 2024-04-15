package com.example.salesapp.model

import com.google.firebase.database.PropertyName

data class Product (
    @get:PropertyName("getDiscount")
    @set:PropertyName("setDiscount")
    var desconto:String?="",

    @get:PropertyName("getImage")
    @set:PropertyName("setImage")
    var imagem: String?="",

    @get:PropertyName("getLatitude")
    @set:PropertyName("setLatitude")
    var latitude: Double?=0.0,

    @get:PropertyName("getLongitude")
    @set:PropertyName("setLongitude")
    var longitude: Double?=0.0,

    @get:PropertyName("getName")
    @set:PropertyName("setName")
    var nome: String?="",

    @get:PropertyName("getPrice")
    @set:PropertyName("setPrice")
    var preco: String?="",

    @get:PropertyName("getStreet")
    @set:PropertyName("setStreet")
    var rua:String?=""
)