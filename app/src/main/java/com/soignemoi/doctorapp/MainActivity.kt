package com.soignemoi.doctorapp.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import com.soignemoi.doctorapp.BuildConfig
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_main.identification
import kotlinx.android.synthetic.main.activity_main.lastname
import kotlinx.android.synthetic.main.activity_main.loginButton
import kotlinx.android.synthetic.main.activity_main.titleofapp
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val viewModel by inject<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide the ActionBar
        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        // Set up colored text
        val text = "L’application réservée aux médecins de SoigneMoi"
        val spannableString = SpannableString(text)

        val startSoigne = text.indexOf("Soigne")
        val endSoigne = startSoigne + "Soigne".length
        val startMoi = text.indexOf("Moi")
        val endMoi = startMoi + "Moi".length

        val greenColor = resources.getColor(R.color.green)
        val orangeColor = resources.getColor(R.color.orange)

        spannableString.setSpan(
            ForegroundColorSpan(greenColor),
            startSoigne,
            endSoigne,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(orangeColor),
            startMoi,
            endMoi,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        titleofapp.text = spannableString

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
