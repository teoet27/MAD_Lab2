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
class AdvViewHolderCard(v: View) : RecyclerView.ViewHolder(v) {
    private val title: TextView = v.findViewById(R.id.advCardTitle)
    private val location: TextView = v.findViewById(R.id.advCardLocation)
    private val duration: TextView = v.findViewById(R.id.advCardDuration)
    private val account: TextView = v.findViewById(R.id.advCardAccount)
    private val bookmark: ImageView = v.findViewById(R.id.item_bookmark)
    private val expired: LinearLayout = v.findViewById(R.id.expired_label)
    private val trading: ConstraintLayout = v.findViewById(R.id.trade_window)

    /**
     * bind:
     * A method to bind the i-th entry of the adsList to the i-th holder properties.
     * @param adv an object of class Advertisement
     */
    @SuppressLint("ResourceAsColor")
    fun bind(adv: Advertisement, userViewModel:UserProfileViewModel,status:Int) {
        this.title.text = adv.advTitle
        this.location.text = adv.advLocation
        this.duration.text = adv.advDuration.toString()
        this.account.text = adv.advAccount

        this.expired.visibility=if(adv.isExpired())View.VISIBLE else View.GONE
        this.trading.visibility=if(status== ACTIVE_AND_SAVED||status==ACTIVE) View.VISIBLE else View.GONE

        if(status==SAVED || status== ACTIVE_AND_SAVED)
            this.bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp)
        else
            this.bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp)
        if(status== ACTIVE_AND_SAVED||status==ACTIVE){
            /*...*/
            if(adv.isExpired()){
                /*...*/
            }
        }
        this.bookmark.setOnClickListener{
            userViewModel.bookmarkAdvertisement(adv.id!!,!(status==SAVED || status== ACTIVE_AND_SAVED))
        }
    }
}