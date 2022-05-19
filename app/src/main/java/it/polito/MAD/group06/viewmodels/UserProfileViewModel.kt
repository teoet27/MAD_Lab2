package it.polito.MAD.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.MAD.group06.models.skill.Skill
import it.polito.MAD.group06.models.userprofile.ArrayListConverter
import it.polito.MAD.group06.models.userprofile.UserProfile
import it.polito.MAD.group06.repository.UserProfileRepository
import java.lang.Exception
import kotlin.concurrent.thread

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserProfileRepository(application)
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * List of Advertisements
     */
    private val _users = MutableLiveData<List<UserProfile>>()
    val listOfUsers: LiveData<List<UserProfile>> = _users

    init {
        listenerRegistration = db.collection("UserProfile")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _users.value = emptyList()
                } else {
                    _users.value = value!!.mapNotNull { elem ->
                        elem.toUser()
                    }
                }
            }
    }

    private fun DocumentSnapshot.toUser(): UserProfile? {
        return try {
            val id = this.get("id") as Long
            val nickname = this.get("nickname") as String
            val fullname = this.get("fullname") as String
            val qualification = this.get("qualification") as String
            val description = this.get("description") as String
            val email = this.get("email") as String
            val phoneNumber = this.get("phone_number") as String
            val location = this.get("location") as String
            val skills = this.get("skills") as ArrayList<String>
            UserProfile(
                id, nickname, fullname, qualification,
                description, email, phoneNumber, location, skills
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

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

    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
    }

}