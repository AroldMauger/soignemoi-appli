package com.soignemoi.doctorapp.login

import android.content.Context
import androidx.lifecycle.ViewModel

class LogoutViewModel : ViewModel() {
    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("DoctorPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("doctorLastName")
            apply()
        }
    }
}
