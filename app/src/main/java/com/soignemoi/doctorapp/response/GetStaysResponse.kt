package com.soignemoi.doctorapp.response

data class GetStaysResponse(
    val id: Int,
    val entrydate:String,
    val leavingdate: String,
    val speciality: String,
    val reason: String,
    val doctor: String,
    val slot: String,
    val status: String,
    val user: String
)
