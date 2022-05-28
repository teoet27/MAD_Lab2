package it.polito.madcourse.group06.models.advertisement

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.utilities.isExpired

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

    /**
     * bind:
     * A method to bind the i-th entry of the adsList to the i-th holder properties.
     * @param adv an object of class Advertisement
     */
    @SuppressLint("ResourceAsColor")
    fun bind(adv: Advertisement) {
        this.title.text = adv.advTitle
        this.location.text = adv.advLocation
        this.duration.text = adv.advDuration.toString()
        this.account.text = adv.advAccount
        this.expired.visibility=if(adv.isExpired())View.VISIBLE else View.GONE
        this.bookmark.setOnClickListener{
            //TODO: Update Advertisement class and its implementations
            /*
            if(adv.bookmarked)
                this.bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp)
            else
                this.bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp)
            var ad=adv
            ad.bookmarked=!adv.bookmarked
            advertisementViewModel.editAdvertisement(ad)
            */
        }
    }
}