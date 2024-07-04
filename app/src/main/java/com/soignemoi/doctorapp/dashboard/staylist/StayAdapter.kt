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
import kotlinx.android.synthetic.main.item_stay.view.stayDate
import kotlinx.android.synthetic.main.item_stay.view.stayReason
import kotlinx.android.synthetic.main.item_stay.view.stayTime
import kotlinx.android.synthetic.main.item_stay.view.stayUser
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StayAdapter (val items:List<GetStaysResponse>, val listener: Listener): RecyclerView.Adapter<StayAdapter.ViewHolder> () {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(stay: GetStaysResponse, listener: Listener){
            val date = LocalDateTime.parse(stay.entrydate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val formattedTime = date.format(DateTimeFormatter.ofPattern("HH':'mm"))
            itemView.stayDate.text = formattedDate
            itemView.stayTime.text = formattedTime
            itemView.stayUser.text = stay.user
            itemView.stayReason.text = stay.reason
            // ajouter AVIS
            // ajouter PRESCRIPTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_stay, parent, false))

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    interface Listener {
        fun onItemSelected (appointment: GetStaysResponse)
    }
}