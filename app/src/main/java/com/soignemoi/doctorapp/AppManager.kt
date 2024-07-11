package com.soignemoi.doctorapp

import android.content.Context
import android.content.SharedPreferences

object AppManager {
    private const val PREF_NAME = "doctorapp_prefs"
    private const val TOKEN_KEY = "auth_token"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = preferences.getString(TOKEN_KEY, null)
        set(value) {
            preferences.edit().putString(TOKEN_KEY, value).apply()
        }

    fun clearToken() {
        preferences.edit().remove(TOKEN_KEY).apply()
    }
}
