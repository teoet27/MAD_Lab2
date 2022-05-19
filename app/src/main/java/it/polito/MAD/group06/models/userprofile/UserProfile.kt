package it.polito.MAD.group06.models.userprofile

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import it.polito.MAD.group06.models.skill.Skill

@Entity(tableName = "userProfileTable")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: Long?,

    @get:PropertyName("nickname")
    @set:PropertyName("nickname")
    var nickname: String?,

    @get:PropertyName("fullname")
    @set:PropertyName("fullname")
    var fullName: String?,

    @get:PropertyName("qualification")
    @set:PropertyName("qualification")
    var qualification: String?,

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String?,

    @get:PropertyName("email")
    @set:PropertyName("email")
    var email: String?,

    @get:PropertyName("phone_number")
    @set:PropertyName("phone_number")
    var phoneNumber: String?,

    @get:PropertyName("location")
    @set:PropertyName("location")
    var location: String?,

    @get:PropertyName("skills")
    @set:PropertyName("skills")
    var skills: ArrayList<String>?
)