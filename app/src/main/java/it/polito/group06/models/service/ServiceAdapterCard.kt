package it.polito.group06.models.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R
import it.polito.group06.viewmodels.ServiceViewModel

/**
 * [ServiceAdapterCard] extending the Adapter of the [RecyclerView] and implements the required methods.
 */
class ServiceAdapterCard(val serviceList:List<Service>,
                         private val serviceViewModel: ServiceViewModel): RecyclerView.Adapter<ServiceViewHolderCard>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolderCard {
        val vg = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.service_item,parent,false)
        return ServiceViewHolderCard(vg)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: ServiceViewHolderCard, position: Int) {
        holder.bind(serviceList[position])
        holder.itemView.setOnClickListener { view ->
            serviceViewModel.setSingleService((serviceList[serviceList.indexOf(serviceList[position])]))
            Navigation.findNavController(view).navigate(R.id.action_ShowListTimeslots_to_showSingleTimeslot)
        }
    }

    /**
     * Simply returns the size of the list of services provided to the adapter.
     */
    override fun getItemCount(): Int = serviceList.size
}