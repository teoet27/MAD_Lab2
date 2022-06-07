package it.polito.madcourse.group06.views.timeslot

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.advertisement.Advertisement
import it.polito.madcourse.group06.utilities.checkTimeslotForm
import it.polito.madcourse.group06.utilities.timeStringToDoubleHour
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.math.roundToInt

class NewSingleTimeslot : Fragment(R.layout.new_time_slot_details_fragment) {

    private val advertisementViewModel by activityViewModels<AdvertisementViewModel>()
    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    private lateinit var newTitle: EditText
    private lateinit var newLocation: EditText
    private lateinit var newDate: TextView
    private lateinit var newStartingTime: TextView
    private lateinit var newEndingTime: TextView
    private lateinit var newDuration: TextView
    private lateinit var newDescription: EditText
    private lateinit var newRestrictions: EditText
    private lateinit var closeButton: Button
    private lateinit var confirmButton: Button
    private lateinit var datePicker: DatePicker
    private lateinit var accountName: String
    private var accountID: String = ""
    private lateinit var skillsChipGroup: ChipGroup
    private lateinit var addNewSkillChip: Chip
    private var newSkillTitleLabel: String = ""
    private lateinit var skillList: ArrayList<String>
    private val selectedSkillsList: ArrayList<String> = arrayListOf()
    private var timeDuration: Double? = null
    private val now = (SimpleDateFormat("HH:mm",Locale.getDefault()).format(Date())).split(":")
    private var timeStartingHour: Int = now[0].toInt()
    private var timeStartingMinute: Int = now[1].toInt()
    private var timeEndingHour: Int = now[0].toInt()
    private var timeEndingMinute: Int = now[1].toInt()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.newTitle = view.findViewById(R.id.newTitle)
        this.newLocation = view.findViewById(R.id.newLocation)
        this.newDate = view.findViewById(R.id.newDate)
        this.newStartingTime = view.findViewById(R.id.newStartingTime)
        this.newEndingTime = view.findViewById(R.id.newEndingTime)
        this.newDuration = view.findViewById(R.id.newDuration)
        this.newDescription = view.findViewById(R.id.newDescription)
        this.newRestrictions = view.findViewById(R.id.newRestrictions)
        this.closeButton = view.findViewById(R.id.closeButton)
        this.confirmButton = view.findViewById(R.id.confirmButton)
        this.datePicker = view.findViewById(R.id.newDatePicker)
        this.skillsChipGroup = view.findViewById(R.id.newSkillChipGroup)
        this.addNewSkillChip = view.findViewById(R.id.add_new_skill_chip)

        userProfileViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            accountName = user.fullName!!
            accountID = user.id!!
            skillList = user.skills!!
            for (skill in skillList) {
                this.skillsChipGroup.addChip(requireContext(), skill)
                this.skillsChipGroup.moveAddChip(
                    requireContext(),
                    view.findViewById(R.id.add_new_skill_chip)!!,
                    this.skillsChipGroup
                )
            }
        }

        this.newStartingTime.setOnClickListener { popTimePickerStarting(this.newStartingTime) }
        this.newEndingTime.setOnClickListener { popTimePickerEnding(this.newEndingTime) }
        this.newDuration.setOnClickListener { popTimePickerDuration(this.newDuration) }

        val today = Calendar.getInstance()
        var chosenDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            chosenDate = "$day/${month + 1}/$year"
        }

        this.addNewSkillChip.setOnClickListener {
            showNewSkillInputWindow(requireContext(), this.skillsChipGroup)
        }

        this.closeButton.setOnClickListener {
            Snackbar.make(
                requireView(), "Creation canceled.", Snackbar.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
        }

        this.confirmButton.setOnClickListener {
            advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) {
                var isPossible=true
                val tmpList = it.filter { it.accountID == accountID }
                for (adv in tmpList) {
                    if (adv.advDate != chosenDate) {
                        continue
                    }
                    val newSTH = newStartingTime.text.toString().convertStringToArrayOfTime()[0]
                    val newSTM = newStartingTime.text.toString().convertStringToArrayOfTime()[1]
                    val staticSTH = adv.advStartingTime.convertStringToArrayOfTime()[0]
                    val staticSTM = adv.advStartingTime.convertStringToArrayOfTime()[1]
                    val newETH = newEndingTime.text.toString().convertStringToArrayOfTime()[0]
                    val newETM = newEndingTime.text.toString().convertStringToArrayOfTime()[1]
                    val staticETH = adv.advEndingTime.convertStringToArrayOfTime()[0]
                    val staticETM = adv.advEndingTime.convertStringToArrayOfTime()[1]

                    if (newETH * 60 + newETM >= staticSTH * 60 + staticSTM && newSTH * 60 + newSTM <= staticSTH * 60 + staticSTM) {
                        isPossible = false
                        Snackbar.make(
                            requireView(),
                            "Error: you have already offered this timeslot; change your starting and/or ending time.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else if (newSTH * 60 + newSTM >= staticSTH * 60 + staticSTM && newETH * 60 + newETM <= staticETH * 60 + staticETM) {
                        isPossible = false
                        Snackbar.make(
                            requireView(),
                            "Error: you have already offered this timeslot; change your starting and/or ending time.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else if (newSTH * 60 + newSTM <= staticETH * 60 + staticETM && newETH * 60 + newETM >= staticETH * 60 + staticETM) {
                        isPossible = false
                        Snackbar.make(
                            requireView(),
                            "Error: you have already offered this timeslot; change your starting and/or ending time.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                if (isPossible &&
                        checkTimeslotForm(
                            requireView(),
                            newTitle.text.toString(),
                            newDescription.text.toString(),
                            newLocation.text.toString(),
                            newStartingTime.text.toString(),
                            newEndingTime.text.toString(),
                            timeStringToDoubleHour(newDuration.text.toString(),"HH h mm min"),
                            chosenDate
                            )
                    ) {
                        advertisementViewModel.insertAdvertisement(
                            Advertisement(
                                "",
                                newTitle.text.toString(),
                                newDescription.text.toString(),
                                newRestrictions.text.toString(),
                                selectedSkillsList,
                                newLocation.text.toString(),
                                chosenDate,
                                newStartingTime.text.toString(),
                                newEndingTime.text.toString(),
                                timeStringToDoubleHour(newDuration.text.toString(),"HH h mm min"),
                                accountName,
                                accountID,
                                null,
                                null,
                                null,
                                0.0,
                                ""
                            )
                        )
                        userProfileViewModel.updateSkillList(skillList)
                        Toast.makeText(
                            context, "Advertisement created successfully!", Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_newTimeSlotDetailsFragment_to_ShowListTimeslots)
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmButton.performClick()
                }
            })
    }

    /**
     * addChip
     * @param
     * @param
     */
    private fun ChipGroup.addChip(
        context: Context,
        skill: String,
        isAlreadySelected: Boolean = false
    ) {
        Chip(context).apply {
            id = View.generateViewId()
            text = skill
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = true
            isFocusable = true
            isChecked = isAlreadySelected

            if (isAlreadySelected) {
                selectedSkillsList.add(skill)
                setTextColor(ContextCompat.getColor(context, R.color.white))
                chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.prussian_blue))
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.black))
                chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGray))
            }

            setOnClickListener {
                if (selectedSkillsList.contains(skill)) {
                    selectedSkillsList.remove(skill)
                } else {
                    selectedSkillsList.add(skill)
                }
                if (isChecked) {
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.prussian_blue
                        )
                    )
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    chipBackgroundColor =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGray))
                }

            }
            addView(this)
        }
    }

    private fun computeDateDifference(
        startingDate: String,
        endingDate: String
    ): Pair<Double, Boolean> {
        var dateDifference: Double = 0.0
        if (startingDate.isNullOrEmpty() || endingDate.isNullOrEmpty()) {
            return Pair(-1.0, false)
        }
        dateDifference = (dateStringToInt(endingDate) - dateStringToInt(startingDate)).toDouble()
        return Pair(
            (dateDifference * 100.0).roundToInt() / 100.0,
            (dateDifference * 100.0).roundToInt() / 100.0 >= 0
        )
    }

    private fun dateStringToInt(date: String): Int {
        var dateInt = 0
        date.split("/").forEachIndexed { index, s ->
            when (index) {
                0 -> dateInt += s.toInt() //day
                1 -> dateInt += (31 - 3 * (s.toInt() == 2).toInt() - (listOf(
                    4,
                    6,
                    9,
                    11
                ).contains(s.toInt())).toInt()) * s.toInt() //month
                2 -> dateInt += (if (s.toInt() % 400 == 0) 366 else 365) * s.toInt() //year
            }
        }
        return dateInt
    }

    private fun ChipGroup.moveAddChip(context: Context, oldAddChip: View, chipGroup: ChipGroup) {
        removeView(oldAddChip)
        Chip(context).apply {
            id = R.id.add_new_skill_chip
            text = "+"
            isClickable = true
            isCheckable = false
            isCheckedIconVisible = false
            isFocusable = false
            setOnClickListener {
                showNewSkillInputWindow(requireContext(), chipGroup)
            }
            addView(this)
        }
    }

    private fun String.convertStringToArrayOfTime(): Array<Int> {
        val out = Array<Int>(2) { i -> i }
        out[0] = this.split(":")[0].toInt()
        out[1] = this.split(":")[1].toInt()
        return out
    }

    /**
     * showNewSkillInputWindow
     *
     * @param context current context
     * @param chipGroup the related chip group in which the new skill will be added
     */
    private fun showNewSkillInputWindow(context: Context, chipGroup: ChipGroup) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        val newSkillTitle = EditText(this.context)
        val linearLayout = LinearLayout(this.context)

        builder.setTitle("Insert here your new skill")
        newSkillTitle.hint = "What is your new skill?"
        newSkillTitle.inputType = InputType.TYPE_CLASS_TEXT
        newSkillTitle.gravity = Gravity.LEFT
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(64, 0, 64, 0)
        linearLayout.addView(newSkillTitle)
        builder.setView(linearLayout)

        /**
         * setPositiveButton
         */
        builder.setPositiveButton("Create", DialogInterface.OnClickListener { dialog, which ->
            newSkillTitleLabel = newSkillTitle.text.toString().replaceFirstChar(Char::titlecase)
            if (newSkillTitleLabel.isNotEmpty()) {
                chipGroup.addChip(context, newSkillTitleLabel, isAlreadySelected = true)
                chipGroup.moveAddChip(
                    context,
                    view?.findViewById(R.id.add_new_skill_chip)!!,
                    chipGroup
                )
                skillList.add(newSkillTitleLabel)
                Snackbar.make(
                    requireView(), "New skill added!", Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    requireView(),
                    "You must provide a name for the new skill.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        /**
         * setNegativeButton
         */
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        builder.show()
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
                newDuration.text.toString().isNullOrEmpty() ||
                newLocation.text.toString().isNullOrEmpty() ||
                newDate.text.toString().isNullOrEmpty())
    }

    /**
     * popTimePickerStarting is the callback to launch the TimePicker for inserting the starting time
     *
     * @param timeBox reference to the TextView of the starting time
     */
    private fun popTimePickerStarting(timeBox: TextView) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
                timeStartingHour = selectedHour
                timeStartingMinute = selectedMinute
                timeBox.text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    timeStartingHour,
                    timeStartingMinute
                )
            }

        val timePickerDialog = TimePickerDialog(
            this.context,
            onTimeSetListener,
            timeStartingHour,
            timeStartingMinute,
            true
        )
        timePickerDialog.setTitle("Select time")
        timePickerDialog.show()
    }

    /**
     * popTimePickerEnding is the callback to launch the TimePicker for inserting the ending time
     *
     * @param timeBox reference to the TextView of the ending time
     */
    private fun popTimePickerEnding(timeBox: TextView) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener() { timepicker, selectedHour, selectedMinute ->
                timeEndingHour = selectedHour
                timeEndingMinute = selectedMinute
                timeBox.text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    timeEndingHour,
                    timeEndingMinute
                )
            }

        val timePickerDialog: TimePickerDialog = TimePickerDialog(
            this.context,
            onTimeSetListener,
            timeStartingHour,
            timeStartingMinute,
            true
        )
        timePickerDialog.setTitle("Select time")
        timePickerDialog.show()
    }

    /**
     * popTimePickerDuration is the callback to launch the TimePicker for inserting the duration
     *
     * @param timeBox reference to the TextView of the duration
     */
    private fun popTimePickerDuration(timeBox: TextView) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener() { timepicker,
                                                   selectedHour,
                                                   selectedMinute ->
                computeTimeDifference(
                    newStartingTime.text.toString(),
                    newEndingTime.text.toString()
                ).first.also { maxDuration ->
                    if ((selectedHour.toDouble() + selectedMinute.toDouble() / 60) > maxDuration
                        && maxDuration > 0) {
                        Toast.makeText(
                            context, "Your service duration cannot exceed your availability time!", Toast.LENGTH_LONG
                        ).show()
                        timeDuration = maxDuration
                    }
                    else {
                        timeDuration = (selectedHour.toDouble() + selectedMinute.toDouble() / 60)
                    }
                }
                timeBox.text = String.format(
                    Locale.getDefault(),
                    "%d h %d min",
                    floor(timeDuration!!).toInt(),
                    ((timeDuration!! - floor(timeDuration!!)) * 60).toInt()
                )
            }
        val timePickerDialog: TimePickerDialog = TimePickerDialog(
            this.context, onTimeSetListener,
            0, 0, true
        )
        timePickerDialog.setTitle("Select Duration")
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
    private fun computeTimeDifference(
        startingTime: String,
        endingTime: String
    ): Pair<Double, Boolean> {
        var timeDifference: Double = 0.0
        if (startingTime.isNullOrEmpty() || endingTime.isNullOrEmpty()) {
            return Pair(-1.0, false)
        }
        val startingHour = startingTime.split(":")[0].toInt()
        val startingMinute = startingTime.split(":")[1].toInt()
        val endingHour = endingTime.split(":")[0].toInt()
        val endingMinute = endingTime.split(":")[1].toInt()

        timeDifference += (endingHour - startingHour) + ((endingMinute - startingMinute) / 60.0)

        return Pair(
            (timeDifference * 100.0).roundToInt() / 100.0,
            (timeDifference * 100.0).roundToInt() / 100.0 >= 0
        )
    }

    private fun Boolean.toInt() = if (this) 1 else 0
}

