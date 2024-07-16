package com.soignemoi.doctorapp.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.AppManager
import com.soignemoi.doctorapp.MainActivity
import com.soignemoi.doctorapp.response.GetStaysResponse
import com.soignemoi.doctorapp.response.LoginResponse
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    fun loginDoctor(context: Context, lastname: String, identification: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        service.logindoctor(lastname, identification).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.status == "success") {
                        AppManager.token = loginResponse.csrf_token  // Stocker le token

                        // Sauvegarder doctorLastName dans SharedPreferences
                        val sharedPreferences = context.getSharedPreferences("DoctorPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("doctorLastName", lastname)
                        editor.apply()

                        onSuccess()
                    } else {
                        onFailure(Throwable("Authentication failed"))
                    }
                } else {
                    onFailure(Throwable("Error: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun fetchDoctorDetails(context: Context, lastname: String, callback: (String, String) -> Unit) {
        val authToken = AppManager.token
        if (authToken != null) {
            service.getStays(doctorLastName = lastname, authHeader = "Bearer $authToken")
                .enqueue(object : Callback<List<GetStaysResponse>> {
                    override fun onResponse(call: Call<List<GetStaysResponse>>, response: Response<List<GetStaysResponse>>) {
                        if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                            val stay = response.body()!![0]
                            val doctorName = stay.doctor.name
                            val specialityName = stay.speciality.name
                            callback(doctorName, specialityName)
                        } else {
                            showDialog(context, "Pas de rendez-vous pour le moment, vous pouvez vous déconnecter") {
                                redirectToMainActivity(context)
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<GetStaysResponse>>, t: Throwable) {
                        callback("", "")
                    }
                })
        } else {
            callback("", "")
        }
    }


    private fun showDialog(context: Context, message: String, onPositiveButtonClick: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Erreur")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onPositiveButtonClick()
            }
            .show()
    }

    private fun redirectToMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}
