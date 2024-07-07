package com.soignemoi.doctorapp.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_main.*  // Assurez-vous que vous utilisez le bon layout ici
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<LoginViewModel>()  // Utilisez `viewModel` de Koin ou le constructeur approprié pour votre ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Assurez-vous que vous utilisez le bon layout ici

        // Hide the ActionBar
        supportActionBar?.hide()

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
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            val lastnameValue = lastname.text.toString()
            val identificationValue = identification.text.toString()

            // Log les valeurs pour déboguer
            println("Trying to log in with lastname: $lastnameValue, identification: $identificationValue")

            viewModel.logindoctor(lastnameValue, identificationValue, { success ->
                if (success) {
                    viewModel.fetchDoctorDetails(lastnameValue, this) { doctorName, specialityName ->
                        val intent = Intent(this, DashboardActivity::class.java).apply {
                            putExtra("doctorName", doctorName)
                            putExtra("specialityName", specialityName)
                        }
                        startActivity(intent)
                    }
                }
            }, this)
        }
    }
}
