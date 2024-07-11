package com.soignemoi.doctorapp.add_opinion

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.request.NewOpinionDTO
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class AddOpinionFragment : Fragment() {
    private val viewModel by sharedViewModel<AddOpinionViewModel>()

    private lateinit var dateAddOpinion: TextView
    private lateinit var descriptionTextView: EditText
    private lateinit var submitOpinion: Button
    private lateinit var returnToMainButton: Button
    private lateinit var viewOpinionDetailsButton: Button

    private var doctorId: Int = 0
    private var stayId: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_opinion, container, false)

        dateAddOpinion = view.findViewById(R.id.dateAddOpinion)
        descriptionTextView = view.findViewById(R.id.opinion)
        submitOpinion = view.findViewById(R.id.submit_opinion)
        returnToMainButton = view.findViewById(R.id.returntomain_button)
        viewOpinionDetailsButton = view.findViewById(R.id.displayOpinions)

        dateAddOpinion.setOnClickListener {
            showDatePickerDialog()
        }
        submitOpinion.setOnClickListener {
            onAddOpinion()
        }
        returnToMainButton.setOnClickListener {
            findNavController().navigate(R.id.action_addOpinion_to_stayList)
        }
        viewOpinionDetailsButton.setOnClickListener {
            navigateToOpinionDetails()
        }
        arguments?.let {
            doctorId = it.getInt("doctorId")
            stayId = it.getInt("stayId")
        }

        return view
    }

    private fun navigateToOpinionDetails() {
        val bundle = Bundle().apply {
            putInt("stayId", stayId)
        }
        findNavController().navigate(R.id.from_add_opinion_to_opinions_details, bundle)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                dateAddOpinion.text = selectedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onAddOpinion() {
        try {
            // Récupérer et formater la date
            val realDate = dateAddOpinion.text.toString().split("/").reversed().joinToString("-")

            // Définir les heures, minutes et secondes par défaut
            val defaultHours = "00"
            val defaultMinutes = "00"
            val defaultSeconds = "00"

            // Combiner la date et l'heure par défaut au format 'Y-m-d H:i:s'
            val formattedDateTime = "$realDate $defaultHours:$defaultMinutes:$defaultSeconds"

            // Récupérer la description
            val description = descriptionTextView.text.toString()

            // Créer l'objet NewOpinionDTO
            val newOpinion = NewOpinionDTO(
                doctorId = doctorId,
                stayId = stayId,
                date = formattedDateTime,
                description = description
            )

            Log.d("AddOpinion", "New opinion: $newOpinion")

            // Appeler l'API pour ajouter la nouvelle opinion
            viewModel.newOpinion(
                newOpinion,
                stayId,  // Passez le stayId ici
                requireContext(),
                onSuccess = {
                    Log.d("AddOpinion", "Opinion added successfully")
                    Toast.makeText(context, "Avis ajouté", Toast.LENGTH_SHORT).show()

                    // Redirection vers le fragment des détails des avis
                    val bundle = Bundle().apply {
                        putInt("stayId", stayId)
                    }
                    findNavController().navigate(R.id.from_add_opinion_to_opinions_details, bundle)
                },
                onFailure = {
                    Log.e("AddOpinion", "Failed to add opinion: ${it.message}")
                    Toast.makeText(context, "Impossible d'ajouter l'avis", Toast.LENGTH_SHORT).show()
                }
            )
        } catch (e: Exception) {
            // Gérer les exceptions
            Toast.makeText(context, "Format de date invalide", Toast.LENGTH_SHORT).show()
            Log.e("AddOpinion", "Date parsing error", e)
        }
    }
}
