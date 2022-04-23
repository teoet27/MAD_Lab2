package it.polito.group06

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Advertisement data class.
 * This data class contains the following information:
 * @param adsTitle The title of the advertisement
 * @param adsDescription A complete description of the work offered
 * @param adsLocation The location where the worker is available
 * @param adsDate Date and time of the timeslot offered
 * @param adsDuration The duration of the offered timeslot
 * @param adsAccount Name and Surname of the worker offering the timeslot
 */
data class Advertisement(
    val adsTitle: String, val adsDescription: String,
    val adsLocation: String, val adsDate: Date,
    val adsDuration: Float, val adsAccount: String,
    val isPrivate: Boolean
)

/**
 * AdsViewHolder extends the ViewHolder of the RecyclerView
 * and provides the references to each component of the advertisement
 * card.
 */
class AdsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val title: TextView = v.findViewById<TextView>(R.id.ads_title)
    val description: TextView = v.findViewById<TextView>(R.id.ads_description)
    val location: TextView = v.findViewById<TextView>(R.id.ads_location)
    val date: TextView = v.findViewById<TextView>(R.id.ads_date)
    val duration: TextView = v.findViewById<TextView>(R.id.ads_duration)
    val account: TextView = v.findViewById<TextView>(R.id.ads_account)
}

/**
 * AdsAdapter extends the Adapter of the RecyclerView and implements the required methods.
 */
class AdsAdapter(val adsList: List<Advertisement>) : RecyclerView.Adapter<AdsViewHolder>() {

    /**
     * On creating the view holder, based on whether the ads is private or not the
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(viewType, parent, false)
        return when(viewType) {
            R.layout.fragment_time_slot_details -> AdsViewHolder(v)
            else -> AdsViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        holder.title.text = adsList[position].adsTitle
        holder.description.text = adsList[position].adsDescription
        holder.location.text = adsList[position].adsLocation
        holder.date.text = adsList[position].adsDate.toString()
        holder.duration.text = adsList[position].adsDuration.toString()
        holder.account.text = adsList[position].adsAccount
    }

    override fun getItemCount(): Int {
        return adsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(adsList[position].isPrivate) {
            true -> R.layout.fragment_time_slot_details
            false -> R.layout.fragment_time_slot_details
        }
    }

}