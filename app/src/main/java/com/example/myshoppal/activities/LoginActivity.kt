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
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        hideStatusBar()
        btn_login.setOnClickListener(this)
        tv_move_register.setOnClickListener(this)
        tv_forgotten_password.setOnClickListener(this)


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


    override fun onClick(v: View?) {

        if (v != null){
            when(v!!.id){
                R.id.tv_forgotten_password -> {
                    startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java))
                }
                R.id.btn_login -> {
                    loginRegisteredUser()
                }
                R.id.tv_move_register ->{
                    startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
                }
            }
        }

    }

    private fun loginRegisteredUser(){
        if (isVerifyLoginDetails()){
            val email = et_login_email.text.toString()
            val password = et_login_password.text.toString()
            showProgressDialog("please Wait!")
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                hideProgressDialog()
                if (it.isSuccessful){

                        FireStoreClass().getUserDetails(this)

                }else{
                    showSnackBar("login failed }",true)
                }
            }.addOnFailureListener {
                hideProgressDialog()
                showSnackBar("${it.message}",true)
            }
        }
    }

    private fun isVerifyLoginDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_login_email.text.toString().trim { it <= ' ' }) -> {
                showSnackBar("email invalid or empty",true)
                false
            }
            TextUtils.isEmpty(et_login_password.text.toString().trim { it<= ' ' }) ->{
                showSnackBar("password invalid or empty",true)
                false
            }
            else ->{
                true
            }
        }
    }

    fun userLoggedInSuccess(user: User){
        hideProgressDialog()

        Log.i("data",user.firstName)
        Log.i("data",user.lastName)
        Log.i("data",user.email)

        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        finish()
    }
}