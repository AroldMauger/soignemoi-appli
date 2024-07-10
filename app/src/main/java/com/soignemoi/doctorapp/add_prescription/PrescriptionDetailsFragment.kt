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
import com.soignemoi.doctorapp.request.EndDateRequest
import com.soignemoi.doctorapp.response.GetMedicineResponse
import com.soignemoi.doctorapp.response.PrescriptionsResponse
import com.soignemoi.doctorapp.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrescriptionDetailsFragment : Fragment(), PrescriptionAdapter.EditEndDateClickListener {

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
        prescriptionAdapter = PrescriptionAdapter(emptyList(), this)
        listPrescriptions.adapter = prescriptionAdapter
        listPrescriptions.layoutManager = LinearLayoutManager(context)

        fetchPrescriptions()
    }

    private fun fetchPrescriptions() {
        service.getPrescriptions(stayId).enqueue(object : Callback<List<PrescriptionsResponse>> {
            override fun onResponse(
                call: Call<List<PrescriptionsResponse>>,
                response: Response<List<PrescriptionsResponse>>
            ) {
                if (response.isSuccessful) {
                    val prescriptions = response.body() ?: emptyList()
                    prescriptionAdapter.updatePrescriptions(prescriptions)
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la récupération des prescriptions", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PrescriptionsResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur de réseau", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onEditEndDateClicked(medicine: GetMedicineResponse, newEndDate: String) {
        // Mettre à jour la date de fin sur le serveur
        service.updateEndDate(medicine.id, EndDateRequest(newEndDate)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Recharger les données après la mise à jour réussie
                    fetchPrescriptions()

                    // Afficher un message à l'utilisateur
                    Toast.makeText(requireContext(), "Date de fin mise à jour avec succès", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la mise à jour de la date de fin", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur de réseau", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
