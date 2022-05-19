package it.polito.MAD.group06.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.MAD.group06.models.advertisement.Advertisement
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

    /**
     * Insertion of a new [Advertisement]
     * @param ad a new advertisement
     */
    fun insertUserProfile(userProfile: UserProfile) {
        db
            .collection("UserProfile")
            .document(userProfile.id.toString())
            .set(
                mapOf(
                    "id" to userProfile.id,
                    "nickname" to userProfile.nickname,
                    "fullName" to userProfile.fullName,
                    "qualification" to userProfile.qualification,
                    "description" to userProfile.description,
                    "email" to userProfile.email,
                    "phone_number" to userProfile.phoneNumber,
                    "location" to userProfile.location,
                    "skills" to userProfile.skills
                )
            )
            .addOnSuccessListener {
                Toast.makeText(context, "Creation completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Creation failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun removeUserProfile(userProfile: UserProfile) {
        db
            .collection("UserProfile")
            .document(userProfile.id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Deletion completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Deletion failed.", Toast.LENGTH_SHORT).show()
            }
    }

    fun removeUserProfileByID(id: Long) {
        db
            .collection("UserProfile")
            .document(id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Deletion completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Deletion failed.", Toast.LENGTH_SHORT).show()
            }
    }

    fun editUserProfile(userProfile: UserProfile) {
        db
            .collection("UserProfile")
            .document(userProfile.id.toString())
            .set(
                mapOf(
                    "id" to userProfile.id,
                    "nickname" to userProfile.nickname,
                    "fullName" to userProfile.fullName,
                    "qualification" to userProfile.qualification,
                    "description" to userProfile.description,
                    "email" to userProfile.email,
                    "phone_number" to userProfile.phoneNumber,
                    "location" to userProfile.location,
                    "skills" to userProfile.skills
                )
            )
            .addOnSuccessListener {
                Toast.makeText(context, "Edit completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Edit failed.", Toast.LENGTH_SHORT).show()
            }
    }

    fun getUserProfileByID(id: Int): UserProfile? {
        var outAdv: UserProfile? = null

        db
            .collection("UserProfile")
            .document(id.toString())
            .get()
            .addOnSuccessListener { dbAdv ->
                outAdv = dbAdv.toObject(UserProfile::class.java)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error in retrieving the User Profile", Toast.LENGTH_LONG).show()
            }

        return outAdv
    }

    fun getListOfUserProfiles(): List<UserProfile> {
        val outAdv: MutableList<UserProfile> = mutableListOf()

        db
            .collection("UserProfile")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    throw Exception()
                } else {
                    for (elem in value!!) {
                        outAdv.add(elem.toObject(UserProfile::class.java))
                    }
                }
            }

        return outAdv
    }

    fun getListOfUserProfilesByAccountID(accountID: Int): List<UserProfile>? {
        val outAdv: MutableList<UserProfile> = mutableListOf()

        db
            .collection("UserProfile")
            .whereEqualTo("accountID", accountID)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    throw Exception()
                } else {
                    for (elem in value!!) {
                        outAdv.add(elem.toObject(UserProfile::class.java))
                    }
                }
            }

        return outAdv
    }

    fun setCurrentUserProfile(id: String, fullname: String, email: String) {
        //this._singleUserProfilePH.id = id.toLong()
        this._singleUserProfilePH.fullName = fullname
        this._singleUserProfilePH.email = email
        this._singleUserProfilePH.location = "placeholder location"
        this._singleUserProfilePH.phoneNumber = "000 000 000"
        this._pvtUserProfile.value = this._singleUserProfilePH
    }

    fun getListOfSkills(viewLifecycleOwner: LifecycleOwner): MutableSet<String> {
        val outListOfSkills = mutableSetOf<String>()

        db
            .collection("UserProfile")
            .addSnapshotListener { listOfUsers, e ->
                if (e != null) {
                    throw Exception()
                } else {
                    for (singleUser in listOfUsers!!) {
                        for (skill in singleUser.get("skills") as ArrayList<String>)
                        outListOfSkills.add(skill)
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