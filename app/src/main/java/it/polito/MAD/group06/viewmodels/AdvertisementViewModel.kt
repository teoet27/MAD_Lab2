package it.polito.MAD.group06.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.MAD.group06.models.advertisement.Advertisement
import it.polito.MAD.group06.models.skill.Skill
import it.polito.MAD.group06.repository.AdvertisementRepository
import java.lang.Exception
import kotlin.concurrent.thread

class AdvertisementViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Repository
     */
    private val repositoryAdv = AdvertisementRepository(application)
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * Single [Advertisement]
     */
    private var _singleAdvertisementPH = Advertisement(
        null, "", "", arrayListOf<Skill>(),
        "", "", "", "", 0.0,
        "", -1
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
            val id = this.get("id") as Long
            val title = this.get("title") as String
            val description = this.get("description") as String
            val listOfSkills = this.get("list_of_skills") as ArrayList<Skill>
            val location = this.get("location") as String
            val date = this.get("date") as String
            val startingTime = this.get("starting_time") as String
            val endingTime = this.get("ending_time") as String
            val duration = this.get("duration") as Double
            val accountName = this.get("account_name") as String
            val accountID = this.get("accountID") as Long
            Advertisement(
                id, title, description, listOfSkills,
                location, date, startingTime,
                endingTime, duration, accountName,
                accountID
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
        db
            .collection("Advertisement")
            .document(ad.id.toString())
            .set(
                mapOf(
                    "id" to ad.id,
                    "title" to ad.advTitle,
                    "description" to ad.advDescription,
                    "list_of_skills" to ad.listOfSkills,
                    "location" to ad.advLocation,
                    "date" to ad.advDate,
                    "starting_time" to ad.advStartingTime,
                    "ending_time" to ad.advEndingTime,
                    "duration" to ad.advDuration,
                    "account_name" to ad.advAccount,
                    "accountID" to ad.accountID
                )
            )
            .addOnSuccessListener {
                Toast.makeText(context, "Creation completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Creation failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun removeAdvertisement(ad: Advertisement) {
        db
            .collection("Advertisement")
            .document(ad.id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Deletion completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Deletion failed.", Toast.LENGTH_SHORT).show()
            }
    }

    fun removeAdvertisementByAccount(accountID: Int) {
        db
            .collection("Advertisement")
            .document()
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Deletion completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Deletion failed.", Toast.LENGTH_SHORT).show()
            }
    }

    fun editAdvertisement(ad: Advertisement) {
        db
            .collection("Advertisement")
            .document()
            .set(mapOf(ad.id.toString() to ad))
            .addOnSuccessListener {
                Toast.makeText(context, "Edit completed.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Edit failed.", Toast.LENGTH_SHORT).show()
            }
    }

    fun getAdvertisementByID(id: Int): Advertisement? {
        var outAdv: Advertisement? = null
        db
            .collection("Advertisement")
            .document(id.toString())
            .get()
            .addOnSuccessListener { dbAdv ->
                outAdv = dbAdv.toObject(Advertisement::class.java)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error in get", Toast.LENGTH_SHORT).show()
            }

        return outAdv
    }

    fun getListOfAdvertisements(): List<Advertisement> {
        val outAdv: MutableList<Advertisement> = mutableListOf()

        db
            .collection("Advertisement")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    throw Exception()
                } else {
                    for (elem in value!!) {
                        outAdv.add(elem.toObject(Advertisement::class.java))
                    }
                }
            }

        return outAdv
    }

    fun getListOfAdvertisementsByAccountID(accountID: Int): List<Advertisement>? {
        val outAdv: MutableList<Advertisement> = mutableListOf()

        db
            .collection("Advertisement")
            .whereEqualTo("accountID", accountID)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    throw Exception()
                } else {
                    for (elem in value!!) {
                        outAdv.add(elem.toObject(Advertisement::class.java))
                    }
                }
            }

        return outAdv
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
     * editSingleAdvertisement is a method to update the info about a single [Advertisement] with the
     * new ones read from the edit view.
     * @param updatedAdv the object of class [Advertisement] which contains all the information to update
     * the advertisement with a certain id.
     */
    fun editSingleAdvertisement(updatedAdv: Advertisement) {
        thread {
            repositoryAdv.updateAdv(updatedAdv)
        }
        this._singleAdvertisementPH = updatedAdv
    }

    /**
     * updateAccountName is a method to update the name of the creator of the advertisement
     * after it's been changed from the edit view for the profile.
     * @param advList a complete list of advertisement
     * @param accountName the new account name from the update profile
     */
    fun updateAccountName(advList: List<Advertisement>, accountName: String) {
        thread {
            for (adv in advList) {
                adv.advAccount = accountName
                repositoryAdv.updateAccountName(adv)
            }
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