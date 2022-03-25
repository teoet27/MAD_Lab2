package com.example.mad_lab2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val editFullNameOBJ = findViewById<EditText>(R.id.EDITfullnameID)
        val editNickNameOBJ = findViewById<EditText>(R.id.EDITnicknameID)
        val editQualificationOBJ = findViewById<EditText>(R.id.EDITqualificationID)

        editFullNameOBJ.setText(intent.getCharSequenceExtra("fullname"))
        editNickNameOBJ.setText(intent.getCharSequenceExtra("nickname"))
        editQualificationOBJ.setText(intent.getCharSequenceExtra("qualification"))
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