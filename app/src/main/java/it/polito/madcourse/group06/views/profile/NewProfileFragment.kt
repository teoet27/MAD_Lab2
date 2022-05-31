package it.polito.madcourse.group06.views.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.GoogleLoginActivity
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.models.userprofile.UserProfile
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import it.polito.madcourse.group06.utilities.*
import java.io.File
import java.io.IOException
import java.util.*

class NewProfileFragment : Fragment() {
    private lateinit var newFullNameOBJ: EditText
    private lateinit var newNicknameOBJ: EditText
    private lateinit var newQualificationOBJ: EditText
    private lateinit var newDescriptionOBJ: EditText
    private lateinit var newEmailOBJ: TextView
    private lateinit var newLocationOBJ: EditText
    private lateinit var newPhoneOBJ: EditText
    private lateinit var profilePictureOBJ: ImageView
    private lateinit var photoURI: Uri
    private lateinit var profilePicturePath: String
    private lateinit var skillsChips: ChipGroup
    private lateinit var newSkillChip: Chip
    private lateinit var imgProfilePicturePath: String
    private lateinit var activityTB: TBMainActivity
    private lateinit var email: String
    private val skillList = arrayListOf<String>()

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 100

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityTB = requireActivity() as TBMainActivity
        return inflater.inflate(R.layout.new_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get current user email
        email=arguments?.getString("email")!!

        activityTB.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activityTB.drawerLock()
        // Camera
        val camera = view.findViewById<ImageView>(R.id.edit_camera_button)
        registerForContextMenu(camera)
        camera.setOnClickListener { activity?.openContextMenu(camera) }

        this.newFullNameOBJ = view.findViewById(R.id.new_fullname_ID)
        this.newNicknameOBJ = view.findViewById(R.id.new_nickname_ID)
        this.newQualificationOBJ = view.findViewById(R.id.new_qualification_ID)
        this.newDescriptionOBJ = view.findViewById(R.id.new_description_show_ID)
        this.newEmailOBJ = view.findViewById(R.id.new_email_show_ID)
        this.newLocationOBJ = view.findViewById(R.id.new_loc_show_ID)
        this.newPhoneOBJ = view.findViewById(R.id.new_phone_show_ID)
        this.profilePictureOBJ = view.findViewById(R.id.profilePictureID)
        this.skillsChips = view.findViewById(R.id.newProfileChipGroup)
        this.newSkillChip = view.findViewById(R.id.newProfileAddNewSkillChip)
        this.imgProfilePicturePath = "staticuser"

        userProfileViewModel.currentUser.observe(this.viewLifecycleOwner) { userProfile ->
            // Fullname
            this.newFullNameOBJ.setText(userProfile.fullName)
            // Nickname
            this.newNicknameOBJ.setText(userProfile.nickname)
            // Qualification
            this.newQualificationOBJ.setText(userProfile.qualification)
            // Phone Number
            this.newPhoneOBJ.setText(userProfile.phoneNumber)
            // Location
            this.newLocationOBJ.setText(userProfile.location)

            // Button for adding a new skill
            this.newSkillChip.setOnClickListener {
                showNewSkillInputWindow(requireContext(), this.skillsChips)
            }

            // Skills
            if (!skillList.isNullOrEmpty()) {
                this.skillsChips.removeAllViews()
                skillList.forEach { skill ->
                    this.skillsChips.addChipForEdit(requireContext(), skill, this.skillsChips)
                }
                this.skillsChips.addPlusChip(requireContext(), this.skillsChips)
            }

            // Email
            this.newEmailOBJ.setText(userProfile.email)
            // Description
            this.newDescriptionOBJ.setText(userProfile.description)

            // Profile Picture
            profilePicturePath = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + '/' + resources.getString(R.string.profile_picture_filename)

            if (userProfile.imgPath.isNullOrEmpty()) {
                userProfileViewModel.retrieveStaticProfilePicture(profilePictureOBJ)
            } else {
                userProfileViewModel.retrieveProfilePicture(profilePictureOBJ, userProfile.imgPath!!)
            }

        }

        // check this option to open onCreateOptionsMenu method
        setHasOptionsMenu(true)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // go back to login activity (do logout first)
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id_bis))
                    .requestEmail()
                    .build()
                mGoogleSignInClient = GoogleSignIn.getClient(view.context, gso)
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    val goBackToGoogleLogin = Intent(view.context, GoogleLoginActivity::class.java)
                    startActivity(goBackToGoogleLogin)
                    activity?.finish()
                }
            }
        })
    }

    private fun ChipGroup.addPlusChip(context: Context, chipGroup: ChipGroup) {
        Chip(context).apply {
            id = R.id.newProfileAddNewSkillChip
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
        newSkillTitle.gravity = Gravity.START
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(64, 0, 64, 0)
        linearLayout.addView(newSkillTitle)
        builder.setView(linearLayout)

        /**
         * setPositiveButton
         */
        builder.setPositiveButton("Create", DialogInterface.OnClickListener { dialog, which ->
            val newSkillTitleLabel = newSkillTitle.text.toString().replaceFirstChar(Char::titlecase)
            if (newSkillTitleLabel.isNotEmpty()) {
                this.skillsChips.removeAllViews()
                for (skill in skillList) {
                    chipGroup.addChipForEdit(context, skill, this.skillsChips)
                }
                chipGroup.addChipForEdit(context, newSkillTitleLabel, this.skillsChips)
                this.skillsChips.addPlusChip(context, this.skillsChips)

                skillList.add(newSkillTitleLabel)
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
     * Dinamically create a chip within a chip group
     *
     * @param context       parent view context
     * @param label         chip name
     */
    private fun ChipGroup.addChipForEdit(context: Context, label: String, chipGroup: ChipGroup) {
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = false
            isCheckedIconVisible = false
            isFocusable = true
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                skillList.remove(text)
                chipGroup.removeView(it)
            }
            setOnClickListener {
                if (isChecked) {
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_deleting))
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGray))
                }
            }
            addView(this)
        }
    }

    /**
     * saveData is a private method for saving data before fragment transaction
     */
    private fun saveData(): Boolean {
        var userIsOkay = false
        var isNicknameAvailable = true

        this.userProfileViewModel.listOfUsers.observe(viewLifecycleOwner) { listOfUsers ->
            for (u in listOfUsers) {
                if (u.nickname?.compareTo(newNicknameOBJ.text.toString(), true) == 0) {
                    isNicknameAvailable = false
                    break
                }
            }
        }

        if (newFullNameOBJ.text.toString() == "") {
            Snackbar.make(
                requireView(),
                "Error: you must provide your full name. Try again.",
                Snackbar.LENGTH_LONG
            ).show()
        } else if (newNicknameOBJ.text.toString() == "") {
            Snackbar.make(
                requireView(),
                "Error: you must provide a nickname. Try again.",
                Snackbar.LENGTH_LONG
            ).show()
        } else if (!isNicknameAvailable) {
            Snackbar.make(
                requireView(),
                "Error: this nickname is not available. Choose another one.",
                Snackbar.LENGTH_LONG
            ).show()
        } else if (userProfileViewModel.lookForNickname(newNicknameOBJ.text.toString())) {
            Snackbar.make(
                requireView(),
                "Error: the nickname you chose is not available.",
                Snackbar.LENGTH_LONG
            ).show()
        } else if (newPhoneOBJ.text.toString() == "") {
            Snackbar.make(
                requireView(),
                "Error: you must provide your phone number. Try again.",
                Snackbar.LENGTH_LONG
            ).show()
        } else if (newLocationOBJ.text.toString() == "") {
            Snackbar.make(
                requireView(),
                "Error: you must provide your location. Try again.",
                Snackbar.LENGTH_LONG
            ).show()
        } else if (this.skillList.isEmpty()) {
            Snackbar.make(
                requireView(),
                "Error: provide at least a skill.",
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            userProfileViewModel.insertUserProfile(
                UserProfile(
                    null,
                    this.newNicknameOBJ.text.toString(),
                    newFullNameOBJ.text.toString(),
                    newQualificationOBJ.text.toString(),
                    newDescriptionOBJ.text.toString(),
                    newEmailOBJ.text.toString(),
                    newPhoneOBJ.text.toString(),
                    newLocationOBJ.text.toString(),
                    this.skillList,
                    5.0, // initial gift credit for the new user
                    0.0,
                    0,
                    null,
                    null,
                    this.imgProfilePicturePath
                )
            )
            userIsOkay = true

            activityTB.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        return userIsOkay
    }

    /**
     * This method invokes an intent to take a picture. First, it checks if there is an
     * available camera, then it creates a file where to store the image and finally the intent
     * is dispatched.
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(view?.context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(profilePicturePath)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        "it.polito.madcourse.group06.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    /**
     * This method returns the Bitmap image from a file whose path is provided
     *
     * @param requestCode  It delivers the user's decision (either take or load a picture)
     * @param resultCode  It represents the result of the invoked intent
     * @param data  It contains the image bitmap in the extras
     * @return Bitmap image results
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val rotatedImage: Bitmap?

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            rotatedImage = handleSamplingAndRotationBitmap(requireContext(), this.photoURI)!!
            this.imgProfilePicturePath = UUID.randomUUID().toString()
            this.userProfileViewModel.uploadProfilePicture(rotatedImage, imgProfilePicturePath)
            view?.findViewById<ImageView>(R.id.profilePictureID)?.setImageBitmap(rotatedImage)
        } else if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            this.photoURI = data?.data!!
            rotatedImage = handleSamplingAndRotationBitmap(requireContext(), this.photoURI)!!
            this.imgProfilePicturePath = UUID.randomUUID().toString()
            this.userProfileViewModel.uploadProfilePicture(rotatedImage, imgProfilePicturePath)
            view?.findViewById<ImageView>(R.id.profilePictureID)?.setImageBitmap(rotatedImage)
        }
    }

    /**
     * This method invokes an intent to load a picture from the filesystem
     */
    private fun dispatchLoadPictureIntent() {
        val loadPictureIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(loadPictureIntent, PICK_IMAGE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            e.printStackTrace()
        }
    }

    /**
     * This method chooses a menu to be inflated on the basis of the availability of the camera
     *
     * @param menu  ContextMenu
     * @param v  View
     * @param menuInfo  ContextMenu info
     * @return true
     */
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        if (view?.context!!.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            inflater.inflate(R.menu.select_take_picture_floating_context_menu, menu)
        else
            inflater.inflate(R.menu.select_picture_floating_context_menu, menu)
    }

    /**
     * This method invokes an intent for either taking or loading a picture
     *
     * @param item  Chosen menu entry
     * @return true
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            resources.getString(R.string.floating_menu_shot_picture) -> dispatchTakePictureIntent()
            resources.getString(R.string.floating_menu_load_picture) -> dispatchLoadPictureIntent()
            resources.getString(R.string.remove_your_picture) -> removeProfilePicture()
        }
        return true
    }

    private fun removeProfilePicture() {
        if (this.imgProfilePicturePath != "staticuser") {
            this.imgProfilePicturePath = "staticuser"
            userProfileViewModel.retrieveStaticProfilePicture(profilePictureOBJ)
        }
    }

    /**
     * This method inflates the option menu
     *
     * @param menu  Menu to be inflated
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_profile_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * This method navigates the view on the basis of the user interaction:
     * click the check icon -> close the edit profile mode, return to show profile mode
     *
     * @param item  Icon chosen by the user interaction
     * @return true
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.complete_user_registration -> {
                if (saveData()) {
                    Snackbar.make(
                        requireView(),
                        "Your profile was updated successfully. Explore our app to discover offered services.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    userProfileViewModel.fetchUserProfile(email)
                    activityTB.drawerUnlock()
                    findNavController().navigate(R.id.action_newProfileFragment_to_ShowListOfServices)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}