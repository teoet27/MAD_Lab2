package it.polito.group06.views

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import it.polito.group06.R
import it.polito.group06.viewmodels.AdvertisementViewModel

class ShowSingleTimeslot: Fragment(R.layout.time_slot_details_fragment) {

    private val advViewModel by viewModels<AdvertisementViewModel>()

    private lateinit var advTitle: TextView
    private lateinit var advAccount: TextView
    private lateinit var advLocation: TextView
    private lateinit var advDate: TextView
    private lateinit var advDuration: TextView
    private lateinit var advDescription: TextView
    private lateinit var editButton: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.advTitle = view.findViewById(R.id.advTitle)
        this.advAccount = view.findViewById(R.id.advAccount)
        this.advLocation = view.findViewById(R.id.advLocation)
        this.advDate = view.findViewById(R.id.advDate)
        this.advDuration = view.findViewById(R.id.advDuration)
        this.advDescription = view.findViewById(R.id.advDescription)
        this.editButton = view.findViewById(R.id.moreButtonID)

        this.advTitle.text = advViewModel.singleAdvertisement.adsTitle
        this.advAccount.text = advViewModel.singleAdvertisement.adsAccount
        this.advLocation.text = advViewModel.singleAdvertisement.adsLocation
        this.advDate.text = advViewModel.singleAdvertisement.adsDate
        this.advDuration.text = advViewModel.singleAdvertisement.adsDuration.toString()
        this.advDescription.text = advViewModel.singleAdvertisement.adsDescription

        this.editButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_showSingleTimeslot_to_editTimeSlotDetailsFragment)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_showSingleTimeslot_to_ShowListTimeslots)
            }
        })

    }

}