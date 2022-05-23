package it.polito.madcourse.group06.models.advertisement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
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

    fun switchSort(mode: Boolean) {
        isSortedUp = mode
        if(isSortedUp) {
            showedData = showedData.sortedBy { it.advTitle }
        } else {
            showedData = showedData.sortedByDescending { it.advTitle }
        }
        notifyDataSetChanged()
    }

}