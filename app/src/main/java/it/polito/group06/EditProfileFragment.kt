package it.polito.group06

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.polito.group06.MVVM.UserProfileDatabase.UserProfile
import it.polito.group06.MVVM.UserProfileDatabase.UserProfileViewModel
import it.polito.group06.utilities.createImageFile
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
    private lateinit var sdh: SaveProfileDataHandler
    private lateinit var vibrator: Vibrator
    private lateinit var profilePictureOBJ: ImageView
    private lateinit var photoURI: Uri
    private lateinit var profilePictureDirectoryPath: String
    private lateinit var profilePicturePath: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 100

    val profile_vm by viewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        val camera = view.findViewById<ImageView>(R.id.edit_camera_button)
        registerForContextMenu(camera)
        camera.setOnClickListener { activity?.openContextMenu(camera) }

        this.editFullNameOBJ = view.findViewById(R.id.edit_fullnameID)
        this.editNicknameOBJ = view.findViewById(R.id.edit_nicknameID)
        this.editQualificationOBJ = view.findViewById(R.id.edit_qualificationID)
        this.editDescriptionOBJ = view.findViewById(R.id.edit_description_show_ID)
        this.editEmailOBJ = view.findViewById(R.id.edit_email_show_ID)
        this.editLocationOBJ = view.findViewById(R.id.edit_loc_show_ID)
        this.editSkillsOBJ = view.findViewById(R.id.edit_skillsListID)
        this.editPhoneOBJ = view.findViewById(R.id.edit_phone_show_ID)
        this.profilePictureOBJ = view.findViewById(R.id.profilePictureID)

        this.editFullNameOBJ.setOnClickListener{
            var tmp=profile_vm.profile.value!!
            tmp.fullName=this.editFullNameOBJ.text.toString()
            profile_vm.editProfile(tmp)}

        this.editNicknameOBJ.setOnClickListener{
            var tmp=profile_vm.profile.value!!
            tmp.fullName=this.editNicknameOBJ.text.toString()
            profile_vm.editProfile(tmp)}

        this.editQualificationOBJ.setOnClickListener{
            var tmp=profile_vm.profile.value!!
            tmp.fullName=this.editQualificationOBJ.text.toString()
            profile_vm.editProfile(tmp)}

        this.editDescriptionOBJ.setOnClickListener{
                var tmp=profile_vm.profile.value!!
                tmp.fullName=this.editDescriptionOBJ.text.toString()
            profile_vm.editProfile(tmp)}

        this.editEmailOBJ.setOnClickListener{
            var tmp=profile_vm.profile.value!!
            tmp.fullName=this.editEmailOBJ.text.toString()
            profile_vm.editProfile(tmp)}

        this.editPhoneOBJ.setOnClickListener{
            var tmp=profile_vm.profile.value!!
            tmp.fullName=this.editPhoneOBJ.text.toString()
            profile_vm.editProfile(tmp)}

        this.editLocationOBJ.setOnClickListener{
            var tmp=profile_vm.profile.value!!
            tmp.fullName=this.editLocationOBJ.text.toString()
            profile_vm.editProfile(tmp)}

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*profile_vm.profile.observe(this.viewLifecycleOwner){ userProfile ->


            this.editFullNameOBJ.setText(userProfile.fullName)

            if (userProfile.nickname?.compareTo("") == 0) {
                this.editNicknameOBJ.setText("No nickname provided.")
            } else {
                this.editNicknameOBJ.setText("@" + userProfile.nickname)
            }
            this.editQualificationOBJ.setText(userProfile.qualification)
            this.editPhoneOBJ.setText(userProfile.phoneNumber)
            this.editLocationOBJ.setText(userProfile.location)

            if(userProfile.skills.isNullOrEmpty()){
                this.editSkillsOBJ.setText(R.string.noskills)
            }else{
                this.editSkillsOBJ.setText(fromArrayListToString(userProfile.skills))
            }


            this.editEmailOBJ.setText(userProfile.email)
            this.editDescriptionOBJ.setText(userProfile.description)

            profilePicturePath = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + '/' + resources.getString(R.string.profile_picture_filename)
            profilePictureDirectoryPath = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()

            getBitmapFromFile(profilePicturePath)?.also{
                this.profilePictureOBJ.setImageBitmap(it)
            }
        }*/

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_editProfileFragment_to_showProfileFragment)
            }
        })
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
                        requireView().context,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
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
        //super.onCreateContextMenu(menu, v, menuInfo)
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
     * These methods modify a single attribute of the Profile data class
     *
     * @param -name- chosen attribute
     * @return modified profile object
     */
    private fun editProfileFullName(profile:UserProfile,fullName:String):UserProfile{
        return UserProfile(
            profile.id,
            profile.nickname,
            fullName,
            profile.qualification,
            profile.description,
            profile.email,
            profile.phoneNumber,
            profile.location,
            profile.skills
        )
    }

    private fun editProfileNickame(profile:UserProfile,nickname:String):UserProfile{
        return UserProfile(
            profile.id,
            nickname,
            profile.fullName,
            profile.qualification,
            profile.description,
            profile.email,
            profile.phoneNumber,
            profile.location,
            profile.skills
        )
    }

    private fun editProfileQualification(profile:UserProfile,qualification:String):UserProfile{
        return UserProfile(
            profile.id,
            profile.nickname,
            profile.fullName,
            qualification,
            profile.description,
            profile.email,
            profile.phoneNumber,
            profile.location,
            profile.skills
        )
    }

    private fun editProfileDescription(profile:UserProfile,description:String):UserProfile{
        return UserProfile(
            profile.id,
            profile.nickname,
            profile.fullName,
            profile.qualification,
            description,
            profile.email,
            profile.phoneNumber,
            profile.location,
            profile.skills
        )
    }

    private fun editProfileEmail(profile:UserProfile,email:String):UserProfile{
        return UserProfile(
            profile.id,
            profile.nickname,
            profile.fullName,
            profile.qualification,
            profile.description,
            email,
            profile.phoneNumber,
            profile.location,
            profile.skills
        )
    }

    private fun editProfilePhoneNumber(profile:UserProfile,phoneNumber:String):UserProfile{
        return UserProfile(
            profile.id,
            profile.nickname,
            profile.fullName,
            profile.qualification,
            profile.description,
            profile.email,
            phoneNumber,
            profile.location,
            profile.skills
        )
    }

    private fun editProfileLocation(profile:UserProfile,location:String):UserProfile{
        return UserProfile(
            profile.id,
            profile.nickname,
            profile.fullName,
            profile.qualification,
            profile.description,
            profile.email,
            profile.phoneNumber,
            location,
            profile.skills
        )
    }

    private fun editProfileSkills(profile:UserProfile,skills:ArrayList<String>):UserProfile{
        return UserProfile(
            profile.id,
            profile.nickname,
            profile.fullName,
            profile.qualification,
            profile.description,
            profile.email,
            profile.phoneNumber,
            profile.location,
            skills
        )
    }
}