package com.example.e_commerce_app_retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app_retrofit.R
import com.example.e_commerce_app_retrofit.databinding.HomeProductDesignBinding
import com.example.e_commerce_app_retrofit.model.ModelItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

import com.example.e_commerce_app_retrofit.view.HomeDirections


class ProductAdapter(
    var mContext: Context,
    var productList: MutableList<ModelItem>,
    var showTrashIcon: Boolean = false,
    private val navController: NavController
) : RecyclerView.Adapter<ProductAdapter.ProductDesignHolder>() {

    inner class ProductDesignHolder(var designHolder: HomeProductDesignBinding) :
        RecyclerView.ViewHolder(designHolder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDesignHolder {
        val binding = HomeProductDesignBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return ProductDesignHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductDesignHolder, position: Int) {
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
        t.tvPrice.text = "$${products.price.toString()}"


        var favoriteCheck:Boolean
        if (isFavorite(products.id)) {
            t.favIcon.setImageResource(R.drawable.fav_full)
            favoriteCheck = true
        } else {
            t.favIcon.setImageResource(R.drawable.fav_empty)
            favoriteCheck = false
        }

        if (showTrashIcon) {
            t.tvProductName.text = products.title
            t.favIcon.setImageResource(R.drawable.trash)
            t.favIcon.setOnClickListener {
                Snackbar.make(it, "Remove ${products.title} product from favorites?", Snackbar.LENGTH_SHORT)
                    .setAction("Yes") {
                        removeFav(products.id)
                        favoriteCheck = false
                    }
                    .show()
            }
        } else {
            if(favoriteCheck){
                t.favIcon.setOnClickListener {
                    Snackbar.make(it, "Remove ${products.title} product from favorites?", Snackbar.LENGTH_SHORT)
                        .setAction("Yes") {
                            removeFav(products.id)
                            t.favIcon.setImageResource(R.drawable.fav_empty)
                            notifyItemChanged(position)
                            favoriteCheck = false
                        }
                        .show()
                }
            }else{
                t.favIcon.setOnClickListener {
                    Snackbar.make(it, "Add ${products.title} product to favorites?", Snackbar.LENGTH_SHORT)
                        .setAction("Yes") {
                            addFav(products.id)
                            t.favIcon.setImageResource(R.drawable.fav_full)
                            favoriteCheck = true
                            notifyItemChanged(position)
                        }
                        .show()
                }
            }
        }

        holder.itemView.setOnClickListener {
            val action = HomeDirections.actionHome2ToProductDetail(
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

    private fun isFavorite(id: Int): Boolean {
        val sharedPreferences = mContext.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(id.toString(), false)
    }

    fun addFav(id: Int) {
        val sharedPreferences = mContext.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(id.toString(), true)
        editor.apply()

        val product = productList.find { it.id == id }
        product?.let {
            if (!productList.contains(it)) {
                productList.add(it)
                notifyDataSetChanged()
            }
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

}