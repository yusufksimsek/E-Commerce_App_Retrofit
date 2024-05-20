package com.example.e_commerce_app_retrofit.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductsClient {
    private const val BASE_URL = "https://fakestoreapi.com/"

    val ProductsApiService: ProductsAPI by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ProductsAPI::class.java)
    }
}