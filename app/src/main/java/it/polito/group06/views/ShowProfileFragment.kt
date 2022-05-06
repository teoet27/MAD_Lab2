package it.polito.group06.views

import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.polito.group06.R
import it.polito.group06.models.user_profile_database.UserProfile
import it.polito.group06.utilities.fromArrayListToString
import it.polito.group06.viewmodels.UserProfileViewModel
import it.polito.group06.utilities.getBitmapFromFile

class ShowProfileFragment : Fragment() {
    private lateinit var fullnameOBJ: TextView
    private lateinit var nicknameOBJ: TextView
    private lateinit var qualificationOBJ: TextView
    private lateinit var descriptionOBJ: TextView
    private lateinit var emailOBJ: TextView
    private lateinit var locationOBJ: TextView
    private lateinit var skillsOBJ: TextView
    private lateinit var phoneOBJ: TextView
    private lateinit var profilePictureOBJ: ImageView

    private lateinit var profilePictureDirectoryPath: String
    private lateinit var profilePicturePath: String

    private val profile_vm by viewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_profile, container, false)
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

        profile_vm.profile.observe(this.viewLifecycleOwner) { userProfile ->

            if (userProfile == null) {
                profile_vm.editProfile(
                    UserProfile(
                        null,
                        "rettore",
                        "Guido Saracco",
                        "Rector @ Politecnico di Torino",
                        "I'm the full time Rector of Politecnico di Torino. I used to be a Chemistry teacher in 1994, but eventually I ended up being an institutional figure in the teaching world.",
                        "rettore@polito.it",
                        "3331112223",
                        "Torino - Italia",
                        null
                    )
                )
            } else {
                this.fullnameOBJ.text = userProfile.fullName

                if (userProfile.nickname?.compareTo("") == 0) {
                    this.nicknameOBJ.text = "No nickname provided."
                } else {
                    this.nicknameOBJ.text = "@" + userProfile.nickname
                }
                this.qualificationOBJ.text = userProfile.qualification
                this.phoneOBJ.text = userProfile.phoneNumber
                this.locationOBJ.text = userProfile.location

                if (userProfile.skills.isNullOrEmpty()) {
                    this.skillsOBJ.text = getString(R.string.noskills)
                } else {
                    this.skillsOBJ.text = fromArrayListToString(userProfile.skills!!)
                }


                this.emailOBJ.text = userProfile.email
                this.descriptionOBJ.text = userProfile.description

                profilePicturePath = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + '/' + resources.getString(R.string.profile_picture_filename)
                profilePictureDirectoryPath = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()

                getBitmapFromFile(profilePicturePath)?.also {
                    this.profilePictureOBJ.setImageBitmap(it)
                }
            }


        }

        // check this option to open onCreateOptionsMenu method
        setHasOptionsMenu(true)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_showProfileFragment_to_frag5ShowListTimeslots)
            }
        })

        // check this option to open onCreateOptionsMenu method
        setHasOptionsMenu(true)
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
            R.id.edit_user_profile ->
                findNavController().navigate(R.id.action_showProfileFragment_to_editProfileFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}