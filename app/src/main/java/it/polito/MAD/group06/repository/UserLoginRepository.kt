package it.polito.MAD.group06.repository

import android.app.Application
import it.polito.MAD.group06.models.userprofile.UserProfile
import it.polito.MAD.group06.models.userprofile.UserProfileDatabase

class UserLoginRepository(application: Application) {
    private val profileDAO = UserProfileDatabase.getDatabase(application).profileDao()

    /**
     * editProfile
     * @param profile a UserProfile object with all the updated fields in order to update the data in memory
     */
    fun editProfile(profile: UserProfile) = profileDAO.editProfile(profile)

    /**
     * profile
     * @return profile (up to now there's a single profile into the db)
     */
    fun profile() = profileDAO.getProfile()
}