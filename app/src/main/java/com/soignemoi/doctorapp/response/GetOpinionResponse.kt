package com.soignemoi.doctorapp.response

data class GetOpinionResponse(
    val id: Int,
    val doctorId: Int,
    val stayId: Int,
    val date: String,
    val description: String
)
