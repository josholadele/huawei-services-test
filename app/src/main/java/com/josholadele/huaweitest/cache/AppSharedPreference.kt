package com.josholadele.huaweitest.cache

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectUser
import java.lang.Exception

class AppSharedPreference : SharedPref() {

    companion object {

    }

    /* fun saveLoggedInUser(user: AGConnectUser) {
         try {
             var userString = Gson().toJson(user)
             this.putString("loggedInUser", userString)
         } catch (ex: Exception) {
             Log.e("Save error", ex.message.toString())
         }
     }*/

    fun saveLoggedInUser(user: String) {
        try {
            this.putString("loggedInUser", user)
        } catch (ex: Exception) {
            Log.e("Save error", ex.message.toString())
        }
    }

    fun logoutUser() {
        try {
            this.removeString("loggedInUser")
        } catch (ex: Exception) {
            Log.e("Deletion error", ex.message.toString())
        }
    }

    fun getUser(): String {
        return try {
            this.getString("loggedInUser", "")
        } catch (ex: Exception) {
            Log.e("Deletion error", ex.message.toString())
            ""
        }
    }

    fun saveAppName(appName: String) {
        try {
            this.putString("appName", appName)
        } catch (ex: Exception) {
            Log.e("Save error", ex.message.toString())
        }
    }

    fun getAppName(defaultValue: String): String {
        return getString("appName", defaultValue)
    }

    fun trySaving(user: String) {
        try {
//            var userString = Gson().toJson(user)
            this.putString("loggedInUser", user)
        } catch (ex: Exception) {
            Log.e("Save error", ex.message.toString())
        }
    }
}