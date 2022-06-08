package it.polito.madcourse.group06.models.advertisement

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import kotlin.collections.ArrayList

/**
 * [Advertisement] data class.
 * This data class contains the following information:
 * @param advTitle The title of the advertisement
 * @param advDescription A complete description of the work offered
 * @param advLocation The location where the worker is available
 * @param advDate Date and time of the timeslot offered
 * @param advStartingTime Starting time for the offered timeslot
 * @param advEndingTime Ending time for the offered timeslot
 * @param advDuration The duration of the offered timeslot
 * @param advAccount Name and Surname of the worker offering the timeslot
 */


@Entity(tableName = "advertisementTable")
data class Advertisement(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String?,

    @get:PropertyName("title")
    @set:PropertyName("title")
    var advTitle: String,

    @get:PropertyName("description")
    @set:PropertyName("description")
    var advDescription: String,

    @get:PropertyName("restrictions")
    @set:PropertyName("restrictions")
    var advRestrictions: String,

    @get:PropertyName("list_of_skills")
    @set:PropertyName("list_of_skills")
    var listOfSkills: ArrayList<String>,

    @get:PropertyName("location")
    @set:PropertyName("location")
    var advLocation: String,

    @get:PropertyName("date")
    @set:PropertyName("date")
    var advDate: String,

    @get:PropertyName("starting_time")
    @set:PropertyName("starting_time")
    var advStartingTime: String,

    @get:PropertyName("ending_time")
    @set:PropertyName("ending_time")
    var advEndingTime: String,

    @get:PropertyName("duration")
    @set:PropertyName("duration")
    var advDuration: Double,

    @get:PropertyName("account_name")
    @set:PropertyName("account_name")
    var advAccount: String,

    @get:PropertyName("accountID")
    @set:PropertyName("accountID")
    var accountID: String,

    @get:PropertyName("rx_user_id")
    @set:PropertyName("rx_user_id")
    var rxUserId: String?,

    @get:PropertyName("rating_user_id")
    @set:PropertyName("rating_user_id")
    var ratingUserId: String?,

    @get:PropertyName("active_at")
    @set:PropertyName("active_at")
    var activeAt: String?,

    @get:PropertyName("active_for")
    @set:PropertyName("active_for")
    var activeFor: Double,

    @get:PropertyName("active_location")
    @set:PropertyName("active_location")
    var activeLocation: String
)

