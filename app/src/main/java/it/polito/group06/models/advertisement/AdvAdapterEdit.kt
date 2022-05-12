package it.polito.group06.models.advertisement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R
import it.polito.group06.viewmodels.AdvertisementViewModel

/**
 * AdsAdapter extends the Adapter of the [RecyclerView] and implements the required methods.
 */
class AdvAdapterEdit(private val adsList: List<Advertisement>, private val advertisementViewModel: AdvertisementViewModel) : RecyclerView.Adapter<AdvViewHolderEdit>() {

    /**
     * On creating the view holder, based on whether the ads is private or not, it'll be created a regular timeslot layout fragment or an empty one.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvViewHolderEdit {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.adv_item, parent, false)
        return AdvViewHolderEdit(v)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: AdvViewHolderEdit, position: Int) {
        holder.bind(adsList[position]) {
            advertisementViewModel.removeAd(adsList.indexOf(adsList[position]).toLong())
            notifyItemRemoved(adsList.indexOf(adsList[position]))
            Navigation.findNavController(holder.itemView).navigate(R.id.action_editTimeSlotDetailsFragment_to_ShowListTimeslots)
        }
    }

    /**
     * Simply returns the size of the list of advertisement provided to the adapter.
     */
    override fun getItemCount(): Int {
        return adsList.size
    }

    /**
     * OnViewRecycled invokes the unbind method to set on null the callback to the listener
     * of the moreButton
     */
    override fun onViewRecycled(holder: AdvViewHolderEdit) {
        holder.unbind()
    }
}