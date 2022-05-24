package it.polito.madcourse.group06.models.advertisement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.utilities.TimeslotTools
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel

/**
 * [AdvAdapterCard] extending the Adapter of the [RecyclerView] and implements the required methods.
 */
class AdvAdapterCard(
    private val adsList: List<Advertisement>,
    private val advViewModel: AdvertisementViewModel
) : RecyclerView.Adapter<AdvViewHolderCard>() {

    private var isMyAdv: Boolean = false
    private var isSortedUp = false
    private var param: Int = 0
    private var showedData = adsList
    private var oldShowedData=listOf<Advertisement>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvViewHolderCard {
        val vg = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.adv_item, parent, false)
        return AdvViewHolderCard(vg)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: AdvViewHolderCard, position: Int) {
        holder.bind(showedData[position])
        holder.itemView.setOnClickListener { view ->
            advViewModel.setSingleAdvertisement((showedData[showedData.indexOf(showedData[position])]))
            Navigation.findNavController(view)
                .navigate(R.id.action_ShowListTimeslots_to_showSingleTimeslot)
        }
    }

    /**
     * Simply returns the size of the list of advertisement provided to the adapter.
     */
    override fun getItemCount(): Int {
        return showedData.size
    }

    /**
     * A method to provide a switch mode between the "All" visualization and the "My adv" one.
     * @param mode true: show only my adv, false: show all the adv
     * @param userID if true, show only this user's adv
     */
    fun switchMode(mode: Boolean, userID: String) {
        isMyAdv = mode
        if (isMyAdv) {
            showedData = showedData.filter { it.accountID == userID }
        } else {
            showedData = adsList
        }
        notifyDataSetChanged()
    }

    fun switchSort(mode: Boolean, param: Int) {
        this.isSortedUp = mode
        this.param = param

        when (mode) {
            true -> when (param) {
                0 -> showedData = showedData.sortedBy { it.advTitle.lowercase() }
                1 -> showedData = showedData.sortedByDescending { it.advDuration }
                2 -> showedData = showedData.sortedByDescending { timeStringToDoubleSec(it.advStartingTime) }
                3 -> showedData = showedData.sortedByDescending { timeStringToDoubleSec(it.advEndingTime) }
                4 -> showedData = showedData.sortedByDescending { dateStringToInt(it.advDate) }
            }
            else -> when (param) {
                0 -> showedData = showedData.sortedByDescending { it.advTitle.lowercase() }
                1 -> showedData = showedData.sortedBy { it.advDuration }
                2 -> showedData = showedData.sortedBy { timeStringToDoubleSec(it.advStartingTime) }
                3 -> showedData = showedData.sortedBy { timeStringToDoubleSec(it.advEndingTime) }
                4 -> showedData = showedData.sortedBy { dateStringToInt(it.advDate) }
            }
        }
        notifyDataSetChanged()
    }

    //methods for search
    fun beforeSearchByName(){
        if(oldShowedData.isNullOrEmpty())
            oldShowedData=showedData
    }
    fun searchByName(name:String){
        showedData=oldShowedData.filter{it.advTitle.contains(name,true)}
        notifyDataSetChanged()
    }


    /**
     * filterAdvertisement
     * @param advList list of all available advertisements
     * @param location to be matched to Adv related attribute
     * @param starting_time to be matched to Adv related attribute
     * @param ending_time to be matched to Adv related attribute
     * @param duration to be matched to Adv related attribute
     * @param starting_date to be matched to Adv related attribute
     * @param ending_date to be matched to Adv related attribute
     * @return the list of Advertisements matching the constraints
     */
    fun filterAdvertisementList(
        advList: List<Advertisement>?,
        advFilter: TimeslotTools.AdvFilter?
    ): List<Advertisement>? {
        //begin debug section
        advList?.filter { adv ->
            if ((advFilter?.starting_date != null && adv.advDate.isLaterThanDate(advFilter.starting_date)) || (advFilter?.starting_date == null))
                println("ok")
            true
        }
        //end debug section
        return if (advFilter == null) advList else advList?.filter { adv ->
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
                    ((advFilter.ending_time != null && adv.advEndingTime.isSoonerThanTime(advFilter.ending_time)) || (advFilter.ending_time == null)) &&

                    ((advFilter.starting_date != null && adv.advDate.isLaterThanDate(advFilter.starting_date)) || (advFilter.starting_date == null)) &&
                    ((advFilter.ending_date != null && adv.advDate.isSoonerThanDate(advFilter.ending_date)) || (advFilter.ending_date == null))
        }
    }

    fun sortAdvertisementList(
        advList: List<Advertisement>?,
        criterion: Int?,
        up_flag: Boolean = true
    ): List<Advertisement>? {
        val sortedList = when (up_flag) {
            true -> when (criterion) {
                0 -> advList?.apply { sortedBy { it.advTitle.lowercase() } }
                1 -> advList?.apply { sortedByDescending { it.advDuration } }
                2 -> advList?.apply { sortedByDescending { timeStringToDoubleSec(it.advStartingTime) } }
                3 -> advList?.apply { sortedByDescending { timeStringToDoubleSec(it.advEndingTime) } }
                4 -> advList?.apply { sortedByDescending { dateStringToInt(it.advDate) } }
                else -> null
            }
            else -> when (criterion) {
                0 -> advList?.apply { sortedByDescending { it.advTitle.lowercase() } }
                1 -> advList?.apply { sortedBy { it.advDuration } }
                2 -> advList?.apply { sortedBy { timeStringToDoubleSec(it.advStartingTime) } }
                3 -> advList?.apply { sortedBy { timeStringToDoubleSec(it.advEndingTime) } }
                4 -> advList?.apply { sortedBy { dateStringToInt(it.advDate) } }
                else -> null
            }
        }
        notifyDataSetChanged()
        return sortedList
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
            String.format("%.2f", dateDifference).toDouble(),
            String.format("%.2f", dateDifference).toDouble() >= 0
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
            String.format("%.2f", timeDifference).toDouble(),
            String.format("%.2f", timeDifference).toDouble() >= 0
        )
    }

    private fun Boolean.toInt() = if (this) 1 else 0

}