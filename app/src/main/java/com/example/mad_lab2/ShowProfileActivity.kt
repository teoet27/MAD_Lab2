package com.example.mad_lab2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class ShowProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        val profile = Profile("Monkey", "D. Luffy", "m.d.luffy@gmail.com", "Red Line - Ocean - World", "Captain", "@the_big_d")
        val fullnameOBJ = findViewById<TextView>(R.id.fullnameID)
        val nicknameOBJ = findViewById<TextView>(R.id.nicknameID)
        val qualificationOBJ = findViewById<TextView>(R.id.qualificationID)
        fullnameOBJ.text = profile.name + " " + profile.surname
        nicknameOBJ.text = profile.nickname
        qualificationOBJ.text = profile.qualification
    }
}