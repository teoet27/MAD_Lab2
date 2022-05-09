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
    private val repo = AdvertisementRepository(application)

    /**
     * List of advertisements
     */
    private var _listOfAdvertisementPH = mutableListOf<Advertisement>()
    private val _pvtListOfAdvertisement = MutableLiveData<MutableList<Advertisement>>().also {
        it.value = _listOfAdvertisementPH
    }
    val listOfAdvertisement: LiveData<MutableList<Advertisement>> = this._pvtListOfAdvertisement

    /**
     * Single [Advertisement]
     */
    private var _singleAdvertisementPH = Advertisement(
        null, "", "",
        "", "", 0f,
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
        return this.repo.advertisements()
    }

    /**
     * Insertion of a new [Advertisement]
     * @param ad a new advertisement
     */
    fun insertAd(ad: Advertisement) {
        thread {
            repo.insertAd(ad)
        }
        this._listOfAdvertisementPH.add(ad)
        this._pvtListOfAdvertisement.value = _listOfAdvertisementPH
    }

    /**
     * Remove an advertisement
     * @param id integer unique number identifying an [Advertisement]
     */
    fun removeAd(id: Long) {
        thread {
            repo.removeAdWithId(id)
        }
        this._pvtListOfAdvertisement.value = _listOfAdvertisementPH
    }

    /**
     * Clear all the db
     */
    fun clearAll() {
        thread {
            repo.clearAll()
        }
        this._listOfAdvertisementPH.clear()
        this._pvtListOfAdvertisement.value = _listOfAdvertisementPH
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

    fun editSingleAdvertisement(updatedAdv: Advertisement) {
        thread {
            repo.updateAdv(updatedAdv.id!!,
            updatedAdv.advTitle,
            updatedAdv.advDescription,
            updatedAdv.advLocation,
            updatedAdv.advDate,
            updatedAdv.advDuration,
            updatedAdv.advAccount,
            updatedAdv.isPrivate)
        }
        this._pvtListOfAdvertisement.value = this._listOfAdvertisementPH
        this._singleAdvertisementPH = updatedAdv
        this._pvtAdvertisement.value = this._singleAdvertisementPH
    }
}