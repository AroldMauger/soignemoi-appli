package com.soignemoi.doctorapp.response

data class PrescriptionsResponse(
    val id: Int,
    val medicines: List<GetMedicineResponse>
)
