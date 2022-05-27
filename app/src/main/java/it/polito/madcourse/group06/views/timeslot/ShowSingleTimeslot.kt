package it.polito.madcourse.group06.views.timeslot

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

class ShowSingleTimeslot : Fragment(R.layout.time_slot_details_fragment) {

    private val advViewModel: AdvertisementViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    private lateinit var advTitle: TextView
    private lateinit var advAccount: TextView
    private lateinit var advLocation: TextView
    private lateinit var advDate: TextView
    private lateinit var advStartingTime: TextView
    private lateinit var advEndingTime: TextView
    private lateinit var advDuration: TextView
    private lateinit var advDescription: TextView
    private lateinit var editButton: ImageView
    private lateinit var skillsChips: ChipGroup
    private lateinit var noSkillsProvidedLabel: TextView
    private var isMine = false
    private lateinit var email: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.advTitle = view.findViewById(R.id.advTitle)
        this.advAccount = view.findViewById(R.id.advAccount)
        this.advLocation = view.findViewById(R.id.advLocation)
        this.advDate = view.findViewById(R.id.advDate)
        this.advStartingTime = view.findViewById(R.id.advStartingTime)
        this.advEndingTime = view.findViewById(R.id.advEndingTime)
        this.advDuration = view.findViewById(R.id.advDuration)
        this.advDescription = view.findViewById(R.id.advDescription)
        this.editButton = view.findViewById(R.id.moreButtonID)
        this.skillsChips = view.findViewById(R.id.showTimeslotSkills)
        this.noSkillsProvidedLabel = view.findViewById(R.id.noSkillsProvidedLabel)

        advViewModel.advertisement.observe(viewLifecycleOwner) { singleAdvertisement ->
            userProfileViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if(singleAdvertisement.accountID == user.id) {
                    isMine=true
                    this.editButton.setImageResource(R.drawable.ic_edit_black_24dp)
                }
                /*else {
                    // DOES NOT WORK: this substitutes current user to clicked one
                    userProfileViewModel.fetchUserProfileById(singleAdvertisement.accountID)
                }*/
            }

            this.advTitle.text = singleAdvertisement.advTitle
            this.advAccount.text = singleAdvertisement.advAccount
            this.advLocation.text = singleAdvertisement.advLocation
            this.advDate.text = singleAdvertisement.advDate
            this.advStartingTime.text = "Starting time: ${singleAdvertisement.advStartingTime}"
            this.advEndingTime.text = "Ending time: ${singleAdvertisement.advEndingTime}"
            this.advDuration.text = "${singleAdvertisement.advDuration} hours"
            if(singleAdvertisement.listOfSkills.size == 0)
            {
                this.noSkillsProvidedLabel.isVisible = true
                this.skillsChips.isVisible = false
            } else {
                this.noSkillsProvidedLabel.isVisible = false
                this.skillsChips.isVisible = true

                this.skillsChips.removeAllViews()
                for (skill in singleAdvertisement.listOfSkills) {
                    this.skillsChips.addChip(requireContext(), skill)
                }
            }
            if (singleAdvertisement.advDescription.isEmpty()) {
                this.advDescription.text = "No description provided"
            } else {
                this.advDescription.text = singleAdvertisement.advDescription
            }
        }

        this.editButton.setOnClickListener {
            if(isMine)
                Navigation.findNavController(view).navigate(R.id.action_showSingleTimeslot_to_editTimeSlotDetailsFragment)
            else{
                /*chat*/
            }
        }

        this.advAccount.setOnClickListener {
            // show user profile of the owner of the advertisement
            if(!isMine) {
                Navigation.findNavController(view).navigate(R.id.action_showSingleTimeslot_to_showProfileOtherFragment)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_showSingleTimeslot_to_ShowListTimeslots)
            }
        })

    }

    /**
     * Dinamically create a chip within a chip group
     *
     * @param context       parent view context
     * @param label         chip name
     */
    private fun ChipGroup.addChip(context: Context, label: String) {
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = false
            isCheckedIconVisible = false
            isFocusable = true
            setTextColor(ContextCompat.getColor(context, R.color.white))
            chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.prussian_blue))
            addView(this)
        }
    }

}