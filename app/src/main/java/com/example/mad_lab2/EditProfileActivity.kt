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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_edit_profile)

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_profile_main_menu, menu)
        return true
    }

    override fun onBackPressed() {
        val intent = Intent()
        val b = Bundle()
        b.putCharSequence("fullname", findViewById<EditText>(R.id.EDITfullnameID).text)
        b.putCharSequence("nickname", findViewById<EditText>(R.id.EDITnicknameID).text)
        b.putCharSequence("qualification", findViewById<EditText>(R.id.EDITqualificationID).text)
        intent.putExtras(b)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.complete_user_editing -> {
                val intent = Intent()
                val b = Bundle()
                b.putCharSequence("fullname", findViewById<EditText>(R.id.EDITfullnameID).text)
                b.putCharSequence("nickname", findViewById<EditText>(R.id.EDITnicknameID).text)
                b.putCharSequence(
                    "qualification",
                    findViewById<EditText>(R.id.EDITqualificationID).text
                )
                intent.putExtras(b)
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}