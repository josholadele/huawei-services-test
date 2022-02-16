package com.josholadele.huaweitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.huawei.agconnect.auth.*
import com.josholadele.huaweitest.cache.AppSharedPreference
import com.josholadele.huaweitest.models.ParcelableAuth
import com.josholadele.huaweitest.utilities.Utilities
import java.util.*

class SetupActivity : AppCompatActivity() {

    lateinit var appSharedPreference: AppSharedPreference

    var isEmail = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_)
//var aViewModel = ViewModelProvider(this).get(AViewModel::class.java)
//        aViewModel.jas.coll
        appSharedPreference = AppSharedPreference()

        val signupButton = findViewById<Button>(R.id.sign_up)
        val signinButton = findViewById<Button>(R.id.sign_in)

        val phoneLayout = findViewById<LinearLayout>(R.id.phone_layout)

        val emailET = findViewById<TextInputEditText>(R.id.email)
        val phoneET = findViewById<TextInputEditText>(R.id.phone_number)
        val passwordET = findViewById<TextInputEditText>(R.id.password)

        val usePhoneTextView = findViewById<TextView>(R.id.use_phone)
        val useEmailTextView = findViewById<TextView>(R.id.use_email)

        usePhoneTextView.setOnClickListener {
            if (isEmail) {
                phoneLayout.visibility = VISIBLE
                useEmailTextView.visibility = VISIBLE
                usePhoneTextView.visibility = GONE
                emailET.visibility = GONE
                isEmail = !isEmail
            }
        }
        useEmailTextView.setOnClickListener {
            if (!isEmail) {
                phoneLayout.visibility = GONE
                useEmailTextView.visibility = GONE
                usePhoneTextView.visibility = VISIBLE
                emailET.visibility = VISIBLE
                isEmail = !isEmail
            }
        }




        signupButton.setOnClickListener {
            val settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30)
                .locale(Locale.UK)
                .build()


            Utilities.showProgressDialog(this, "Please wait")
            val task = if (isEmail) {
                AGConnectAuth.getInstance().requestVerifyCode(emailET.text.toString(), settings)
            } else {
                AGConnectAuth.getInstance()
                    .requestVerifyCode("+44", phoneET.text.toString(), settings)
            }

            task.addOnSuccessListener {
                // onSuccess
                Utilities.hideProgressDialog()
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                var parcelableAuth =
                    ParcelableAuth()
                parcelableAuth.password = passwordET.text.toString()
                parcelableAuth.isEmail = isEmail
                if (isEmail) {
                    parcelableAuth.email = emailET.text.toString()
                } else {
                    parcelableAuth.countryCode = "+44"
                    parcelableAuth.phoneNumber = phoneET.text.toString()
                }
                val intent = Intent(this, SetupOTPActivity::class.java)
                intent.putExtra("auth", parcelableAuth)
                startActivity(intent)
//                setUpNewUser()
            }.addOnFailureListener {
                // onFail
                Utilities.hideProgressDialog()
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }

        signinButton.setOnClickListener(View.OnClickListener {
            val credential = if (isEmail) {
                EmailAuthProvider.credentialWithPassword(
                    emailET.text.toString(),
                    passwordET.text.toString()
                )
            } else {
                PhoneAuthProvider.credentialWithPassword(
                    "+44",
                    phoneET.text.toString(),
                    passwordET.text.toString()
                )
            }
            Utilities.showProgressDialog(this, "Please wait")
            AGConnectAuth.getInstance().signOut()
            AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener {
                // Obtain sign-in information.
                Utilities.hideProgressDialog()
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                val user = AGConnectAuth.getInstance().currentUser

                var parcelableAuth =
                    ParcelableAuth()
                parcelableAuth.isEmail = isEmail
                if (isEmail) {
                    parcelableAuth.email = emailET.text.toString()
                } else {
                    parcelableAuth.countryCode = "+44"
                    parcelableAuth.phoneNumber = phoneET.text.toString()
                }
                parcelableAuth.userId=user.uid

                appSharedPreference.saveLoggedInUser(parcelableAuth.toString())

                val intent = Intent(this, HomeActivity::class.java)

                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

            }.addOnFailureListener {
                // onFail
                Utilities.hideProgressDialog()
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }


        })
    }
}

