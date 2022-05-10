package it.polito.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.polito.group06.models.time_slot_adv_database.Advertisement
import it.polito.group06.models.time_slot_adv_database.AdvertisementRepository
import kotlin.concurrent.thread

class AdvertisementViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Repository
     */
    private val repositoryAdv = AdvertisementRepository(application)

    /**
     * Single [Advertisement]
     */
    private var _singleAdvertisementPH = Advertisement(
        null, "", "",
        "", "", "", "", 0f,
        "", false
    )
    private val _pvtAdvertisement = MutableLiveData<Advertisement>().also {
        it.value = _singleAdvertisementPH
    }
    val advertisement: LiveData<Advertisement> = this._pvtAdvertisement

    /**
     * getFullListOfAdvertisement
     */
    fun getFullListOfAdvertisement(): LiveData<List<Advertisement>> {
        return this.repositoryAdv.advertisements()
    }

    /**
     * Insertion of a new [Advertisement]
     * @param ad a new advertisement
     */
    fun insertAd(ad: Advertisement) {
        thread {
            repositoryAdv.insertAd(ad)
        }
    }

    /**
     * Remove an advertisement
     * @param id integer unique number identifying an [Advertisement]
     */
    fun removeAd(id: Long) {
        thread {
            repositoryAdv.removeAdWithId(id)
        }
    }

    /**
     * Clear all the db
     */
    fun clearAll() {
        thread {
            repositoryAdv.clearAll()
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
     * editSingleAdvertisement is a method to update the info about a single [Advertisement] with the
     * new ones read from the edit view.
     * @param updatedAdv the object of class [Advertisement] which contains all the information to update
     * the advertisement with a certain id.
     */
    fun editSingleAdvertisement(updatedAdv: Advertisement) {
        thread {
            repositoryAdv.updateAdv(
                updatedAdv.id!!,
                updatedAdv.advTitle,
                updatedAdv.advDescription,
                updatedAdv.advLocation,
                updatedAdv.advDate,
                updatedAdv.advDuration,
                updatedAdv.advAccount,
                updatedAdv.isPrivate
            )
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
                println(adv)
                repositoryAdv.updateAccountName(
                    adv.id!!,
                    adv.advTitle,
                    adv.advDescription,
                    adv.advLocation,
                    adv.advDate,
                    adv.advDuration,
                    accountName,
                    adv.isPrivate
                )
            }
        }
    }
}