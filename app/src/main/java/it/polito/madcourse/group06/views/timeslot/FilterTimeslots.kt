package it.polito.madcourse.group06.views.timeslot

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
import it.polito.madcourse.group06.utilities.AdvFilter
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
    private lateinit var minDuration: Chip
    private lateinit var maxDuration: Chip
    private lateinit var applyButton: Button
    private lateinit var wholeWord: CheckBox

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.cancel = view.findViewById(R.id.cancel_button)
        this.reset = view.findViewById(R.id.reset_button)
        this.location = view.findViewById(R.id.location)
        this.fromDate = view.findViewById(R.id.add_starting_date_chip)
        this.fromTime = view.findViewById(R.id.starting_time)
        this.toDate = view.findViewById(R.id.add_ending_date_chip)
        this.toTime = view.findViewById(R.id.ending_time)
        this.maxDuration = view.findViewById(R.id.max_duration)
        this.minDuration = view.findViewById(R.id.min_duration)

        this.applyButton = view.findViewById(R.id.apply_button)
        this.wholeWord = view.findViewById(R.id.whole_word)

        sharedViewModel.searchState.observe(viewLifecycleOwner) {

            this.location.setText(it.filter?.location ?: "")
            this.wholeWord.isChecked = it.filter?.whole_word ?: false
            this.fromTime.setText(if (it.filter?.starting_time.isNullOrEmpty()) "+" else it.filter?.starting_time)
            this.toTime.setText(if (it.filter?.ending_time.isNullOrEmpty()) "+" else it.filter?.ending_time)
            this.minDuration.setText(if (it.filter?.min_duration.isNullOrEmpty()) "+" else it.filter?.min_duration)
            this.maxDuration.setText(if (it.filter?.max_duration.isNullOrEmpty()) "+" else it.filter?.max_duration)
            this.fromDate.setText(if (it.filter?.starting_date.isNullOrEmpty()) "+" else it.filter?.starting_date)
            this.toDate.setText(if (it.filter?.ending_date.isNullOrEmpty()) "+" else it.filter?.ending_date)
        }


        this.fromDate.setOnClickListener { popUpStartingDatePicker() }
        this.fromTime.setOnClickListener { popUpStartingTimePicker() }
        this.toDate.setOnClickListener { popUpEndingDatePicker() }
        this.toTime.setOnClickListener { popUpEndingTimePicker() }
        this.minDuration.setOnClickListener { popUpMinDurationPicker() }
        this.maxDuration.setOnClickListener { popUpMaxDurationPicker() }

        this.cancel.setOnClickListener { slideOutFragment(this) }

        this.wholeWord.setOnClickListener {
            if (location.text.isEmpty())
                this.wholeWord.isChecked = false
        }

        this.reset.setOnClickListener {
            this.location.text = null
            this.wholeWord.isChecked = false
            this.fromDate.text = "+"
            this.toDate.text = "+"
            this.fromTime.text = "+"
            this.toTime.text = "+"
            this.minDuration.text = "+"
            this.maxDuration.text = "+"
        }

        this.applyButton.setOnClickListener {
            sharedViewModel.updateSearchState(filter=
                AdvFilter(
                    location = location.text.toString(),
                    whole_word = wholeWord.isChecked && this.location.text.toString().isNotEmpty(),
                    starting_time = if (fromTime.text == "+") null else fromTime.text.toString(),
                    ending_time = if (toTime.text == "+") null else toTime.text.toString(),
                    min_duration = if (minDuration.text == "+") null else minDuration.text.toString(),
                    max_duration = if (maxDuration.text == "+") null else maxDuration.text.toString(),
                    starting_date = if (fromDate.text == "+") null else fromDate.text.toString(),
                    ending_date = if (toDate.text == "+") null else toDate.text.toString(),
                )
            )
            slideOutFragment(this, true)
        }


        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val frag = activity?.supportFragmentManager!!.findFragmentById(R.id.nav_host_fragment_content_main)
                view.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_down))
                activity?.supportFragmentManager?.beginTransaction()?.remove(frag!!)?.commit()
            }
        })
    }

    private fun slideOutFragment(frag: Fragment, filterSet: Boolean = false) {
        view?.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_down))
        if (!filterSet)
            sharedViewModel.updateSearchState()

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
            onTimeSetListener, Calendar.getInstance().get(Calendar.HOUR)+12*Calendar.getInstance().get(Calendar.AM_PM),
            Calendar.getInstance().get(Calendar.MINUTE), true
        )

        timePickerDialog.setTitle("Starting Time")
        timePickerDialog.show()
    }

    /**
     * popUpEndingTimePicker is the callback to launch the TimePicker for inserting the ending time
     */
    private fun popUpEndingTimePicker() {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
            this.toTime.text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
        }

        var pm = Calendar.getInstance().get(Calendar.AM_PM)
        var h = Calendar.getInstance().get(Calendar.HOUR)
        var m = Calendar.getInstance().get(Calendar.MINUTE)
        if (this.fromTime.text != "+") {
            h = this.fromTime.text.split(":")[0].toInt()
            m = this.fromTime.text.split(":")[1].toInt()
            pm=0
        }

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            onTimeSetListener, h+12*pm, m, true
        )

        timePickerDialog.setTitle("Ending Time")
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

        datePickerDialog.setTitle("Starting Date")
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

        datePickerDialog.setTitle("Ending Date")
        datePickerDialog.show()
    }

    /**
     * popUpMinDurationPicker is the callback to launch the TimePicker for inserting the minimum duration
     */
    private fun popUpMinDurationPicker() {

        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
            this.minDuration.text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
        }

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            onTimeSetListener, 0, 0, true
        )

        timePickerDialog.setTitle("Min Duration")
        timePickerDialog.show()
    }

    /**
     * popUpMaxDurationPicker is the callback to launch the TimePicker for inserting the maximum duration
     */
    private fun popUpMaxDurationPicker() {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
            this.maxDuration.text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
        }

        var h = 0
        var m = 0
        if (this.minDuration.text != "+") {
            h = this.minDuration.text.split(":")[0].toInt()
            m = this.minDuration.text.split(":")[1].toInt()
        }

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            onTimeSetListener, h, m, true
        )

        timePickerDialog.setTitle("Max Duration")
        timePickerDialog.show()
    }

}