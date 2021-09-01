package com.example.myshoppal.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        hideStatusBar()
        setupToolBar()
        getProfileDetails()

        btn_logout.setOnClickListener(this)
    }

    private fun setupToolBar(){
        setSupportActionBar(toolbar_setting_activity)
        val actionbar = supportActionBar

        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
            actionbar.title = "Setting"
        }
        toolbar_setting_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun hideStatusBar(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun getProfileDetails(){
        showProgressDialog("Please wait!")
        FireStoreClass().getUserDetails(this)
    }


    fun profileDetailsSuccess(user: User){
        hideProgressDialog()
        GlideLoader(this).loadUserPicture(Uri.parse(user.image),iv_profile)
        tv_name.text = user.firstName + " "+user.lastName
        tv_gender.text = user.gender
        tv_mobile_no.text = user.mobile.toString()


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_logout ->{
                FireStoreClass().signOutUser()
                val intent = Intent(this,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }


}