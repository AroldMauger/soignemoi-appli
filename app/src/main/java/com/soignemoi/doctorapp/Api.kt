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
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import com.soignemoi.doctorapp.request.ChangeMedicinesDTO
import com.soignemoi.doctorapp.request.EndDateRequest
import com.soignemoi.doctorapp.request.LoginRequest
import com.soignemoi.doctorapp.response.GetOpinionResponse
import com.soignemoi.doctorapp.request.NewOpinionDTO
import com.soignemoi.doctorapp.response.PrescriptionsResponse
import java.util.concurrent.TimeUnit

object ApiConfiguration {
    const val TIMEOUT: Long = 60
}

// Intercepteur pour le logging des requêtes
val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

// Configuration du client HTTP
private val client by lazy {
    OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = AppManager.token
            val requestBuilder = chain.request().newBuilder()
            token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }
        .addInterceptor(interceptor)
        .connectTimeout(ApiConfiguration.TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(ApiConfiguration.TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(ApiConfiguration.TIMEOUT, TimeUnit.SECONDS)
        .build()
}

// Création de l'instance Retrofit
val service: Api by lazy {
    Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}

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
        @Query("doctorLastName") doctorLastName: String? = null,
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") authHeader: String
    ): Call<List<GetStaysResponse>>

    @POST("api/opinions/new_opinion")
    fun newOpinion(
        @Body newOpinion: NewOpinionDTO,
        @Header("Authorization") authHeader: String
    ): Call<Void>

    @GET("api/opinions")
    fun getOpinionsByStay(
        @Query("stayId") stayId: Int,
        @Header("Authorization") authHeader: String
    ): Call<List<GetOpinionResponse>>

    @PUT("api/prescriptions/{stayId}")
    fun changeMedicines(
        @Path("stayId") stayId: Int,
        @Body prescriptions: ChangeMedicinesDTO,
        @Header("Authorization") authHeader: String
    ): Call<Any>

    @GET("api/prescriptions/{stayId}")
    fun getPrescriptions(
        @Path("stayId") stayId: Int,
        @Header("Authorization") authHeader: String
    ): Call<List<PrescriptionsResponse>>

    @PATCH("api/medicines/{medicineId}/enddate")
    fun updateEndDate(
        @Path("medicineId") medicineId: Int,
        @Body endDateRequest: EndDateRequest,
        @Header("Authorization") authHeader: String
    ): Call<Void>
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
