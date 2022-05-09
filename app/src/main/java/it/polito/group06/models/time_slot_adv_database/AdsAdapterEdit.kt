package it.polito.group06.models.time_slot_adv_database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R

/**
 * AdsAdapter extends the Adapter of the [RecyclerView] and implements the required methods.
 */
class AdsAdapterEdit(private val adsList: List<Advertisement>, private val advertisementViewModel: AdvertisementViewModel) : RecyclerView.Adapter<AdsViewHolderEdit>() {

    /**
     * On creating the view holder, based on whether the ads is private or not, it'll be created a regular timeslot layout fragment or an empty one.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolderExpanded {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.adv_item, parent, false)
        return AdsViewHolderExpanded(v)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: AdsViewHolderExpanded, position: Int) {
        holder.bind(adsList[position]) {}
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
    override fun onViewRecycled(holder: AdsViewHolderExpanded) {
        holder.unbind()
    }
}