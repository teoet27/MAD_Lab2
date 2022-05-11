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

    /**
     * Edit a single advertisement
     */
    @Query(
        "UPDATE advertisementTable " +
                "SET id = :id," +
                "advTitle = :advTitle," +
                "advDescription = :advDescription," +
                "advLocation = :advLocation," +
                "advDate = :advDate," +
                "advDuration = :advDuration," +
                "advAccount = :advAccount," +
                "isPrivate = :isPrivate" +
                " WHERE id = :id"
    )
    fun updateAdv(
        id: Long,
        advTitle: String,
        advDescription: String,
        advLocation: String,
        advDate: String,
        advDuration: Float,
        advAccount: String,
        isPrivate: Boolean
    )

    /**
     * Update the account name for all the advertisement
     */
    @Query(
        "UPDATE advertisementTable " +
                "SET id = :id," +
                "advTitle = :advTitle," +
                "advDescription = :advDescription," +
                "advLocation = :advLocation," +
                "advDate = :advDate," +
                "advDuration = :advDuration," +
                "advAccount = :advAccount," +
                "isPrivate = :isPrivate " +
                "WHERE id = :id"
    )
    fun updateAccountName(
        id: Long,
        advTitle: String,
        advDescription: String,
        advLocation: String,
        advDate: String,
        advDuration: Float,
        advAccount: String,
        isPrivate: Boolean
    )
}