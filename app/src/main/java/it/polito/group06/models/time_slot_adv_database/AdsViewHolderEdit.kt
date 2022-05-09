package it.polito.group06.models.time_slot_adv_database

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R

/**
 * [AdsViewHolderEdit] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the advertisement
 * card.
 */
class AdsViewHolderEdit(v: View) : RecyclerView.ViewHolder(v) {
    private val title: TextView = v.findViewById(R.id.newTitle)
    private val description: TextView = v.findViewById(R.id.newDescription)
    private val location: TextView = v.findViewById(R.id.newLocation)
    private val date: TextView = v.findViewById(R.id.newDate)
    private val duration: TextView = v.findViewById(R.id.newDuration)
    private val account: TextView = v.findViewById(R.id.ads_account)
    private val removeButton: ImageView = v.findViewById(R.id.deleteButton)

    /**
     * bind:
     * A method to bind the i-th entry of the adsList to the i-th holder properties.
     * @param adv an object of class Advertisement
     * @param removeButtonAction the callback to be set at the removeButton listener
     */
    fun bind(adv: Advertisement, removeButtonAction: () -> Unit) {
        this.title.text = adv.advTitle
        this.description.text = adv.advDescription
        this.location.text = adv.advLocation
        this.date.text = adv.advDate
        this.duration.text = adv.advDuration.toString()
        this.account.text = adv.advAccount
        this.removeButton.setOnClickListener { removeButtonAction }
    }

    /**
     * unbind:
     * A method to provide safeness when recycling a component in a RecyclerView
     */
    fun unbind() {
        removeButton.setOnClickListener(null)
    }
}