package it.polito.group06.repository

import android.app.Application
import it.polito.group06.models.service.Service
import it.polito.group06.models.service.ServiceDatabase

class ServiceRepository(application: Application) {
    private val serviceDAO = ServiceDatabase.getDatabase(application).serviceDao()

    fun insertService(service: Service) = serviceDAO.insertService(service)
    fun services() = serviceDAO.findAll()
    fun removeServiceWithName(name: String) = serviceDAO.removeServiceWithName(name)
}