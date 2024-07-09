package com.soignemoi.doctorapp.request

data class NewMedicineDTO(
    val name: String,
    val dosage: String,
    val startdate: String,
    val enddate: String
)