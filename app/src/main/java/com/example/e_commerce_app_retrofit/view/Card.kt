package com.example.e_commerce_app_retrofit.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce_app_retrofit.R
import com.example.e_commerce_app_retrofit.adapter.CardAdapter
import com.example.e_commerce_app_retrofit.adapter.FavoriteAdapter
import com.example.e_commerce_app_retrofit.databinding.FragmentCardBinding
import com.example.e_commerce_app_retrofit.model.ModelItem


class Card : Fragment() {

    private lateinit var binding: FragmentCardBinding
    private lateinit var cardAdapter: CardAdapter
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardBinding.inflate(inflater, container, false)
        val view = binding.root

        val cardProductList = loadCards()


        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvCard.layoutManager = layoutManager

        cardAdapter = CardAdapter(requireContext(), cardProductList.toMutableList(),::updateTotalPrice)
        binding.rvCard.adapter = cardAdapter

        updateTotalPrice()

        binding.btnOrder.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Order Now")
            alertDialog.setMessage("Are you sure you want to complete your order?")
            alertDialog.setPositiveButton("Yes") { dialog, _ ->
                val total = cardAdapter.calculateTotalPrice()
                val action = CardDirections.actionCard3ToPayment(total.toFloat())
                navController.navigate(action)

            }
            alertDialog.setNegativeButton("Cancel") { dialog, _ ->

                dialog.dismiss()
            }
            alertDialog.show()
        }

        return view
    }

    private fun updateTotalPrice() {
        val total = cardAdapter.calculateTotalPrice()
        binding.tvTotalPrice.text = "$${String.format("%.2f", total)}"
    }

    private fun loadCards(): List<ModelItem> {
        val sharedPreferences = requireContext().getSharedPreferences("MyCard", Context.MODE_PRIVATE)
        val cardProducts = sharedPreferences.all

        val cardList = ArrayList<ModelItem>()

        for ((key, value) in cardProducts) {
            if (value is Boolean && value) {
                val cardproductModel = Home.allProducts.find { it.id.toString() == key }
                cardproductModel?.let {
                    cardList.add(it)
                }
            }
        }

        return cardList
    }

}