package it.polito.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import it.polito.group06.models.time_slot_adv_database.TimeSlotAd
import it.polito.group06.models.time_slot_adv_database.TimeSlotAdRepository
import kotlin.concurrent.thread

class TimeSlotAdViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = TimeSlotAdRepository(application)
    val ads = repo.advertisements()

    fun insertAd(ad: TimeSlotAd) {
        thread {
            repo.insertAd(ad)
        }
    }

    fun removeAd(id: Long) {
        thread {
            repo.removeAdWithId(id)
        }
    }
}