package com.soignemoi.doctorapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soignemoi.doctorapp.BuildConfig
import kotlinx.android.synthetic.main.activity_main.loginButton
import kotlinx.android.synthetic.main.activity_main.identification
import kotlinx.android.synthetic.main.activity_main.lastname
import org.koin.android.ext.android.inject
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_main.lastname

class MainActivity : AppCompatActivity() {
    private val viewModel by inject<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListeners()
        if (BuildConfig.DEBUG) {
            lastname.setText("YIN")
            identification.setText("CAR001")
        }
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            viewModel.login(lastname.text.toString(), identification.text.toString()) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
        }
    }
}