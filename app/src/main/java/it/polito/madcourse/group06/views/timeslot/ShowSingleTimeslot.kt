package it.polito.madcourse.group06.views.timeslot

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

class ShowSingleTimeslot : Fragment(R.layout.time_slot_details_fragment) {

    private val advViewModel: AdvertisementViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    private lateinit var advTitle: TextView
    private lateinit var advAccount: TextView
    private lateinit var advLocation: TextView
    private lateinit var advDate: TextView
    private lateinit var advStartingTime: TextView
    private lateinit var advEndingTime: TextView
    private lateinit var advDuration: TextView
    private lateinit var advDescription: TextView
    private lateinit var editButton: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.advTitle = view.findViewById(R.id.advTitle)
        this.advAccount = view.findViewById(R.id.advAccount)
        this.advLocation = view.findViewById(R.id.advLocation)
        this.advDate = view.findViewById(R.id.advDate)
        this.advStartingTime = view.findViewById(R.id.advStartingTime)
        this.advEndingTime = view.findViewById(R.id.advEndingTime)
        this.advDuration = view.findViewById(R.id.advDuration)
        this.advDescription = view.findViewById(R.id.advDescription)
        this.editButton = view.findViewById(R.id.moreButtonID)

        advViewModel.advertisement.observe(viewLifecycleOwner) { singleAdvertisement ->
            userProfileViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                this.editButton.isVisible = singleAdvertisement.accountID == user.id
            }

            this.advTitle.text = singleAdvertisement.advTitle
            this.advAccount.text = singleAdvertisement.advAccount
            this.advLocation.text = singleAdvertisement.advLocation
            this.advDate.text = singleAdvertisement.advDate
            this.advStartingTime.text = "Starting time: ${singleAdvertisement.advStartingTime}"
            this.advEndingTime.text = "Ending time: ${singleAdvertisement.advEndingTime}"
            this.advDuration.text = "${singleAdvertisement.advDuration} hours"
            if (singleAdvertisement.advDescription.isEmpty()) {
                this.advDescription.text = "No description provided"
            } else {
                this.advDescription.text = singleAdvertisement.advDescription
            }
        }

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