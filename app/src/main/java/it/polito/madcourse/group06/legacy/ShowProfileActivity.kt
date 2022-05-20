package it.polito.madcourse.group06.legacy

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
import it.polito.madcourse.group06.R

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
        setContentView(R.layout.fragment_show_profile)

        sdh = SaveProfileDataHandler(applicationContext)
        var profile = sdh.retrieveData()

        if (profile == null) {
            profile = Profile(
                "Guido Saracco",
                "Management of Students, Public Relationships, Chemistry, Teaching, R&D",
                "rettore@polito.it",
                "Torino - Italia",
                "Rector @ Politecnico di Torino",
                "rettore",
                "I'm the full time Rector of Politecnico di Torino. I used to be a Chemistry teacher in 1994, but eventually I ended up being an institutional figure in the teaching world.",
                "null",
                "3331112223"
            )
        }

        this.fullnameOBJ = findViewById(R.id.edit_fullname_ID)
        this.nicknameOBJ = findViewById(R.id.edit_nickname_ID)
        this.qualificationOBJ = findViewById(R.id.edit_qualification_ID)
        this.descriptionOBJ = findViewById(R.id.description_show_ID)
        this.emailOBJ = findViewById(R.id.email_show_ID)
        this.locationOBJ = findViewById(R.id.loc_show_ID)
        this.skillsOBJ = findViewById(R.id.skillsListID)
        this.phoneOBJ = findViewById(R.id.phone_show_ID)
        this.profilePictureOBJ = findViewById(R.id.profilePictureID)

        this.fullnameOBJ.text = profile.fullname

        if (profile.nickname.compareTo("") == 0) {
            this.nicknameOBJ.text = "No nickname provided."
        } else {
            this.nicknameOBJ.text = "@" + profile.nickname
        }
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

                b.putCharSequence("group06.lab2.fullname", findViewById<TextView>(R.id.edit_fullname_ID).text)
                b.putCharSequence("group06.lab2.nickname", findViewById<TextView>(R.id.edit_nickname_ID).text.toString().split("@")[1])
                b.putCharSequence("group06.lab2.qualification", findViewById<TextView>(R.id.edit_qualification_ID).text)
                b.putCharSequence("group06.lab2.phone", findViewById<TextView>(R.id.phone_show_ID).text)
                b.putCharSequence("group06.lab2.location", findViewById<TextView>(R.id.loc_show_ID).text)
                b.putCharSequence("group06.lab2.skills", findViewById<TextView>(R.id.skillsListID).text)
                b.putCharSequence("group06.lab2.email", findViewById<TextView>(R.id.email_show_ID).text)
                b.putCharSequence("group06.lab2.description", findViewById<TextView>(R.id.description_show_ID).text)

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
            this.fullnameOBJ.text = data?.getCharSequenceExtra("group06.lab2.fullname").toString()
            this.nicknameOBJ.text = "@" + data?.getCharSequenceExtra("group06.lab2.nickname").toString()
            this.qualificationOBJ.text = data?.getCharSequenceExtra("group06.lab2.qualification").toString()
            this.descriptionOBJ.text = data?.getCharSequenceExtra("group06.lab2.description").toString()
            this.emailOBJ.text = data?.getCharSequenceExtra("group06.lab2.email").toString()
            if (data?.getCharSequenceExtra("group06.lab2.skills")?.length == 0 ||
                data?.getCharSequenceExtra("group06.lab2.skills").toString().split(" ").size == data?.getCharSequenceExtra("group06.lab2.skills").toString().length + 1
            ) {
                this.skillsOBJ.setText(R.string.noskills)
            } else {
                this.skillsOBJ.text =
                    data?.getCharSequenceExtra("group06.lab2.skills").toString()
            }
            this.locationOBJ.text = data?.getCharSequenceExtra("group06.lab2.location").toString()
            this.phoneOBJ.text = data?.getCharSequenceExtra("group06.lab2.phone").toString()
            getBitmapFromFile(profilePicturePath)?.also {
                this.profilePictureOBJ.setImageBitmap(it)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("group06.lab2.fullname", this.fullnameOBJ.text)
        outState.putCharSequence("group06.lab2.nickname", this.nicknameOBJ.text)
        outState.putCharSequence("group06.lab2.qualification", this.qualificationOBJ.text)
        outState.putCharSequence("group06.lab2.location", this.locationOBJ.text)
        outState.putCharSequence("group06.lab2.email", this.emailOBJ.text)
        outState.putCharSequence("group06.lab2.phone", this.phoneOBJ.text)
        outState.putCharSequence("group06.lab2.description", this.descriptionOBJ.text)
        outState.putCharSequence("group06.lab2.skills", this.skillsOBJ.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.fullnameOBJ.text = savedInstanceState.getCharSequence("group06.lab2.fullname")
        this.nicknameOBJ.text = savedInstanceState.getCharSequence("group06.lab2.nickname")
        this.qualificationOBJ.text = savedInstanceState.getCharSequence("group06.lab2.qualification")
        this.locationOBJ.text = savedInstanceState.getCharSequence("group06.lab2.location")
        this.emailOBJ.text = savedInstanceState.getCharSequence("group06.lab2.email")
        this.phoneOBJ.text = savedInstanceState.getCharSequence("group06.lab2.phone")
        this.descriptionOBJ.text = savedInstanceState.getCharSequence("group06.lab2.description")
        this.skillsOBJ.text = savedInstanceState.getCharSequence("group06.lab2.skills")
    }
}