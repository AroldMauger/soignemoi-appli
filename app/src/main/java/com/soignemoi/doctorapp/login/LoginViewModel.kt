package com.soignemoi.doctorapp.login

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.AppManager
import com.soignemoi.doctorapp.response.GetStaysResponse
import com.soignemoi.doctorapp.response.LoginResponse
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    fun loginDoctor(lastname: String, identification: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        service.logindoctor(lastname, identification).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.status == "success") {
                        AppManager.token = loginResponse.token  // Stocker le token
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

    fun fetchDoctorDetails(lastname: String, callback: (String, String) -> Unit) {
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
                            callback("Inconnu", "Inconnue")
                        }
                    }

                    override fun onFailure(call: Call<List<GetStaysResponse>>, t: Throwable) {
                        callback("Inconnu", "Inconnue")
                    }
                })
        } else {
            callback("Inconnu", "Inconnue")
        }
    }

    private fun showDialog(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle("Erreur")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
