package com.josholadele.huaweitest.cache

import android.content.SharedPreferences

class AppSharedPreference : SharedPref() {

    companion object {

    }

    fun saveLoggedInUser(){
        this.putString("loggedInUser","")
    }
}