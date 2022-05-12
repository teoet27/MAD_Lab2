package it.polito.group06.models.userprofile

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
    @Query("SELECT * FROM userProfileTable")
    fun getProfile(): LiveData<UserProfile>

}