package com.example.myshoppal.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.fragments.ProductsFragment
import com.example.myshoppal.models.Product
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class ProductListAdapter(
    private val context:Context,
    private var itemList : ArrayList<Product>,
    private var productFragment : ProductsFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = itemList[position]
        if (holder is ItemViewHolder){
            GlideLoader(context).loadProductPicture(Uri.parse(model.image),holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "$ ${model.price}"

            holder.itemView.iv_product_item_delete.setOnClickListener {
               productFragment.deleteProductItem(model.productId)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view)

}