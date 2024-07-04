package com.soignemoi.doctorapp.login

import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.request.LoginRequest
import com.soignemoi.doctorapp.response.LoginResponse
import com.soignemoi.doctorapp.service
import com.soignemoi.doctorapp.callback

class LoginViewModel : ViewModel(){
    fun login(email: String, identification: String) {
        service.login(
            LoginRequest(email, identification)
        )
            .enqueue(callback <LoginResponse>(
                {
                    var toto = "" // gérer si code 200
                }, {
                    var toto = ""
                }
            ))
    }
    }
