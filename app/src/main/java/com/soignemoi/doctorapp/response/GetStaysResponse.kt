package com.soignemoi.doctorapp.response

import com.soignemoi.doctorapp.dataclass.Doctor
import com.soignemoi.doctorapp.dataclass.Opinion
import com.soignemoi.doctorapp.dataclass.Prescription
import com.soignemoi.doctorapp.dataclass.Reason
import com.soignemoi.doctorapp.dataclass.Slot
import com.soignemoi.doctorapp.dataclass.Speciality
import com.soignemoi.doctorapp.dataclass.User

data class GetStaysResponse(
    val id: Int,
    val entrydate: String,
    val leavingdate: String,
    val speciality: Speciality,
    val reason: Reason,
    val doctor: Doctor,
    val slot: Slot,
    val user: User,
    val status: String,
    val opinions: List<Opinion>,
    val prescriptions: List<Prescription>
)

