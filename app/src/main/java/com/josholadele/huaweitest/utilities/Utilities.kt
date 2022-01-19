package com.josholadele.huaweitest.utilities

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog

class Utilities {
    companion object {
        private lateinit var progressDialog: ProgressDialog
        private lateinit var alertDialog: AlertDialog

        fun showProgressDialog(context: Context?, message: String) {
            try {
                progressDialog = ProgressDialog(context)
                progressDialog.setMessage("$message...")
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.setCancelable(false)
                progressDialog.show()
            } catch (ex: Exception) {
            }
        }

        fun hideProgressDialog() {
            try {
                progressDialog.dismiss()
            } catch (ignored: Exception) {
            }
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo ?: return false
            val network = info.state
            return network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING
        }

        fun showAlertDialog(context: Context?, message: String?, listener: DialogInterface.OnClickListener?) {
            alertDialog = AlertDialog.Builder(context!!).create()
            alertDialog.setTitle("")
            alertDialog.setMessage(message)
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yesText", listener)
            try {
                alertDialog.show()
            } catch (ex: java.lang.Exception) {
            }
        }

    }
}