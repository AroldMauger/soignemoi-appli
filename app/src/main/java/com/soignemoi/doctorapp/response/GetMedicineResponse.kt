package com.soignemoi.doctorapp.response

data class GetMedicineResponse(
    val id: Int,
    val name: String,
    val dosage: String,
    val startdate: String,
    val enddate: String
)

