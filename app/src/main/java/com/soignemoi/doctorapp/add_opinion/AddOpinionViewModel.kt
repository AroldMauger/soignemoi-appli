package com.soignemoi.doctorapp.add_opinion

import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.response.GetOpinionResponse
import com.soignemoi.doctorapp.response.NewOpinionDTO
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOpinionViewModel : ViewModel() {
    // Fonction pour ajouter un nouvel avis
    fun newOpinion(newOpinion: NewOpinionDTO, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        service.newOpinion(newOpinion).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(Throwable(response.message()))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    // Fonction pour récupérer les avis pour un séjour spécifique
    fun getOpinionsForStay(
        stayId: Int,
        onSuccess: (List<GetOpinionResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        service.getOpinionsByStay(stayId).enqueue(object : Callback<List<GetOpinionResponse>> {
            override fun onResponse(call: Call<List<GetOpinionResponse>>, response: Response<List<GetOpinionResponse>>) {
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: emptyList())
                } else {
                    onFailure(Exception("Error fetching opinions: ${response.errorBody()?.string()}"))
                }
            }

            override fun onFailure(call: Call<List<GetOpinionResponse>>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}
