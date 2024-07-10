package com.soignemoi.doctorapp.add_prescription

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.response.GetMedicineResponse
import com.soignemoi.doctorapp.response.PrescriptionsResponse
import java.text.SimpleDateFormat
import java.util.*

class PrescriptionAdapter(
    private var prescriptions: List<PrescriptionsResponse>,
    private val editEndDateClickListener: EditEndDateClickListener
) : RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>() {

    interface EditEndDateClickListener {
        fun onEditEndDateClicked(medicine: GetMedicineResponse, newEndDate: String)
    }

    // Expose the current list of prescriptions for updating end dates
    val currentPrescriptions: List<PrescriptionsResponse>
        get() = prescriptions

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prescription, parent, false)
        return PrescriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        val prescription = prescriptions[position]
        holder.bind(prescription)
    }

    override fun getItemCount() = prescriptions.size

    fun updatePrescriptions(prescriptions: List<PrescriptionsResponse>) {
        this.prescriptions = prescriptions
        notifyDataSetChanged()
    }

    inner class PrescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listMedicines: RecyclerView = itemView.findViewById(R.id.listMedicines)
        private val medicineAdapter: DisplayMedicineAdapter

        init {
            medicineAdapter = DisplayMedicineAdapter(emptyList(), object : DisplayMedicineAdapter.EditEndDateClickListener {
                override fun onEditEndDateClicked(medicine: GetMedicineResponse) {
                    showDatePicker(medicine)
                }
            })
            listMedicines.adapter = medicineAdapter
            listMedicines.layoutManager = LinearLayoutManager(itemView.context)
        }

        fun bind(prescription: PrescriptionsResponse) {
            medicineAdapter.updateMedicines(prescription.medicines)
        }

        private fun showDatePicker(medicine: GetMedicineResponse) {
            val calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(medicine.enddate) ?: calendar.time
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                itemView.context,
                { _, newYear, newMonth, newDay ->
                    val newEndDate = String.format("%04d-%02d-%02d", newYear, newMonth + 1, newDay)
                    editEndDateClickListener.onEditEndDateClicked(medicine, newEndDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }
}
