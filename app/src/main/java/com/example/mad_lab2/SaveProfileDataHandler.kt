package com.example.mad_lab2

import android.content.Context
import com.google.gson.Gson

class SaveProfileDataHandler(context: Context) {
    private val sharedPrefereces =
        context.getSharedPreferences("com.example.mad_lab2_profileData", Context.MODE_PRIVATE)

    public fun storeData(profile: Profile?) {
        sharedPrefereces.edit().putString("profile", Gson().toJson(profile)).apply()
    }

    public fun retrieveData(): Profile? {
        return Gson().fromJson(sharedPrefereces.getString("profile", null),Profile::class.java)?:null
    }
}