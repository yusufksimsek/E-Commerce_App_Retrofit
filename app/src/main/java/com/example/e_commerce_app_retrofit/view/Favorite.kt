package com.example.e_commerce_app_retrofit.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app_retrofit.adapter.FavoriteAdapter
import com.example.e_commerce_app_retrofit.databinding.FragmentFavoriteBinding
import com.example.e_commerce_app_retrofit.model.ModelItem


class Favorite : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root

        val favoriteProductList = loadFavorites()

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFavorites.layoutManager = layoutManager

        favoriteAdapter = FavoriteAdapter(requireContext(), favoriteProductList.toMutableList(), showTrashIcon = true, navController)
        binding.rvFavorites.adapter = favoriteAdapter

        favoriteAdapter.updateData(favoriteProductList)

        return view
    }

    private fun loadFavorites(): List<ModelItem> {
        val sharedPreferences =
            requireContext().getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val favoriProducts = sharedPreferences.all.keys.toList()
        Log.e("Favorite Products", favoriProducts.toString())

        val favoriteProductList = ArrayList<ModelItem>()

        for (title in favoriProducts) {
            val isFav = sharedPreferences.getBoolean(title, false)

            val productModel = Home.allProducts.find { it.id.toString() == title.toString() }

            if (productModel != null && isFav) {
                productModel.title
                favoriteProductList.add(productModel)
            }
        }

        return favoriteProductList
    }


}