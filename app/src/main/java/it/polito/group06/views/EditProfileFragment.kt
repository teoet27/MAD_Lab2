package it.polito.group06.views

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.polito.group06.R
import it.polito.group06.models.user_profile_database.UserProfile
import it.polito.group06.viewmodels.UserProfileViewModel
import it.polito.group06.utilities.*
import java.io.File
import java.io.IOException


class EditProfileFragment : Fragment() {
    private lateinit var editFullNameOBJ: EditText
    private lateinit var editNicknameOBJ: EditText
    private lateinit var editQualificationOBJ: EditText
    private lateinit var editDescriptionOBJ: EditText
    private lateinit var editEmailOBJ: EditText
    private lateinit var editLocationOBJ: EditText
    private lateinit var editSkillsOBJ: EditText
    private lateinit var editPhoneOBJ: EditText
    private lateinit var profilePictureOBJ: ImageView
    private lateinit var photoURI: Uri
    private lateinit var profilePictureDirectoryPath: String
    private lateinit var profilePicturePath: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 100

    private val usrViewModel by viewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val camera = view.findViewById<ImageView>(R.id.edit_camera_button)
        registerForContextMenu(camera)
        camera.setOnClickListener { activity?.openContextMenu(camera) }

        this.editFullNameOBJ = view.findViewById(R.id.edit_fullname_ID)
        this.editNicknameOBJ = view.findViewById(R.id.edit_nickname_ID)
        this.editQualificationOBJ = view.findViewById(R.id.edit_qualification_ID)
        this.editDescriptionOBJ = view.findViewById(R.id.edit_description_show_ID)
        this.editEmailOBJ = view.findViewById(R.id.edit_email_show_ID)
        this.editLocationOBJ = view.findViewById(R.id.edit_loc_show_ID)
        this.editSkillsOBJ = view.findViewById(R.id.edit_skillsListID)
        this.editPhoneOBJ = view.findViewById(R.id.edit_phone_show_ID)
        this.profilePictureOBJ = view.findViewById(R.id.profilePictureID)

        usrViewModel.profile.observe(this.viewLifecycleOwner) { userProfile ->

            if (userProfile != null){
                this.editFullNameOBJ.setText(userProfile.fullName)

                if (userProfile.nickname?.compareTo("") == 0) {
                    this.editNicknameOBJ.setText("No nickname provided.")
                } else {
                    this.editNicknameOBJ.setText(userProfile.nickname)
                }
                this.editQualificationOBJ.setText(userProfile.qualification)
                this.editPhoneOBJ.setText(userProfile.phoneNumber)
                this.editLocationOBJ.setText(userProfile.location)

                if (!userProfile.skills.isNullOrEmpty()) {
                    this.editSkillsOBJ.setText(fromArrayListToString(userProfile.skills!!))
                }


                this.editEmailOBJ.setText(userProfile.email)
                this.editDescriptionOBJ.setText(userProfile.description)

                profilePicturePath = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + '/' + resources.getString(R.string.profile_picture_filename)
                profilePictureDirectoryPath =
                    view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()

                getBitmapFromFile(profilePicturePath)?.also {
                    this.profilePictureOBJ.setImageBitmap(it)
                } ?: this.profilePictureOBJ.setImageResource(R.drawable.propic)

            }


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

    /**Private method for saving data before fragment transaction*/
    private fun saveData() {
        usrViewModel.profile.observe(this.viewLifecycleOwner) {
            usrViewModel.editProfile(
                UserProfile(
                    it.id,
                    editNicknameOBJ.text.toString(),
                    editFullNameOBJ.text.toString(),
                    editQualificationOBJ.text.toString(),
                    editDescriptionOBJ.text.toString(),
                    editEmailOBJ.text.toString(),
                    editPhoneOBJ.text.toString(),
                    editLocationOBJ.text.toString(),
                    fromStringToArrayList(editSkillsOBJ.text.toString()),
                )
            )
        }
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
                        "it.polito.group06.android.fileprovider",
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