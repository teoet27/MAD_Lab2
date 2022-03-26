package com.example.mad_lab2

import org.json.JSONObject
import android.content.Context

class SaveProfileDataHandler(context: Context) {
    private val jsonObject = JSONObject()
    private val sharedPrefereces =
        context.getSharedPreferences("com.example.mad_lab2_profileData", Context.MODE_PRIVATE)

    private fun putIntoJSON(profile: Profile?) {
        if (profile != null)
            this.jsonObject.put("profile", profile)
    }

    private fun getFromJSON(): Profile? {
        return this.jsonObject.get("profile") as Profile?
    }

    public fun storeData(profile: Profile?) {
        putIntoJSON(profile)
        sharedPrefereces.edit().putString("profile", jsonObject.toString()).apply()
    }

    public fun retrieveData(): Profile? {
        val tmp = sharedPrefereces.getString("profile", null) ?: return null
        jsonObject.put("profile", JSONObject(tmp))
        return getFromJSON()
    }
}