package com.soignemoi.doctorapp.login

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.AppManager
import com.soignemoi.doctorapp.request.LoginRequest
import com.soignemoi.doctorapp.response.LoginResponse
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    fun logindoctor(lastname: String, identification: String, callback: (Boolean) -> Unit, context: Context) {
        service.logindoctor(lastname, identification)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        AppManager.token = response.body()!!.token
                        Log.d("LoginViewModel", "Token: ${AppManager.token}") // Add this log statement
                        callback(true)
                    } else {
                        showDialog(context, "Identifiants incorrects")
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showDialog(context, "Erreur de connexion")
                    callback(false)
                }
            })
    }

    private fun showDialog(context: Context, message: String) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Erreur")
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }
}
