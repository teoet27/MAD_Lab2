package it.polito.group06.models.time_slot_adv_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimeSlotAdDao {

    /** Add(Update) to database **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAd(timeSlotAd: TimeSlotAd)

    /** Read from database **/
    @Query("SELECT * FROM time_slot_advertisement_table")
    fun findAll(): LiveData<List<TimeSlotAd>>


    /** Delete TimeSlotAdv **/
    @Query("DELETE FROM time_slot_advertisement_table WHERE id = :id")
    fun removeAdWithId(id: Long)


}