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
    val ads = repo.advertisements()

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
     * Insertion of a new [Advertisement]
     * @param ad a new advertisement
     */
    fun insertAd(ad: Advertisement) {
        thread {
            repo.insertAd(ad)
        }
    }

    /**
     * Remove an advertisement
     * @param id integer unique number identifying an [Advertisement]
     */
    fun removeAd(id: Long) {
        thread {
            repo.removeAdWithId(id)
        }
    }

    /**
     * Clear all the db
     */
    fun clearAll() {
        thread {
            repo.clearAll()
        }
    }

    /**
     * updateAdv is a method to update the [_pvtAdvertisement] and it's called by the adapter to
     * trigger all the observators.
     * @param newAdv an object of class [Advertisement] which contains all the information to fill
     * the advertisement view.
     */
    fun updateAdv(newAdv: Advertisement) {
        this._singleAdvertisementPH = newAdv
        this._pvtAdvertisement.value = _singleAdvertisementPH
    }
}