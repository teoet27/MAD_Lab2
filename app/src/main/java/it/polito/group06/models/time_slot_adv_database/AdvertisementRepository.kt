package it.polito.group06.models.time_slot_adv_database

import android.app.Application

class AdvertisementRepository(application: Application) {
    private val adsDao = AdvertisementDatabase.getDatabase(application).adsDao()

    fun insertAd(ad: Advertisement) = adsDao.insertAd(ad)
    fun advertisements() = adsDao.findAll()
    fun removeAdWithId(id: Long) = adsDao.removeAdWithId(id)
}