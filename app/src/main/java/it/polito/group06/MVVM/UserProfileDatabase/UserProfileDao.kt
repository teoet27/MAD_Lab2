package it.polito.group06.MVVM.UserProfileDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.polito.group06.MVVM.TimeSlotAdvDatabase.TimeSlotAd

@Dao
interface UserProfileDao {
    /** Add(Update) to database **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun editProfile(profile: UserProfile)

    /** Read from database **/
    @Query("SELECT * FROM user_profile_table")
    fun getProfile(): LiveData<UserProfile>

}