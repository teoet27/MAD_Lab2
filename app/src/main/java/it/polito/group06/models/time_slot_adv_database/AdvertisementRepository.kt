package it.polito.group06.models.time_slot_adv_database

import android.app.Application

class AdvertisementRepository(application: Application) {
    private val adsDao = AdvertisementDatabase.getDatabase(application).adsDao()

    fun insertAd(ad: Advertisement) = adsDao.insertAd(ad)
    fun advertisements() = adsDao.findAll()
    fun removeAdWithId(id: Long) = adsDao.removeAdWithId(id)
    fun clearAll() = adsDao.clearAll()
    fun updateAdv(id: Long, advTitle: String, advDescription: String, advLocation: String, advDate: String, advDuration: Float, advAccount: String, isPrivate: Boolean) = adsDao.updateAdv(id, advTitle, advDescription, advLocation, advDate, advDuration, advAccount, isPrivate)
}