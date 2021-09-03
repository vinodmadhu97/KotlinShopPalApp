package com.example.myshoppal.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshoppal.R

class GlideLoader(val context:Context) {

    fun loadUserPicture(imageUri: Uri,imageView:ImageView){
        Glide
            .with(context)
            .load(imageUri)
            .centerCrop()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(imageView)
    }

    fun loadProductPicture(imageUri: Uri,imageView:ImageView){
        Glide
            .with(context)
            .load(imageUri)
            .centerCrop()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(imageView)
    }
}