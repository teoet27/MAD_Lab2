package it.polito.group06.models.time_slot_adv_database

import android.app.Application

class TimeSlotAdRepository(application: Application) {
    private val adsDao = TimeSlotAdDatabase.getDatabase(application).adsDao()

    fun insertAd(ad: TimeSlotAd) = adsDao.insertAd(ad)
    fun advertisements() = adsDao.findAll()
    fun removeAdWithId(id: Long) = adsDao.removeAdWithId(id)
}