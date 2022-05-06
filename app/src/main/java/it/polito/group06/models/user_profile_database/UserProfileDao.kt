package it.polito.group06.models.user_profile_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserProfileDao {
    /**
     * editProfile edits the profile on the database
     * @param profile the profile data structure with the updated fields
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun editProfile(profile: UserProfile)

    /**
     * getProfile reads the profile data from database.
     * @return a LiveData object with UserProfile
     */
    @Query("SELECT * FROM user_profile_table")
    fun getProfile(): LiveData<UserProfile>

}