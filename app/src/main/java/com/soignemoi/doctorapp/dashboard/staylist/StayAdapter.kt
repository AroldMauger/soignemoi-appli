package com.soignemoi.doctorapp.dashboard.staylist

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.soignemoi.doctorapp.R
import com.soignemoi.doctorapp.response.GetStaysResponse
import kotlinx.android.synthetic.main.item_stay.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StayAdapter(
    private val doctorId: Int,
    private val items: List<GetStaysResponse>,
    private val listener: Listener
) : RecyclerView.Adapter<StayAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(stay: GetStaysResponse, doctorId: Int, listener: Listener) {
            val date = LocalDateTime.parse(stay.entrydate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val formattedTime = date.format(DateTimeFormatter.ofPattern("HH':'mm"))
            val userFirstName = stay.user.firstname
            val userLastName = stay.user.lastname
            itemView.stayDate.text = formattedDate
            itemView.stayTime.text = formattedTime
            itemView.stayUser.text = "$userFirstName $userLastName"
            itemView.stayReason.text = stay.reason.name
            itemView.addOpinion.setOnClickListener {
                listener.onAddOpinion(stay, doctorId)
            }
            itemView.addPrescription.setOnClickListener {
                listener.onAddPrescription(stay, doctorId)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_stay, parent, false))

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], doctorId, listener)
    }

    interface Listener {
        fun onAddOpinion(stay: GetStaysResponse, doctorId: Int)
        fun onAddPrescription(stay: GetStaysResponse, doctorId: Int)
    }
}
