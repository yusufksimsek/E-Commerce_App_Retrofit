package com.example.e_commerce_app_retrofit.service

import com.example.e_commerce_app_retrofit.model.ModelItem
import retrofit2.Call
import retrofit2.http.GET

interface ProductsAPI {
    @GET("products/categories")
    fun getCategories(): Call<List<String>>

    @GET("products")
    fun getProducts(): Call<List<ModelItem>>
}