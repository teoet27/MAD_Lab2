package it.polito.group06

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

data class Advertisement (val adsTitle: String, val adsDescription: String, val adsLocation: String, val adsDate: Date, val adsDuration: Float)

class AdsViewHolder (v: View): RecyclerView.ViewHolder(v) {
    val title: TextView = v.findViewById<TextView>(R.id.ads_title)
    val description: TextView = v.findViewById<TextView>(R.id.ads_description)
    val location: TextView = v.findViewById<TextView>(R.id.ads_location)
    val date: TextView = v.findViewById<TextView>(R.id.ads_date)
    val duration: TextView = v.findViewById<TextView>(R.id.ads_duration)
}

class AdsAdapter(val adsList: List<Advertisement>) : RecyclerView.Adapter<AdsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val vg = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.fragment_time_slot_details, parent, false)
        return AdsViewHolder(vg)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        holder.title.text = adsList[position].adsTitle
        holder.description.text = adsList[position].adsDescription
        holder.location.text = adsList[position].adsLocation
        holder.date.text = adsList[position].adsDate.toString()
        holder.duration.text = adsList[position].adsDuration.toString()
    }

    override fun getItemCount(): Int {
        return adsList.size
    }

}