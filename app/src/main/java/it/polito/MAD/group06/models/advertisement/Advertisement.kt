package it.polito.MAD.group06.models.advertisement

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var advTitle: String, var advDescription: String,
    var advLocation: String, var advDate: String,
    var advStartingTime: String, var advEndingTime: String,
    var advDuration: Float, var advAccount: String,
    var isPrivate: Boolean
)

