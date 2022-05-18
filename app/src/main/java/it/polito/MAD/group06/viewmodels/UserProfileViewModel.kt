package it.polito.MAD.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import it.polito.MAD.group06.models.skill.Skill
import it.polito.MAD.group06.models.userprofile.ArrayListConverter
import it.polito.MAD.group06.models.userprofile.UserProfile
import it.polito.MAD.group06.repository.UserProfileRepository
import kotlin.concurrent.thread

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserProfileRepository(application)

    /**
     * This is the instance of the profile fetched from the database through the repository
     */
    var profile = repo.profile()

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