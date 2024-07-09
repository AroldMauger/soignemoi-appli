package com.soignemoi.doctorapp.response

data class NewOpinionDTO(
    val doctorId: Int,
    val stayId: Int,
    val date: String,
    val description: String
)