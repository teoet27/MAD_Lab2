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
        isSortedUp = mode
        this.param = param
        sortAdvertisementList(showedData, param, isSortedUp)

        if (isSortedUp) {
            showedData = showedData.sortedBy { it.advTitle }
        } else {
            showedData = showedData.sortedByDescending { it.advTitle }
        }
        notifyDataSetChanged()
    }

    fun filterList(advFilter: TimeslotTools.AdvFilter) {

    }

    fun sortAdvertisementList(advList: List<Advertisement>?, criterion: Int?, up_flag: Boolean = true): List<Advertisement>? {
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
                1 -> dateInt += (31 - 3 * (s.toInt() == 2).toInt() - (listOf(4, 6, 9, 11).contains(s.toInt())).toInt()) * s.toInt() //month
                2 -> dateInt += (if (s.toInt() % 400 == 0) 366 else 365) * s.toInt() //year
            }
        }
        return dateInt
    }

    private fun timeStringToDoubleSec(time: String): Double {
        return time.split(":").fold(0.0) { a, b -> (a.toDouble() + b.toDouble()) * 60.0 }
    }

    private fun Boolean.toInt() = if (this) 1 else 0

}