package com.soignemoi.doctorapp.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.response.GetStaysResponse
import com.soignemoi.doctorapp.service

class DashboardViewModel : ViewModel() {
    val stays = mutableListOf<GetStaysResponse>()
    var selectedStay: GetStaysResponse? = null

    fun getStays(callback: () -> Unit) {
        stays.clear()
        service.getStays()
            .enqueue(com.soignemoi.doctorapp.callback<List<GetStaysResponse>>(
                {
                    if (it.code() == 200) {
                        stays.addAll(it.body()!!)
                        callback()
                    }
                }, {
                    Log.e("DashboardViewModel", "Failed to load stays: ${it.message}")
                }
            ))
    }

    // Méthode pour filtrer les séjours en fonction du nom du docteur
    fun filterStaysByDoctorName(doctorName: String) {
        val filteredStays = stays.filter { it.doctor.name == doctorName }
        stays.clear()
        stays.addAll(filteredStays)
    }
}
