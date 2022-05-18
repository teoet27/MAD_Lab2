package it.polito.MAD.group06.repository

import android.app.Application
import it.polito.MAD.group06.models.service.Service
import it.polito.MAD.group06.models.service.ServiceDatabase
import it.polito.MAD.group06.remote.FirestoreDatabase

class ServiceRepository(application: Application) {
    private val db = FirestoreDatabase.getDatabase(application)
    private val serviceDAO = ServiceDatabase.getDatabase(application).serviceDao()

    fun insertService(name:String) = serviceDAO.insertService(Service(name))
    fun services() = serviceDAO.findAll()
    fun removeServiceWithName(name: String) = serviceDAO.removeServiceWithName(name)
}