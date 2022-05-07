package it.polito.group06.views

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.polito.group06.R
import it.polito.group06.models.time_slot_adv_database.Advertisement
import it.polito.group06.viewmodels.AdvertisementViewModel

class NewSingleTimeslot : Fragment(R.layout.new_time_slot_details_fragment) {

    private val advViewModel by viewModels<AdvertisementViewModel>()
    private lateinit var newTitle: TextView
    private lateinit var newLocation: TextView
    private lateinit var newDate: TextView
    private lateinit var newDuration: TextView
    private lateinit var newDescription: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.newTitle = view.findViewById(R.id.newTitle)
        this.newLocation = view.findViewById(R.id.newLocation)
        this.newDate = view.findViewById(R.id.newDate)
        this.newDuration = view.findViewById(R.id.newDuration)
        this.newDescription = view.findViewById(R.id.newDescription)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                advViewModel.insertAd(
                    Advertisement(
                        null,
                        newTitle.text.toString(),
                        newDescription.text.toString(),
                        newLocation.text.toString(),
                        newDate.text.toString(),
                        0f,
                        "Guido Saracco",
                        false
                    )
                )
                findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
            }
        })
    }
}