package com.josholadele.huaweitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.huawei.agconnect.auth.*
import com.josholadele.huaweitest.cache.AppSharedPreference
import com.josholadele.huaweitest.models.ParcelableAuth
import com.josholadele.huaweitest.utilities.Utilities

class SetupOTPActivity : AppCompatActivity() {

    lateinit var appSharedPreference: AppSharedPreference
    lateinit var parcelableAuth: ParcelableAuth

    lateinit var otpET: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_otp)

        parcelableAuth = intent.getParcelableExtra("auth")!!

        appSharedPreference = AppSharedPreference()

        val verifyButton = findViewById<Button>(R.id.verify)

        otpET = findViewById(R.id.otp)


        verifyButton.setOnClickListener {
            setUpNewUser()
        }
    }

    private fun setUpNewUser() {
        var phoneUser: PhoneUser
        var emailUser: EmailUser

        if (parcelableAuth.isEmail) {
            emailUser = EmailUser.Builder()
                .setEmail(parcelableAuth.email)
                .setVerifyCode(otpET.text.toString())
                .setPassword(parcelableAuth.password)
                .build()

            Utilities.showProgressDialog(this, "Please wait")
            AGConnectAuth.getInstance().createUser(emailUser).addOnSuccessListener {
                // A newly created user account is automatically signed in to your app.

                Utilities.hideProgressDialog()
                val user = AGConnectAuth.getInstance().currentUser
                parcelableAuth.userId = user.uid
                appSharedPreference.saveLoggedInUser(parcelableAuth.toString())
                val intent = Intent(this, HomeActivity::class.java)

                startActivity(intent)

            }.addOnFailureListener {
                // onFail
                Utilities.hideProgressDialog()
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }


        } else {
            phoneUser = PhoneUser.Builder()
                .setCountryCode(parcelableAuth.countryCode)
                .setPhoneNumber(parcelableAuth.phoneNumber)
                .setVerifyCode(otpET.text.toString())
                .setPassword(parcelableAuth.password)
                .build()

            Utilities.showProgressDialog(this, "Please wait")
            AGConnectAuth.getInstance().createUser(phoneUser)
                .addOnSuccessListener {
                    // A newly created user account is automatically signed in to your app.

                    Utilities.hideProgressDialog()
                    val user = AGConnectAuth.getInstance().currentUser

                    parcelableAuth.userId = user.uid

                    appSharedPreference.saveLoggedInUser(parcelableAuth.toString())

                    val intent = Intent(this, HomeActivity::class.java)

                    startActivity(intent)

                }
                .addOnFailureListener {
                    // onFail
                    Utilities.hideProgressDialog()
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

        }

    }
}

