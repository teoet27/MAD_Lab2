package it.polito.group06.MVVM.UserProfileDatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.polito.group06.MVVM.TimeSlotAdvDatabase.TimeSlotAd
import kotlin.concurrent.thread

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserProfileRepository(application)

    /**
     * This is the instance of the profile fetched from the database through the repository
     */
    val profile = repo.profile()

    /**
     * editProfile
     * @param profile the data structure with all the updating fields
     */
    fun editProfile(profile: UserProfile) {
        thread {
            repo.editProfile(profile)
        }
    }

}