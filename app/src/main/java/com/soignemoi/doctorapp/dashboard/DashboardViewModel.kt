package com.soignemoi.doctorapp.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.AppManager
import com.soignemoi.doctorapp.response.GetStaysResponse
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    private val _stays = mutableSetOf<GetStaysResponse>()
    val stays: List<GetStaysResponse> get() = _stays.toList()  // Expose stays en tant que liste non mutable

    var selectedStay: GetStaysResponse? = null
    var doctorId: Int = 0  // Ajoutez cette propriété pour stocker doctorId

    fun getStays(doctorLastName: String? = null, context: Context, callback: () -> Unit) {
        val authToken = AppManager.token ?: ""  // Récupérez le token depuis AppManager
        service.getStays(doctorLastName = doctorLastName, authHeader = "Bearer $authToken").enqueue(object : Callback<List<GetStaysResponse>> {
            override fun onResponse(call: Call<List<GetStaysResponse>>, response: Response<List<GetStaysResponse>>) {
                if (response.isSuccessful) {
                    val staysResponse = response.body()
                    staysResponse?.let {
                        // Nettoyez et ajoutez les nouveaux stays
                        _stays.clear()
                        _stays.addAll(it)

                        // Extraire `doctorId` à partir de `GetStaysResponse` et le stocker dans SharedPreferences
                        if (it.isNotEmpty()) {
                            doctorId = it[0].doctor?.id ?: 0
                            val sharedPreferences = context.getSharedPreferences("DoctorPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putInt("doctorId", doctorId)
                            editor.apply()
                        }
                    }
                } else {
                    Log.e("Api", "Error fetching stays: ${response.errorBody()?.string()}")
                }
                callback()  // Appel du callback pour indiquer que la récupération est terminée
            }

            override fun onFailure(call: Call<List<GetStaysResponse>>, t: Throwable) {
                Log.e("Api", "Request failed", t)
                callback()  // Appel du callback même en cas d'échec
            }
        })
    }

    // Méthode pour filtrer les séjours en fonction du nom du docteur
    fun filterStaysByDoctorName(doctorName: String) {
        _stays.removeIf { it.doctor?.name != doctorName }
    }
}

