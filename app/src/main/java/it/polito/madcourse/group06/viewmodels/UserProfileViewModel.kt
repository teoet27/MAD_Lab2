package it.polito.madcourse.group06.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import it.polito.madcourse.group06.models.userprofile.UserProfile
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import kotlin.collections.ArrayList

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * Single [UserProfile]
     */
    private var _singleUserProfilePH = UserProfile(
        "", "", "", "", "", "", "",
        "", null, 0.0, 0.0, 0.0,
        ArrayList<String>(), ArrayList<String>(), null
    )
    private val _pvtUserProfile = MutableLiveData<UserProfile>().also { it.value = _singleUserProfilePH }
    val currentUser: LiveData<UserProfile> = this._pvtUserProfile

    /**
     * Other [UserProfile]
     */
    private var _otherUserProfilePH = UserProfile(
        "", "", "", "", "", "",
        "", "", null, 0.0, 0.0, 0.0,
        ArrayList<String>(), ArrayList<String>(), null
    )
    private val _pvtOtherUserProfile = MutableLiveData<UserProfile>().also { it.value = _otherUserProfilePH }
    val otherUser: LiveData<UserProfile> = this._pvtOtherUserProfile

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
            val credit = this.get("credit") as Double
            val rating_sum = this.get("rating_sum") as Double

            val nRatings: Double = this.getDouble("n_ratings") as Double

            val comments_services_rx = this.get("comments_services_rx") as ArrayList<String>?
            val comments_services_done = this.get("comments_services_done") as ArrayList<String>?
            val imgPath = this.get("img_path") as String
            UserProfile(
                id, nickname, fullname, qualification,
                description, email, phoneNumber, location, skills, credit,
                rating_sum, nRatings, comments_services_rx, comments_services_done, imgPath
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
                    null, null, null, null, null, 0.0,
                    0.0, 0.0, ArrayList<String>(), ArrayList<String>(), null
                )
                this._pvtUserProfile.value = this._singleUserProfilePH
            }
    }

    /**
     * Fetch the user profile by their ID from the db
     * @param id the id of the user
     */
    fun fetchUserProfileById(id: String) {
        db
            .collection("UserProfile")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { query ->
                query.forEach { docSnap ->
                    this._otherUserProfilePH = docSnap.toUser()!!
                    this._pvtOtherUserProfile.value = this._otherUserProfilePH
                }
            }
            .addOnFailureListener {
                this._otherUserProfilePH = UserProfile(
                    null, null, null, null,
                    null, null, null, null, null, 0.0,
                    0.0, 0.0, ArrayList<String>(), ArrayList<String>(), null
                )
                this._pvtOtherUserProfile.value = this._otherUserProfilePH
            }
    }

    /**
     * Look for nicknames in the database to check if one is already present
     * (to be used in the new user profile fragment)
     * @param nickname the email of the user
     * @return whether it already exists or not
     */
    fun lookForNickname(nickname: String): Boolean {
        var searchResult: Boolean = false
        db
            .collection("UserProfile")
            .whereEqualTo("nickname", nickname)
            .get()
            .addOnSuccessListener {
                searchResult = true
            }
            .addOnFailureListener {
                searchResult = false
            }
        return searchResult
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
                    "skills" to userProfile.skills,
                    "credit" to userProfile.credit,
                    "rating_sum" to userProfile.rating_sum,
                    "n_ratings" to userProfile.n_ratings,
                    "comments_services_rx" to userProfile.comments_services_rx,
                    "comments_services_done" to userProfile.comments_services_done,
                    "img_path" to userProfile.imgPath
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
        /**
         * Deletion of the user
         */
        db
            .collection("UserProfile")
            .document(id)
            .delete()

        /**
         * On-Cascade deletion of their advertisements
         */
        db
            .collection("Advertisement")
            .whereEqualTo("accountID", id)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    throw Exception()
                } else {
                    Log.e("VALUE:", value.toString())
                    if (value?.documents.isNullOrEmpty()) {
                        Log.e("VALUE:", value?.documents.toString())
                        val listOfDocuments: MutableList<DocumentSnapshot>? = value?.documents
                        for (doc in listOfDocuments!!) {
                            Log.e("doc:", doc.toString())
                            doc.reference.delete()
                        }
                    }
                }
            }
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
                "skills", userProfile.skills,
                "credit", userProfile.credit,
                "rating_sum", userProfile.rating_sum,
                "n_ratings", userProfile.n_ratings,
                "comments_services_rx", userProfile.comments_services_rx,
                "comments_services_done", userProfile.comments_services_done,
                "img_path", userProfile.imgPath
            )
            .addOnSuccessListener {
                Toast.makeText(context, "Edit completed.", Toast.LENGTH_SHORT).show()
                this._singleUserProfilePH = userProfile
                this._pvtUserProfile.value = this._singleUserProfilePH
            }
            .addOnFailureListener {
                Toast.makeText(context, "Edit failed.", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Edit the [UserProfile] information with the newer ones passed as parameter.
     * @param userProfile the object containing the most updated values
     */
    fun editOtherUserProfile(userProfile: UserProfile) {
        db
            .collection("UserProfile")
            .document(this._otherUserProfilePH.id!!)
            .update(
                "id", userProfile.id,
                "nickname", userProfile.nickname,
                "fullname", userProfile.fullName,
                "qualification", userProfile.qualification,
                "description", userProfile.description,
                "email", userProfile.email,
                "phone_number", userProfile.phoneNumber,
                "location", userProfile.location,
                "skills", userProfile.skills,
                "credit", userProfile.credit,
                "rating_sum", userProfile.rating_sum,
                "n_ratings", userProfile.n_ratings,
                "comments_services_rx", userProfile.comments_services_rx,
                "comments_services_done", userProfile.comments_services_done,
                "img_path", userProfile.imgPath
            )
            .addOnSuccessListener {
                Toast.makeText(context, "Edit completed.", Toast.LENGTH_SHORT).show()
                this._otherUserProfilePH = userProfile
                this._pvtOtherUserProfile.value = this._otherUserProfilePH
            }
            .addOnFailureListener {
                Toast.makeText(context, "Edit failed.", Toast.LENGTH_SHORT).show()
            }
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
     * updateOtherListOfCommentsServicesDone is a method which allows to update the list of comments for the services
     * done by the current user.
     * @param updatedListOfComments a list of comments for services done by the user
     */
    fun updateOtherListOfCommentsServicesDone(updatedListOfComments: ArrayList<String>) {
        val userProfile = _otherUserProfilePH
        userProfile.comments_services_done = updatedListOfComments
        this.editOtherUserProfile(userProfile)
    }

    /**
     * Method to upload the new profile picture to the Firebase Storage
     * @param profilePictureBitmap the Bitmap object containing the profile picture
     */
    fun uploadProfilePicture(profilePictureBitmap: Bitmap?, imgFilename: String): String {
        val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
        profilePictureBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        val profilePath = "images/${imgFilename}.jpg"
        val profilePathReference = this.storage.getReference(profilePath)
        val profilePathMetadata: StorageMetadata = StorageMetadata.Builder().setCustomMetadata("accountID", imgFilename).build()

        /**
         * uploadTask can be used to keep track of the upload through a progress bar or something similar
         */
        val uploadTask: UploadTask = profilePathReference.putBytes(byteArray, profilePathMetadata)
        return profilePath
    }

    /**
     * The method to retrieve the profile picture from the storage. If the image does not exist, the placeholder is fetched.
     * @param imageView the image view to be set with the profile picture
     * @param imgPath the path where the image is located into the Firebase Storage
     */
    fun retrieveProfilePicture(imageView: ImageView, imgPath: String) {
        var suffix = if (imgPath == "staticuser") "png" else "jpg"
        val profilePathReference = this.storage.getReferenceFromUrl("gs://timebankingmadg06.appspot.com").child("images/${imgPath}.${suffix}")
        val localFile = File.createTempFile(imgPath, ".${suffix}")
        profilePathReference.getFile(localFile)
            .addOnSuccessListener {
                val bitmapProfilePicture = BitmapFactory.decodeFile(localFile.absolutePath)
                imageView.setImageBitmap(bitmapProfilePicture)
            }
            .addOnFailureListener { error -> error.printStackTrace() }
    }

    fun retrieveStaticProfilePicture(imageView: ImageView) {
        val profilePathReference = this.storage.getReferenceFromUrl("gs://timebankingmadg06.appspot.com").child("images/staticuser.png")
        val localFile = File.createTempFile("staticuser", ".png")
        profilePathReference.getFile(localFile)
            .addOnSuccessListener {
                val bitmapProfilePicture = BitmapFactory.decodeFile(localFile.absolutePath)
                imageView.setImageBitmap(bitmapProfilePicture)
            }
            .addOnFailureListener { error -> error.printStackTrace() }
    }

    /**
     * Unsubscribe from the Listener Registration
     */
    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
    }

}