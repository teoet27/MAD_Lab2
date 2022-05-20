package it.polito.madcourse.group06.views.timeslot

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
class FilterTimeslots : Fragment(R.layout.fragment_filter_timeslots) {
    private lateinit var location :EditText
    private lateinit var fromDate: Chip
    private lateinit var fromTime: Chip
    private lateinit var toDate: Chip
    private lateinit var toTime: Chip
    private lateinit var durationSpinner :Spinner
    private lateinit var applyButton: Button
    private lateinit var cancel :Button
    private lateinit var reset :Button

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
        this.durationSpinner = view.findViewById(R.id.duration_spinner)
        this.applyButton=view.findViewById(R.id.apply_button)

        sharedViewModel.select(true)

        this.fromDate.setOnClickListener{ popUpStartingDatePicker() }
        this.fromTime.setOnClickListener{ popUpStartingTimePicker() }
        this.toDate.setOnClickListener{ popUpEndingDatePicker() }
        this.toTime.setOnClickListener{ popUpEndingTimePicker() }

        this.cancel.setOnClickListener{

            view.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.slide_out_down))
            sharedViewModel.select(false)

            activity?.supportFragmentManager?.
            beginTransaction()?.
            remove(this)?.
            commit()
        }

        this.reset.setOnClickListener{
            //reset filters
            this.location.text = null
            this.fromDate.text="+"
            this.toDate.text="+"
            this.fromTime.text="+"
            this.toTime.text="+"
            this.durationSpinner.setSelection(0)
        }

        this.applyButton.setOnClickListener{
            var advFilter=
                AdvFilter(location=location.text.toString(),
                    starting_time=if(fromDate.text=="+") null else fromDate.text.substring(11,15),
                    ending_time=if(toDate.text=="+") null else fromDate.text.substring(11,15),
                    duration=durationSpinner.selectedItem.toString().toDouble(),
                    starting_date=if(fromDate.text=="+") null else fromDate.text.substring(0,9),
                    ending_date=if(toDate.text=="+") null else toDate.text.substring(0,9),
                )
            //navigate back and put transmit advFilter
            findNavController().navigate(R.id.action_filterTimeslots_to_ShowListTimeslots,bundleOf("filter" to advFilter))
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.slide_out_down))
                if (activity?.supportFragmentManager?.backStackEntryCount!! > 0)
                    activity?.supportFragmentManager?.popBackStackImmediate()
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

    /**
     * popUpStartingTimePicker is the callback to launch the TimePicker for inserting the starting time
     */
    private fun popUpStartingTimePicker(){

        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
            this.fromTime.text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
        }

        val timePickerDialog = TimePickerDialog(requireContext(),
            onTimeSetListener, Calendar.getInstance().get(Calendar.HOUR),
            Calendar.getInstance().get(Calendar.MINUTE), true)

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

        var h=Calendar.getInstance().get(Calendar.HOUR)
        var m=Calendar.getInstance().get(Calendar.MINUTE)
        if (this.fromTime.text!="+"){
            h=this.fromTime.text.split(":")[0].toInt()
            m=this.fromTime.text.split(":")[1].toInt()
        }

        val timePickerDialog = TimePickerDialog(requireContext(),
            onTimeSetListener, h, m, true)

        timePickerDialog.setTitle("Select Ending Time")
        timePickerDialog.show()
    }

    /**
     * popUpStartingDatePicker is the callback to launch the TimePicker for inserting the starting time
     */
    private fun popUpStartingDatePicker(){

        val onDateSetListener: DatePickerDialog.OnDateSetListener? = DatePickerDialog.OnDateSetListener{ _, y, m, d->
            this.fromDate.text = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m+1,y)
        }

        val datePickerDialog = DatePickerDialog(requireContext(),
            onDateSetListener, Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        datePickerDialog.setTitle("Select Starting Date")
        datePickerDialog.show()
    }

    /**
     * popUpEndingDatePicker is the callback to launch the TimePicker for inserting the starting time
     */
    private fun popUpEndingDatePicker(){

        val onDateSetListener: DatePickerDialog.OnDateSetListener? = DatePickerDialog.OnDateSetListener{ _, y, m, d->
            this.toDate.text = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m+1,y)
        }

        var y=Calendar.getInstance().get(Calendar.YEAR)
        var m=Calendar.getInstance().get(Calendar.MONTH)
        var d=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        if (this.fromDate.text!="+"){
            d=this.fromDate.text.split("/")[0].toInt()
            m=this.fromDate.text.split("/")[1].toInt()-1
            y=this.fromDate.text.split("/")[2].toInt()
        }

        val datePickerDialog = DatePickerDialog(requireContext(),
            onDateSetListener, y,m,d)

        datePickerDialog.setTitle("Select Starting Date")
        datePickerDialog.show()
    }
}