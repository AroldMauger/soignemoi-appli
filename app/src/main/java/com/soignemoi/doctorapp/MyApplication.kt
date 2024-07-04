package com.garagetrempu.android

import android.app.Application
import android.content.Context
import org.koin.android.ext.android.startKoin

class MyApplication: Application() {
    lateinit var appManager: AppManager

    companion object {
        lateinit var applicationContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        startKoin(this, allModules())
        MyApplication.applicationContext = applicationContext
    }
}