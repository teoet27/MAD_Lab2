package it.polito.madcourse.group06.views.profile

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.userprofile.UserProfile
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import it.polito.madcourse.group06.utilities.*
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import java.io.File
import java.io.IOException


class EditProfileFragment : Fragment() {
    private lateinit var editFullNameOBJ: EditText
    private lateinit var editNicknameOBJ: EditText
    private lateinit var editQualificationOBJ: EditText
    private lateinit var editDescriptionOBJ: EditText
    private lateinit var editEmailOBJ: EditText
    private lateinit var editLocationOBJ: EditText
    private lateinit var editPhoneOBJ: EditText
    private lateinit var profilePictureOBJ: ImageView
    private lateinit var photoURI: Uri
    private lateinit var profilePictureDirectoryPath: String
    private lateinit var profilePicturePath: String
    private lateinit var skillsChips: ChipGroup
    private var userID: Long = -1
    private val skillList = arrayListOf<String>()

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 100

    private val advertisementViewModel by activityViewModels<AdvertisementViewModel>()
    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Camera
        val camera = view.findViewById<ImageView>(R.id.edit_camera_button)
        registerForContextMenu(camera)
        camera.setOnClickListener { activity?.openContextMenu(camera) }

        this.editFullNameOBJ = view.findViewById(R.id.edit_fullname_ID)
        this.editNicknameOBJ = view.findViewById(R.id.edit_nickname_ID)
        this.editQualificationOBJ = view.findViewById(R.id.edit_qualification_ID)
        this.editDescriptionOBJ = view.findViewById(R.id.edit_description_show_ID)
        this.editEmailOBJ = view.findViewById(R.id.edit_email_show_ID)
        this.editLocationOBJ = view.findViewById(R.id.edit_loc_show_ID)
        this.editPhoneOBJ = view.findViewById(R.id.edit_phone_show_ID)
        this.profilePictureOBJ = view.findViewById(R.id.profilePictureID)
        this.skillsChips = view.findViewById(R.id.editProfileChipGroup)

        userProfileViewModel.currentUser.observe(this.viewLifecycleOwner) { userProfile ->
            // Save the ID
            this.userID = userProfile.id!!
            // Fullname
            this.editFullNameOBJ.setText(userProfile.fullName)
            // Nickname
            this.editNicknameOBJ.setText(userProfile.nickname)
            // Qualification
            this.editQualificationOBJ.setText(userProfile.qualification)
            // Phone Number
            this.editPhoneOBJ.setText(userProfile.phoneNumber)
            // Location
            this.editLocationOBJ.setText(userProfile.location)

            // Skills
            if (!userProfile.skills.isNullOrEmpty()) {
                userProfile.skills!!.forEach { skill ->
                    this.skillsChips.addChipForEdit(requireContext(), skill)
                    this.skillsChips.setOnCheckedChangeListener { chipGroup, checkedId ->
                        val selected_service = chipGroup.findViewById<Chip>(checkedId)?.text
                        Toast.makeText(chipGroup.context, selected_service ?: "No Choice", Toast.LENGTH_LONG).show()
                    }
                }
                this.skillList.addAll(userProfile.skills!!)
            }

            // Email
            this.editEmailOBJ.setText(userProfile.email)
            // Description
            this.editDescriptionOBJ.setText(userProfile.description)

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

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                saveData()
                findNavController().navigate(R.id.action_editProfileFragment_to_showProfileFragment)
            }
        })
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
            setChipDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp) as ChipDrawable)
            isClickable = true
            isCheckable = false
            isCheckedIconVisible = false
            isFocusable = true
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
    private fun saveData() {
        userProfileViewModel.editUserProfile(
            UserProfile(
                this.userID,
                this.editNicknameOBJ.text.toString(),
                editFullNameOBJ.text.toString(),
                editQualificationOBJ.text.toString(),
                editDescriptionOBJ.text.toString(),
                editEmailOBJ.text.toString(),
                editPhoneOBJ.text.toString(),
                editLocationOBJ.text.toString(),
                this.skillList
            )
        )
        advertisementViewModel.updateAdvAccountNameByAccountID(this.userID, editFullNameOBJ.text.toString())
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
                        "it.polito.MAD.group06.android.fileprovider",
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
            saveProfilePicture(rotatedImage, profilePictureDirectoryPath)
            view?.findViewById<ImageView>(R.id.profilePictureID)?.setImageBitmap(rotatedImage)
        } else if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            this.photoURI = data?.data!!
            rotatedImage = handleSamplingAndRotationBitmap(requireContext(), this.photoURI)!!
            saveProfilePicture(rotatedImage, profilePictureDirectoryPath)
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
                saveData()
                findNavController().navigate(R.id.action_editProfileFragment_to_showProfileFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}