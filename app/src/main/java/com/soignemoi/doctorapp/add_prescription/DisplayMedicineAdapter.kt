package com.soignemoi.doctorapp.add_prescription

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.response.GetMedicineResponse

class DisplayMedicineAdapter(private var medicines: List<GetMedicineResponse>) :
    RecyclerView.Adapter<DisplayMedicineAdapter.MedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicines, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val medicine = medicines[position]
        holder.bind(medicine)
    }

    override fun getItemCount() = medicines.size

    fun updateMedicines(medicines: List<GetMedicineResponse>) {
        this.medicines = medicines
        notifyDataSetChanged()
    }

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMedicineName: TextView = itemView.findViewById(R.id.textMedicineName)
        private val textDosage: TextView = itemView.findViewById(R.id.textDosage)
        private val textStartDate: TextView = itemView.findViewById(R.id.textStartDate)
        private val textEndDate: TextView = itemView.findViewById(R.id.textEndDate)

        fun bind(medicine: GetMedicineResponse) {
            textMedicineName.text = medicine.name
            textDosage.text = medicine.dosage
            textStartDate.text = medicine.startdate
            textEndDate.text = medicine.enddate
        }
    }
}
