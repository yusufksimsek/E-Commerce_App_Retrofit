package com.example.e_commerce_app_retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app_retrofit.databinding.CardProductDesignBinding
import com.example.e_commerce_app_retrofit.model.ModelItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class CardAdapter(
    var mContext: Context,
    var productList: MutableList<ModelItem>,
    private val onTotalPriceUpdated: () -> Unit
) : RecyclerView.Adapter<CardAdapter.CardProductDesignHolder>() {

    inner class CardProductDesignHolder(var designHolder: CardProductDesignBinding) :
        RecyclerView.ViewHolder(designHolder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardProductDesignHolder {
        val binding = CardProductDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardProductDesignHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: CardProductDesignHolder, position: Int) {
        val products = productList[position]
        val t = holder.designHolder


        var quantity = getQuantityFromSharedPreferences(products.id)
        t.tvQuantity.text = quantity.toString()
        var cardTotal = (quantity) * (products.price)
        t.tvCardprice.text = "$${cardTotal}"

        t.addIcon.setOnClickListener {
            ++quantity
            updateQuantityAndPrice(t, quantity, products)
            saveQuantityToSharedPreferences(products.id, quantity)
            onTotalPriceUpdated.invoke()

        }

        t.deleteIcon.setOnClickListener {
            if (quantity > 1) {
                --quantity
                updateQuantityAndPrice(t, quantity, products)
                saveQuantityToSharedPreferences(products.id, quantity)
                onTotalPriceUpdated.invoke()
            }
        }


        Picasso.get().load(products.image).into(t.ivCardimage)
        t.removeIcon.setOnClickListener {
            Snackbar.make(
                it,
                "Remove ${products.title} product from favorites?",
                Snackbar.LENGTH_SHORT
            )
                .setAction("Yes") {
                    removeProductFromCard(products.id)
                }
                .show()
        }

    }

    fun removeProductFromCard(id: Int) {
        val sharedPreferences = mContext.getSharedPreferences("MyCard", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(id.toString())
        editor.remove("quantity_$id")
        editor.apply()

        removeProduct(id)

        onTotalPriceUpdated.invoke()
    }


    fun removeProduct(id: Int) {
        productList.removeAll { it.id == id }
        notifyDataSetChanged()
    }

    fun calculateTotalPrice(): Double {
        var total = 0.0

        for (product in productList) {
            val quantity = getQuantityFromSharedPreferences(product.id)
            total += quantity * product.price
        }

        return total
    }

    private fun updateQuantityAndPrice(
        t: CardProductDesignBinding,
        quantity: Int,
        products: ModelItem
    ) {
        t.tvQuantity.text = quantity.toString()
        products.quantity = quantity
        var cardTotal = (quantity) * (products.price)
        t.tvCardprice.text = "$${cardTotal}"
        saveQuantityToSharedPreferences(products.id, quantity)
    }

    private fun saveQuantityToSharedPreferences(id: Int, quantity: Int) {
        val sharedPreferences = mContext.getSharedPreferences("MyCard", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("quantity_$id", quantity)
        editor.apply()
    }

    private fun getQuantityFromSharedPreferences(id: Int): Int {
        val sharedPreferences = mContext.getSharedPreferences("MyCard", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("quantity_$id", 1)
    }

}