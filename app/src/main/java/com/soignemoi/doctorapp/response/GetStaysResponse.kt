package com.soignemoi.doctorapp.response

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

data class Speciality(val id: Int, val name: String, val code: String)
data class Reason(val id: Int, val name: String)
data class Doctor(val id: Int, val name: String)
data class Slot(val id: Int, val time: Time)
data class User(val id: Int, val firstname: String, val lastname: String)
data class Time(val date: String, val timezone_type: Int, val timezone: String)
data class Opinion(val id: Int, val comment: String)
data class Prescription(val id: Int, val medicine: String)
