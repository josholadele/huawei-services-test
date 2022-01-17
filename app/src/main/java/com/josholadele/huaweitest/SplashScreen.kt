package com.josholadele.huaweitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        val intent = Intent(this,MainActivity::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
            // Your Code
            val intent = Intent(this,SetupActivity::class.java)

            startActivity(intent)
        }, 2000)
    }
}