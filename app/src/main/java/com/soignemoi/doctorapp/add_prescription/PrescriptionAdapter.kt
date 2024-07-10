package com.soignemoi.doctorapp.add_prescription

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.response.PrescriptionsResponse

class PrescriptionAdapter(private var prescriptions: List<PrescriptionsResponse>) :
    RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>() {

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

    class PrescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listMedicines: RecyclerView = itemView.findViewById(R.id.listMedicines)
        private val medicineAdapter: DisplayMedicineAdapter

        init {
            medicineAdapter = DisplayMedicineAdapter(emptyList())
            listMedicines.adapter = medicineAdapter
            listMedicines.layoutManager = LinearLayoutManager(itemView.context)
        }

        fun bind(prescription: PrescriptionsResponse) {
            medicineAdapter.updateMedicines(prescription.medicines)
        }
    }
}
