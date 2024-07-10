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
import com.soignemoi.doctorapp.response.PrescriptionsResponse
import com.soignemoi.doctorapp.service

class PrescriptionDetailsFragment : Fragment() {

    private var stayId: Int = 0
    private lateinit var listPrescriptions: RecyclerView
    private lateinit var prescriptionAdapter: PrescriptionAdapter
    private lateinit var returnToMainButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prescription_details, container, false)
        returnToMainButton = view.findViewById(R.id.returntomain_button)
        returnToMainButton.setOnClickListener {
            findNavController().navigate(R.id.return_to_main)
        }
        listPrescriptions = view.findViewById(R.id.listPrescriptions)
        stayId = arguments?.getInt("stayId") ?: 0

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurez l'adaptateur et le LayoutManager pour le RecyclerView
        prescriptionAdapter = PrescriptionAdapter(emptyList())
        listPrescriptions.adapter = prescriptionAdapter
        listPrescriptions.layoutManager = LinearLayoutManager(context)

        fetchPrescriptions()
    }

    private fun fetchPrescriptions() {
        service.getPrescriptions(stayId).enqueue(callback(
            success = { response ->
                if (response.isSuccessful) {
                    val prescriptions = response.body() ?: emptyList()
                    prescriptionAdapter.updatePrescriptions(prescriptions)
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la récupération des prescriptions", Toast.LENGTH_SHORT).show()
                }
            },
            failure = {
                Toast.makeText(requireContext(), "Erreur de réseau", Toast.LENGTH_SHORT).show()
            }
        ))
    }
}
