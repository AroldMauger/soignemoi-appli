package com.soignemoi.doctorapp.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.loginButton
import kotlinx.android.synthetic.main.activity_main.identification
import kotlinx.android.synthetic.main.activity_main.user
import org.koin.android.ext.android.inject
import com.soignemoi.doctorapp.R
class MainActivity : AppCompatActivity() {
    private val viewModel by inject<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListeners()
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            viewModel.login(user.text.toString(), identification.text.toString())
        }
    }
}