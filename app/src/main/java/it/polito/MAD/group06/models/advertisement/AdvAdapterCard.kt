package it.polito.MAD.group06.models.advertisement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import it.polito.MAD.group06.R
import it.polito.MAD.group06.viewmodels.AdvertisementViewModel

/**
 * [AdvAdapterCard] extending the Adapter of the [RecyclerView] and implements the required methods.
 */
class AdvAdapterCard(private val adsList: List<Advertisement>,
                     private val advViewModel: AdvertisementViewModel) : RecyclerView.Adapter<AdvViewHolderCard>() {

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
        holder.bind(adsList[position])
        holder.itemView.setOnClickListener { view ->
            advViewModel.setSingleAdvertisement((adsList[adsList.indexOf(adsList[position])]))
            Navigation.findNavController(view).navigate(R.id.action_ShowListTimeslots_to_showSingleTimeslot)
        }
    }

    /**
     * Simply returns the size of the list of advertisement provided to the adapter.
     */
    override fun getItemCount(): Int {
        return adsList.size
    }
}