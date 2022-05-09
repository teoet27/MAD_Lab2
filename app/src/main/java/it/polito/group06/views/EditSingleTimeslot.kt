package it.polito.group06.views

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import it.polito.group06.R
import it.polito.group06.viewmodels.AdvertisementViewModel
import it.polito.group06.viewmodels.UserProfileViewModel

class EditSingleTimeslot: Fragment(R.layout.edit_time_slot_details_fragment) {

    private val advViewModel: AdvertisementViewModel by activityViewModels<AdvertisementViewModel>()
    private val usrViewModel: UserProfileViewModel by activityViewModels<UserProfileViewModel>()

    private lateinit var advTitle: TextView
    private lateinit var advLocation: TextView
    private lateinit var advDate: TextView
    private lateinit var advDuration: TextView
    private lateinit var advDescription: TextView
    private lateinit var deleteButton: ImageView
    private lateinit var accountName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.advTitle = view.findViewById(R.id.newTitle)
        this.advLocation = view.findViewById(R.id.newLocation)
        this.advDate = view.findViewById(R.id.newDate)
        this.advDuration = view.findViewById(R.id.newDuration)
        this.advDescription = view.findViewById(R.id.newDescription)
        this.deleteButton = view.findViewById(R.id.deleteButton)

        usrViewModel.profile.observe(viewLifecycleOwner) { user ->
            accountName = user.fullName!!
        }

        advViewModel.advertisement.observe(viewLifecycleOwner) { singleAdvertisement ->
            this.advTitle.text = singleAdvertisement.adsTitle
            this.advLocation.text = singleAdvertisement.adsLocation
            this.advDate.text = singleAdvertisement.adsDate
            this.advDuration.text = singleAdvertisement.adsDuration.toString()
            this.advDescription.text = singleAdvertisement.adsDescription
            this.deleteButton.setOnClickListener {
                advViewModel.removeAd(singleAdvertisement.id!!)
                findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_ShowListTimeslots)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // save the data
                findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_showSingleTimeslot)
            }
        })
    }
}