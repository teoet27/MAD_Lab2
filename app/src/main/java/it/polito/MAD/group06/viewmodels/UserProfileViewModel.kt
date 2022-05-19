package it.polito.MAD.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.MAD.group06.models.userprofile.UserProfile
import java.lang.Exception

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * Single [UserProfile]
     */
    private var _singleUserProfilePH = UserProfile(
        -1, "", "", "", "", "", "", "", null
    )
    private val _pvtUserProfile = MutableLiveData<UserProfile>().also {
        it.value = _singleUserProfilePH
    }
    val currentUser: LiveData<UserProfile> = this._pvtUserProfile

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

    fun setCurrentUserProfile(id: String, fullname: String, email: String) {
        //this._singleUserProfilePH.id = id.toLong()
        this._singleUserProfilePH.fullName = fullname
        this._singleUserProfilePH.email = email
        this._singleUserProfilePH.location = "placeholder location"
        this._singleUserProfilePH.phoneNumber = "000 000 000"
        this._pvtUserProfile.value = this._singleUserProfilePH
    }

    fun getListOfSkills(viewLifecycleOwner: LifecycleOwner): LiveData<MutableSet<String>> {
        val outListOfSkills = MutableLiveData<MutableSet<String>>()

        listOfUsers.observe(viewLifecycleOwner) {
            for (u in it) {
                for(skill in u.skills!!) {
                    outListOfSkills.value?.add(skill)
                }
            }
        }

        return outListOfSkills
    }

    /**
     * Unsubscribe from the Listener Registration
     */
    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
    }

}