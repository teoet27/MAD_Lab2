package it.polito.madcourse.group06.models.advertisement

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.utilities.*
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import org.w3c.dom.Text

/**
 * [AdvViewHolderCard] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the advertisement
 * card.
 */
class AdvViewHolderCard(private val v: View,private val userViewModel:UserProfileViewModel,) : RecyclerView.ViewHolder(v) {
    private val title: TextView = v.findViewById(R.id.advCardTitle)
    private val location: TextView = v.findViewById(R.id.advCardLocation)
    private val duration: TextView = v.findViewById(R.id.advCardDuration)
    private val account: TextView = v.findViewById(R.id.advCardAccount)
    private val bookmark: ImageView = v.findViewById(R.id.item_bookmark)

    /**
     * bind:
     * A method to bind the i-th entry of the adsList to the i-th holder properties.
     * @param adv an object of class Advertisement
     */
    fun bind(adv: Advertisement,viewType:Int) {
        this.title.text = adv.advTitle
        this.location.text = adv.advLocation
        this.duration.text = adv.advDuration.toString()
        this.account.text = adv.advAccount

        this.bookmark.setOnClickListener{
            userViewModel.bookmarkAdvertisement(adv.id!!)
        }
        if(viewType==R.layout.adv_to_rate_item||viewType==R.layout.adv_to_rate_item_saved)
             v.findViewById<Button>(R.id.rate_button).setOnClickListener{
                 /*open rate fragment*/
             }
        if(viewType==R.layout.adv_active_item||viewType==R.layout.adv_active_item_saved){
            v.findViewById<TextView>(R.id.trade_credit).text=hoursToCredit(adv.advDuration).toString()
            v.findViewById<TextView>(R.id.trade_starting_time).text=adv.advStartingTime
        }
    }
}