package it.polito.madcourse.group06.views.timeslot

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.utilities.ServiceTools.AdvFilter
import it.polito.madcourse.group06.viewmodels.SharedViewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [FilterTimeslots.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterTimeslots : Fragment(R.layout.filter_timeslots) {
    private lateinit var cancel: Button
    private lateinit var reset: Button

    private lateinit var location: EditText
    private lateinit var fromDate: Chip
    private lateinit var fromTime: Chip
    private lateinit var toDate: Chip
    private lateinit var toTime: Chip
    private lateinit var durationSpinner: Spinner
    private lateinit var applyButton: Button

    private lateinit var sortTitleChip: Chip
    private lateinit var sortDurationChip: Chip
    private lateinit var sortStartingTimeChip: Chip
    private lateinit var sortStartingDateChip: Chip
    private lateinit var sortEndingTimeChip: Chip
    private lateinit var sortEndingDateChip: Chip

    private var titleChipState = 0
    private var durationChipState = 0
    private var startingTimeChipState = 0
    private var endingTimeChipState = 0
    private var startingDateChipState = 0
    private var endingDateChipState = 0

    private val sharedViewModel: SharedViewModel by activityViewModels()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.cancel = view.findViewById(R.id.cancel_button)
        this.reset = view.findViewById(R.id.reset_button)
        this.location = view.findViewById(R.id.location)
        this.fromDate = view.findViewById(R.id.add_starting_date_chip)
        this.fromTime = view.findViewById(R.id.starting_time)
        this.toDate = view.findViewById(R.id.add_ending_date_chip)
        this.toTime = view.findViewById(R.id.ending_time)
        this.durationSpinner = view.findViewById(R.id.duration_spinner)

        this.sortTitleChip = view.findViewById(R.id.sort_title_chip)
        this.sortDurationChip = view.findViewById(R.id.sort_duration_chip)
        this.sortStartingDateChip = view.findViewById(R.id.sort_starting_date_chip)
        this.sortStartingTimeChip = view.findViewById(R.id.sort_starting_time_chip)
        this.sortEndingDateChip = view.findViewById(R.id.sort_ending_date_chip)
        this.sortEndingTimeChip = view.findViewById(R.id.sort_ending_time_chip)

        this.applyButton = view.findViewById(R.id.apply_button)


        sharedViewModel.select(true)

        this.fromDate.setOnClickListener { popUpStartingDatePicker() }
        this.fromTime.setOnClickListener { popUpStartingTimePicker() }
        this.toDate.setOnClickListener { popUpEndingDatePicker() }
        this.toTime.setOnClickListener { popUpEndingTimePicker() }

        this.cancel.setOnClickListener { slideOutFragment(this) }

        this.reset.setOnClickListener {
            //reset filters
            this.location.text = null
            this.fromDate.text = "+"
            this.toDate.text = "+"
            this.fromTime.text = "+"
            this.toTime.text = "+"
            this.durationSpinner.setSelection(0)

            this.sortTitleChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
            this.sortDurationChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
            this.sortStartingDateChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
            this.sortStartingTimeChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
            this.sortEndingDateChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
            this.sortEndingTimeChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
            titleChipState = 0
            durationChipState = 0
            startingTimeChipState = 0
            endingTimeChipState = 0
            startingDateChipState = 0
            endingDateChipState = 0
        }

        this.applyButton.setOnClickListener {
            sharedViewModel.setFilter(
                AdvFilter(
                    location = location.text.toString(),
                    starting_time = if (fromDate.text == "+") null else fromDate.text.substring(11, 15),
                    ending_time = if (toDate.text == "+") null else fromDate.text.substring(11, 15),
                    duration = durationSpinner.selectedItem.toString().run {
                        if (this == "Any") null else this.toDouble()
                    },
                    starting_date = if (fromDate.text == "+") null else fromDate.text.substring(0, 9),
                    ending_date = if (toDate.text == "+") null else toDate.text.substring(0, 9),
                )
            )
            slideOutFragment(this)
        }

        this.sortTitleChip.setOnClickListener {
            titleChipState = (titleChipState + 1) % 3
            when (titleChipState) {
                0 -> this.sortTitleChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
                1 -> this.sortTitleChip.chipIcon = resources.getDrawable(R.drawable.sort_up, null)
                2 -> this.sortTitleChip.chipIcon = resources.getDrawable(R.drawable.sort_down, null)
            }
        }
        this.sortDurationChip.setOnClickListener {
            durationChipState = (durationChipState + 1) % 3
            when (durationChipState) {
                0 -> this.sortDurationChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
                1 -> this.sortDurationChip.chipIcon = resources.getDrawable(R.drawable.sort_up, null)
                2 -> this.sortDurationChip.chipIcon = resources.getDrawable(R.drawable.sort_down, null)
            }
        }
        this.sortStartingDateChip.setOnClickListener {
            startingDateChipState = (startingDateChipState + 1) % 3
            when (startingDateChipState) {
                0 -> this.sortStartingDateChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
                1 -> this.sortStartingDateChip.chipIcon = resources.getDrawable(R.drawable.sort_up, null)
                2 -> this.sortStartingDateChip.chipIcon = resources.getDrawable(R.drawable.sort_down, null)
            }
        }
        this.sortStartingTimeChip.setOnClickListener {
            startingTimeChipState = (startingTimeChipState + 1) % 3
            when (startingTimeChipState) {
                0 -> this.sortStartingTimeChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
                1 -> this.sortStartingTimeChip.chipIcon = resources.getDrawable(R.drawable.sort_up, null)
                2 -> this.sortStartingTimeChip.chipIcon = resources.getDrawable(R.drawable.sort_down, null)
            }
        }
        this.sortEndingDateChip.setOnClickListener {
            endingDateChipState = (endingDateChipState + 1) % 3
            when (endingDateChipState) {
                0 -> this.sortEndingDateChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
                1 -> this.sortEndingDateChip.chipIcon = resources.getDrawable(R.drawable.sort_up, null)
                2 -> this.sortEndingDateChip.chipIcon = resources.getDrawable(R.drawable.sort_down, null)
            }
        }
        this.sortEndingTimeChip.setOnClickListener {
            endingTimeChipState = (endingTimeChipState + 1) % 3
            when (endingTimeChipState) {
                0 -> this.sortEndingTimeChip.chipIcon = resources.getDrawable(R.drawable.sort_directions, null)
                1 -> this.sortEndingTimeChip.chipIcon = resources.getDrawable(R.drawable.sort_up, null)
                2 -> this.sortEndingTimeChip.chipIcon = resources.getDrawable(R.drawable.sort_down, null)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val frag = activity?.supportFragmentManager!!.findFragmentById(R.id.nav_host_fragment_content_main)
                view.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_down))

                sharedViewModel.select(false)

                activity?.supportFragmentManager?.beginTransaction()?.remove(frag!!)?.commit()
            }
        })

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.durations,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            durationSpinner.adapter = adapter
        }
    }

    private fun slideOutFragment(frag: Fragment) {
        view?.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_down))
        sharedViewModel.select(false)

        activity?.supportFragmentManager?.beginTransaction()?.remove(frag)?.commit()
    }

    /**
     * popUpStartingTimePicker is the callback to launch the TimePicker for inserting the starting time
     */
    private fun popUpStartingTimePicker() {

        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
            this.fromTime.text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
        }

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            onTimeSetListener, Calendar.getInstance().get(Calendar.HOUR),
            Calendar.getInstance().get(Calendar.MINUTE), true
        )

        timePickerDialog.setTitle("Select Starting Time")
        timePickerDialog.show()
    }

    /**
     * popUpEndingTimePicker is the callback to launch the TimePicker for inserting the ending time
     */
    private fun popUpEndingTimePicker() {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
            this.toTime.text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
        }

        var h = Calendar.getInstance().get(Calendar.HOUR)
        var m = Calendar.getInstance().get(Calendar.MINUTE)
        if (this.fromTime.text != "+") {
            h = this.fromTime.text.split(":")[0].toInt()
            m = this.fromTime.text.split(":")[1].toInt()
        }

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            onTimeSetListener, h, m, true
        )

        timePickerDialog.setTitle("Select Ending Time")
        timePickerDialog.show()
    }

    /**
     * popUpStartingDatePicker is the callback to launch the TimePicker for inserting the starting time
     */
    private fun popUpStartingDatePicker() {

        val onDateSetListener: DatePickerDialog.OnDateSetListener? = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            this.fromDate.text = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y)
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            onDateSetListener, Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setTitle("Select Starting Date")
        datePickerDialog.show()
    }

    /**
     * popUpEndingDatePicker is the callback to launch the TimePicker for inserting the starting time
     */
    private fun popUpEndingDatePicker() {

        val onDateSetListener: DatePickerDialog.OnDateSetListener? = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            this.toDate.text = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y)
        }

        var y = Calendar.getInstance().get(Calendar.YEAR)
        var m = Calendar.getInstance().get(Calendar.MONTH)
        var d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        if (this.fromDate.text != "+") {
            d = this.fromDate.text.split("/")[0].toInt()
            m = this.fromDate.text.split("/")[1].toInt() - 1
            y = this.fromDate.text.split("/")[2].toInt()
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            onDateSetListener, y, m, d
        )

        datePickerDialog.setTitle("Select Starting Date")
        datePickerDialog.show()
    }
}