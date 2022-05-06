package it.polito.group06.models.time_slot_adv_database

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R

/**
 * [AdsViewHolderCard] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the advertisement
 * card.
 */
class AdsViewHolderCard(v: View) : RecyclerView.ViewHolder(v) {
    private val title: TextView = v.findViewById(R.id.advCardTitle)
    private val location: TextView = v.findViewById(R.id.advCardLocation)
    private val duration: TextView = v.findViewById(R.id.advCardDuration)
    private val account: TextView = v.findViewById(R.id.advCardAccount)

    /**
     * bind:
     * A method to bind the i-th entry of the adsList to the i-th holder properties.
     * @param adv an object of class Advertisement
     */
    fun bind(adv: Advertisement) {
        this.title.text = adv.adsTitle
        this.location.text = adv.adsLocation
        this.duration.text = adv.adsDuration.toString()
        this.account.text = adv.adsAccount
    }
}