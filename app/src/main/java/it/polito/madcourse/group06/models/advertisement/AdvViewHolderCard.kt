package it.polito.madcourse.group06.models.advertisement

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
import it.polito.madcourse.group06.views.review.RatingFragment
import java.text.SimpleDateFormat
import java.util.*


/**
 * [AdvViewHolderCard] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the advertisement
 * card.
 */
class AdvViewHolderCard(private val v: View, private val userViewModel: UserProfileViewModel) :
    RecyclerView.ViewHolder(v) {
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
    fun bind(adv: Advertisement, viewType: Int) {
        this.title.text = adv.advTitle
        this.location.text = adv.advLocation
        this.duration.text =
            timeDoubleHourToString(if (adv.activeFor > 0) adv.activeFor else adv.advDuration)
        this.account.text = adv.advAccount

        this.bookmark.setOnClickListener {
            userViewModel.bookmarkAdvertisement(adv.id!!)
        }
        if (viewType == R.layout.adv_to_rate_item || viewType == R.layout.adv_to_rate_item_saved)
            v.findViewById<Button>(R.id.rate_button).setOnClickListener {
                RatingFragment().also {
                    it.arguments = bundleOf(
                        "rx_user_id" to adv.rxUserId,
                        "adv_account_id" to adv.accountID,
                        "adv_title" to adv.advTitle,
                        "adv_id" to adv.id
                    )
                    (v.context as TBMainActivity).supportFragmentManager.beginTransaction()
                        .add(R.id.nav_host_fragment_content_main, it, "rating_fragment")
                        .commit()
                }
            }

        if (viewType == R.layout.adv_active_item || viewType == R.layout.adv_active_item_saved) {
            var tradeStartingTime = ""

            SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(Date()).also { currentDate ->
                if (adv.advDate != currentDate) {
                    tradeStartingTime =
                        "${dateListToString(adv.advDate)}, at ${adv.activeAt}"
                } else {
                    SimpleDateFormat("HH:mm",Locale.getDefault()).format(Date()).also { currentTime ->
                        if (adv.activeAt?.isLaterThanTime(currentTime) == true)
                            tradeStartingTime = "Starts at ${adv.activeAt}"
                        else if (timeStringToDoubleHour(currentTime)
                            < timeStringToDoubleHour(adv.activeAt!!) + adv.activeFor
                        )
                            tradeStartingTime = "Started"
                    }
                }
            }
            v.findViewById<TextView>(R.id.trade_starting_time).text = tradeStartingTime
            v.findViewById<TextView>(R.id.trade_credit).text =
                hoursToCredit(adv.activeFor).toString()
            v.findViewById<TextView>(R.id.trade_duration).text =
                timeDoubleHourToString(adv.activeFor)
            v.findViewById<TextView>(R.id.trade_location).text = "In ${adv.activeLocation}"
        }
    }
}