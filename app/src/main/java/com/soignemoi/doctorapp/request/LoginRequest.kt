package com.soignemoi.doctorapp.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("lastname")
    val lastname : String,
    @SerializedName("identification")
    val identification : String
)
