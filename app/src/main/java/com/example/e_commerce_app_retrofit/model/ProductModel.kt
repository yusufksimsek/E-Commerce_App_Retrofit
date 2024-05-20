package com.example.e_commerce_app_retrofit.model

data class ModelItem(
    var category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    var title: String,
    var quantity: Int

)

data class Rating(
    val count: Int,
    val rate: Double
)

