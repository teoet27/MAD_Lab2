package it.polito.madcourse.group06.models.advertisement

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.utilities.ACTIVE
import it.polito.madcourse.group06.utilities.ACTIVE_AND_SAVED
import it.polito.madcourse.group06.utilities.SAVED
import it.polito.madcourse.group06.utilities.isExpired
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

/**
 * [AdvViewHolderCard] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the advertisement
 * card.
 */
class AdvViewHolderCard(v: View,private val userViewModel:UserProfileViewModel,) : RecyclerView.ViewHolder(v) {
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
        /*if(rate layout)
            rate_button = find.... .apply{setOnClickListener{...}}
         else if(active layout)
            date = find ....apply{it.text=adv.advDate}
            cost = find ....apply{it.text=adv.duration.toCost()}
         */
    }
}