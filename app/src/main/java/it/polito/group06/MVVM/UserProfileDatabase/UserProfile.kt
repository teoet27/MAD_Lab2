package it.polito.group06.MVVM.UserProfileDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "user_profile_table")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var nickname: String?,
    var fullName: String?,
    var qualification: String?,
    var description: String?,
    var email: String?,
    var phoneNumber: String?,
    var location: String?,
    var skills: ArrayList<String>?
)