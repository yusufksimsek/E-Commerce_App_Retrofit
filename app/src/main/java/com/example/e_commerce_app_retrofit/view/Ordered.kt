package com.example.e_commerce_app_retrofit.view

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce_app_retrofit.R
import com.example.e_commerce_app_retrofit.databinding.FragmentOrderedBinding


class Ordered : Fragment() {

    private lateinit var binding: FragmentOrderedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderedBinding.inflate(inflater,container,false)
        val view = binding.root



        return view
    }



}