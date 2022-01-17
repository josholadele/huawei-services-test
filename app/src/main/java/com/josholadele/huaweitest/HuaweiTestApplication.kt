package com.josholadele.huaweitest

import android.app.Application
import android.content.Context

class HuaweiTestApplication : Application() {


    companion object {
        @Volatile lateinit var mInstance: HuaweiTestApplication

        fun getInstance(): Application {
            return mInstance
        }
    }


    override fun onCreate() {
        mInstance = this
        super.onCreate()
    }
}