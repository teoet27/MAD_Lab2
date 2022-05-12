package it.polito.group06.models.userprofile

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ArrayListConverter {
    @TypeConverter
    fun fromArrayList(list: ArrayList<String>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toArrayList(s: String?): ArrayList<String>? {
        val collectionType: Type = object : TypeToken<ArrayList<String>?>() {}.type
        return Gson().fromJson(s, collectionType)
    }
}