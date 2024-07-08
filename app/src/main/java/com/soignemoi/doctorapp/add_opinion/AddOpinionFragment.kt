package com.soignemoi.doctorapp.add_opinion

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.soignemoi.doctorapp.R
import kotlinx.android.synthetic.main.fragment_add_opinion.dateAddOpinion
import kotlinx.android.synthetic.main.fragment_add_opinion.returntomain_button
import java.util.*

class AddOpinionFragment : Fragment() {

    private lateinit var dateAddOpinion: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_opinion, container, false)

        dateAddOpinion = view.findViewById(R.id.dateTextView)

        dateAddOpinion.setOnClickListener {
            showDatePickerDialog()
        }

        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                dateAddOpinion.text = selectedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        returntomain_button.setOnClickListener {
            findNavController().navigate(R.id.from_addopinion_to_list)
        }

    }
}
