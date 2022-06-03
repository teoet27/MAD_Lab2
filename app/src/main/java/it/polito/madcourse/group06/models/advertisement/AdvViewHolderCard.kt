package it.polito.madcourse.group06.models.advertisement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.utilities.*
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import it.polito.madcourse.group06.views.RatingFragment
import java.text.SimpleDateFormat
import java.util.*


/**
 * [AdvViewHolderCard] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the advertisement
 * card.
 */
class AdvViewHolderCard(private val v: View, private val userViewModel: UserProfileViewModel) : RecyclerView.ViewHolder(v) {
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
        this.duration.text = timeDoubleHourToString(if(adv.activeFor>0) adv.activeFor else adv.advDuration)
        this.account.text = adv.advAccount

            this.bookmark.setOnClickListener{
                userViewModel.bookmarkAdvertisement(adv.id!!)
            }
            if(viewType == R.layout.adv_to_rate_item || viewType == R.layout.adv_to_rate_item_saved)
                 v.findViewById<Button>(R.id.rate_button).setOnClickListener {
                 RatingFragment().also {
                     it.arguments = bundleOf(
                         "other_id" to adv.accountID,
                         "adv_title" to adv.advTitle,
                         "adv_id" to adv.id
                     )
                     (v.context as TBMainActivity).supportFragmentManager.beginTransaction()
                         .add(R.id.nav_host_fragment_content_main, it, "rating_fragment")
                         .commit()
                     }
                 }

        if(viewType == R.layout.adv_active_item || viewType == R.layout.adv_active_item_saved){
            v.findViewById<TextView>(R.id.trade_credit).text = hoursToCredit(adv.activeFor).toString()
            var timeDate=""
            if (adv.advDate != SimpleDateFormat("dd/MM/yyyy").format(Date())) {
                timeDate="Starts at ${adv.activeAt}, on ${adv.advDate}"
            }
            else {
                if(adv.activeAt?.isLaterThanTime(SimpleDateFormat("HH:mm").format(Date()))==true)
                    timeDate="Starts at ${adv.activeAt}"
                else if(timeStringToDoubleHour(SimpleDateFormat("HH:mm").format(Date()))
                        >timeStringToDoubleHour(adv.activeAt!!)+adv.activeFor) {
                    timeDate="Ended"//do stuff
                }
                else {
                    timeDate="Started"
                }
            }
            v.findViewById<TextView>(R.id.trade_starting_time).text = timeDate
        }
    }
}