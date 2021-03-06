package com.josholadele.huaweitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.huawei.agconnect.remoteconfig.AGConnectConfig
import com.josholadele.huaweitest.cache.AppSharedPreference

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val appNameTV = findViewById<TextView>(R.id.app_name)

        val appSharedPreference = AppSharedPreference()
        val currentName = appSharedPreference.getAppName(resources.getString(R.string.app_name))
        appNameTV.text = currentName


        val config = AGConnectConfig.getInstance()
        val userString = appSharedPreference.getUser()
        Handler(Looper.getMainLooper()).postDelayed({
            config.fetch().addOnSuccessListener {
                config.apply(it)
                // Use the configured values.
                val appName = config.getValueAsString("app_name")
                appSharedPreference.saveAppName(appName)
                if (!appName.equals(currentName)) {
                    appNameTV.text = appName
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Your Code
                        val intent = if (userString.isEmpty()) {
                            Intent(this, SetupActivity::class.java)
                        } else {
                            Intent(this, HomeActivity::class.java)
                        }
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }, 2000)
                } else {
                    val intent = if (userString.isEmpty()) {
                        Intent(this, SetupActivity::class.java)
                    } else {
                        Intent(this, HomeActivity::class.java)
                    }

                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }.addOnFailureListener {
                val intent = if (userString.isEmpty()) {
                    Intent(this, SetupActivity::class.java)
                } else {
                    Intent(this, HomeActivity::class.java)
                }

                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        }, 1000)
//        val intent = Intent(this,MainActivity::class.java)
    }
}