package com.example.mad_lab2

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import java.io.*


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
    private lateinit var profilePictureOBJ: ImageView
    private lateinit var photoURI: Uri
    private lateinit var profilePictureDirectoryPath: String
    private lateinit var profilePicturePath: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.edit_profile_activity_main)

        sdh = SaveProfileDataHandler(applicationContext)

        this.editFullNameOBJ = findViewById(R.id.edit_fullnameID)
        this.editNickNameOBJ = findViewById(R.id.edit_nicknameID)
        this.editQualificationOBJ = findViewById(R.id.edit_qualificationID)
        this.editDescriptionOBJ = findViewById(R.id.edit_description_show_ID)
        this.editEmailOBJ = findViewById(R.id.edit_email_show_ID)
        this.editLocationOBJ = findViewById(R.id.edit_loc_show_ID)
        this.editSkillsOBJ = findViewById(R.id.edit_skillsListID)
        this.editPhoneOBJ = findViewById(R.id.edit_phone_show_ID)
        this.profilePictureOBJ = findViewById(R.id.edit_profilePictureID)

        this.editFullNameOBJ.setText(intent.getCharSequenceExtra("fullname"))
        this.editNickNameOBJ.setText(intent.getCharSequenceExtra("nickname"))
        this.editQualificationOBJ.setText(intent.getCharSequenceExtra("qualification"))
        this.editDescriptionOBJ.setText(intent.getCharSequenceExtra("description"))
        this.editEmailOBJ.setText(intent.getCharSequenceExtra("email"))
        this.editLocationOBJ.setText(intent.getCharSequenceExtra("location"))
        if (intent.getCharSequenceExtra("skills")?.toString()?.compareTo("No skills.") == 0)
            this.editSkillsOBJ.setText("")
        else
            this.editSkillsOBJ.setText(intent.getCharSequenceExtra("skills"))
        this.editPhoneOBJ.setText(intent.getCharSequenceExtra("phone"))
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        profilePicturePath =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + '/' + resources.getString(
                R.string.profile_picture_filename
            )
        profilePictureDirectoryPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()

        getBitmapFromFile(profilePicturePath)?.also {
            this.profilePictureOBJ.setImageBitmap(it)
        }

        val cam: ImageView = findViewById(R.id.edit_camera_button)

        registerForContextMenu(cam)
        cam.setOnClickListener { openContextMenu(cam) }
    }

    fun getBitmapFromFile(path: String): Bitmap? {
        return BitmapFactory.decodeFile(path)
    }


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
        return File(profilePicturePath)
    }

    private fun dispatchLoadPictureIntent() {
        val loadPictureIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(loadPictureIntent, PICK_IMAGE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val rotatedImage: Bitmap?

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            rotatedImage = handleSamplingAndRotationBitmap(applicationContext, this.photoURI)!!
            saveProfilePicture(rotatedImage!!, profilePictureDirectoryPath)
            findViewById<ImageView>(R.id.edit_profilePictureID).setImageBitmap(rotatedImage)
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            this.photoURI = data?.data!!
            rotatedImage = handleSamplingAndRotationBitmap(applicationContext, this.photoURI)!!
            saveProfilePicture(rotatedImage!!, profilePictureDirectoryPath)
            findViewById<ImageView>(R.id.edit_profilePictureID).setImageBitmap(rotatedImage)
        }
    }

    private fun saveProfilePicture(bitmap: Bitmap, dir: String) {
        val imageFile = File(dir, "profile_picture.jpg")
        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) ?: throw IOException()
            stream.flush()
            stream.close()
        } catch (e: IOException) {
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

        val profile: Profile
        if (this.editSkillsOBJ.text.toString().compareTo("") == 0) {
            profile = Profile(
                this.editFullNameOBJ.text.toString(),
                this.editEmailOBJ.text.toString(),
                this.editLocationOBJ.text.toString(),
                this.editQualificationOBJ.text.toString(),
                this.editNickNameOBJ.text.toString(),
                this.editDescriptionOBJ.text.toString(),
                this.profilePicturePath,
                this.editPhoneOBJ.text.toString()
            )
        } else {
            profile = Profile(
                this.editFullNameOBJ.text.toString(),
                this.editSkillsOBJ.text.toString(),
                this.editEmailOBJ.text.toString(),
                this.editLocationOBJ.text.toString(),
                this.editQualificationOBJ.text.toString(),
                this.editNickNameOBJ.text.toString(),
                this.editDescriptionOBJ.text.toString(),
                this.profilePicturePath,
                this.editPhoneOBJ.text.toString()
            )
        }

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

                val profile: Profile
                if (this.editSkillsOBJ.text.toString().compareTo("") == 0) {
                    profile = Profile(
                        this.editFullNameOBJ.text.toString(),
                        this.editEmailOBJ.text.toString(),
                        this.editLocationOBJ.text.toString(),
                        this.editQualificationOBJ.text.toString(),
                        this.editNickNameOBJ.text.toString(),
                        this.editDescriptionOBJ.text.toString(),
                        this.profilePicturePath,
                        this.editPhoneOBJ.text.toString()
                    )
                } else {
                    profile = Profile(
                        this.editFullNameOBJ.text.toString(),
                        this.editSkillsOBJ.text.toString(),
                        this.editEmailOBJ.text.toString(),
                        this.editLocationOBJ.text.toString(),
                        this.editQualificationOBJ.text.toString(),
                        this.editNickNameOBJ.text.toString(),
                        this.editDescriptionOBJ.text.toString(),
                        this.profilePicturePath,
                        this.editPhoneOBJ.text.toString()
                    )
                }

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
        getBitmapFromFile(profilePicturePath)?.also {
            this.profilePictureOBJ.setImageBitmap(it)
        }
    }

    //SUPPORT METHODS FOR ROTATING IMAGES///////////////////////////////////////////////////////////
    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    @Throws(IOException::class)
    fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri?): Bitmap? {
        val MAX_HEIGHT = 1024
        val MAX_WIDTH = 1024

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var imageStream: InputStream? = context.contentResolver.openInputStream(selectedImage!!)
        BitmapFactory.decodeStream(imageStream, null, options)
        imageStream?.close()

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        imageStream = context.contentResolver.openInputStream(selectedImage)
        var img = BitmapFactory.decodeStream(imageStream, null, options)
        img = img?.let { rotateImageIfRequired(context, it, selectedImage) }
        return img
    }

    /**
     * Calculate an inSampleSize for use in a [BitmapFactory.Options] object when decoding
     * bitmaps using the decode* methods from [BitmapFactory]. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     * method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).
            val totalPixels = (width * height).toFloat()

            // Anything more than 2x the requested pixels we'll sample down further
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
        }
        return inSampleSize
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    @Throws(IOException::class)
    private fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(selectedImage)
        val ei: ExifInterface
        ei =
            if (Build.VERSION.SDK_INT > 23) ExifInterface(input!!) else ExifInterface(selectedImage.path!!)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }
}