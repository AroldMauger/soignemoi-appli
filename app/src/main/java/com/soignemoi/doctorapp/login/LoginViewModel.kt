package com.soignemoi.doctorapp.login

import androidx.lifecycle.ViewModel
import com.soignemoi.doctorapp.AppManager
import com.soignemoi.doctorapp.request.LoginRequest
import com.soignemoi.doctorapp.response.LoginResponse
import com.soignemoi.doctorapp.service
import com.soignemoi.doctorapp.callback

class LoginViewModel : ViewModel(){
    fun login(lastname: String, identification: String, callback: (()->Unit)) {
        service.login(
            LoginRequest(lastname, identification)
        )
            .enqueue(callback <LoginResponse>(
                {
                    if (it.code() === 200) {
                        AppManager.token = it.body()!!.token
                        callback()
                    }
                }, {
                    var toto = ""
                }
            ))
    }
    }
