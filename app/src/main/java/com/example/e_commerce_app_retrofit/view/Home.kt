package com.example.e_commerce_app_retrofit.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce_app_retrofit.R
import com.example.e_commerce_app_retrofit.adapter.CategoryAdapter
import com.example.e_commerce_app_retrofit.adapter.ProductAdapter
import com.example.e_commerce_app_retrofit.databinding.FragmentHomeBinding
import com.example.e_commerce_app_retrofit.model.ModelItem
import com.example.e_commerce_app_retrofit.service.ProductsClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private var categoryList: ArrayList<String> = ArrayList()
    private val navController by lazy { findNavController() }

    companion object {
        var productList = ArrayList<ModelItem>()
        var allProducts = listOf<ModelItem>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.popBackStack()
        }

        binding.rvCategories.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(requireContext(), categoryList, "All")
        binding.rvCategories.adapter = categoryAdapter
        fetchCategories()

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        productAdapter = ProductAdapter(requireContext(), productList, false,navController)
        binding.rvProducts.adapter = productAdapter
        fetchProducts()

        categoryAdapter.setOnItemClickListener { selectedCategory ->
            if (selectedCategory == "All") {
                fetchProducts()
            } else {
                fetchProductsByCategory(selectedCategory)
            }
            categoryAdapter.updateSelectedCategory(selectedCategory)
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val searchTerm = s.toString().trim()
                fetchProductsBySearch(searchTerm)
            }
        })

        return view
    }

    private fun fetchProductsByCategory(category: String) {
        val productsApiService = ProductsClient.ProductsApiService
        val call = productsApiService.getProducts()

        call.enqueue(object : Callback<List<ModelItem>> {
            override fun onResponse(
                call: Call<List<ModelItem>>, response: Response<List<ModelItem>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val filteredProducts = it.filter { product ->
                            product.category == category
                        }
                        productList.clear()
                        productList.addAll(filteredProducts)

                        productAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("HomeFragment", "API response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<ModelItem>>, t: Throwable) {
                Log.e("HomeFragment", "API call failure")
            }
        })
    }

    private fun fetchCategories() {
        val productsApiService = ProductsClient.ProductsApiService
        val call = productsApiService.getCategories()

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(
                call: Call<List<String>>, response: Response<List<String>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        categoryList.add("All")
                        categoryList.addAll(it)
                        categoryAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("HomeFragment", "API response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e("HomeFragment", "API call  failure")
            }
        })
    }

    private fun fetchProducts() {
        val productsApiService = ProductsClient.ProductsApiService
        val call = productsApiService.getProducts()

        call.enqueue(object : Callback<List<ModelItem>> {
            override fun onResponse(
                call: Call<List<ModelItem>>, response: Response<List<ModelItem>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        productList.clear()
                        productList.addAll(it)
                        productAdapter.notifyDataSetChanged()
                        allProducts = productList.toList()

                    }
                } else {
                    Log.e("HomeFragment", "API response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<ModelItem>>, t: Throwable) {
                Log.e("HomeFragment", "API call  failure")
            }
        })
    }

    private fun fetchProductsBySearch(searchTerm: String) {
        val productsApiService = ProductsClient.ProductsApiService
        val call = productsApiService.getProducts()

        call.enqueue(object : Callback<List<ModelItem>> {
            override fun onResponse(
                call: Call<List<ModelItem>>, response: Response<List<ModelItem>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val filteredProducts = it.filter { product ->
                            product.title.contains(searchTerm, ignoreCase = true)
                        }
                        productList.clear()
                        productList.addAll(filteredProducts)
                        productAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("HomeFragment", "API response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<ModelItem>>, t: Throwable) {
                Log.e("HomeFragment", "API call failure")
            }
        })
    }

}


