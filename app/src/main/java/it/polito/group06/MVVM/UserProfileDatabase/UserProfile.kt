package it.polito.group06.MVVM.UserProfileDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_slot_advertisement_table")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id:Long?,
    val nickname:String?,
    val fullName: String?,
    val qualification: String?,
    val skills: List<String>,
    val description: String?,
    val email: String?,
    val phoneNumber: String?,
    val location: String?
)