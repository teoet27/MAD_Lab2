package it.polito.MAD.group06.views

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import it.polito.MAD.group06.R
import it.polito.MAD.group06.models.advertisement.Advertisement
import it.polito.MAD.group06.viewmodels.AdvertisementViewModel
import it.polito.MAD.group06.viewmodels.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditSingleTimeslot : Fragment(R.layout.edit_time_slot_details_fragment) {

    private val advViewModel: AdvertisementViewModel by activityViewModels()
    private val usrViewModel: UserProfileViewModel by activityViewModels()
    private val dumbAdvertisement: Advertisement = Advertisement(
        null, "", "",
        "", "", "", "", 0.0,
        "", -1
    )

    private lateinit var advTitle: TextView
    private lateinit var advLocation: TextView
    private lateinit var advStartingTime: TextView
    private lateinit var advEndingTime: TextView
    private lateinit var advDescription: TextView
    private lateinit var deleteButton: ImageView
    private lateinit var accountName: String
    private lateinit var datePicker: DatePicker
    private lateinit var chosenDate: String
    private var timeStartingHour: Int = 0
    private var timeStartingMinute: Int = 0
    private var timeEndingHour: Int = 0
    private var timeEndingMinute: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.advTitle = view.findViewById(R.id.newTitle)
        this.advLocation = view.findViewById(R.id.newLocation)
        this.advDescription = view.findViewById(R.id.newDescription)
        this.advStartingTime = view.findViewById(R.id.editStartingTime)
        this.advEndingTime = view.findViewById(R.id.editEndingTime)
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

            this.advStartingTime.text = singleAdvertisement.advStartingTime
            this.advEndingTime.text = singleAdvertisement.advEndingTime

            this.advStartingTime.setOnClickListener { popTimePickerStarting(this.advStartingTime) }
            this.advEndingTime.setOnClickListener { popTimePickerEnding(this.advEndingTime) }

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

            this.advDescription.text = singleAdvertisement.advDescription
            this.deleteButton.setOnClickListener {
                // TODO
                //advViewModel.removeAd(singleAdvertisement.id!!)
                Toast.makeText(
                    context, "Advertisement removed successfully!", Toast.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_ShowListTimeslots)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val (timeDifference, isTimeDifferenceOk) = computeTimeDifference(advStartingTime.text.toString(), advEndingTime.text.toString())
                if (!isTimeDifferenceOk && timeDifference < 0) {
                    Snackbar.make(
                        requireView(), "Error: starting and ending time must be not empty. Try again.", Snackbar.LENGTH_LONG
                    ).show()
                } else if (!isTimeDifferenceOk) {
                    Snackbar.make(
                        requireView(), "Error: the starting time must be before the ending time. Try again.", Snackbar.LENGTH_LONG
                    ).show()
                } else if (isAdvValid()) {
                    dumbAdvertisement.advTitle = advTitle.text.toString()
                    dumbAdvertisement.advLocation = advLocation.text.toString()
                    dumbAdvertisement.advDescription = advDescription.text.toString()
                    dumbAdvertisement.advDate = chosenDate
                    dumbAdvertisement.advStartingTime = advStartingTime.text.toString()
                    dumbAdvertisement.advEndingTime = advEndingTime.text.toString()
                    dumbAdvertisement.advDuration = timeDifference
                    advViewModel.editSingleAdvertisement(dumbAdvertisement)
                    Toast.makeText(
                        context, "Advertisement edited successfully!", Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_showSingleTimeslot)
                } else {
                    Snackbar.make(
                        requireView(), "Error: you need to provide at least a title, a starting and ending time, a location and a date. Try again.", Snackbar.LENGTH_LONG
                    ).show()
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
        return !(advTitle.text.toString().isNullOrEmpty() ||
                advStartingTime.text.toString().isNullOrEmpty() ||
                advEndingTime.text.toString().isNullOrEmpty() ||
                advLocation.text.toString().isNullOrEmpty() ||
                chosenDate.isNullOrEmpty())
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
    private fun computeTimeDifference(startingTime: String, endingTime: String): Pair<Double, Boolean> {
        var timeDifference: Double = 0.0
        if (startingTime.isNullOrEmpty() || endingTime.isNullOrEmpty()) {
            Snackbar.make(
                requireView(), "Error: starting and ending time must be not empty. Try again.", Snackbar.LENGTH_LONG
            ).show()
            return Pair(0.0, false)
        }
        val startingHour = startingTime.split(":")[0].toInt()
        val startingMinute = startingTime.split(":")[1].toInt()
        val endingHour = endingTime.split(":")[0].toInt()
        val endingMinute = endingTime.split(":")[1].toInt()

        timeDifference += (endingHour - startingHour) + ((endingMinute - startingMinute) / 60.0)

        return Pair(
            String.format("%.2f", timeDifference).toDouble(),
            String.format("%.2f", timeDifference).toDouble() >= 0
        )
    }
}