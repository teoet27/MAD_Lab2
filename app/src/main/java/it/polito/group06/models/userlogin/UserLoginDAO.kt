package it.polito.group06.models.userlogin

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserLoginDAO {
    /**
     * editProfile edits the profile on the database
     * @param profile the profile data structure with the updated fields
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun editProfile(profile: UserLogin)

    /**
     * getProfile reads the profile data from database.
     * @return a LiveData object with UserProfile
     */
    @Query("SELECT * FROM userProfileTable")
    fun getProfile(): LiveData<UserLogin>

}