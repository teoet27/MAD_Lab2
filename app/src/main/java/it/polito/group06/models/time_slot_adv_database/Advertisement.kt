package it.polito.group06.models.time_slot_adv_database

import java.util.*
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Advertisement] data class.
 * This data class contains the following information:
 * @param adsTitle The title of the advertisement
 * @param adsDescription A complete description of the work offered
 * @param adsLocation The location where the worker is available
 * @param adsDate Date and time of the timeslot offered
 * @param adsDuration The duration of the offered timeslot
 * @param adsAccount Name and Surname of the worker offering the timeslot
 */


@Entity(tableName = "advertisementTable")
data class Advertisement(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val adsTitle: String, val adsDescription: String,
    val adsLocation: String, val adsDate: String,
    val adsDuration: Float, val adsAccount: String,
    val isPrivate: Boolean
)

