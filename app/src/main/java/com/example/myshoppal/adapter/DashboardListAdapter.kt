package com.example.myshoppal.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.models.Product
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.*

class DashboardListAdapter(private var context : Context, private var dashBoardItemList : ArrayList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(view : View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = dashBoardItemList[position]

        if (holder is ItemViewHolder){
            GlideLoader(context).loadUserPicture(Uri.parse(model.image),holder.itemView.iv_dashboard_item_image)
            holder.itemView.tv_dashboard_item_title.text = model.title
            holder.itemView.tv_dashboard_item_price.text = "$ ${model.price}"


        }
    }

    override fun getItemCount(): Int {
        return dashBoardItemList.size
    }
}