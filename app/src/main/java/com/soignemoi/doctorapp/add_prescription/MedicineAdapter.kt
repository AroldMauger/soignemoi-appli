package com.soignemoi.doctorapp.add_prescription

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.request.NewMedicineDTO
import kotlinx.android.synthetic.main.item_add_medicines.view.btn_remove
import kotlinx.android.synthetic.main.item_add_medicines.view.editDosage
import kotlinx.android.synthetic.main.item_add_medicines.view.editEndDate
import kotlinx.android.synthetic.main.item_add_medicines.view.editMedicine
import kotlinx.android.synthetic.main.item_add_medicines.view.editStartDate

class MedicineAdapter(
    private val medicines: MutableList<NewMedicineDTO>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    interface OnItemClickListener {
        fun onItemRemove(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_medicines, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val medicine = medicines[position]
        holder.bind(medicine)
        holder.itemView.btn_remove.setOnClickListener {
            listener.onItemRemove(position)
        }
    }

    override fun getItemCount() = medicines.size

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(medicine: NewMedicineDTO) {
            itemView.editMedicine.setText(medicine.name)
            itemView.editDosage.setText(medicine.dosage)
            itemView.editStartDate.setText(medicine.startdate)
            itemView.editEndDate.setText(medicine.enddate)
        }
    }
}
