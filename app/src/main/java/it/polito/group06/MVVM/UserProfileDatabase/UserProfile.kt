package it.polito.group06.MVVM.UserProfileDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile_table")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id:Long?,
    val nickname:String?,
    val fullName: String?,
    val qualification: String?,
    val skills: ArrayList<String>,
    val description: String?,
    val email: String?,
    val phoneNumber: String?,
    val location: String?
)