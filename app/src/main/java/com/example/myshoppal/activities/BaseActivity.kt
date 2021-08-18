package com.example.myshoppal.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.diaolog_progress.*

open class BaseActivity : AppCompatActivity() {

    var isDoubleBackPressed = false

    private lateinit var mProgressDialog : Dialog
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

    fun showProgressDialog(text:String){
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.diaolog_progress)
        mProgressDialog.tv_progress_text.text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun doubleClickToBack(){
        if (isDoubleBackPressed){
            super.onBackPressed()
            return
        }
        this.isDoubleBackPressed = true

        Toast.makeText(this,"please click again to exit",Toast.LENGTH_SHORT).show()

        Handler().postDelayed({isDoubleBackPressed = false},2000)
    }
}