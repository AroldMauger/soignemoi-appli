package com.soignemoi.doctorapp.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soignemoi.doctorapp.R
import kotlinx.android.synthetic.main.fragment_dashboard.titledoctor

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Récupérer les données depuis l'intent
        val doctorName = intent.getStringExtra("doctorName") ?: ""
        val specialityName = intent.getStringExtra("specialityName") ?: ""

        // Définir le texte du TextView avec les informations du médecin et de la spécialité
        titledoctor.text = "Bonjour $specialityName $doctorName !"

    }
}
