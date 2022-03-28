package com.example.mad_lab2

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var editFullNameOBJ: EditText
    private lateinit var editNickNameOBJ: EditText
    private lateinit var editQualificationOBJ: EditText
    private lateinit var sdh: SaveProfileDataHandler
    lateinit var currentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_edit_profile)

        sdh = SaveProfileDataHandler(applicationContext)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.editFullNameOBJ = findViewById<EditText>(R.id.EDITfullnameID2)
            this.editNickNameOBJ = findViewById<EditText>(R.id.EDITnicknameID2)
            this.editQualificationOBJ = findViewById<EditText>(R.id.EDITqualificationID2)
        } else {
            this.editFullNameOBJ = findViewById<EditText>(R.id.EDITfullnameID)
            this.editNickNameOBJ = findViewById<EditText>(R.id.EDITnicknameID)
            this.editQualificationOBJ = findViewById<EditText>(R.id.EDITqualificationID)
        }

        this.editFullNameOBJ.setText(intent.getCharSequenceExtra("fullname"))
        this.editNickNameOBJ.setText(intent.getCharSequenceExtra("nickname"))
        this.editQualificationOBJ.setText(intent.getCharSequenceExtra("qualification"))

        val cam: ImageView=findViewById(R.id.camera_button)
        registerForContextMenu(cam)
        cam.setOnClickListener{openContextMenu(cam)}
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            inflater.inflate(R.menu.select_take_picture_floating_context_menu, menu)
        else
            inflater.inflate(R.menu.select_picture_floating_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.title){
            resources.getString(R.string.floating_menu_shot_picture) -> dispatchTakePictureIntent()
            resources.getString(R.string.floating_menu_load_picture) -> dispatchLoadPictureIntent()
        }
        return true
    }

    val REQUEST_IMAGE_CAPTURE = 1
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
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

    val PICK_IMAGE = 100
    private fun dispatchLoadPictureIntent() {
        val loadPictureIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(loadPictureIntent, PICK_IMAGE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            findViewById<ImageView>(R.id.edit_user_image).setImageBitmap(imageBitmap)
            //save image in proper location and load on ShowProfileActivity
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_profile_main_menu, menu)
        return true
    }

    override fun onBackPressed() {
        val intent = Intent()
        val b = Bundle()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            b.putCharSequence("fullname", findViewById<EditText>(R.id.EDITfullnameID2).text)
            b.putCharSequence("nickname", findViewById<EditText>(R.id.EDITnicknameID2).text)
            b.putCharSequence(
                "qualification",
                findViewById<EditText>(R.id.EDITqualificationID2).text
            )
        } else {
            b.putCharSequence("fullname", findViewById<EditText>(R.id.EDITfullnameID).text)
            b.putCharSequence("nickname", findViewById<EditText>(R.id.EDITnicknameID).text)
            b.putCharSequence(
                "qualification",
                findViewById<EditText>(R.id.EDITqualificationID).text
            )
        }
        intent.putExtras(b)
        val profile: Profile = Profile(
            this.editFullNameOBJ.text.toString(),
            "",
            "",
            this.editQualificationOBJ.text.toString(),
            this.editNickNameOBJ.text.toString(),
            ""
        )
        this.sdh.storeData(profile)
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

                intent.putExtras(b)
                val profile: Profile = Profile(
                    this.editFullNameOBJ.text.toString(),
                    "",
                    "",
                    this.editQualificationOBJ.text.toString(),
                    this.editNickNameOBJ.text.toString(),
                    ""
                )
                this.sdh.storeData(profile)
                setResult(Activity.RESULT_OK, intent)
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
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.editFullNameOBJ.setText(savedInstanceState.getCharSequence("fullname"))
        this.editNickNameOBJ.setText(savedInstanceState.getCharSequence("nickname"))
        this.editQualificationOBJ.setText(savedInstanceState.getCharSequence("qualification"))
    }
}
