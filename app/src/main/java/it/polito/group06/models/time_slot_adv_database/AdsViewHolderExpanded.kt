package it.polito.group06.models.time_slot_adv_database

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R

/**
 * [AdsViewHolderExpanded] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the advertisement
 * card.
 */
class AdsViewHolderExpanded(v: View) : RecyclerView.ViewHolder(v) {
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
        this.date.text = adv.adsDate
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