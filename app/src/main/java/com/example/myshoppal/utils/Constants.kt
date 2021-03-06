package com.example.myshoppal.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast

object Constants {
    const val USERS = "users"
    const val PRODUCT = "products"

    const val MYSHOPPAL_PREFERENCES = "myshoppalPref"
    const val LOGGED_IN_USERNAME = "logged_in_username"
    const val EXTRA_USER_DETAILS = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val MALE = "male"
    const val FEMALE = "female"
    const val PHONE = "mobile"
    const val GENDER = "gender"
    const val IMAGE = "image"
    const val PROFILE_COMPLETED = "profileCompleted"
    const val USER_PROFILE_IMAGE = "user_profile_image"
    const val PRODUCT_IMAGE = "product_image"
    const val USER_ID = "userId"


    fun showImageChooser(activity: Activity){
        val galleyIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleyIntent, PICK_IMAGE_REQUEST_CODE)

    }

    fun getFileExtension(activity:Activity, uri : Uri) :String{

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(activity.contentResolver.getType(uri))!!

    }

}