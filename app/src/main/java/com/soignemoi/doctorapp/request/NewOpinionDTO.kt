package com.soignemoi.doctorapp.request

data class NewOpinionDTO(
    val doctorId: Int,
    val stayId: Int,
    val date: String,
    val description: String
)