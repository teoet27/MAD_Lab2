package it.polito.MAD.group06.views.timeslot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.MAD.group06.R
import it.polito.MAD.group06.models.advertisement.AdvAdapterCard
import it.polito.MAD.group06.viewmodels.AdvertisementViewModel
import it.polito.MAD.group06.utilities.ServiceTools

class ShowListOfTimeslots : Fragment(R.layout.show_timeslots_frag){

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var newAdvButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.show_timeslots_frag,
            container,
            false
        );
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.newAdvButton = view.findViewById(R.id.newAdvButton)
        this.newAdvButton.setOnClickListener {
            findNavController().navigate(R.id.action_ShowListTimeslots_to_newTimeSlotDetailsFragment)
        }

        advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfAdv ->
            /**
             * If there are no advertisements in the DB proper texts are shown.
             */
            listOfAdv.filter{ it.listOfSkills.contains(arguments?.getString("selected_skill")) }
            view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible = listOfAdv.isEmpty()
            view.findViewById<ImageView>(R.id.create_hint).isVisible = listOfAdv.isEmpty()

            if (listOfAdv.isNotEmpty()) {
                this.recyclerView = view.findViewById(R.id.rvAdvFullList)
                this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                this.recyclerView.adapter = AdvAdapterCard(listOfAdv, advertisementViewModel)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_ShowListTimeslots_to_showListOfServices)
            }
        })
    }
}
