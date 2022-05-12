package it.polito.group06.models.userprofile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userProfileTable")
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