package com.example.mad_lab2

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class EditProfileActivity : AppCompatActivity() {
    private lateinit var editFullNameOBJ: EditText
    private lateinit var editNickNameOBJ: EditText
    private lateinit var editQualificationOBJ: EditText
    private lateinit var editDescriptionOBJ: EditText
    private lateinit var editEmailOBJ: EditText
    private lateinit var editLocationOBJ: EditText
    private lateinit var editSkillsOBJ: EditText
    private lateinit var editPhoneOBJ: EditText
    private lateinit var sdh: SaveProfileDataHandler
    private lateinit var vibrator: Vibrator
    lateinit var photoURI: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_edit_profile)

        sdh = SaveProfileDataHandler(applicationContext)

        this.editFullNameOBJ = findViewById(R.id.edit_fullnameID)
        this.editNickNameOBJ = findViewById(R.id.edit_nicknameID)
        this.editQualificationOBJ = findViewById(R.id.edit_qualificationID)
        this.editDescriptionOBJ = findViewById(R.id.edit_description_show_ID)
        this.editEmailOBJ = findViewById(R.id.edit_email_show_ID)
        this.editLocationOBJ = findViewById(R.id.edit_loc_show_ID)
        this.editSkillsOBJ = findViewById(R.id.edit_skillsListID)
        this.editPhoneOBJ = findViewById(R.id.edit_phone_show_ID)

        this.editFullNameOBJ.setText(intent.getCharSequenceExtra("fullname"))
        this.editNickNameOBJ.setText(intent.getCharSequenceExtra("nickname"))
        this.editQualificationOBJ.setText(intent.getCharSequenceExtra("qualification"))
        this.editDescriptionOBJ.setText(intent.getCharSequenceExtra("description"))
        this.editEmailOBJ.setText(intent.getCharSequenceExtra("email"))
        this.editLocationOBJ.setText(intent.getCharSequenceExtra("location"))
        this.editSkillsOBJ.setText(intent.getCharSequenceExtra("skills"))
        this.editPhoneOBJ.setText(intent.getCharSequenceExtra("phone"))
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val cam: ImageView = findViewById(R.id.edit_camera_button)

        registerForContextMenu(cam)
        cam.setOnClickListener { openContextMenu(cam) }
    }

    val REQUEST_IMAGE_CAPTURE = 1
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File( storageDir,R.string.profile_picture_filename.toString())
    }

    val PICK_IMAGE = 100
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap=getBitmapFromUri(photoURI)
            findViewById<ImageView>(R.id.edit_profilePictureID).setImageBitmap(bitmap)
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            photoURI=data?.data!!
            getBitmapFromUri(photoURI)?.also {
                saveProfilePicture(it, getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString())
                findViewById<ImageView>(R.id.edit_profilePictureID).setImageBitmap(it)
            }
        }
    }
    fun getBitmapFromUri(imageURI:Uri): Bitmap? {
        contentResolver.notifyChange(imageURI, null)
        val cr = contentResolver
        val bitmap: Bitmap
        return try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, imageURI)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveProfilePicture(bitmap:Bitmap,dir:String){
        val imageFile=File(dir,R.string.profile_picture_filename.toString())
        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            inflater.inflate(R.menu.select_take_picture_floating_context_menu, menu)
        else
            inflater.inflate(R.menu.select_picture_floating_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            resources.getString(R.string.floating_menu_shot_picture) -> dispatchTakePictureIntent()
            resources.getString(R.string.floating_menu_load_picture) -> dispatchLoadPictureIntent()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_profile_main_menu, menu)
        return true
    }

    override fun onBackPressed() {
        val intent = Intent()
        val b = Bundle()

        b.putCharSequence("fullname", findViewById<EditText>(R.id.edit_fullnameID).text)
        b.putCharSequence("nickname", findViewById<EditText>(R.id.edit_nicknameID).text)
        b.putCharSequence(
            "qualification",
            findViewById<EditText>(R.id.edit_qualificationID).text
        )
        b.putCharSequence(
            "description",
            findViewById<EditText>(R.id.edit_description_show_ID).text
        )
        b.putCharSequence("email", findViewById<EditText>(R.id.edit_email_show_ID).text)
        b.putCharSequence("location", findViewById<EditText>(R.id.edit_loc_show_ID).text)
        b.putCharSequence("skills", findViewById<EditText>(R.id.edit_skillsListID).text)
        b.putCharSequence("phone", findViewById<EditText>(R.id.edit_phone_show_ID).text)

        intent.putExtras(b)
        val profile: Profile = Profile(
            this.editFullNameOBJ.text.toString(),
            this.editSkillsOBJ.text.toString(),
            this.editEmailOBJ.text.toString(),
            this.editLocationOBJ.text.toString(),
            this.editQualificationOBJ.text.toString(),
            this.editNickNameOBJ.text.toString(),
            this.editDescriptionOBJ.text.toString(),
            "null",
            this.editPhoneOBJ.text.toString()
        )
        this.sdh.storeData(profile)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(120)
        }
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.complete_user_editing -> {
                val intent = Intent()
                val b = Bundle()

                b.putCharSequence("fullname", this.editFullNameOBJ.text)
                b.putCharSequence("nickname", this.editNickNameOBJ.text)
                b.putCharSequence("qualification", this.editQualificationOBJ.text)
                b.putCharSequence("description", this.editDescriptionOBJ.text)
                b.putCharSequence("email", this.editEmailOBJ.text)
                b.putCharSequence("location", this.editLocationOBJ.text)
                b.putCharSequence("skills", this.editSkillsOBJ.text)
                b.putCharSequence("phone", this.editPhoneOBJ.text)

                intent.putExtras(b)
                val profile: Profile = Profile(
                    this.editFullNameOBJ.text.toString(),
                    this.editSkillsOBJ.text.toString(),
                    this.editEmailOBJ.text.toString(),
                    this.editLocationOBJ.text.toString(),
                    this.editQualificationOBJ.text.toString(),
                    this.editNickNameOBJ.text.toString(),
                    this.editDescriptionOBJ.text.toString(),
                    "null",
                    this.editPhoneOBJ.text.toString()
                )
                this.sdh.storeData(profile)
                setResult(Activity.RESULT_OK, intent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            120,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(120)
                }
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("fullname", this.editFullNameOBJ.text)
        outState.putCharSequence("nickname", this.editNickNameOBJ.text)
        outState.putCharSequence("qualification", this.editQualificationOBJ.text)
        outState.putCharSequence("description", this.editDescriptionOBJ.text)
        outState.putCharSequence("email", this.editEmailOBJ.text)
        outState.putCharSequence("location", this.editLocationOBJ.text)
        outState.putCharSequence("skills", this.editSkillsOBJ.text)
        outState.putCharSequence("phone", this.editPhoneOBJ.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.editFullNameOBJ.setText(savedInstanceState.getCharSequence("fullname"))
        this.editNickNameOBJ.setText(savedInstanceState.getCharSequence("nickname"))
        this.editQualificationOBJ.setText(savedInstanceState.getCharSequence("qualification"))
        this.editDescriptionOBJ.setText(savedInstanceState.getCharSequence("description"))
        this.editEmailOBJ.setText(savedInstanceState.getCharSequence("email"))
        this.editLocationOBJ.setText(savedInstanceState.getCharSequence("location"))
        this.editSkillsOBJ.setText(savedInstanceState.getCharSequence("skills"))
        this.editPhoneOBJ.setText(savedInstanceState.getCharSequence("phone"))
    }
}