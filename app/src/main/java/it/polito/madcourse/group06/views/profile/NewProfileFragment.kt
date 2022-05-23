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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.models.userprofile.UserProfile
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import it.polito.madcourse.group06.utilities.*
import java.io.File
import java.io.IOException

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
    private lateinit var profilePictureDirectoryPath: String
    private lateinit var profilePicturePath: String
    private lateinit var skillsChips: ChipGroup
    private lateinit var addSkillButton: ImageView
    private lateinit var newSkillTitleLabel: String
    private lateinit var imgProfilePicturePath: String
    private val skillList = arrayListOf<String>()

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 100

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as TBMainActivity).setDrawerLocked()

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
        this.addSkillButton = view.findViewById(R.id.addNewSkillButtonID)

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
            // Add New Skill Button
            this.addSkillButton.setOnClickListener {
                showNewSkillInputWindow(requireContext(), this.skillsChips)
            }
            // Skills
            /*if (!userProfile.skills.isNullOrEmpty()) {
                userProfile.skills!!.forEach { skill ->
                    this.skillsChips.addChipForEdit(requireContext(), skill)
                    this.skillsChips.setOnCheckedChangeListener { chipGroup, checkedId ->
                        val selected_service = chipGroup.findViewById<Chip>(checkedId)?.text
                        Toast.makeText(
                            chipGroup.context,
                            selected_service ?: "No Choice",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                this.skillList.addAll(userProfile.skills!!)
            }*/

            // Email
            this.newEmailOBJ.setText(userProfile.email)
            // Description
            this.newDescriptionOBJ.setText(userProfile.description)
            // Profile Picture
            profilePicturePath = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + '/' + resources.getString(R.string.profile_picture_filename)
            profilePictureDirectoryPath =
                view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
            getBitmapFromFile(profilePicturePath)?.also {
                this.profilePictureOBJ.setImageBitmap(it)
            } ?: this.profilePictureOBJ.setImageResource(R.drawable.propic)
        }

        // check this option to open onCreateOptionsMenu method
        setHasOptionsMenu(true)
        // show dialog box with welcome message
        showCustomDialog()
    }


    /**
     * TODO
     * addChip
     * @param
     * @param
     */
    private fun ChipGroup.addChip(context: Context, skill: String) {
        Chip(context).apply {
            id = View.generateViewId()
            text = skill
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = true
            isFocusable = true
            isChecked = true
            setTextColor(ContextCompat.getColor(context, R.color.white))
            chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.prussian_blue))

            setOnClickListener {
                if (skillList.any { x ->
                        x == skill
                    }) {
                    for (s in skillList) {
                        if (s == skill) {
                            skillList.remove(s)
                            break
                        }
                    }
                } else {
                    skillList.add(skill)
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
            newSkillTitleLabel = newSkillTitle.text.toString()
            if (newSkillTitleLabel.isNotEmpty()) {
                chipGroup.addChip(context, newSkillTitleLabel)
                chipGroup.moveAddChip(context, view?.findViewById(R.id.add_new_skill_chip)!!, chipGroup)

                Snackbar.make(
                    requireView(), "New skill added!", Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    requireView(), "You must provide a name for the new skill.", Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun showCustomDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.welcome_box)

        val yesBtn = dialog.findViewById<Button>(R.id.yesBtn)
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Dinamically create a chip within a chip group
     *
     * @param context       parent view context
     * @param label         chip name
     */
    private fun ChipGroup.addChipForEdit(context: Context, label: String) {
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            setChipDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_close_black_24dp
                ) as ChipDrawable
            )
            isClickable = true
            isCheckable = false
            isCheckedIconVisible = false
            isFocusable = true
            setOnClickListener {
                if (isChecked) {
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.red_deleting
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

    /**
     * saveData is a private method for saving data before fragment transaction
     */
    private fun saveData(): Boolean {
        var userIsOkay: Boolean = false
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
                    imgProfilePicturePath
                )
            )
            userIsOkay = true
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
            // saveProfilePicture(rotatedImage, profilePictureDirectoryPath)
            imgProfilePicturePath = this.userProfileViewModel.uploadProfilePicture(rotatedImage)
            view?.findViewById<ImageView>(R.id.profilePictureID)?.setImageBitmap(rotatedImage)
        } else if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            this.photoURI = data?.data!!
            rotatedImage = handleSamplingAndRotationBitmap(requireContext(), this.photoURI)!!
            // saveProfilePicture(rotatedImage, profilePictureDirectoryPath)
            imgProfilePicturePath = this.userProfileViewModel.uploadProfilePicture(rotatedImage)
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
        }
        return true
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
            R.id.complete_user_editing -> {
                if (saveData()) {
                    Snackbar.make(
                        requireView(),
                        "Your profile was updated successfully. Explore our app to discover offered services.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_newProfileFragment_to_ShowListOfServices)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        (activity as TBMainActivity).setDrawerUnlocked()
        super.onDestroy()
    }
}