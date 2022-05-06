package it.polito.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import it.polito.group06.models.time_slot_adv_database.Advertisement
import it.polito.group06.models.time_slot_adv_database.AdvertisementRepository
import kotlin.concurrent.thread

class TimeSlotAdViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AdvertisementRepository(application)

    /**
     * List of advertisements
     */
    val ads = repo.advertisements()

    /**
     * Insertion of a new advertisement
     * @param ad a new advertisement
     */
    fun insertAd(ad: Advertisement) {
        thread {
            repo.insertAd(ad)
        }
    }

    /**
     * Remove an advertisement
     * @param id integer unique number identifying an advertisement
     */
    fun removeAd(id: Long) {
        thread {
            repo.removeAdWithId(id)
        }
    }
}