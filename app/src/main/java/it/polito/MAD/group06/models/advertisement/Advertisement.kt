package it.polito.MAD.group06.models.advertisement

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

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
    var id: Long?,

    @get:PropertyName("title")
    @set:PropertyName("title")
    var advTitle: String,

    @get:PropertyName("description")
    @set:PropertyName("description")
    var advDescription: String,


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
    var advDuration: Float,


    @get:PropertyName("account")
    @set:PropertyName("account")
    var advAccount: String,


    @get:PropertyName("accountID")
    @set:PropertyName("accountID")
    var accountID: Int
)

