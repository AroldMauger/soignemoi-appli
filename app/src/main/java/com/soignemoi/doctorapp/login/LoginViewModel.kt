package com.soignemoi.doctorapp.login

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.AppManager
import com.soignemoi.doctorapp.request.LoginRequest
import com.soignemoi.doctorapp.response.GetStaysResponse
import com.soignemoi.doctorapp.response.LoginResponse
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    fun logindoctor(
        lastname: String,
        identification: String,
        callback: (Boolean) -> Unit,
        context: Context
    ) {
        service.logindoctor(lastname, identification)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        AppManager.token = response.body()!!.token
                        Log.d("LoginViewModel", "Token: ${AppManager.token}")

                        // Stockage du `doctorLastName` dans SharedPreferences
                        val sharedPreferences = context.getSharedPreferences("DoctorPrefs", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("doctorLastName", lastname)
                            putString("authToken", AppManager.token)
                            apply()
                        }

                        // Appel de la méthode pour récupérer les détails du médecin
                        fetchDoctorDetails(lastname, context) { doctorName, specialityName ->
                            callback(true)
                        }
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

    fun fetchDoctorDetails(lastname: String, context: Context, callback: (String, String) -> Unit) {
        val authToken = AppManager.getToken(context)
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
            showDialog(context, "Token manquant, veuillez vous reconnecter")
            callback("Inconnu", "Inconnue")
        }
    }

    private fun showDialog(context: Context, message: String) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Erreur")
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }
}
