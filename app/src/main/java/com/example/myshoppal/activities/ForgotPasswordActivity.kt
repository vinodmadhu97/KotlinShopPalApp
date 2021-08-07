package com.example.myshoppal.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.myshoppal.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        hideStatusBar()

        tv_forgot_to_login.setOnClickListener {
            onBackPressed()
        }
        btn_reset_submit.setOnClickListener {
            resetPassword()
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

    private fun resetPassword(){

        val email = et_password_reset_email.text.toString().trim { it <= ' ' }

        if (email.isEmpty()){
            showSnackBar("email is invalid or empty",true)
        }else{

                showProgressDialog("Sending")
                FirebaseAuth.getInstance().sendPasswordResetEmail(et_password_reset_email.text.toString().trim { it <= ' ' }).addOnCompleteListener {
                    hideProgressDialog()
                    if (it.isSuccessful){
                        Toast.makeText(this,"Please check your email",Toast.LENGTH_LONG).show()
                        finish()
                        onBackPressed()
                    }
                }.addOnFailureListener {
                    hideProgressDialog()
                    Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()


                }
            showSnackBar("ok",false)
        }

    }
}