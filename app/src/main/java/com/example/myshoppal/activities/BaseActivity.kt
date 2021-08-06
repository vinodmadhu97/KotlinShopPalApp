package com.example.myshoppal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showSnackBar(text:String,isError:Boolean){

        val snackBar = Snackbar.make(findViewById(android.R.id.content),text,Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (isError){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarError))
        }else{
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarSuccess))
        }

        snackBar.show()
    }
}