package com.soignemoi.doctorapp.add_prescription

import android.content.Context
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
import com.soignemoi.doctorapp.request.ChangeMedicinesDTO
import com.soignemoi.doctorapp.request.NewMedicineDTO
import com.soignemoi.doctorapp.service
import com.soignemoi.doctorapp.AppManager  // Assurez-vous d'avoir cette classe pour gérer les tokens

class AddPrescriptionFragment : Fragment(), MedicineAdapter.OnItemClickListener {

    private var stayId: Int = 0
    private val medicines = mutableListOf<NewMedicineDTO>()
    private lateinit var medicineAdapter: MedicineAdapter
    private lateinit var btnAddPrescription: Button
    private lateinit var submitPrescription: Button
    private lateinit var listMedicines: RecyclerView
    private lateinit var returnToMainButton: Button
    private lateinit var viewPrescriptionDetailsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_prescription, container, false)
        returnToMainButton = view.findViewById(R.id.returntomain_button)
        viewPrescriptionDetailsButton = view.findViewById(R.id.displayPrescriptions)
        viewPrescriptionDetailsButton.setOnClickListener {
            navigateToPrescriptionDetails()
        }
        // Récupérez l'argument depuis le Bundle
        stayId = arguments?.getInt("stayId") ?: 0

        // Initialisez les vues ici
        btnAddPrescription = view.findViewById(R.id.btn_add_prescription)
        submitPrescription = view.findViewById(R.id.submit_prescription)
        listMedicines = view.findViewById(R.id.listMedicines)
        returnToMainButton.setOnClickListener {
            findNavController().navigate(R.id.from_addprescription_to_list)
        }
        return view
    }

    private fun navigateToPrescriptionDetails() {
        val bundle = Bundle().apply {
            putInt("stayId", stayId)
        }
        findNavController().navigate(R.id.from_add_prescription_to_prescriptions_details, bundle)
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
        val authToken = AppManager.token  // Récupérez le token d'authentification
        if (authToken != null) {
            service.changeMedicines(stayId, ChangeMedicinesDTO(prescriptions = medicines), "Bearer $authToken").enqueue(callback(
                success = { response ->
                    if (response.isSuccessful) {
                        val bundle = Bundle().apply {
                            putInt("stayId", stayId)
                        }
                        findNavController().navigate(R.id.from_add_prescription_to_prescriptions_details, bundle)
                    } else {
                        Toast.makeText(requireContext(), "Erreur lors de la création de la prescription", Toast.LENGTH_SHORT).show()
                    }
                },
                failure = {
                    Toast.makeText(requireContext(), "Erreur de réseau", Toast.LENGTH_SHORT).show()
                }
            ))
        } else {
            Toast.makeText(requireContext(), "Token manquant, veuillez vous reconnecter", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemRemove(position: Int) {
        medicines.removeAt(position)
        medicineAdapter.notifyItemRemoved(position)
    }
}
