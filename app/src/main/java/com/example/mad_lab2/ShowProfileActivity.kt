package com.example.mad_lab2

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        sdh = SaveProfileDataHandler(applicationContext)
        var profile = sdh.retrieveData()

        if (profile == null) {
            profile = Profile(
                "Monkey D. Luffy",
                "Powerful Warrior, Obscure Powers",
                "luffy@mail.com",
                "The Great Sea",
                "Captain @ Going Merry",
                "@cappello_di_paglia",
                "Sono un pirata davvero potente che però in teoria non sa cosa scrivere quindi... Mangiamo?",
                "null",
                "3775268111"
            )
        }

        this.fullnameOBJ = findViewById(R.id.fullnameID)
        this.nicknameOBJ = findViewById(R.id.nicknameID)
        this.qualificationOBJ = findViewById(R.id.qualificationID)
        this.descriptionOBJ = findViewById(R.id.description_show_ID)
        this.emailOBJ = findViewById(R.id.email_show_ID)
        this.locationOBJ = findViewById(R.id.loc_show_ID)
        this.skillsOBJ = findViewById(R.id.skillsListID)
        this.phoneOBJ = findViewById(R.id.phone_show_ID)

        this.fullnameOBJ.text = profile?.fullname
        this.nicknameOBJ.text = profile?.nickname
        this.qualificationOBJ.text = profile?.qualification
        this.phoneOBJ.text = profile?.phoneNumber
        this.locationOBJ.text = profile?.location
        this.skillsOBJ.text = profile?.skills
        this.emailOBJ.text = profile?.email
        this.descriptionOBJ.text = profile?.description

    }

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

                b.putCharSequence("fullname", findViewById<TextView>(R.id.fullnameID).text)
                b.putCharSequence("nickname", findViewById<TextView>(R.id.nicknameID).text)
                b.putCharSequence(
                    "qualification",
                    findViewById<TextView>(R.id.qualificationID).text
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
            this.skillsOBJ.text =
                data?.getCharSequenceExtra("skills").toString()
            this.locationOBJ.text =
                data?.getCharSequenceExtra("location").toString()
            this.phoneOBJ.text =
                data?.getCharSequenceExtra("phone").toString()
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