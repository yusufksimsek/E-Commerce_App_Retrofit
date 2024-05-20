package com.example.e_commerce_app_retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app_retrofit.R
import com.example.e_commerce_app_retrofit.databinding.HomeProductDesignBinding
import com.example.e_commerce_app_retrofit.model.ModelItem
import com.example.e_commerce_app_retrofit.view.FavoriteDirections
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class FavoriteAdapter(
    var mContext: Context,
    var productList: MutableList<ModelItem>,
    var showTrashIcon: Boolean = true,
    private val navController: NavController
) : RecyclerView.Adapter<FavoriteAdapter.FavProductDesignHolder>(){

    inner class FavProductDesignHolder(var designHolder:HomeProductDesignBinding) :
            RecyclerView.ViewHolder(designHolder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavProductDesignHolder {
        val binding = HomeProductDesignBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return FavProductDesignHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: FavProductDesignHolder, position: Int) {
        val products = productList[position]
        val t = holder.designHolder

        Picasso.get().load(products.image).into(t.imageView)

        val maxDescriptionLength = 50
        if (products.title.length > maxDescriptionLength) {
            val truncatedDescription = "${products.title.substring(0, maxDescriptionLength)}..."
            t.tvProductName.text = truncatedDescription
        } else {
            t.tvProductName.text = products.title
        }
        t.favIcon.setImageResource(R.drawable.trash)
        t.tvPrice.text = "$${products.price.toString()}"
        t.favIcon.setOnClickListener {
            Snackbar.make(it, "Remove ${products.title} product from favorites?", Snackbar.LENGTH_SHORT)
                .setAction("Yes") {
                    removeFav(products.id)
                }
                .show()
        }

        holder.itemView.setOnClickListener {
            val action = FavoriteDirections.actionFavorite2ToProductDetail(
                productId = products.id,
                productImage = products.image,
                productName = products.title,
                productPrice = products.price.toFloat(),
                productDescription = products.description,
                productRating = products.rating.rate.toFloat()
            )
            navController.navigate(action)
        }

    }

    fun removeFav(id: Int) {
        val sharedPreferences = mContext.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(id.toString())
        editor.apply()

        if(showTrashIcon) {
            removeProduct(id)
        }
    }

    fun removeProduct(id: Int) {
        productList.removeAll { it.id == id }
        notifyDataSetChanged()
    }

    fun updateData(newProductList: List<ModelItem>) {
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }

}