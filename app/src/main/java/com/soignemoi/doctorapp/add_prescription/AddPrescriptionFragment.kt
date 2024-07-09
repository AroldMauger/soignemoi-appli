package com.soignemoi.doctorapp.add_prescription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.callback
import com.soignemoi.doctorapp.dataclass.Prescription
import com.soignemoi.doctorapp.request.ChangeMedicinesDTO
import com.soignemoi.doctorapp.request.NewMedicineDTO
import com.soignemoi.doctorapp.service
import kotlinx.android.synthetic.main.fragment_add_prescription.btn_add_prescription
import kotlinx.android.synthetic.main.fragment_add_prescription.listMedicines
import kotlinx.android.synthetic.main.fragment_add_prescription.submit_prescription
import kotlinx.android.synthetic.main.item_add_medicines.editDosage
import kotlinx.android.synthetic.main.item_add_medicines.editEndDate
import kotlinx.android.synthetic.main.item_add_medicines.editMedicine
import kotlinx.android.synthetic.main.item_add_medicines.editStartDate
class AddPrescriptionFragment : Fragment(), MedicineAdapter.OnItemClickListener {

    private var stayId: Int = 0
    private val medicines = mutableListOf<NewMedicineDTO>()
    private lateinit var medicineAdapter: MedicineAdapter
    private lateinit var btnAddPrescription: Button
    private lateinit var submitPrescription: Button
    private lateinit var listMedicines: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_prescription, container, false)

        // Récupérez l'argument depuis le Bundle
        stayId = arguments?.getInt("stayId") ?: 0

        // Initialisez les vues ici
        btnAddPrescription = view.findViewById(R.id.btn_add_prescription)
        submitPrescription = view.findViewById(R.id.submit_prescription)
        listMedicines = view.findViewById(R.id.listMedicines)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurez l'adaptateur et le LayoutManager pour le RecyclerView
        medicineAdapter = MedicineAdapter(medicines, this)
        listMedicines.adapter = medicineAdapter
        listMedicines.layoutManager = LinearLayoutManager(context)

        // Configurez les boutons
        btnAddPrescription.setOnClickListener {
            showAddMedicineDialog()
        }

        submitPrescription.setOnClickListener {
            if (medicines.isNotEmpty()) {
                addPrescription(stayId)  // Passez l'ID du séjour ici
            } else {
                Toast.makeText(requireContext(), "Veuillez ajouter au moins un médicament", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddMedicineDialog() {
        val dialog = AddMedicineDialogFragment { medicine ->
            medicines.add(medicine)
            medicineAdapter.notifyDataSetChanged()
        }
        dialog.show(parentFragmentManager, "AddMedicineDialogFragment")
    }

    private fun addPrescription(stayId: Int) {
        service.changeMedicines(stayId, ChangeMedicinesDTO(prescriptions = medicines)).enqueue(callback(
            success = { response ->
                if (response.isSuccessful) {
                    findNavController().navigate(R.id.from_addprescription_to_list)
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la création de la prescription", Toast.LENGTH_SHORT).show()
                }
            },
            failure = {
                Toast.makeText(requireContext(), "Erreur de réseau", Toast.LENGTH_SHORT).show()
            }
        ))
    }

    override fun onItemRemove(position: Int) {
        medicines.removeAt(position)
        medicineAdapter.notifyItemRemoved(position)
    }
}
