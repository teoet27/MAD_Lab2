package it.polito.group06.repository

import android.app.Application
import it.polito.group06.models.advertisement.Advertisement
import it.polito.group06.models.advertisement.AdvertisementDatabase

class AdvertisementRepository(application: Application) {
    private val adsDao = AdvertisementDatabase.getDatabase(application).adsDao()

    fun insertAd(ad: Advertisement) = adsDao.insertAd(ad)
    fun advertisements() = adsDao.findAll()
    fun removeAdWithId(id: Long) = adsDao.removeAdWithId(id)
    fun clearAll() = adsDao.clearAll()
    fun updateAdv(adv: Advertisement) = adsDao.updateAccountName(adv)
    fun updateAccountName(adv: Advertisement) = adsDao.updateAccountName(adv)
}