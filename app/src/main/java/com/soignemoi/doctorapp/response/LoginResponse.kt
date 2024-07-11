package com.soignemoi.doctorapp.response

data class LoginResponse(
    val status: String,
    val token: String,
    val csrf_token: String
)
