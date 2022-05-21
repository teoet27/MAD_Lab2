package it.polito.madcourse.group06.views.timeslot

import android.os.Bundle
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
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.advertisement.AdvAdapterCard
import it.polito.madcourse.group06.utilities.ServiceTools
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.SharedViewModel
import kotlin.math.sqrt

class ShowListOfTimeslots : Fragment(R.layout.show_timeslots_frag){

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var newAdvButton: Button
    private lateinit var filterButton:Button

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
        this.filterButton = view.findViewById(R.id.filter_button)
        this.recyclerView = view.findViewById(R.id.rvAdvFullList)

        this.newAdvButton.setOnClickListener {
            findNavController().navigate(R.id.action_ShowListTimeslots_to_newTimeSlotDetailsFragment)
        }

        sharedViewModel.selected.observe(viewLifecycleOwner){
            enableUI(!it)
        }
        filterButton.setOnClickListener{
            activity?.supportFragmentManager!!.
            beginTransaction().
            setCustomAnimations(R.anim.slide_in_up,0).
            add(R.id.nav_host_fragment_content_main,FilterTimeslots()).
            commit()
        }
        advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfAdv ->
            /**
             * If there are no advertisements in the DB proper texts are shown.
             */
            listOfAdv.filter{ it.listOfSkills.contains(arguments?.getString("selected_skill"))||
                    arguments?.getString("selected_skill")=="All"}.also{
                    view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible = it.isEmpty()
                    view.findViewById<ImageView>(R.id.create_hint).isVisible = it.isEmpty()

                    if (it.isNotEmpty()) {
                        this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                        this.recyclerView.adapter = AdvAdapterCard(it, advertisementViewModel)
                    }
            }

            sharedViewModel.filter.observe(viewLifecycleOwner){ filter->
                ServiceTools().filterAdvertisementList(listOfAdv,filter)?.also{
                    view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible = it.isEmpty()
                    view.findViewById<ImageView>(R.id.create_hint).isVisible = it.isEmpty()

                    if (it.isNotEmpty()) {
                        this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                        this.recyclerView.adapter = AdvAdapterCard(it, advertisementViewModel)
                    }
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_ShowListTimeslots_to_showListOfServices)
            }
        })
    }

    private fun enableUI(switch: Boolean) {
        this.filterButton.isEnabled= switch
        this.recyclerView.suppressLayout(!switch)
        this.recyclerView.isClickable= switch
    }
}
