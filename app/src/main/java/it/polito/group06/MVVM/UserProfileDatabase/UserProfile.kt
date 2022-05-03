package it.polito.group06.MVVM.UserProfileDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "user_profile_table")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id:Long?,
    val nickname:String?,
    var fullName: String?,
    val qualification: String?,
    val description: String?,
    val email: String?,
    val phoneNumber: String?,
    val location: String?,
    val skills: ArrayList<String>?
    )