package it.polito.group06.models.time_slot_adv_database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Advertisement] data class.
 * This data class contains the following information:
 * @param advTitle The title of the advertisement
 * @param advDescription A complete description of the work offered
 * @param advLocation The location where the worker is available
 * @param advDate Date and time of the timeslot offered
 * @param advDuration The duration of the offered timeslot
 * @param advAccount Name and Surname of the worker offering the timeslot
 */


@Entity(tableName = "advertisementTable")
data class Advertisement(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var advTitle: String, var advDescription: String,
    var advLocation: String, var advDate: String,
    var advDuration: Float, var advAccount: String,
    var isPrivate: Boolean
)

