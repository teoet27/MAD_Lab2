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
import it.polito.group06.models.time_slot_adv_database.Advertisement
import it.polito.group06.viewmodels.AdvertisementViewModel
import it.polito.group06.viewmodels.UserProfileViewModel

class EditSingleTimeslot : Fragment(R.layout.edit_time_slot_details_fragment) {

    private val advViewModel: AdvertisementViewModel by activityViewModels<AdvertisementViewModel>()
    private val usrViewModel: UserProfileViewModel by activityViewModels<UserProfileViewModel>()
    private val dumbAdvertisement: Advertisement = Advertisement(
        null, "", "",
        "", "", 0f,
        "", false
    )

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
            if (user == null || user.fullName == null)
                accountName = "Guido Saracco"
            else
                accountName = user.fullName!!
        }

        advViewModel.advertisement.observe(viewLifecycleOwner) { singleAdvertisement ->
            dumbAdvertisement.id = singleAdvertisement.id
            dumbAdvertisement.advAccount = singleAdvertisement.advAccount
            this.advTitle.text = singleAdvertisement.advTitle
            this.advLocation.text = singleAdvertisement.advLocation
            this.advDate.text = singleAdvertisement.advDate
            this.advDuration.text = singleAdvertisement.advDuration.toString()
            this.advDescription.text = singleAdvertisement.advDescription
            this.deleteButton.setOnClickListener {
                advViewModel.removeAd(singleAdvertisement.id!!)
                findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_ShowListTimeslots)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dumbAdvertisement.advTitle = advTitle.text.toString()
                dumbAdvertisement.advLocation = advLocation.text.toString()
                dumbAdvertisement.advDescription = advDescription.text.toString()
                dumbAdvertisement.advDate = advDate.text.toString()
                dumbAdvertisement.advDuration = advDuration.text.toString().toFloat()
                advViewModel.editSingleAdvertisement(dumbAdvertisement)
                findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_showSingleTimeslot)
            }
        })
    }
}