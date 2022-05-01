package it.polito.group06.MVVM.UserProfileDatabase

import android.app.Application

class UserProfileRepository(application: Application) {
    private val profileDao=UserProfileDatabase.getDatabase(application).profileDao()

    fun editProfile(profile:UserProfile)=profileDao.editProfile(profile)
    fun profile()=profileDao.getProfile()
}