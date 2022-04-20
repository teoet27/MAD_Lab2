package it.polito.group06.MVVM.TimeSlotAdvDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_slot_advertisement_table")
data class TimeSlotAd(
    @PrimaryKey(autoGenerate = true) val id:Long,
    val title: String?,
    val description: String?,
    val duration: String?,
    val location: String?,
)