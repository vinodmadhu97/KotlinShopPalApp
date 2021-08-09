package com.example.myshoppal.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        hideStatusBar()
        setupToolBar()
        moveToLoginActivity()
        registerUser()
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

    private fun setupToolBar(){
        setSupportActionBar(signup_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        signup_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
        signup_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun moveToLoginActivity(){
        tv_move_login.setOnClickListener {
            onBackPressed()
        }

    }

    private fun registerUser(){

        btn_register.setOnClickListener {
            if (isVerifySIgnUpData()){
                val email = et_signup_email.text.toString().trim { it <= ' ' }
                val password = et_signup_password.text.toString().trim { it <= ' ' }
                val fName = et_signup_fName.text.toString().trim { it <= ' ' }
                val lName = et_signup_lName.text.toString().trim { it <= ' ' }


                showProgressDialog("Please Wait")
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {

                    if (it.isSuccessful){
                        val firebaseUser = it.result!!.user!!
                        val user = User(
                            firebaseUser.uid,
                            fName,
                            lName,
                            email
                        )

                        FireStoreClass().registerUser(this,user)


                    }else{
                        hideProgressDialog()
                        Log.i("data","${it.exception!!.message}")
                    }
                }.addOnFailureListener {
                    hideStatusBar()
                    showSnackBar(it.message.toString(),true)
                }
            }else{
                hideProgressDialog()
            }
        }


    }

    private fun isVerifySIgnUpData() :Boolean{
        return when{

            TextUtils.isEmpty(et_signup_fName.text.toString().trim{ it <= ' '}) ->{
                showSnackBar("please Enter the first name",true)
                false
            }
            TextUtils.isEmpty(et_signup_lName.text.toString().trim{ it <= ' '}) ->{
                showSnackBar("please Enter the last name",true)
                false
            }
            TextUtils.isEmpty(et_signup_email.text.toString().trim{ it <= ' '}) ->{
                showSnackBar("please Enter the Email address",true)
                false
            }
            TextUtils.isEmpty(et_signup_password.text.toString().trim{ it <= ' '}) ->{
                showSnackBar("please Enter the password",true)
                false
            }
            TextUtils.isEmpty(et_signup_rePassword.text.toString().trim{ it <= ' '}) ->{
                showSnackBar("please Re-enter the password",true)
                false
            }

            et_signup_password.text.toString() != et_signup_rePassword.text.toString() ->{
                showSnackBar("password mismatch",true)
                false
            }
            !cb_term_agree.isChecked ->{
                showSnackBar("please agree with terms and conditions",true)
                false
            }

            else -> {

                true
            }
        }
    }


    fun userRegistrationSuccessFull(){
        hideProgressDialog()
        Toast.makeText(this,"Registration success",Toast.LENGTH_LONG).show()
        finish()
        val intent = Intent(this,MainActivity::class.java)
        
        startActivity(intent)
    }


}