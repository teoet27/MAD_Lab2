package it.polito.group06.models.userlogin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userProfileTable")
data class UserLogin(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var email: String?,
    var password: String?
)