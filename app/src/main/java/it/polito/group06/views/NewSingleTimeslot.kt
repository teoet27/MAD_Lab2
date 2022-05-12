package it.polito.group06.views

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import it.polito.group06.R
import it.polito.group06.models.advertisement.Advertisement
import it.polito.group06.viewmodels.AdvertisementViewModel
import it.polito.group06.viewmodels.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewSingleTimeslot : Fragment(R.layout.new_time_slot_details_fragment) {

    private val advViewModel by viewModels<AdvertisementViewModel>()
    private val usrViewModel by viewModels<UserProfileViewModel>()
    private lateinit var newTitle: EditText
    private lateinit var newLocation: EditText
    private lateinit var newDate: TextView
    private lateinit var newStartingTime: TextView
    private lateinit var newEndingTime: TextView
    private lateinit var newDescription: EditText
    private lateinit var closeButton: ImageView
    private lateinit var confirmButton: ImageView
    private lateinit var datePicker: DatePicker
    private lateinit var accountName: String
    private var timeStartingHour: Int = 0
    private var timeStartingMinute: Int = 0
    private var timeEndingHour: Int = 0
    private var timeEndingMinute: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.newTitle = view.findViewById(R.id.newTitle)
        this.newLocation = view.findViewById(R.id.newLocation)
        this.newDate = view.findViewById(R.id.newDate)
        this.newStartingTime = view.findViewById(R.id.newStartingTime)
        this.newEndingTime = view.findViewById(R.id.newEndingTime)
        this.newDescription = view.findViewById(R.id.newDescription)
        this.closeButton = view.findViewById(R.id.closeButton)
        this.confirmButton = view.findViewById(R.id.confirmButton)
        this.datePicker = view.findViewById(R.id.date_Picker)

        usrViewModel.profile.observe(viewLifecycleOwner) { user ->
            if (user == null || user.fullName == null)
                accountName = "Guido Saracco"
            else
                accountName = user.fullName!!
        }

        this.newStartingTime.setOnClickListener { popTimePickerStarting(this.newStartingTime) }
        this.newEndingTime.setOnClickListener { popTimePickerEnding(this.newEndingTime) }

        val today = Calendar.getInstance()
        var chosenDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            chosenDate = "$day/${month + 1}/$year"
        }

        this.closeButton.setOnClickListener {
            Snackbar.make(
                requireView(), "Creation canceled.", Snackbar.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
        }

        this.confirmButton.setOnClickListener {
            val (timeDifference, isTimeDifferenceOk) = computeTimeDifference(newStartingTime.text.toString(), newEndingTime.text.toString())
            if (areAllFieldsEmpty()) {
                Snackbar.make(
                    requireView(), "All fields are empty: fill them to create a new Advertisement.", Snackbar.LENGTH_LONG
                ).show()
            } else if (!isTimeDifferenceOk && timeDifference < 0) {
                Snackbar.make(
                    requireView(), "Error: starting and ending time must be not empty. Try again.", Snackbar.LENGTH_LONG
                ).show()
            } else if (!isTimeDifferenceOk) {
                Snackbar.make(
                    requireView(), "Error: the starting time must be before the ending time. Try again.", Snackbar.LENGTH_LONG
                ).show()
            } else if (isAdvValid()) {
                advViewModel.insertAd(
                    Advertisement(
                        null,
                        newTitle.text.toString(),
                        newDescription.text.toString(),
                        newLocation.text.toString(),
                        chosenDate,
                        newStartingTime.text.toString(),
                        newEndingTime.text.toString(),
                        timeDifference,
                        accountName,
                        false
                    )
                )
                Toast.makeText(
                    context, "Advertisement created successfully!", Toast.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
            } else {
                Snackbar.make(
                    requireView(), "Error: you need to provide at least a title, a starting and ending time, a location and a date. Try again.", Snackbar.LENGTH_LONG
                ).show()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val (timeDifference, isTimeDifferenceOk) = computeTimeDifference(newStartingTime.text.toString(), newEndingTime.text.toString())
                if (areAllFieldsEmpty()) {
                    Snackbar.make(
                        requireView(), "Creation canceled.", Snackbar.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
                } else if (!isTimeDifferenceOk && timeDifference < 0) {
                    Snackbar.make(
                        requireView(), "Error: starting and ending time must be not empty. Try again.", Snackbar.LENGTH_LONG
                    ).show()
                } else if (!isTimeDifferenceOk) {
                    Snackbar.make(
                        requireView(), "Error: the starting time must be before the ending time. Try again.", Snackbar.LENGTH_LONG
                    ).show()
                } else if (isAdvValid()) {
                    advViewModel.insertAd(
                        Advertisement(
                            null,
                            newTitle.text.toString(),
                            newDescription.text.toString(),
                            newLocation.text.toString(),
                            chosenDate,
                            newStartingTime.text.toString(),
                            newEndingTime.text.toString(),
                            timeDifference,
                            accountName,
                            false
                        )
                    )
                    Toast.makeText(
                        context, "Advertisement created successfully!", Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
                } else {
                    Snackbar.make(
                        requireView(), "Creation canceled.", Snackbar.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
                }
            }
        })
    }

    /**
     * isAdvValid is a method which returns whether it's possible to actually insert a new
     * advertisement. The criteria is that an advertisement should at least have a title, a location,
     * a date and a duration.
     *
     * @return whether it's possible to actually create an advertisement or not
     */
    private fun isAdvValid(): Boolean {
        return !(newTitle.text.toString().isNullOrEmpty() ||
                newStartingTime.text.toString().isNullOrEmpty() ||
                newEndingTime.text.toString().isNullOrEmpty() ||
                newLocation.text.toString().isNullOrEmpty() ||
                newDate.text.toString().isNullOrEmpty())
    }

    /**
     * popTimePickerStarting is the callback to launch the TimePicker for inserting the starting time
     * @param timeBox reference to the TextView of the starting time
     */
    private fun popTimePickerStarting(timeBox: TextView) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
            timeStartingHour = selectedHour
            timeStartingMinute = selectedMinute
            timeBox.text = String.format(Locale.getDefault(), "%02d:%02d", timeStartingHour, timeStartingMinute)
        }

        val timePickerDialog = TimePickerDialog(this.context, onTimeSetListener, timeStartingHour, timeStartingMinute, true)
        timePickerDialog.setTitle("Select time")
        timePickerDialog.show()
    }

    /**
     * popTimePickerEnding is the callback to launch the TimePicker for inserting the ending time
     * @param timeBox reference to the TextView of the ending time
     */
    private fun popTimePickerEnding(timeBox: TextView) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
            timeEndingHour = selectedHour
            timeEndingMinute = selectedMinute
            timeBox.text = String.format(Locale.getDefault(), "%02d:%02d", timeEndingHour, timeEndingMinute)
        }

        val timePickerDialog: TimePickerDialog = TimePickerDialog(this.context, onTimeSetListener, timeEndingHour, timeEndingMinute, true)
        timePickerDialog.setTitle("Select time")
        timePickerDialog.show()
    }

    /**
     * computeTimeDifference is a method which return the time difference from two "time-strings" and whether
     * they are acceptable or not.
     *
     * @param startingTime the starting time
     * @param endingTime the ending time
     * @return a Pair<Float, Boolean> where it's specified the time difference and its acceptability
     */
    private fun computeTimeDifference(startingTime: String, endingTime: String): Pair<Float, Boolean> {
        var timeDifference: Float = 0.00f
        if (startingTime.isNullOrEmpty() || endingTime.isNullOrEmpty()) {
            return Pair(-1f, false)
        }
        val startingHour = startingTime.split(":")[0].toInt()
        val startingMinute = startingTime.split(":")[1].toInt()
        val endingHour = endingTime.split(":")[0].toInt()
        val endingMinute = endingTime.split(":")[1].toInt()

        timeDifference += (endingHour - startingHour) + ((endingMinute - startingMinute) / 60f)

        return Pair(
            String.format("%.2f", timeDifference).toFloat(),
            String.format("%.2f", timeDifference).toFloat() >= 0
        )
    }

    /**
     * areAllFieldsEmpty to check whether all fields are empty or not
     * @return whether all fields are empty or not
     */
    private fun areAllFieldsEmpty(): Boolean {
        return this.newTitle.text.toString().isNullOrEmpty() &&
                this.newLocation.text.toString().isNullOrEmpty() &&
                this.newStartingTime.text.toString().isNullOrEmpty() &&
                this.newEndingTime.text.toString().isNullOrEmpty() &&
                this.newDescription.text.toString().isNullOrEmpty()
    }
}
