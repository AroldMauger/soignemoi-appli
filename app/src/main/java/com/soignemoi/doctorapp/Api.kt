package com.soignemoi.doctorapp

import android.util.Log
import com.soignemoi.doctorapp.response.GetStaysResponse
import com.soignemoi.doctorapp.response.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.soignemoi.doctorapp.dataclass.Prescription
import com.soignemoi.doctorapp.request.ChangeMedicinesDTO
import com.soignemoi.doctorapp.request.NewMedicineDTO
import com.soignemoi.doctorapp.response.GetOpinionResponse
import com.soignemoi.doctorapp.request.NewOpinionDTO
import com.soignemoi.doctorapp.response.GetMedicineResponse
import com.soignemoi.doctorapp.response.MedicineResponse
import com.soignemoi.doctorapp.response.PrescriptionsResponse
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

object ApiConfiguration {
    const val TIMEOUT: Long = 60
}

// Intercepteur pour le logging des requêtes
val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

// Configuration du client HTTP
val client = OkHttpClient.Builder()
    .connectTimeout(ApiConfiguration.TIMEOUT, TimeUnit.SECONDS)
    .addInterceptor(interceptor)
    .retryOnConnectionFailure(true)
    .build()

// Création de l'instance Retrofit
val service: Api = Retrofit.Builder()
    .baseUrl(BuildConfig.API_URL)  // Assurez-vous que BuildConfig.API_URL est défini correctement dans votre gradle
    .client(client)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
    .build()
    .create(Api::class.java)

// Définition de l'interface API
interface Api {
    @POST("api/auth/login_doctor")
    @FormUrlEncoded
    fun logindoctor(
        @Field("lastname") lastname: String,
        @Field("identification") identification: String
    ): Call<LoginResponse>

    @GET("api/stays")
    fun getStays(
        @Query("doctorLastName") doctorLastName: String? = null, // Paramètre de requête pour le filtrage
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") authHeader: String? = null  // En-tête pour l'authentification
    ): Call<List<GetStaysResponse>>

    @POST("api/opinions/new_opinion")
    fun newOpinion(
        @Body newOpinion: NewOpinionDTO
    ): Call<Void>

    @GET("api/opinions")
    fun getOpinionsByStay(@Query("stayId") stayId: Int): Call<List<GetOpinionResponse>>

    @POST("api/prescriptions")
    fun addPrescription(
        @Body prescription: Prescription
    ): Call<Prescription>


    @POST("api/prescriptions/{prescriptionId}/medicines")
    fun addMedicine(
        @Path("prescriptionId") prescriptionId: Int,
        @Body newMedicine: NewMedicineDTO):
            Call<MedicineResponse>

    @PUT("api/prescriptions/{stayId}")
    fun changeMedicines(
        @Path("stayId") stayId: Int,
        @Body prescriptions: ChangeMedicinesDTO
    ): Call<Any>
    @GET("api/prescriptions/{stayId}")
    fun getPrescriptions(@Path("stayId") stayId: Int):
            Call<List<PrescriptionsResponse>>

    @GET("api/prescriptions/{prescriptionId}/medicines")
    fun getMedicines(@Path("prescriptionId") prescriptionId: Int):
            Call<List<GetMedicineResponse>>

}

// Fonction pour gérer les callbacks des requêtes API
fun <T> callback(success: ((Response<T>) -> Unit)?, failure: ((Throwable) -> Unit)? = null): Callback<T> {
    return object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            success?.invoke(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("Api", "onFailure : $t")
            failure?.invoke(t)
        }
    }
}
