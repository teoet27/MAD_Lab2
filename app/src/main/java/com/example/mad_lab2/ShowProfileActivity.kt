package com.example.mad_lab2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class ShowProfileActivity : AppCompatActivity() {
    private lateinit var fullnameOBJ: TextView
    private lateinit var nicknameOBJ: TextView
    private lateinit var qualificationOBJ: TextView
    private lateinit var descriptionOBJ: TextView
    private lateinit var emailOBJ: TextView
    private lateinit var locationOBJ: TextView
    private lateinit var skillsOBJ: TextView
    private lateinit var phoneOBJ: TextView
    private lateinit var sdh: SaveProfileDataHandler
    private lateinit var profilePictureOBJ: ImageView

    private lateinit var profilePictureDirectoryPath: String
    private lateinit var profilePicturePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.show_profile_activity_main)

        sdh = SaveProfileDataHandler(applicationContext)
        var profile = sdh.retrieveData()

        if (profile == null) {
            profile = Profile(
                "Monkey D. Luffy",
                "Super Punch, The Mysterious D. Power",
                "luffy@mail.com",
                "The Great Sea",
                "Captain @ Going Merry",
                "@cappello_di_paglia",
                "Sono un pirata davvero potente che però in teoria non sa cosa scrivere quindi... Mangiamo?",
                "null",
                "3775268111"
            )
        }

        this.fullnameOBJ = findViewById(R.id.edit_fullnameID)
        this.nicknameOBJ = findViewById(R.id.edit_nicknameID)
        this.qualificationOBJ = findViewById(R.id.edit_qualificationID)
        this.descriptionOBJ = findViewById(R.id.description_show_ID)
        this.emailOBJ = findViewById(R.id.email_show_ID)
        this.locationOBJ = findViewById(R.id.loc_show_ID)
        this.skillsOBJ = findViewById(R.id.skillsListID)
        this.phoneOBJ = findViewById(R.id.phone_show_ID)
        this.profilePictureOBJ = findViewById(R.id.edit_profilePictureID)

        this.fullnameOBJ.text = profile.fullname
        this.nicknameOBJ.text = profile.nickname
        this.qualificationOBJ.text = profile.qualification
        this.phoneOBJ.text = profile.phoneNumber
        this.locationOBJ.text = profile.location
        if (profile.skillList.size == 0) {
            this.skillsOBJ.setText(R.string.noskills)
        } else {
            this.skillsOBJ.text = fromArrayListToString(profile.skillList)
        }
        this.emailOBJ.text = profile.email
        this.descriptionOBJ.text = profile.description

        profilePicturePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + '/' + resources.getString(R.string.profile_picture_filename)
        profilePictureDirectoryPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()

        getBitmapFromFile(profilePicturePath)?.also {
            this.profilePictureOBJ.setImageBitmap(it)
        }
    }

    /**
     * This method returns the Bitmap image from a file whose path is provided
     *
     * @param path  The path were to find the file
     * @return Bitmap image results
     */
    fun getBitmapFromFile(path: String): Bitmap? {
        return BitmapFactory.decodeFile(path)
    }

    /**
     * This method inflates the option menu
     *
     * @param menu  Menu to be inflated
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.show_profile_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_user_profile -> {
                // switch to edit mode
                val intent = Intent(this, EditProfileActivity::class.java)
                val b = Bundle()

                b.putCharSequence("fullname", findViewById<TextView>(R.id.edit_fullnameID).text)
                b.putCharSequence("nickname", findViewById<TextView>(R.id.edit_nicknameID).text)
                b.putCharSequence(
                    "qualification",
                    findViewById<TextView>(R.id.edit_qualificationID).text
                )
                b.putCharSequence("phone", findViewById<TextView>(R.id.phone_show_ID).text)
                b.putCharSequence("location", findViewById<TextView>(R.id.loc_show_ID).text)
                b.putCharSequence("skills", findViewById<TextView>(R.id.skillsListID).text)
                b.putCharSequence("email", findViewById<TextView>(R.id.email_show_ID).text)
                b.putCharSequence(
                    "description",
                    findViewById<TextView>(R.id.description_show_ID).text
                )

                intent.putExtras(b)
                startActivityForResult(intent, 1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This utility method changes an array list into a string with a specific format
     *
     * @param list  List of skills
     * @return string of skills
     */
    private fun fromArrayListToString(list: ArrayList<String>): String {
        var out = ""
        for (i in list.indices) {
            out += list[i]
            if (i != list.size - 1)
                out += ", "
        }
        return out
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            this.fullnameOBJ.text =
                data?.getCharSequenceExtra("fullname").toString()
            this.nicknameOBJ.text =
                data?.getCharSequenceExtra("nickname").toString()
            this.qualificationOBJ.text =
                data?.getCharSequenceExtra("qualification").toString()
            this.descriptionOBJ.text =
                data?.getCharSequenceExtra("description").toString()
            this.emailOBJ.text =
                data?.getCharSequenceExtra("email").toString()
            if (data?.getCharSequenceExtra("skills")?.length == 0 ||
                data?.getCharSequenceExtra("skills").toString().split(" ").size == data?.getCharSequenceExtra("skills").toString().length + 1
            ) {
                this.skillsOBJ.setText(R.string.noskills)
            } else {
                this.skillsOBJ.text =
                    data?.getCharSequenceExtra("skills").toString()
            }
            this.locationOBJ.text =
                data?.getCharSequenceExtra("location").toString()
            this.phoneOBJ.text =
                data?.getCharSequenceExtra("phone").toString()
            getBitmapFromFile(profilePicturePath)?.also {
                this.profilePictureOBJ.setImageBitmap(it)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("fullname", this.fullnameOBJ.text)
        outState.putCharSequence("nickname", this.nicknameOBJ.text)
        outState.putCharSequence("qualification", this.qualificationOBJ.text)
        outState.putCharSequence("location", this.locationOBJ.text)
        outState.putCharSequence("email", this.emailOBJ.text)
        outState.putCharSequence("phone", this.phoneOBJ.text)
        outState.putCharSequence("description", this.descriptionOBJ.text)
        outState.putCharSequence("skills", this.skillsOBJ.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.fullnameOBJ.text = savedInstanceState.getCharSequence("fullname")
        this.nicknameOBJ.text = savedInstanceState.getCharSequence("nickname")
        this.qualificationOBJ.text = savedInstanceState.getCharSequence("qualification")
        this.locationOBJ.text = savedInstanceState.getCharSequence("location")
        this.emailOBJ.text = savedInstanceState.getCharSequence("email")
        this.phoneOBJ.text = savedInstanceState.getCharSequence("phone")
        this.descriptionOBJ.text = savedInstanceState.getCharSequence("description")
        this.skillsOBJ.text = savedInstanceState.getCharSequence("skills")
    }
}