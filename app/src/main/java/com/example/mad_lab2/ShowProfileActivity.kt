package com.example.mad_lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.mad_lab2.Profile

class ShowProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        val profile:Profile = Profile("Sibilla", "Sagristano")
        val fullnameOBJ = findViewById<TextView>(R.id.fullnameID)
        val nicknameOBJ = findViewById<TextView>(R.id.nicknameID)
        val qualificationOBJ = findViewById<TextView>(R.id.qualificationID)
        fullnameOBJ.text = profile.name + " " + profile.surname
        nicknameOBJ.text = profile.nickname
        qualificationOBJ.text = profile.qualification
    }
}