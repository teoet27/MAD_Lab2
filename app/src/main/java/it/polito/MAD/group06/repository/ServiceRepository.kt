package it.polito.MAD.group06.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.MAD.group06.models.service.Service
import it.polito.MAD.group06.models.service.ServiceDatabase

class ServiceRepository(application: Application) {
    private val db = FirebaseFirestore.getInstance()
    private val serviceDAO = ServiceDatabase.getDatabase(application).serviceDao()

    fun insertService(name:String) = serviceDAO.insertService(Service(name))
    fun services() = serviceDAO.findAll()
    fun removeServiceWithName(name: String) = serviceDAO.removeServiceWithName(name)
}