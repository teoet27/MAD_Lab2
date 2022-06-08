package it.polito.madcourse.group06.views.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

class ShowMyProfileFragment : Fragment() {
    private lateinit var fullnameOBJ: TextView
    private lateinit var nicknameOBJ: TextView
    private lateinit var qualificationOBJ: TextView
    private lateinit var descriptionOBJ: TextView
    private lateinit var emailOBJ: TextView
    private lateinit var locationOBJ: TextView
    private lateinit var skillsOBJ: TextView
    private lateinit var phoneOBJ: TextView
    private lateinit var profilePictureOBJ: ImageView
    private lateinit var confirmedBadge: ImageView
    private lateinit var skillsChips: ChipGroup
    private lateinit var credit: TextView

    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.show_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.fullnameOBJ = view.findViewById(R.id.fullname_ID)
        this.nicknameOBJ = view.findViewById(R.id.nickname_ID)
        this.qualificationOBJ = view.findViewById(R.id.qualification_ID)
        this.descriptionOBJ = view.findViewById(R.id.description_show_ID)
        this.emailOBJ = view.findViewById(R.id.email_show_ID)
        this.locationOBJ = view.findViewById(R.id.loc_show_ID)
        this.skillsOBJ = view.findViewById(R.id.skillsListID)
        this.phoneOBJ = view.findViewById(R.id.phone_show_ID)
        this.profilePictureOBJ = view.findViewById(R.id.profilePictureID)
        this.confirmedBadge=view.findViewById(R.id.edit_camera_button)
        this.skillsChips = view.findViewById(R.id.skill_chips_group)
        this.credit = view.findViewById(R.id.profileCredit)

        userProfileViewModel.currentUser.observe(this.viewLifecycleOwner) { userProfile ->
            Log.e("profile", userProfile.toString())

            // Show confirmed badge only if the user has been rated by others (i.e. he's an active user)
            this.confirmedBadge.visibility=if(userProfile.comments_services_done.isNullOrEmpty()) View.GONE else View.VISIBLE

            // Fullname
            this.fullnameOBJ.text = userProfile.fullName

            // Nickname
            if (userProfile.nickname?.compareTo("") == 0) {
                this.nicknameOBJ.text = "No nickname provided."
            } else {
                this.nicknameOBJ.text = "@${userProfile.nickname}"
            }

            // Qualification
            this.qualificationOBJ.text = userProfile.qualification
            // Phone Number
            this.phoneOBJ.text = userProfile.phoneNumber
            // Location
            this.locationOBJ.text = userProfile.location

            // Skills
            if (userProfile.skills.isNullOrEmpty()) {
                this.skillsChips.isVisible = false
                this.skillsOBJ.isVisible = true
                this.skillsOBJ.text = getString(R.string.noskills)
            } else {
                this.skillsOBJ.isVisible = false
                this.skillsChips.isVisible = true
                this.skillsChips.removeAllViews()
                userProfile.skills!!.forEach { sk ->
                    this.skillsChips.addChip(requireContext(), sk)
                    this.skillsChips.setOnCheckedChangeListener { chipGroup, checkedId ->
                        val selectedService = chipGroup.findViewById<Chip>(checkedId)?.text
                        Toast.makeText(chipGroup.context, selectedService ?: "Please select a skill.", Toast.LENGTH_LONG).show()
                    }
                }
            }

            // Email
            this.emailOBJ.text = userProfile.email
            // Description
            this.descriptionOBJ.text = userProfile.description

            // Profile Picture
            if (userProfile.imgPath.isNullOrEmpty()) {
                userProfileViewModel.retrieveStaticProfilePicture(profilePictureOBJ)
            } else {
                userProfileViewModel.retrieveProfilePicture(profilePictureOBJ, userProfile.imgPath!!)
            }

            // Credit
            this.credit.text = userProfile.credit.toInt().toString()
        }

        view.findViewById<TextView>(R.id.profileCredit).setOnClickListener {
            Toast.makeText(context, "Credits are the TimeBanking currency and they are used as payment for services. 1 credit is equivalent to 15 minutes worth of work.", Toast.LENGTH_LONG).show()
        }
        view.findViewById<ImageView>(R.id.profileCreditSymbol).setOnClickListener {
            Toast.makeText(context, "Credits are the TimeBanking currency and they are used as payment for services. 1 credit is equivalent to 15 minutes worth of work.", Toast.LENGTH_LONG).show()
        }

        // Check this option to open onCreateOptionsMenu method
        setHasOptionsMenu(true)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_showMyProfileFragment_to_ShowListOfServices)
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
            addView(this)
        }
    }

    /**
     * This method inflates the option menu
     *
     * @param menu  Menu to be inflated
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_profile_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * This method navigates the view on the basis of the user interaction:
     * click the pen icon -> open the edit profile mode
     *
     * @param item  Icon chosen by the user interaction
     * @return true
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_user_profile -> {
                Snackbar.make(
                    requireView(), "Edit mode.", Snackbar.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_showMyProfileFragment_to_editProfileFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}