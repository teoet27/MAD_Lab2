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
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class EditSingleTimeslot : Fragment(R.layout.edit_time_slot_details_fragment) {

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val dumbAdvertisement: Advertisement = Advertisement(
        "", "", "", arrayListOf<String>(),
        "", "", "", "", 0.0,
        "", ""
    )

    private lateinit var advTitle: TextView
    private lateinit var advLocation: TextView
    private lateinit var advStartingTime: TextView
    private lateinit var advEndingTime: TextView
    private lateinit var advDescription: TextView
    private lateinit var deleteButton: ImageView
    private lateinit var datePicker: DatePicker
    private lateinit var chosenDate: String
    private lateinit var skillsChips: ChipGroup
    private lateinit var newSkillChip: Chip
    private lateinit var accountID: String
    private lateinit var confirmButton: Button
    private lateinit var discardButton: Button
    private var timeStartingHour: Int = 0
    private var timeStartingMinute: Int = 0
    private var timeEndingHour: Int = 0
    private var timeEndingMinute: Int = 0
    private var selectedSkillsList: ArrayList<String> = arrayListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.advTitle = view.findViewById(R.id.editTitle)
        this.advLocation = view.findViewById(R.id.editLocation)
        this.advDescription = view.findViewById(R.id.editDescription)
        this.advStartingTime = view.findViewById(R.id.editStartingTime)
        this.advEndingTime = view.findViewById(R.id.editEndingTime)
        this.deleteButton = view.findViewById(R.id.deleteButton)
        this.datePicker = view.findViewById(R.id.editDatePicker)
        this.skillsChips = view.findViewById(R.id.editTimeslotSkillChipGroup)
        this.newSkillChip = view.findViewById(R.id.editTimeslotAddSkillChip)
        this.confirmButton = view.findViewById(R.id.confirmButtonID)
        this.discardButton = view.findViewById(R.id.discardButtonID)

        advertisementViewModel.advertisement.observe(viewLifecycleOwner) { singleAdvertisement ->
            // A dumb advertisement which will be filled with the newest information
            dumbAdvertisement.id = singleAdvertisement.id
            dumbAdvertisement.advAccount = singleAdvertisement.advAccount
            dumbAdvertisement.accountID = singleAdvertisement.accountID
            selectedSkillsList = singleAdvertisement.listOfSkills
            accountID = dumbAdvertisement.accountID

            // Title
            this.advTitle.text = singleAdvertisement.advTitle

            // Location
            this.advLocation.text = singleAdvertisement.advLocation

            // Starting Time
            this.advStartingTime.text = singleAdvertisement.advStartingTime
            this.advStartingTime.setOnClickListener { popTimePickerStarting(this.advStartingTime) }

            // Ending Time
            this.advEndingTime.text = singleAdvertisement.advEndingTime
            this.advEndingTime.setOnClickListener { popTimePickerEnding(this.advEndingTime) }

            // Date
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

            // Description
            this.advDescription.text = singleAdvertisement.advDescription

            // Delete Button
            this.deleteButton.setOnClickListener {
                advertisementViewModel.removeAdvertisementByID(singleAdvertisement.id!!)
                findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_ShowListTimeslots)
            }

            // Button for adding a new skill
            this.newSkillChip.setOnClickListener {
                showNewSkillInputWindow(requireContext(), this.skillsChips)
            }

            // Skills
            userProfileViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if (!user.skills.isNullOrEmpty()) {
                    this.skillsChips.removeAllViews()
                    user.skills?.forEach { skill ->
                        this.skillsChips.addChipWithCheck(requireContext(), skill, selectedSkillsList.contains(skill))
                    }
                    this.skillsChips.addPlusChip(requireContext(), this.skillsChips)
                }
            }

            this.discardButton.setOnClickListener {
                findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_showSingleTimeslot)
            }

            this.confirmButton.setOnClickListener {
                val (timeDifference, isTimeDifferenceOk) = computeTimeDifference(advStartingTime.text.toString(), advEndingTime.text.toString())
                advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfTimeslots ->
                    var isPossible = true
                    val tmpList = listOfTimeslots.filter { it.accountID == accountID }
                    for (adv in tmpList) {
                        if(adv.id == dumbAdvertisement.id){
                            continue
                        }
                        if (adv.advDate != chosenDate) {
                            continue
                        }
                        val newSTH = advStartingTime.text.toString().convertStringToArrayOfTime()[0]
                        val newSTM = advStartingTime.text.toString().convertStringToArrayOfTime()[1]
                        val staticSTH = adv.advStartingTime.convertStringToArrayOfTime()[0]
                        val staticSTM = adv.advStartingTime.convertStringToArrayOfTime()[1]
                        val newETH = advEndingTime.text.toString().convertStringToArrayOfTime()[0]
                        val newETM = advEndingTime.text.toString().convertStringToArrayOfTime()[1]
                        val staticETH = adv.advEndingTime.convertStringToArrayOfTime()[0]
                        val staticETM = adv.advEndingTime.convertStringToArrayOfTime()[1]

                        if (newETH * 60 + newETM >= staticSTH * 60 + staticSTM && newSTH * 60 + newSTM <= staticSTH * 60 + staticSTM) {
                            isPossible = false
                            Snackbar.make(
                                requireView(), "Error: you have already offered this timeslot; change your starting and/or ending time.", Snackbar.LENGTH_LONG
                            ).show()
                        } else if (newSTH * 60 + newSTM >= staticSTH * 60 + staticSTM && newETH * 60 + newETM <= staticETH * 60 + staticETM) {
                            isPossible = false
                            Snackbar.make(
                                requireView(), "Error: you have already offered this timeslot; change your starting and/or ending time.", Snackbar.LENGTH_LONG
                            ).show()
                        } else if (newSTH * 60 + newSTM <= staticETH * 60 + staticETM && newETH * 60 + newETM >= staticETH * 60 + staticETM) {
                            isPossible = false
                            Snackbar.make(
                                requireView(), "Error: you have already offered this timeslot; change your starting and/or ending time.", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    if(isPossible) {
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
                            dumbAdvertisement.listOfSkills = selectedSkillsList
                            advertisementViewModel.editAdvertisement(dumbAdvertisement)
                            findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_showSingleTimeslot)
                        } else {
                            Snackbar.make(
                                requireView(), "Error: you need to provide at least a title, a starting and ending time, a skill, a location and a date. Try again.", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val (timeDifference, isTimeDifferenceOk) = computeTimeDifference(advStartingTime.text.toString(), advEndingTime.text.toString())
                advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfTimeslots ->
                    var isPossible = true
                    val tmpList = listOfTimeslots.filter { it.accountID == accountID }
                    for (adv in tmpList) {
                        if (adv.advDate != chosenDate) {
                            continue
                        }
                        val newSTH = advStartingTime.text.toString().convertStringToArrayOfTime()[0]
                        val newSTM = advStartingTime.text.toString().convertStringToArrayOfTime()[1]
                        val staticSTH = adv.advStartingTime.convertStringToArrayOfTime()[0]
                        val staticSTM = adv.advStartingTime.convertStringToArrayOfTime()[1]
                        val newETH = advEndingTime.text.toString().convertStringToArrayOfTime()[0]
                        val newETM = advEndingTime.text.toString().convertStringToArrayOfTime()[1]
                        val staticETH = adv.advEndingTime.convertStringToArrayOfTime()[0]
                        val staticETM = adv.advEndingTime.convertStringToArrayOfTime()[1]

                        if (newETH * 60 + newETM >= staticSTH * 60 + staticSTM && newSTH * 60 + newSTM <= staticSTH * 60 + staticSTM) {
                            isPossible = false
                            Snackbar.make(
                                requireView(), "Error: you have already offered this timeslot; change your starting and/or ending time.", Snackbar.LENGTH_LONG
                            ).show()
                        } else if (newSTH * 60 + newSTM >= staticSTH * 60 + staticSTM && newETH * 60 + newETM <= staticETH * 60 + staticETM) {
                            isPossible = false
                            Snackbar.make(
                                requireView(), "Error: you have already offered this timeslot; change your starting and/or ending time.", Snackbar.LENGTH_LONG
                            ).show()
                        } else if (newSTH * 60 + newSTM <= staticETH * 60 + staticETM && newETH * 60 + newETM >= staticETH * 60 + staticETM) {
                            isPossible = false
                            Snackbar.make(
                                requireView(), "Error: you have already offered this timeslot; change your starting and/or ending time.", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    if(isPossible) {
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
                            dumbAdvertisement.listOfSkills = selectedSkillsList
                            advertisementViewModel.editAdvertisement(dumbAdvertisement)
                            findNavController().navigate(R.id.action_editTimeSlotDetailsFragment_to_showSingleTimeslot)
                        } else {
                            Snackbar.make(
                                requireView(), "Error: you need to provide at least a title, a starting and ending time, a skill, a location and a date. Try again.", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        })
    }

    private fun String.convertStringToArrayOfTime(): Array<Int> {
        val out = Array<Int>(2) { i -> i }
        out[0] = this.split(":")[0].toInt()
        out[1] = this.split(":")[1].toInt()
        return out
    }

    /**
     * Dinamically create a chip within a chip group
     *
     * @param context       parent view context
     * @param label         chip name
     */
    private fun ChipGroup.addChipWithCheck(context: Context, skill: String, isAlreadySelected: Boolean = false) {
        Chip(context).apply {
            id = View.generateViewId()
            text = skill
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = true
            isFocusable = true
            isChecked = isAlreadySelected

            if (isAlreadySelected) {
                setTextColor(ContextCompat.getColor(context, R.color.white))
                chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.prussian_blue))
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.black))
                chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGray))
            }

            setOnClickListener {
                if (selectedSkillsList.contains(skill)) {
                    selectedSkillsList.remove(skill)
                } else {
                    selectedSkillsList.add(skill)
                }
                if (isChecked) {
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.prussian_blue))
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGray))
                }

            }
            addView(this)
        }
    }

    private fun ChipGroup.addPlusChip(context: Context, chipGroup: ChipGroup) {
        Chip(context).apply {
            id = R.id.editProfileAddNewSkillChip
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
            val newSkillTitleLabel = newSkillTitle.text.toString()
            if (newSkillTitleLabel.isNotEmpty()) {
                this.skillsChips.removeView(view?.findViewById(R.id.editProfileAddNewSkillChip))
                chipGroup.addChipWithCheck(context, newSkillTitleLabel, true)
                this.skillsChips.addPlusChip(context, this.skillsChips)

                val user = userProfileViewModel.currentUser.value!!
                user.skills?.add(newSkillTitleLabel)
                userProfileViewModel.editUserProfile(user)

                Snackbar.make(
                    requireView(), "New skill added!", Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    requireView(), "You must provide a name for the new skill.", Snackbar.LENGTH_LONG
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
     * a skill, a date and a duration.
     *
     * @return whether it's possible to actually create an advertisement or not
     */
    private fun isAdvValid(): Boolean {
        return !(advTitle.text.toString().isNullOrEmpty() ||
                advStartingTime.text.toString().isNullOrEmpty() ||
                advEndingTime.text.toString().isNullOrEmpty() ||
                advLocation.text.toString().isNullOrEmpty() ||
                selectedSkillsList.isNullOrEmpty() ||
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
            return Pair(-1.0, false)
        }
        val startingHour = startingTime.split(":")[0].toInt()
        val startingMinute = startingTime.split(":")[1].toInt()
        val endingHour = endingTime.split(":")[0].toInt()
        val endingMinute = endingTime.split(":")[1].toInt()

        timeDifference += (endingHour - startingHour) + ((endingMinute - startingMinute) / 60.0)

        return Pair(
            (timeDifference * 100.0).roundToInt() / 100.0,
            timeDifference >= 0
        )
    }
}