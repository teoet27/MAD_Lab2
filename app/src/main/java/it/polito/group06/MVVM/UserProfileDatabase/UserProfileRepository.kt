package it.polito.group06.MVVM.UserProfileDatabase

import android.app.Application
import androidx.lifecycle.LiveData

class UserProfileRepository(application: Application) {
    private val profileDao = UserProfileDatabase.getDatabase(application).profileDao()

    /**
     * editProfile
     * @param profile a UserProfile object with all the updated fields in order to update the data in memory
     */
    fun editProfile(profile: UserProfile) = profileDao.editProfile(profile)

    /**
     * profile
     * @return profile (up to now there's a single profile into the db)
     */
    fun profile() = profileDao.getProfile()
}