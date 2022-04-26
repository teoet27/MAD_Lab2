package it.polito.group06

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * [Advertisement] data class.
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
 * [AdsViewHolder] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the advertisement
 * card.
 */
class AdsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val title: TextView = v.findViewById(R.id.ads_title)
    private val description: TextView = v.findViewById(R.id.ads_description)
    private val location: TextView = v.findViewById(R.id.ads_location)
    private val date: TextView = v.findViewById(R.id.ads_date)
    private val duration: TextView = v.findViewById(R.id.ads_duration)
    private val account: TextView = v.findViewById(R.id.ads_account)
    private val moreButton: ImageView = v.findViewById(R.id.moreButtonID)

    /**
     * bind:
     * A method to bind the i-th entry of the adsList to the i-th holder properties.
     * @param adv an object of class Advertisement
     * @param moreButtonAction the callback to be set at the moreButton listener
     */
    fun bind(adv: Advertisement, moreButtonAction: () -> Unit) {
        this.title.text = adv.adsTitle
        this.description.text = adv.adsDescription
        this.location.text = adv.adsLocation
        this.date.text = adv.adsDate.toString()
        this.duration.text = adv.adsDuration.toString()
        this.account.text = adv.adsAccount
        this.moreButton.setOnClickListener { moreButtonAction }
    }

    /**
     * unbind:
     * A method to provide safeness when recycling a component in a RecyclerView
     */
    fun unbind() {
        moreButton.setOnClickListener(null)
    }
}

/**
 * AdsAdapter extends the Adapter of the [RecyclerView] and implements the required methods.
 */
class AdsAdapter(val adsList: MutableList<Advertisement>) : RecyclerView.Adapter<AdsViewHolder>() {

    /**
     * On creating the view holder, based on whether the ads is private or not, it'll be created a regular timeslot layout fragment or an empty one.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.fragment_time_slot_details, parent, false)
        return AdsViewHolder(v)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        holder.bind(adsList[position]) {}
    }

    /**
     * Simply returns the size of the list of advertisement provided to the adapter.
     */
    override fun getItemCount(): Int {
        return adsList.size
    }

    /**
     * OnViewRecycled invokes the unbind method to set on null the callback to the listener
     * of the moreButton
     */
    override fun onViewRecycled(holder: AdsViewHolder) {
        holder.unbind()
    }
}