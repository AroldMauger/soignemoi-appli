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
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder

object ApiConfiguration {
    const val TIMEOUT: Long = 60
}

val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val gson = GsonBuilder().setLenient().create()

val client = OkHttpClient.Builder()
    .connectTimeout(ApiConfiguration.TIMEOUT, TimeUnit.SECONDS)
    .addInterceptor(interceptor)
    .retryOnConnectionFailure(true)
    .build()

val service: Api = Retrofit.Builder()
    .baseUrl(BuildConfig.API_URL)
    .client(client)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
    .create(Api::class.java)

interface Api {
    @POST("api/auth/login_doctor")
    @FormUrlEncoded
    fun logindoctor(
        @Field("lastname") lastname: String,
        @Field("identification") identification: String
    ): Call<LoginResponse>

    @GET("api/stays")
    fun getStays(
    ): Call<List<GetStaysResponse>>
}

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
