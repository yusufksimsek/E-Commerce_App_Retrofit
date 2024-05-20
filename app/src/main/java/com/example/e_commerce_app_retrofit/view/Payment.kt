package com.example.e_commerce_app_retrofit.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce_app_retrofit.databinding.FragmentPaymentBinding


class Payment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private val navController by lazy { findNavController() }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val view = binding.root

        val totalProductPrice = arguments?.getFloat("totalProductPrice") ?: 0
        binding.tvPaymentPrice.text = totalProductPrice.toString()

        binding.btnOrderNow.setOnClickListener {
            val action = PaymentDirections.actionPaymentToOrdered()
            navController.navigate(action)
        }

        binding.txtCardInfo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length % 5 == 0 && it.length < 17) {
                        val index = it.length - 1
                        if (index >= 0 && it[index] != ' ') {
                            it.insert(index, " ")
                        }
                    }
                }
            }
        })

        return view
    }
}