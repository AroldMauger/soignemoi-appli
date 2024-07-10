package com.soignemoi.doctorapp.opinion_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.add_opinion.AddOpinionViewModel
import com.soignemoi.doctorapp.add_opinion.OpinionAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel

class OpinionDetailsFragment : Fragment() {
    private val viewModel by sharedViewModel<AddOpinionViewModel>()
    private lateinit var opinionsRecyclerView: RecyclerView
    private lateinit var opinionsAdapter: OpinionAdapter
    private lateinit var returnToMainButton: Button
    private var stayId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_opinion_details, container, false)

        returnToMainButton = view.findViewById(R.id.returntomain_button)
        returnToMainButton.setOnClickListener {
            findNavController().navigate(R.id.return_to_main)
        }
        opinionsRecyclerView = view.findViewById(R.id.opinionsRecyclerView)
        opinionsRecyclerView.layoutManager = LinearLayoutManager(context)
        opinionsAdapter = OpinionAdapter(emptyList())
        opinionsRecyclerView.adapter = opinionsAdapter

        arguments?.let {
            stayId = it.getInt("stayId")
        }

        fetchOpinions()
        return view
    }

    private fun fetchOpinions() {
        viewModel.getOpinionsForStay(stayId, requireContext(), onSuccess = { opinions ->
            opinionsAdapter.updateOpinions(opinions)
        }, onFailure = {
            Log.e("OpinionDetails", "Failed to fetch opinions: ${it.message}")
        })
    }
}
