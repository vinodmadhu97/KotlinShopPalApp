package com.example.myshoppal

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideStatusBar()
        moveFromSplashActivity()
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

    private fun moveFromSplashActivity(){
        //DEPRECATED WAY
        /*Handler().postDelayed({
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
        },2000)*/

        // NEW WAY
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
        }, 2000)
    }
}