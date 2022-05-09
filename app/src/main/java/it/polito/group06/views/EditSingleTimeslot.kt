package it.polito.group06.views

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
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
import java.text.SimpleDateFormat
import java.util.*

class EditSingleTimeslot : Fragment(R.layout.edit_time_slot_details_fragment) {

    private val advViewModel: AdvertisementViewModel by activityViewModels()
    private val usrViewModel: UserProfileViewModel by activityViewModels()
    private val dumbAdvertisement: Advertisement = Advertisement(
        null, "", "",
        "", "", 0f,
        "", false
    )

    private lateinit var advTitle: TextView
    private lateinit var advLocation: TextView
    private lateinit var advDuration: TextView
    private lateinit var advDescription: TextView
    private lateinit var deleteButton: ImageView
    private lateinit var accountName: String
    private lateinit var datePicker: DatePicker
    private lateinit var chosenDate: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.advTitle = view.findViewById(R.id.newTitle)
        this.advLocation = view.findViewById(R.id.newLocation)
        this.advDuration = view.findViewById(R.id.newDuration)
        this.advDescription = view.findViewById(R.id.newDescription)
        this.deleteButton = view.findViewById(R.id.deleteButton)
        this.datePicker = view.findViewById(R.id.newDateDP)

        usrViewModel.profile.observe(viewLifecycleOwner) { user ->
            accountName = if (user?.fullName == null) "Guido Saracco" else user.fullName!!
        }

        advViewModel.advertisement.observe(viewLifecycleOwner) { singleAdvertisement ->
            dumbAdvertisement.id = singleAdvertisement.id
            dumbAdvertisement.advAccount = singleAdvertisement.advAccount
            this.advTitle.text = singleAdvertisement.advTitle
            this.advLocation.text = singleAdvertisement.advLocation

            val loadedDate = Calendar.getInstance()
            loadedDate.set(
                singleAdvertisement.advDate.split("/")[2].toInt(),
                singleAdvertisement.advDate.split("/")[1].toInt() - 1,
                singleAdvertisement.advDate.split("/")[0].toInt()
            )
            chosenDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            datePicker.init(
                loadedDate.get(Calendar.YEAR), loadedDate.get(Calendar.MONTH),
                loadedDate.get(Calendar.DAY_OF_MONTH)
            ) { view, year, month, day ->
                chosenDate = "$day/${month + 1}/$year"
            }

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
                dumbAdvertisement.advDate = chosenDate
                dumbAdvertisement.advDuration = advDuration.text.toString().toFloat()
                advViewModel.editSingleAdvertisement(dumbAdvertisement)
                findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_showSingleTimeslot)
            }
        })
    }
}