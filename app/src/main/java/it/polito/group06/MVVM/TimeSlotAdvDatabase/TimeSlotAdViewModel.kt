package it.polito.group06.MVVM.TimeSlotAdvDatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlin.concurrent.thread

class TimeSlotAdViewModel(application: Application):AndroidViewModel(application) {
    private val repo = TimeSlotAdRepository(application)
    val ads=repo.advertisements()

    fun insertAd(ad:TimeSlotAd){
        thread{
            repo.insertAd(ad)
        }
    }
    fun removeAd(id:Long){
        thread{
            repo.removeAdWithId(id)
        }
    }
}