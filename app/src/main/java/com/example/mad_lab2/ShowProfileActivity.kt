package com.example.mad_lab2

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        val profile = Profile("Monkey", "D. Luffy", "m.d.luffy@gmail.com", "Red Line - Ocean - World", "Captain", "@the_big_d")
        val fullnameOBJ: TextView
        val nicknameOBJ: TextView
        val qualificationOBJ: TextView
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            fullnameOBJ = findViewById(R.id.fullnameID2)
            nicknameOBJ = findViewById(R.id.nicknameID2)
            qualificationOBJ = findViewById(R.id.qualificationID2)
        }
        else // (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            fullnameOBJ = findViewById(R.id.fullnameID)
            nicknameOBJ = findViewById(R.id.nicknameID)
            qualificationOBJ = findViewById(R.id.qualificationID)
        }
        fullnameOBJ.text = profile.name + " " + profile.surname
        nicknameOBJ.text = profile.nickname
        qualificationOBJ.text = profile.qualification
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
                intent.putExtra("fullname", findViewById<TextView>(R.id.fullnameID).text)
                intent.putExtra("nickname", findViewById<TextView>(R.id.nicknameID).text)
                intent.putExtra("qualification", findViewById<TextView>(R.id.qualificationID).text)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}