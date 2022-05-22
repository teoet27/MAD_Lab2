package it.polito.madcourse.group06.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import it.polito.madcourse.group06.models.userprofile.UserProfile
import java.lang.Exception

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * Single [UserProfile]
     */
    private var _singleUserProfilePH = UserProfile(
        "", "", "", "", "", "", "", "", null
    )
    private val _pvtUserProfile = MutableLiveData<UserProfile>().also { it.value = _singleUserProfilePH }
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

    /**
     * toUser is an extension function which provide translation from
     * [DocumentSnapshot] to [UserProfile]
     * @return [UserProfile]
     */
    private fun DocumentSnapshot.toUser(): UserProfile? {
        return try {
            val id = this.get("id") as String
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
     * Fetch the user profile by their related email from the db
     * @param email the email of the user
     */
    fun fetchUserProfile(email: String) {
        db
            .collection("UserProfile")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { query ->
                query.forEach { docSnap ->
                    this._singleUserProfilePH = docSnap.toUser()!!
                    this._pvtUserProfile.value = this._singleUserProfilePH
                }
            }
            .addOnFailureListener {
                this._singleUserProfilePH = UserProfile(
                    null, null, null, null,
                    null, null, null, null, null
                )
                this._pvtUserProfile.value = this._singleUserProfilePH
            }
    }

    /**
     * Insertion of a new [UserProfile]
     * @param userProfile a new advertisement
     */
    fun insertUserProfile(userProfile: UserProfile) {
        var userID: String = ""
        db
            .collection("UserProfile")
            .document().also { userID = it.id }
            .set(
                mapOf(
                    "id" to userID,
                    "nickname" to userProfile.nickname,
                    "fullname" to userProfile.fullName,
                    "qualification" to userProfile.qualification,
                    "description" to userProfile.description,
                    "email" to userProfile.email,
                    "phone_number" to userProfile.phoneNumber,
                    "location" to userProfile.location,
                    "skills" to userProfile.skills
                )
            )
        this._singleUserProfilePH = userProfile
        this._singleUserProfilePH.id = userID
        this._pvtUserProfile.value = this._singleUserProfilePH
    }

    /**
     * Removing of a [UserProfile] by their ID
     * @param id user's ID
     */
    fun removeUserProfileByID(id: String) {
        db
            .collection("UserProfile")
            .document(id)
            .delete()
    }

    /**
     * Edit the [UserProfile] information with the newer ones passed as parameter.
     * @param userProfile the object containing the most updated values
     */
    fun editUserProfile(userProfile: UserProfile) {
        db
            .collection("UserProfile")
            .document(this._singleUserProfilePH.id!!)
            .update(
                "id", userProfile.id,
                "nickname", userProfile.nickname,
                "fullname", userProfile.fullName,
                "qualification", userProfile.qualification,
                "description", userProfile.description,
                "email", userProfile.email,
                "phone_number", userProfile.phoneNumber,
                "location", userProfile.location,
                "skills", userProfile.skills
            )
            .addOnSuccessListener {
                Toast.makeText(context, "Edit completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Edit failed.", Toast.LENGTH_SHORT).show()
            }
        this._singleUserProfilePH = userProfile
        this._pvtUserProfile.value = this._singleUserProfilePH
    }

    /**
     * Set the current user profile in order to make it visible to the observer
     */
    fun setCurrentUserProfile(fullname: String, email: String) {
        this._singleUserProfilePH.fullName = fullname
        this._singleUserProfilePH.email = email
        this._pvtUserProfile.value = this._singleUserProfilePH
    }

    /**
     * updateSkillList is a method which allows to update the list of skills for the current user
     * such that it can add new skills while creating an advertisement.
     * @param updatedListOfSkills a list of new skills for the user
     */
    fun updateSkillList(updatedListOfSkills: ArrayList<String>) {
        val userProfile = _singleUserProfilePH
        userProfile.skills = updatedListOfSkills
        this.editUserProfile(userProfile)
    }

    /**
     * Method to upload the new profile picture to the Firebase Storage
     */
    fun uploadProfilePicture(propic: Bitmap?, dataUri: Uri){
        val imgDB: StorageReference = FirebaseStorage.getInstance().getReference("uploads")
        val fileRef: StorageReference = imgDB.child(System.currentTimeMillis().toString() + ".jpg")
        fileRef.putFile(dataUri)
    }

    /**
     * Unsubscribe from the Listener Registration
     */
    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
    }

}