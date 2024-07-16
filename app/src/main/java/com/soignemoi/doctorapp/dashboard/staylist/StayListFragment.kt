package com.soignemoi.doctorapp.dashboard.staylist

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.soignemoi.doctorapp.AppManager
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.dashboard.DashboardViewModel
import com.soignemoi.doctorapp.login.MainActivity
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

        val logoutButton = view.findViewById<TextView>(R.id.logout)
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        val sharedPreferences = requireContext().getSharedPreferences("DoctorPrefs", Context.MODE_PRIVATE)
        val doctorLastName = sharedPreferences.getString("doctorLastName", null)
        if (doctorLastName == null) {
            showDialog("Aucun nom de docteur trouvé dans les préférences.")
            return
        }

        viewModel.doctorId = sharedPreferences.getInt("doctorId", 0)

        // Récupérer les séjours sans délai
        viewModel.getStays(doctorLastName, requireContext()) {
            viewModel.filterStaysByDoctorName(doctorLastName)
            list.adapter = StayAdapter(viewModel.doctorId, viewModel.stays, this)
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

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Voulez-vous vraiment vous déconnecter?")
            .setPositiveButton("Oui") { dialog, id ->
                logout()
            }
            .setNegativeButton("Non") { dialog, id ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun logout() {
        // Logique de déconnexion, par exemple, nettoyer les informations de session
        AppManager.token = null
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Erreur")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
