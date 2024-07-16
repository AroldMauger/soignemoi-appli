package com.soignemoi.doctorapp

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.dashboard.DashboardActivity
import com.soignemoi.doctorapp.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*
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
        val greenColor = resources.getColor(R.color.green, theme)
        val orangeColor = resources.getColor(R.color.orange, theme)

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

            // Vérifiez que les valeurs ne sont pas vides
            if (lastnameValue.isBlank() || identificationValue.isBlank()) {
                showDialog("Veuillez entrer un nom et un identifiant")
                return@setOnClickListener
            }

            // Appel de loginDoctor avec des callbacks pour success et failure
            viewModel.loginDoctor(this, lastnameValue, identificationValue, {
                viewModel.fetchDoctorDetails(this, lastnameValue) { doctorName, specialityName ->
                    val intent = Intent(this, DashboardActivity::class.java).apply {
                        putExtra("doctorName", doctorName)
                        putExtra("specialityName", specialityName)
                    }
                    startActivity(intent)
                    finish()  // Optionnel : pour empêcher l'utilisateur de revenir à l'écran de connexion
                }
            }, { error ->
                showDialog("Erreur lors de la connexion : ${error.message}")
            })
        }
    }


    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Erreur")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
