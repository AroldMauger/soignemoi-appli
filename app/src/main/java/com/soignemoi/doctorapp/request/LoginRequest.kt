package com.soignemoi.doctorapp.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email : String,
    @SerializedName("identification")
    val identification : String
)
