package it.polito.madcourse.group06.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.madcourse.group06.models.advertisement.Advertisement
import it.polito.madcourse.group06.models.userprofile.UserProfile
import java.lang.Exception

class AdvertisementViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Repository
     */
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * Single [Advertisement]
     */
    private var _singleAdvertisementPH = Advertisement(
        "", "", "", "", arrayListOf<String>(),
        "", "", "", "", 0.0,
        "", "", "", "", "", 0.0, ""
    )
    private val _pvtAdvertisement = MutableLiveData<Advertisement>().also {
        it.value = _singleAdvertisementPH
    }
    val advertisement: LiveData<Advertisement> = this._pvtAdvertisement

    /**
     * List of Advertisements
     */
    private val _advertisements = MutableLiveData<List<Advertisement>>()
    val listOfAdvertisements: LiveData<List<Advertisement>> = _advertisements

    init {
        listenerRegistration = db.collection("Advertisement")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _advertisements.value = emptyList()
                } else {
                    _advertisements.value = value!!.mapNotNull { elem ->
                        elem.toAdvertisement()
                    }
                }
            }
    }

    private fun DocumentSnapshot.toAdvertisement(): Advertisement? {
        return try {
            val id = this.get("id") as String
            val title = this.get("title") as String
            val description = this.get("description") as String
            val restrictions = this.get("restrictions") as String
            val listOfSkills = this.get("list_of_skills") as ArrayList<String>?
            val location = this.get("location") as String
            val date = this.get("date") as String
            val startingTime = this.get("starting_time") as String
            val endingTime = this.get("ending_time") as String
            val duration = this.get("duration") as Double
            val accountName = this.get("account_name") as String
            val accountID = this.get("accountID") as String
            val rxUserId = this.get("rx_user_id") as String?
            val ratingUserId = this.get("rating_user_id") as String?
            val activeAt = this.get("active_at") as String?
            val activeFor = this.get("active_for") as Double
            val activeLocation = this.get("active_location") as String
            Advertisement(
                id, title, description, restrictions, listOfSkills ?: arrayListOf<String>(),
                location, date, startingTime,
                endingTime, duration, accountName,
                accountID, rxUserId, ratingUserId, activeAt, activeFor, activeLocation
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
    fun insertAdvertisement(ad: Advertisement) {
        var advID: String = ""
        db
            .collection("Advertisement")
            .document(/*ad.id.toString()*/).also { advID = it.id }
            .set(
                mapOf(
                    "id" to advID,
                    "title" to ad.advTitle,
                    "description" to ad.advDescription,
                    "restrictions" to ad.advRestrictions,
                    "list_of_skills" to ad.listOfSkills,
                    "location" to ad.advLocation,
                    "date" to ad.advDate,
                    "starting_time" to ad.advStartingTime,
                    "ending_time" to ad.advEndingTime,
                    "duration" to ad.advDuration,
                    "account_name" to ad.advAccount,
                    "accountID" to ad.accountID,
                    "rx_user_id" to ad.rxUserId,
                    "rating_user_id" to ad.ratingUserId,
                    "active_at" to ad.activeAt,
                    "active_for" to ad.activeFor,
                    "active_location" to ad.activeLocation,
                )
            )
            .addOnSuccessListener {
                Toast.makeText(context, "Creation completed.", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Deletion of an [Advertisement] by its ID
     * @param id the ID of the Advertisement
     */
    fun removeAdvertisementByID(id: String) {
        db
            .collection("Advertisement")
            .document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Deletion completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Deletion failed.", Toast.LENGTH_SHORT).show()
            }

        db
            .collection("Chat")
            .whereEqualTo("adv_id", id)
            .addSnapshotListener { value, error ->
                if(error != null)
                    throw Exception()
                for (adv in value!!.documents) { adv.reference.delete() }
            }
    }

    /**
     * A method to remove all the [Advertisement] related to a single account
     * @param accountID the ID of the account
     */
    fun removeAdvertisementByAccount(accountID: Int) {
        db
            .collection("Advertisement")
            .whereEqualTo("accountID", accountID)
            .addSnapshotListener { listOfAdv, e ->
                if (e != null) {
                    throw Exception()
                } else {
                    for (adv in listOfAdv!!) {
                        val convertedAdv = adv.toAdvertisement()
                        removeAdvertisementByID(convertedAdv?.id!!)
                    }
                }
            }
    }

    /**
     * Activate an [Advertisement] with the account ID of the requesting account
     * @param rxUserId the requesting account ID
     */
    fun activateAdvertisement(rxUserId: String, activeAt: String, activeFor: Double, activeLocation: String) {
        this._pvtAdvertisement.value.also { ad ->
            ad!!.rxUserId = rxUserId
            ad.ratingUserId = rxUserId
            ad.activeAt = activeAt
            ad.activeFor = activeFor
            ad.activeLocation = activeLocation

            db
                .collection("Advertisement")
                .document(ad.id!!)
                .set(
                    mapOf(
                        "id" to ad.id,
                        "title" to ad.advTitle,
                        "description" to ad.advDescription,
                        "restrictions" to ad.advRestrictions,
                        "list_of_skills" to ad.listOfSkills,
                        "location" to ad.advLocation,
                        "date" to ad.advDate,
                        "starting_time" to ad.advStartingTime,
                        "ending_time" to ad.advEndingTime,
                        "duration" to ad.advDuration,
                        "account_name" to ad.advAccount,
                        "accountID" to ad.accountID,
                        "rx_user_id" to ad.rxUserId,
                        "rating_user_id" to ad.ratingUserId,
                        "active_at" to ad.activeAt,
                        "active_for" to ad.activeFor,
                        "active_location" to ad.activeLocation
                    )
                )
        }
    }

    fun deactivateAd(isServiceDone: Boolean) {
        this._singleAdvertisementPH.also { ad ->
            if (isServiceDone) {
                ad.rxUserId = null
            } else {
                ad.ratingUserId = null
            }
            db
                .collection("Advertisement")
                .document(ad.id!!)
                .set(
                    mapOf(
                        "id" to ad.id,
                        "title" to ad.advTitle,
                        "description" to ad.advDescription,
                        "restrictions" to ad.advRestrictions,
                        "list_of_skills" to ad.listOfSkills,
                        "location" to ad.advLocation,
                        "date" to ad.advDate,
                        "starting_time" to ad.advStartingTime,
                        "ending_time" to ad.advEndingTime,
                        "duration" to ad.advDuration,
                        "account_name" to ad.advAccount,
                        "accountID" to ad.accountID,
                        "rx_user_id" to ad.rxUserId,
                        "rating_user_id" to ad.ratingUserId,
                        "active_at" to ad.activeAt,
                        "active_for" to ad.activeFor,
                        "active_location" to ad.activeLocation
                    )
                )
                .addOnSuccessListener {
                    this._singleAdvertisementPH = ad
                    this._pvtAdvertisement.value = this._singleAdvertisementPH
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Edit failed.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    /**
     * Edit an [Advertisement] with the info passed through an Object of class [Advertisement]
     * @param ad the updated [Advertisement]
     */
    fun editAdvertisement(ad: Advertisement) {
        db
            .collection("Advertisement")
            .document(ad.id!!)
            .set(
                mapOf(
                    "id" to ad.id,
                    "title" to ad.advTitle,
                    "description" to ad.advDescription,
                    "restrictions" to ad.advRestrictions,
                    "list_of_skills" to ad.listOfSkills,
                    "location" to ad.advLocation,
                    "date" to ad.advDate,
                    "starting_time" to ad.advStartingTime,
                    "ending_time" to ad.advEndingTime,
                    "duration" to ad.advDuration,
                    "account_name" to ad.advAccount,
                    "accountID" to ad.accountID,
                    "rx_user_id" to ad.rxUserId,
                    "rating_user_id" to ad.ratingUserId,
                    "active_at" to ad.activeAt,
                    "active_for" to ad.activeFor,
                    "active_location" to ad.activeLocation
                )
            )
            .addOnSuccessListener {
                this._singleAdvertisementPH = ad
                this._pvtAdvertisement.value = this._singleAdvertisementPH
            }
            .addOnFailureListener {
                Toast.makeText(context, "Edit failed.", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Update the name of the account which created the [Advertisement] with a new one
     * @param accountID the ID of the account
     * @param newAccountName the new name for the account creating the advertisement
     */
    fun updateAdvAccountNameByAccountID(accountID: String, newAccountName: String) {
        db
            .collection("Advertisement")
            .addSnapshotListener { listOfAdvs, e ->
                if (e != null) {
                    throw Exception()
                } else {
                    for (adv in listOfAdvs!!) {
                        val thatAdv = adv.toAdvertisement()
                        if (thatAdv?.accountID == accountID) {
                            thatAdv.advAccount = newAccountName
                            this.editAdvertisement(thatAdv)
                        }
                    }
                }
            }
    }

    /**
     * setSingleAdvertisement is a method to update the [_pvtAdvertisement] and it's called by the adapter to
     * trigger all the observators.
     * @param newAdv an object of class [Advertisement] which contains all the information to fill
     * the advertisement view.
     */
    fun setSingleAdvertisement(newAdv: Advertisement) {
        this._singleAdvertisementPH = newAdv
        this._pvtAdvertisement.value = _singleAdvertisementPH
    }

    /**
     * fetchSingleAdvertisementById: Fetch the advertisement by their id
     * @param id the id of the ad
     */
    fun fetchSingleAdvertisementById(id: String) {
        db
            .collection("Advertisement")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { query ->
                query.forEach { docSnap ->
                    this._singleAdvertisementPH = docSnap.toAdvertisement()!!
                    this._pvtAdvertisement.value = this._singleAdvertisementPH
                }
            }
            .addOnFailureListener {
                this._singleAdvertisementPH = Advertisement(
                    "", "", "", "", arrayListOf<String>(),
                    "", "", "", "", 0.0,
                    "", "", "", "", "", 0.0, ""
                )
                this._pvtAdvertisement.value = this._singleAdvertisementPH
            }
    }

    /**
     * Unsubscribe from the Listener Registration
     */
    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
    }
}