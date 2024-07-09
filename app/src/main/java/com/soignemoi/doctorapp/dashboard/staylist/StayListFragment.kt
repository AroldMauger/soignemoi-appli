package com.soignemoi.doctorapp.dashboard.staylist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.dashboard.DashboardViewModel
import com.soignemoi.doctorapp.response.GetStaysResponse
import kotlinx.android.synthetic.main.fragment_dashboard.list
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class StayListFragment : Fragment(), StayAdapter.Listener {

    private val viewModel: DashboardViewModel by viewModel { parametersOf(requireContext()) }  // Passez le Context ici

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("DoctorPrefs", Context.MODE_PRIVATE)
        val doctorLastName = sharedPreferences.getString("doctorLastName", null) ?: return
        viewModel.doctorId = sharedPreferences.getInt("doctorId", 0)  // Récupérez doctorId depuis SharedPreferences

        viewModel.getStays(doctorLastName, requireContext()) {
            // Appliquez le filtrage dans ViewModel en cas de besoin
            viewModel.filterStaysByDoctorName(doctorLastName)
            // Assignez les données filtrées à l’adaptateur
            list.adapter = StayAdapter(viewModel.doctorId, viewModel.stays, this)  // Passez doctorId ici
            list.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onAddOpinion(stay: GetStaysResponse, doctorId: Int) {
        viewModel.selectedStay = stay
        findNavController().navigate(R.id.from_list_to_add_opinion, Bundle().apply {
            putInt("doctorId", doctorId)  // Assurez-vous que vous passez doctorId
            putInt("stayId", stay.id)
        })
    }

    override fun onAddPrescription(stay: GetStaysResponse, doctorId: Int) {
        viewModel.selectedStay = stay
        findNavController().navigate(R.id.action_stayList_to_add_prescription, Bundle().apply {
            putInt("stayId", stay.id)
        })
    }
}
