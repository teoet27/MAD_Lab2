package it.polito.madcourse.group06.models.advertisement

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.utilities.*
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import it.polito.madcourse.group06.views.timeslot.ShowSingleTimeslot
import java.util.*
import kotlin.math.roundToInt

/**
 * [AdvAdapterCard] extending the Adapter of the [RecyclerView] and implements the required methods.
 */
class AdvAdapterCard(
    private val adsList: List<Advertisement>,
    private val advViewModel: AdvertisementViewModel,
    private val userViewModel: UserProfileViewModel,
    private val activity: Activity
) : RecyclerView.Adapter<AdvViewHolderCard>() {

    private var showedData = adsList
    private var userID: String? = null
    private var savedAdsIDs = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvViewHolderCard {
        val vg = LayoutInflater
            .from(parent.context).run {
                when (viewType) {
                    R.layout.adv_item -> inflate(R.layout.adv_item, parent, false)
                    R.layout.adv_item_saved -> inflate(R.layout.adv_item_saved, parent, false)
                    R.layout.adv_active_item -> inflate(R.layout.adv_active_item, parent, false)
                    R.layout.adv_active_item_saved -> inflate(R.layout.adv_active_item_saved, parent, false)
                    R.layout.adv_expired_item -> inflate(R.layout.adv_expired_item, parent, false)
                    R.layout.adv_expired_item_saved -> inflate(R.layout.adv_expired_item_saved, parent, false)
                    R.layout.adv_to_rate_item -> inflate(R.layout.adv_to_rate_item, parent, false)
                    R.layout.adv_to_rate_item_saved -> inflate(R.layout.adv_to_rate_item_saved, parent, false)
                    R.layout.adv_ended_item_saved -> inflate(R.layout.adv_ended_item_saved, parent, false)
                    R.layout.adv_finished_item -> inflate(R.layout.adv_finished_item, parent, false)
                    else -> inflate(R.layout.adv_item, parent, false)
                }
            }
        return AdvViewHolderCard(vg, userViewModel)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: AdvViewHolderCard, position: Int) {
        holder.bind(showedData[position], getItemViewType(position), userID!!)
        holder.itemView.setOnClickListener {
            advViewModel.setSingleAdvertisement((showedData[showedData.indexOf(showedData[position])]))
            (activity as TBMainActivity).supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, 0)
                .add(R.id.nav_host_fragment_content_main, ShowSingleTimeslot(), "single_timeslot")
                .commit()
        }
    }

    override fun getItemViewType(position: Int): Int {
        // case 0: adv was active and it is ended
        return if (showedData[position].isEnded()) {
            if (savedAdsIDs.contains(showedData[position].id)) {
                R.layout.adv_ended_item_saved
            } else {
                R.layout.adv_finished_item
            }
        }
        // case 1: adv was active, it needs to be rated
        else if ((showedData[position].isToBeRated() && userID == showedData[position].accountID
                    && !showedData[position].rxUserId.isNullOrEmpty())
            ||
            ((userID == showedData[position].ratingUserId
                    && showedData[position].isToBeRated()))
        ) {
            if (savedAdsIDs.contains(showedData[position].id)) {
                R.layout.adv_to_rate_item_saved
            } else {
                R.layout.adv_to_rate_item
            }
        }
        // case 2: generic adv is expired
        else if (showedData[position].isExpired()) {
            if (savedAdsIDs.contains(showedData[position].id))
                R.layout.adv_expired_item_saved
            else
                R.layout.adv_expired_item
        }
        // case 3: active ads, still to begin
        else if (userID == showedData[position].accountID
            && !showedData[position].rxUserId.isNullOrEmpty()
            || (userID == showedData[position].ratingUserId)
        ) {
            if (savedAdsIDs.contains(showedData[position].id))
                R.layout.adv_active_item_saved
            else
                R.layout.adv_active_item
        }
        // case 4: generic adv, not in any particular state
        else {
            if (savedAdsIDs.contains(showedData[position].id))
                R.layout.adv_item_saved
            else
                R.layout.adv_item
        }
    }

    /**
     * Simply returns the size of the list of advertisement provided to the adapter.
     */
    override fun getItemCount(): Int {
        return showedData.size
    }

    /**
     * A method initialize the dataset according to the selected tab among
     * - Services
     * - Active timeslots
     * - Saved timeslots
     * - My timeslots
     *
     * @param myAds: if true the inital set is based on Ads whose owner ID coincide with [userID]
     * @param userID: owner ID
     * @param activeAdsFlag: if true the initial set is based on Ads marked as "active" by inserting them in [adsIDsDs]
     * @param savedAdsFlag: if true the initial set is based on Ads marked as "saved" by inserting them in [adsIDsDs]
     */
    fun initDataset(
        myAds: Boolean? = null,
        activeAdsFlag: Boolean? = null,
        savedAdsFlag: Boolean? = null
    ): List<Advertisement> {

        // My timeslot filtering
        return if (myAds == true) {
            adsList.filter { it.accountID == userID }
        }
        // Active or Saved timeslot filtering
        else if (activeAdsFlag == true) {
            adsList.filter {
                (it.ratingUserId == userID ||
                        (it.accountID == userID && !it.rxUserId.isNullOrEmpty())) && !it.isEnded()
            }
        } else if (savedAdsFlag == true) {
            adsList.filter { savedAdsIDs.contains(it.id) && !it.isEnded() }
        } else
            adsList.filter { it.isAvailable() && !it.isEnded() }
    }

    /**
     * A method to provide filtering options for selecting a sorted subset of the initialized one
     * @param myAds: if true the inital set is based on Ads whose owner ID coincide with [userID]
     * @param userID: owner ID
     * @param activeAdsFlag: if true the initial set is based on Ads marked as "active" by inserting them in [adsIDsDs]
     * @param savedAdsFlag: if true the initial set is based on Ads marked as "saved" by inserting them in [adsIDsDs]
     * @param savedAdsIDs: list of saved Ads IDs to be considered for the current recycler view,
     * @param selectedSkill: filter all Ads of the initial set associated to the selected skill
     * @param advFilter: AdvFilter to filter Ads matching some criteria (location, duration etc.)
     * @param sortParam: criterion to sort the selected Ads
     * @param sortUp: flag to set the sort direction
     * @param search: filters among the currently selected Ads, those whose title or skill list contain the string [search]
     */
    fun updateDataSet(
        myAds: Boolean? = null,
        userID: String? = null,
        activeAdsFlag: Boolean? = null,
        savedAdsFlag: Boolean? = null,
        savedAdsIDs: List<String>? = null,
        selectedSkill: String? = null,
        advFilter: AdvFilter? = null,
        sortParam: Int? = null,
        sortUp: Boolean? = null,
        search: String? = null
    ) {

        if (savedAdsIDs != null) {
            this.savedAdsIDs = savedAdsIDs
        }

        // init dataset
        this.userID = userID
        showedData = initDataset(myAds, activeAdsFlag, savedAdsFlag)

        // SelectedSkill filtering phase (only in main page)
        showedData =
            showedData.filter { it.listOfSkills.contains(selectedSkill) || selectedSkill == ALL_SERVICES }

        //Filtering phase
        showedData =
            if (advFilter == null) showedData else showedData.filter { adv ->
                ((advFilter.location != null && !advFilter.whole_word && advFilter.location.lowercase()
                    .contains(adv.advLocation.lowercase(), true)) ||
                        (advFilter.location != null && !advFilter.whole_word && adv.advLocation.lowercase()
                            .contains(advFilter.location.lowercase(), true)) ||
                        (advFilter.location != null && advFilter.whole_word && advFilter.location.lowercase() == adv.advLocation.lowercase()) || (advFilter.location == null)) &&

                        ((advFilter.min_duration != null && adv.advDuration >= timeStringToDoubleHour(
                            advFilter.min_duration
                        )) || (advFilter.min_duration == null)) &&
                        ((advFilter.max_duration != null && adv.advDuration <= timeStringToDoubleHour(
                            advFilter.max_duration
                        )) || (advFilter.max_duration == null)) &&

                        ((advFilter.starting_time != null && adv.advStartingTime.isLaterThanTime(
                            advFilter.starting_time
                        )) || (advFilter.starting_time == null)) &&
                        ((advFilter.ending_time != null && adv.advEndingTime.isSoonerThanTime(
                            advFilter.ending_time
                        )) || (advFilter.ending_time == null)) &&

                        ((advFilter.starting_date != null && adv.advDate.isLaterThanDate(advFilter.starting_date)) || (advFilter.starting_date == null)) &&
                        ((advFilter.ending_date != null && adv.advDate.isSoonerThanDate(advFilter.ending_date)) || (advFilter.ending_date == null))
            }

        // Sorting phase
        if (sortUp != null) {
            when (sortUp) {
                true -> when (sortParam) {
                    0 -> showedData = showedData.sortedBy { it.advTitle.lowercase() }
                    1 -> showedData = showedData.sortedByDescending { it.advDuration }
                    2 -> showedData =
                        showedData.sortedByDescending { timeStringToDoubleSec(it.advStartingTime) }
                    3 -> showedData =
                        showedData.sortedByDescending { timeStringToDoubleSec(it.advEndingTime) }
                    4 -> showedData = showedData.sortedByDescending { dateStringToInt(it.advDate) }
                }
                else -> when (sortParam) {
                    0 -> showedData = showedData.sortedByDescending { it.advTitle.lowercase() }
                    1 -> showedData = showedData.sortedBy { it.advDuration }
                    2 -> showedData =
                        showedData.sortedBy { timeStringToDoubleSec(it.advStartingTime) }
                    3 -> showedData =
                        showedData.sortedBy { timeStringToDoubleSec(it.advEndingTime) }
                    4 -> showedData = showedData.sortedBy { dateStringToInt(it.advDate) }
                }
            }
        }

        // Search
        if (search != null)
            showedData = showedData.filter {
                it.advTitle.contains(search.toString(), true) ||
                        !it.listOfSkills.none { skill -> skill.contains(search.toString(), true) }
            }

        notifyDataSetChanged()
    }

    private fun dateStringToInt(date: String): Int {
        var dateInt = 0
        date.split("/").forEachIndexed { index, s ->
            when (index) {
                0 -> dateInt += s.toInt() //day
                1 -> dateInt += (31 - 3 * (s.toInt() == 2).toInt() - (listOf(
                    4,
                    6,
                    9,
                    11
                ).contains(s.toInt())).toInt()) * s.toInt() //month
                2 -> dateInt += (if (s.toInt() % 400 == 0) 366 else 365) * s.toInt() //year
            }
        }
        return dateInt
    }

    private fun timeStringToDoubleSec(time: String): Double {
        return time.split(":").fold(0.0) { a, b -> (a.toDouble() + b.toDouble()) * 60.0 }
    }

    private fun String.isLaterThanTime(time: String): Boolean {
        return computeTimeDifference(time, this).first >= 0
    }

    private fun String.isSoonerThanTime(time: String): Boolean {
        return computeTimeDifference(time, this).first <= 0
    }

    private fun String.isLaterThanDate(date: String): Boolean {
        return computeDateDifference(date, this).first >= 0
    }

    private fun String.isSoonerThanDate(date: String): Boolean {
        return computeDateDifference(date, this).first <= 0
    }

    private fun timeStringToDoubleHour(time: String): Double {
        return time.split(":").foldRight(0.0) { a, b -> (a.toDouble() + b.toDouble()) / 60.0 } * 60
    }

    private fun computeDateDifference(
        startingDate: String,
        endingDate: String
    ): Pair<Double, Boolean> {
        var dateDifference: Double = 0.0
        if (startingDate.isNullOrEmpty() || endingDate.isNullOrEmpty()) {
            return Pair(-1.0, false)
        }
        dateDifference = (dateStringToInt(endingDate) - dateStringToInt(startingDate)).toDouble()
        return Pair(
            (dateDifference * 100.0).roundToInt() / 100.0,
            (dateDifference * 100.0).roundToInt() / 100.0 >= 0
        )
    }

    private fun computeTimeDifference(
        startingTime: String,
        endingTime: String
    ): Pair<Double, Boolean> {
        var timeDifference: Double = 0.0
        if (startingTime.isNullOrEmpty() || endingTime.isNullOrEmpty()) {
            return Pair(-1.0, false)
        }

        timeDifference = timeStringToDoubleSec(endingTime) - timeStringToDoubleSec(startingTime)

        return Pair(
            (timeDifference * 100.0).roundToInt() / 100.0,
            (timeDifference * 100.0).roundToInt() / 100.0 >= 0
        )
    }
}