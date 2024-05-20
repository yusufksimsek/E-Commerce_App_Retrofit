package com.example.e_commerce_app_retrofit.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.e_commerce_app_retrofit.R
import com.example.e_commerce_app_retrofit.databinding.FragmentProductDetailBinding
import com.example.e_commerce_app_retrofit.model.ModelItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso


class ProductDetail : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private val args: ProductDetailArgs by navArgs()

    companion object {
        var productList = ArrayList<ModelItem>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        val productId = args.productId
        val productName = args.productName
        val productImage = args.productImage
        val productPrice = args.productPrice
        val productDescription = args.productDescription
        val productRating = args.productRating

        binding.tvTitleDetail.text = productName
        binding.tvPriceDetail.text = "$$productPrice"
        binding.tvRating.text = "$productRating"
        binding.tvDescription.text = productDescription
        Picasso.get().load(productImage).into(binding.ivProductDetail)

        binding.btnAddToCardDetail.setOnClickListener {
            addCard(productId)
            Snackbar.make(it, "Product added your card!", Snackbar.LENGTH_SHORT)
                .show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        return view
    }

    fun addCard(id: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("MyCard", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(id.toString(), true)
        editor.putInt("quantity_$id", 1)
        editor.apply()

        val product = productList.find { it.id == id }
        product?.let {
            if (!productList.contains(it)) {
                productList.add(it)
            }
        }
    }


}
