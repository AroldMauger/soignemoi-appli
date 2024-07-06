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

class MainActivity : AppCompatActivity() {
    private val viewModel by inject<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListeners()
        if (BuildConfig.DEBUG) {
            lastname.setText("JO")
            identification.setText("CHI001")
        }
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            val lastnameValue = lastname.text.toString()
            val identificationValue = identification.text.toString()

            // Log les valeurs pour déboguer
            println("Trying to log in with lastname: $lastnameValue, identification: $identificationValue")

            viewModel.logindoctor(lastnameValue, identificationValue, { success ->
                if (success) {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                }
            }, this)
        }
    }
}