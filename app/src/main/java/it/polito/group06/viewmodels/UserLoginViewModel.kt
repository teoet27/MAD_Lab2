package it.polito.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import it.polito.group06.models.userprofile.UserProfile
import it.polito.group06.repository.UserLoginRepository
import it.polito.group06.repository.UserProfileRepository
import kotlin.concurrent.thread

class UserLoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserLoginRepository(application)

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