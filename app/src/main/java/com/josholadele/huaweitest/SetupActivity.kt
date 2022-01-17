package com.josholadele.huaweitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.PhoneUser
import com.huawei.agconnect.auth.VerifyCodeSettings
import com.josholadele.huaweitest.cache.AppSharedPreference
import java.util.*

class SetupActivity : AppCompatActivity() {

    lateinit var appSharedPreference : AppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        appSharedPreference = AppSharedPreference()

        val signupButton = findViewById<Button>(R.id.sign_up)
        val signinButton = findViewById<Button>(R.id.sign_in)

        signupButton.setOnClickListener {
            val settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30)
                .locale(Locale.CHINA)
                .build()
            val task = AGConnectAuth.getInstance().requestVerifyCode("+44", "07404373949", settings)
            task.addOnSuccessListener {
                // onSuccess
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                setUpNewUser()
            }.addOnFailureListener {
                // onFail
                Toast.makeText(this, "Error occurred", Toast.LENGTH_LONG).show()
            }
        }

        signinButton.setOnClickListener(View.OnClickListener {

        })
    }

    private fun setUpNewUser() {

        val phoneUser = PhoneUser.Builder()
            .setCountryCode("your country code")
            .setPhoneNumber("your phoneNumber")
            .setVerifyCode("verify code")
            .setPassword("password")
            .build()
        AGConnectAuth.getInstance().createUser(phoneUser).addOnSuccessListener {
            // A newly created user account is automatically signed in to your app.

            val user = AGConnectAuth.getInstance().currentUser
appSharedPreference.saveLoggedInUser()
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)

        }.addOnFailureListener {
            // onFail
        }
    }
}

