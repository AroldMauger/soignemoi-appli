package com.soignemoi.doctorapp

import android.content.Context
import org.koin.standalone.KoinComponent

class AppManager(val context: Context): KoinComponent {
    companion object {
        var token:String? = null;
    }
}