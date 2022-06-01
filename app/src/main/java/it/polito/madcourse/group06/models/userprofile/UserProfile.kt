package it.polito.madcourse.group06.models.userprofile

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "userProfileTable")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String?,

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
    var skills: ArrayList<String>?,

    @get:PropertyName("credit")
    @set:PropertyName("credit")
    var credit: Double,

    @get:PropertyName("rating_sum")
    @set:PropertyName("rating_sum")
    var rating_sum: Double,

    @get:PropertyName("n_ratings")
    @set:PropertyName("n_ratings")
    var n_ratings: Double,

    @get:PropertyName("comments_services_rx")
    @set:PropertyName("comments_services_rx")
    var comments_services_rx: ArrayList<String>?,

    @get:PropertyName("comments_services_done")
    @set:PropertyName("comments_services_done")
    var comments_services_done: ArrayList<String>?,

    @get:PropertyName("img_path")
    @set:PropertyName("img_path")
    var imgPath: String?,

    @get:PropertyName("saved_ads_ids")
    @set:PropertyName("saved_ads_ids")
    var saved_ads_ids: ArrayList<String>?,

    @get:PropertyName("active_ads_ids")
    @set:PropertyName("active_ads_ids")
    var active_ads_ids: ArrayList<String>?
)