package it.polito.group06.models.time_slot_adv_database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R

/**
 * AdsAdapter extends the Adapter of the [RecyclerView] and implements the required methods.
 */
class AdsAdapterCard(val adsList: List<Advertisement>) : RecyclerView.Adapter<AdsViewHolderCard>() {

    /**
     * On creating the view holder, based on whether the ads is private or not, it'll be created a regular timeslot layout fragment or an empty one.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolderCard {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.adv_item, parent, false)
        return AdsViewHolderCard(v)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: AdsViewHolderCard, position: Int) {
        holder.bind(adsList[position])
    }

    /**
     * Simply returns the size of the list of advertisement provided to the adapter.
     */
    override fun getItemCount(): Int {
        return adsList.size
    }
}