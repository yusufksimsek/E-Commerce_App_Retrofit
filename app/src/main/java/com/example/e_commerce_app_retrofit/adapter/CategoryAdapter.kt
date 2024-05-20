package com.example.e_commerce_app_retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app_retrofit.R
import com.example.e_commerce_app_retrofit.databinding.HomeCategoriesDesignBinding

class CategoryAdapter(
    var mContext: Context,
    var categoryList: List<String>,
    var selectedCategory: String
) : RecyclerView.Adapter<CategoryAdapter.CategoryDesignHolder>() {

    inner class CategoryDesignHolder(var designHolder: HomeCategoriesDesignBinding) :
        RecyclerView.ViewHolder(designHolder.root)

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryDesignHolder {
        val binding =
            HomeCategoriesDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CategoryDesignHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryDesignHolder, position: Int) {
        val categories = categoryList[position]
        val t = holder.designHolder

        t.tvCategoryName.text = categories

        t.root.setOnClickListener {
            onItemClickListener?.invoke(categories)
        }

        val color = if (categories == selectedCategory) {
            ContextCompat.getColor(mContext, R.color.selectedCategoryColor)
        } else {
            ContextCompat.getColor(mContext, android.R.color.white)
        }

        t.cardViewCategory.setBackgroundColor(color)
    }

    fun updateSelectedCategory(newSelectedCategory: String) {
        selectedCategory = newSelectedCategory
        notifyDataSetChanged()
    }

}