package it.polito.group06.views

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.polito.group06.R
import it.polito.group06.models.time_slot_adv_database.Advertisement
import it.polito.group06.viewmodels.AdvertisementViewModel
import it.polito.group06.viewmodels.UserProfileViewModel
import android.widget.DatePicker;
import android.widget.TextView
import java.util.*

class NewSingleTimeslot : Fragment(R.layout.new_time_slot_details_fragment) {

    private val advViewModel by viewModels<AdvertisementViewModel>()
    private val usrViewModel by viewModels<UserProfileViewModel>()
    private lateinit var newTitle: EditText
    private lateinit var newLocation: EditText
    private lateinit var newDate: TextView
    private lateinit var newDuration: EditText
    private lateinit var newDescription: EditText
    private lateinit var closeButton: ImageView
    private lateinit var confirmButton: ImageView
    private lateinit var datePicker: DatePicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.newTitle = view.findViewById(R.id.newTitle)
        this.newLocation = view.findViewById(R.id.newLocation)
        this.newDate = view.findViewById(R.id.newDate)
        this.newDuration = view.findViewById(R.id.newDuration)
        this.newDescription = view.findViewById(R.id.newDescription)
        this.closeButton = view.findViewById(R.id.closeButton)
        this.confirmButton = view.findViewById(R.id.confirmButton)
        this.datePicker = view.findViewById(R.id.date_Picker)

        val today = Calendar.getInstance()
        var chosen_date = ""
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            chosen_date = "$day/${month + 1}/$year"
        }

        this.closeButton.setOnClickListener {
            findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
        }

        this.confirmButton.setOnClickListener {
            if (isAdvAvailable()) {
                advViewModel.insertAd(
                    Advertisement(
                        null,
                        newTitle.text.toString(),
                        newDescription.text.toString(),
                        newLocation.text.toString(),
                        chosen_date,
                        newDuration.text.toString().toFloat(),
                        "Guido Saracco",
                        false
                    )
                )
            }
            findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isAdvAvailable()) {
                    advViewModel.insertAd(
                        Advertisement(
                            null,
                            newTitle.text.toString(),
                            newDescription.text.toString(),
                            newLocation.text.toString(),
                            chosen_date,
                            newDuration.text.toString().toFloat(),
                            "Guido Saracco",
                            false
                        )
                    )
                }
                findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
            }
        })
    }

    /**
     * isAdvAvailable is a method which returns whether it's possible to actually insert a new
     * advertisement. The criteria is that an advertisement should at least have a title, a location,
     * a date and a duration.
     *
     * @return whether it's possible to actually create an advertisement or not
     */
    private fun isAdvAvailable(): Boolean {
        return !(newTitle.text.toString().isNullOrEmpty() ||
                newDuration.text.toString().isNullOrEmpty() ||
                newLocation.text.toString().isNullOrEmpty() ||
                newDate.text.toString().isNullOrEmpty())
    }
}
