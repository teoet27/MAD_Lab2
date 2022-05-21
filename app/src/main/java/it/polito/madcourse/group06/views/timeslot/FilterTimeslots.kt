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
import it.polito.madcourse.group06.utilities.TimeslotTools.AdvFilter
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
    private lateinit var wholeWord:CheckBox



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
        this.maxDuration = view.findViewById(R.id.max_duration)
        this.minDuration = view.findViewById(R.id.min_duration)

        this.applyButton = view.findViewById(R.id.apply_button)
        this.wholeWord=view.findViewById(R.id.whole_word)


        sharedViewModel.select(true)

        this.fromDate.setOnClickListener { popUpStartingDatePicker() }
        this.fromTime.setOnClickListener { popUpStartingTimePicker() }
        this.toDate.setOnClickListener { popUpEndingDatePicker() }
        this.toTime.setOnClickListener { popUpEndingTimePicker() }
        this.minDuration.setOnClickListener{popUpMinDurationPicker()}
        this.maxDuration.setOnClickListener{popUpMaxDurationPicker()}

        this.cancel.setOnClickListener { slideOutFragment(this) }

        this.reset.setOnClickListener {
            //reset filters
            this.location.text = null
            this.fromDate.text = "+"
            this.toDate.text = "+"
            this.fromTime.text = "+"
            this.toTime.text = "+"
            this.minDuration.text="+"
            this.maxDuration.text="+"
        }

        this.applyButton.setOnClickListener {
            sharedViewModel.setFilter(
                AdvFilter(
                    location = location.text.toString(),
                    whole_word=wholeWord.isChecked,
                    starting_time = if (fromTime.text == "+") null else fromTime.text.toString(),
                    ending_time = if (toTime.text == "+") null else toTime.text.toString(),
                    min_duration = if (minDuration.text == "+") null else minDuration.text.toString(),
                    max_duration = if (maxDuration.text == "+") null else maxDuration.text.toString(),
                    starting_date = if (fromDate.text == "+") null else fromDate.text.toString(),
                    ending_date = if (toDate.text == "+") null else toDate.text.toString(),
                )
            )
            slideOutFragment(this)
        }


        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val frag = activity?.supportFragmentManager!!.findFragmentById(R.id.nav_host_fragment_content_main)
                view.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_down))

                sharedViewModel.select(false)

                activity?.supportFragmentManager?.beginTransaction()?.remove(frag!!)?.commit()
            }
        })
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
            onTimeSetListener, 0,0, true
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