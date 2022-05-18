package it.polito.MAD.group06.repository

import android.app.Application
import it.polito.MAD.group06.models.advertisement.Advertisement
import it.polito.MAD.group06.models.advertisement.AdvertisementDatabase
import it.polito.MAD.group06.remote.FirestoreDatabase

class AdvertisementRepository(application: Application) {
    private val db = FirestoreDatabase.getDatabase(application)
    private val adsDAO = AdvertisementDatabase.getDatabase(application).adsDao()

    fun insertAd(ad: Advertisement) = adsDAO.insertAd(ad)
    fun advertisements() = adsDAO.findAll()
    fun removeAdWithId(id: Long) = adsDAO.removeAdWithId(id)
    fun clearAll() = adsDAO.clearAll()
    fun updateAdv(adv: Advertisement) = adsDAO.updateAccountName(adv)
    fun updateAccountName(adv: Advertisement) = adsDAO.updateAccountName(adv)
}