package com.soignemoi.doctorapp.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.response.GetStaysResponse
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    val stays = mutableListOf<GetStaysResponse>()
    var selectedStay: GetStaysResponse? = null

    fun getStays(doctorLastName: String? = null, callback: () -> Unit) {
        stays.clear()
        service.getStays(doctorLastName = doctorLastName).enqueue(object : Callback<List<GetStaysResponse>> {
            override fun onResponse(call: Call<List<GetStaysResponse>>, response: Response<List<GetStaysResponse>>) {
                if (response.isSuccessful) {
                    val staysResponse = response.body()
                    staysResponse?.let {
                        stays.addAll(it)
                        Log.d("Api", "Fetched stays: $stays")
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
        val filteredStays = stays.filter { it.doctor?.name == doctorName }
        stays.clear()
        stays.addAll(filteredStays)
    }
}
