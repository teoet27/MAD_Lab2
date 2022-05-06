package it.polito.group06.models.time_slot_adv_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AdvertisementDAO {

    /**
     * Add(Update) to database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAd(timeSlotAd: Advertisement)

    /**
     * Read from database
     */
    @Query("SELECT * FROM advertisementTable")
    fun findAll(): LiveData<List<Advertisement>>


    /**
     * Delete Advertisement
     */
    @Query("DELETE FROM advertisementTable WHERE id = :id")
    fun removeAdWithId(id: Long)

    /**
     * Clean up the database
     */
    @Query("DELETE FROM advertisementTable")
    fun clearAll()


}