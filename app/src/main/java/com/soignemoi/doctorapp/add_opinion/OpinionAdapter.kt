package com.soignemoi.doctorapp.add_opinion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.response.GetOpinionResponse
import java.text.SimpleDateFormat

class OpinionAdapter(private var opinions: List<GetOpinionResponse>) :
    RecyclerView.Adapter<OpinionAdapter.OpinionViewHolder>() {

    inner class OpinionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val opinionDate: TextView = view.findViewById(R.id.opinionDate)
        val opinionContent: TextView = view.findViewById(R.id.opinionContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpinionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_opinion, parent, false)
        return OpinionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OpinionViewHolder, position: Int) {
        val opinion = opinions[position]
        holder.opinionDate.text = formatDateString(opinion.date)
        holder.opinionContent.text = opinion.description
    }

    override fun getItemCount(): Int = opinions.size

    fun updateOpinions(newOpinions: List<GetOpinionResponse>) {
        opinions = newOpinions
        notifyDataSetChanged()
    }

    fun formatDateString(dateString: String): String {
        // Définir le format d'entrée et de sortie de la date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy")

        // Analyser la date d'entrée
        val date = inputFormat.parse(dateString)
        // Retourner la date formatée
        return outputFormat.format(date)
    }
}
