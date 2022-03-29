package com.example.mad_lab2

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_edit_profile)

        sdh = SaveProfileDataHandler(applicationContext)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.editFullNameOBJ = findViewById(R.id.edit_fullnameID_land)
            this.editNickNameOBJ = findViewById(R.id.edit_nicknameID_land)
            this.editQualificationOBJ = findViewById(R.id.edit_qualificationID_land)
            this.editDescriptionOBJ = findViewById(R.id.edit_description_show_ID_land)
            this.editEmailOBJ = findViewById(R.id.edit_email_show_ID_land)
            this.editLocationOBJ = findViewById(R.id.edit_loc_show_ID_land)
            this.editSkillsOBJ = findViewById(R.id.edit_skillsListID_land)
            this.editPhoneOBJ = findViewById(R.id.edit_phone_show_ID_land)
        } else {
            this.editFullNameOBJ = findViewById(R.id.edit_fullnameID)
            this.editNickNameOBJ = findViewById(R.id.edit_nicknameID)
            this.editQualificationOBJ = findViewById(R.id.edit_qualificationID)
            this.editDescriptionOBJ = findViewById(R.id.edit_description_show_ID)
            this.editEmailOBJ = findViewById(R.id.edit_email_show_ID)
            this.editLocationOBJ = findViewById(R.id.edit_loc_show_ID)
            this.editSkillsOBJ = findViewById(R.id.edit_skillsListID)
            this.editPhoneOBJ = findViewById(R.id.edit_phone_show_ID)
        }

        this.editFullNameOBJ.setText(intent.getCharSequenceExtra("fullname"))
        this.editNickNameOBJ.setText(intent.getCharSequenceExtra("nickname"))
        this.editQualificationOBJ.setText(intent.getCharSequenceExtra("qualification"))
        this.editDescriptionOBJ.setText(intent.getCharSequenceExtra("description"))
        this.editEmailOBJ.setText(intent.getCharSequenceExtra("email"))
        this.editLocationOBJ.setText(intent.getCharSequenceExtra("location"))
        this.editSkillsOBJ.setText(intent.getCharSequenceExtra("skills"))
        this.editPhoneOBJ.setText(intent.getCharSequenceExtra("phone"))
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
            b.putCharSequence("fullname", findViewById<EditText>(R.id.edit_fullnameID_land).text)
            b.putCharSequence("nickname", findViewById<EditText>(R.id.edit_nicknameID_land).text)
            b.putCharSequence(
                "qualification",
                findViewById<EditText>(R.id.edit_qualificationID_land).text
            )
            b.putCharSequence("description", findViewById<EditText>(R.id.edit_description_show_ID_land).text)
            b.putCharSequence("email", findViewById<EditText>(R.id.edit_email_show_ID_land).text)
            b.putCharSequence("location", findViewById<EditText>(R.id.edit_loc_show_ID_land).text)
            b.putCharSequence("skills", findViewById<EditText>(R.id.edit_skillsListID_land).text)
            b.putCharSequence("phone", findViewById<EditText>(R.id.edit_phone_show_ID_land).text)
        } else {
            b.putCharSequence("fullname", findViewById<EditText>(R.id.edit_fullnameID).text)
            b.putCharSequence("nickname", findViewById<EditText>(R.id.edit_nicknameID).text)
            b.putCharSequence(
                "qualification",
                findViewById<EditText>(R.id.edit_qualificationID).text
            )
            b.putCharSequence("description", findViewById<EditText>(R.id.edit_description_show_ID).text)
            b.putCharSequence("email", findViewById<EditText>(R.id.edit_description_show_ID).text)
            b.putCharSequence("location", findViewById<EditText>(R.id.edit_loc_show_ID).text)
            b.putCharSequence("skills", findViewById<EditText>(R.id.edit_skillsListID).text)
            b.putCharSequence("phone", findViewById<EditText>(R.id.edit_phone_show_ID).text)
        }
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