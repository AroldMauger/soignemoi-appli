package com.soignemoi.doctorapp.add_prescription

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.request.NewMedicineDTO
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddMedicineDialogFragment(private val onAddMedicine: (NewMedicineDTO) -> Unit) : DialogFragment() {

    private lateinit var editMedicine: EditText
    private lateinit var editDosage: EditText
    private lateinit var editStartDate: EditText
    private lateinit var editEndDate: EditText
    private lateinit var btnAddMedicine: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisez les vues
        editMedicine = view.findViewById(R.id.editMedicine)
        editDosage = view.findViewById(R.id.editDosage)
        editStartDate = view.findViewById(R.id.editStartDate)
        editEndDate = view.findViewById(R.id.editEndDate)
        btnAddMedicine = view.findViewById(R.id.btn_add_medicine)

        // Configurez le sélecteur de date pour la date de début et de fin
        editStartDate.setOnClickListener { showDatePicker(editStartDate) }
        editEndDate.setOnClickListener { showDatePicker(editEndDate) }

        btnAddMedicine.setOnClickListener {
            addMedicine()
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // Formatage de la date en AAAA-MM-JJ
            val date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            editText.setText(date)
        }, year, month, day).show()
    }


    private fun addMedicine() {
        val medicineName = editMedicine.text.toString().trim()
        val dosage = editDosage.text.toString().trim()
        val startDate = editStartDate.text.toString().trim()
        val endDate = editEndDate.text.toString().trim()

        // Vérifiez que tous les champs sont remplis
        if (medicineName.isEmpty()) {
            editMedicine.error = "Veuillez entrer le nom du médicament"
            return
        }
        if (dosage.isEmpty()) {
            editDosage.error = "Veuillez entrer le dosage"
            return
        }
        if (startDate.isEmpty()) {
            editStartDate.error = "Veuillez entrer la date de début"
            return
        }
        if (endDate.isEmpty()) {
            editEndDate.error = "Veuillez entrer la date de fin"
            return
        }

        // Vérifiez que la date de fin est après la date de début
        if (!isValidDateRange(startDate, endDate)) {
            editEndDate.error = "La date de fin doit être après la date de début"
            return
        }

        val medicine = NewMedicineDTO(medicineName, dosage, startDate, endDate)
        onAddMedicine(medicine)
        dismiss()
    }

    private fun isValidDateRange(startDate: String, endDate: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val start = sdf.parse(startDate)
            val end = sdf.parse(endDate)
            start != null && end != null && !start.after(end)
        } catch (e: ParseException) {
            false
        }
    }

}
