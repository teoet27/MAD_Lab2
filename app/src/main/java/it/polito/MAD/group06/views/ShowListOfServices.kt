package it.polito.MAD.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.MAD.group06.R

class ShowListOfServices : Fragment(R.layout.fragment_service_list){

    // private val serviceViewModel: ServiceViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.fragment_service_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*serviceViewModel.insertService("Gardening")
        serviceViewModel.insertService("Dog sitter")*/
        /*
        serviceViewModel.getFullListOfServices().observe(this.viewLifecycleOwner) { listOfServices ->
            /**
             * If there are no services in the DB proper texts are shown.
             */
            view.findViewById<TextView>(R.id.defaultTextServicesList).isVisible = listOfServices == null || listOfServices.isEmpty()

            if (!(listOfServices == null || listOfServices.isEmpty())) {
                this.recyclerView = view.findViewById(R.id.rvServicesFullList)
                this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                this.recyclerView.adapter = ServiceAdapterCard(listOfServices,serviceViewModel)
            }
        }
        */
    }
}