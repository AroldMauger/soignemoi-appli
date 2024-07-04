package com.soignemoi.doctorapp.dashboard.staylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.dashboard.DashboardViewModel
import com.soignemoi.doctorapp.extension.navigateSafe
import com.soignemoi.doctorapp.response.GetStaysResponse
import kotlinx.android.synthetic.main.fragment_dashboard.list
import org.koin.android.viewmodel.ext.android.sharedViewModel

class StayListFragment : Fragment(), StayAdapter.Listener{
    val viewModel by sharedViewModel<DashboardViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStays {
            list.adapter = StayAdapter(viewModel.stays, this)
            list.layoutManager = LinearLayoutManager(context)
        }
    }
    override fun onItemSelected(appointment: GetStaysResponse) {
        viewModel.selectedStay= appointment
        findNavController().navigateSafe(R.id.from_list_to_opinion)
    }
}