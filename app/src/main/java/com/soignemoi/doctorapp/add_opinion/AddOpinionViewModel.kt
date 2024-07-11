package com.soignemoi.doctorapp.add_opinion

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.soignemoi.doctorapp.AppManager
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.response.GetOpinionResponse
import com.soignemoi.doctorapp.request.NewOpinionDTO
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOpinionViewModel : ViewModel() {
    // Fonction pour ajouter un nouvel avis
    fun newOpinion(
        newOpinion: NewOpinionDTO,
        stayId: Int,  // Ajoutez un paramètre stayId
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val authToken = AppManager.token
        if (authToken != null) {
            Log.d("AddOpinionViewModel", "CSRF Token utilisé pour ajouter un avis: $authToken")
            service.newOpinion(newOpinion, "Bearer $authToken").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Appeler le callback pour rediriger après succès
                        onSuccess()
                    } else {
                        onFailure(Throwable(response.message()))
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    onFailure(t)
                }
            })
        } else {
            onFailure(Throwable("Token manquant, veuillez vous reconnecter"))
        }
    }

    // Fonction pour récupérer les avis pour un séjour spécifique
    fun getOpinionsForStay(
        stayId: Int,
        context: Context,
        onSuccess: (List<GetOpinionResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val authToken = AppManager.token
        if (authToken != null) {
            service.getOpinionsByStay(stayId, "Bearer $authToken").enqueue(object : Callback<List<GetOpinionResponse>> {
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
        } else {
            onFailure(Throwable("Token manquant, veuillez vous reconnecter"))
        }
    }
}

