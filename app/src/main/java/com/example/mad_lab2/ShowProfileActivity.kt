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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        val profile = Profile(
            "Monkey",
            "D. Luffy",
            "m.d.luffy@gmail.com",
            "Red Line - Ocean - World",
            "Captain",
            "@the_big_d"
        )

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.fullnameOBJ = findViewById(R.id.fullnameID2)
            this.nicknameOBJ = findViewById(R.id.nicknameID2)
            this.qualificationOBJ = findViewById(R.id.qualificationID2)
        } else
        {
            this.fullnameOBJ = findViewById(R.id.fullnameID)
            this.nicknameOBJ = findViewById(R.id.nicknameID)
            this.qualificationOBJ = findViewById(R.id.qualificationID)
        }
        this.fullnameOBJ.text = profile.name + " " + profile.surname
        this.nicknameOBJ.text = profile.nickname
        this.qualificationOBJ.text = profile.qualification
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
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    b.putCharSequence("fullname", findViewById<TextView>(R.id.fullnameID2).text)
                    b.putCharSequence("nickname", findViewById<TextView>(R.id.nicknameID2).text)
                    b.putCharSequence(
                        "qualification",
                        findViewById<TextView>(R.id.qualificationID2).text
                    )

                } else {
                    b.putCharSequence("fullname", findViewById<TextView>(R.id.fullnameID).text)
                    b.putCharSequence("nickname", findViewById<TextView>(R.id.nicknameID).text)
                    b.putCharSequence(
                        "qualification",
                        findViewById<TextView>(R.id.qualificationID).text
                    )
                }
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
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("fullname", this.fullnameOBJ.text)
        outState.putCharSequence("nickname", this.nicknameOBJ.text)
        outState.putCharSequence("qualification", this.qualificationOBJ.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.fullnameOBJ.text = savedInstanceState.getCharSequence("fullname")
        this.nicknameOBJ.text = savedInstanceState.getCharSequence("nickname")
        this.qualificationOBJ.text = savedInstanceState.getCharSequence("qualification")
    }
}