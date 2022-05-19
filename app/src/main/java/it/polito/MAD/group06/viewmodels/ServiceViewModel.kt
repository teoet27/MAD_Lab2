package it.polito.MAD.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.MAD.group06.models.advertisement.Advertisement
import it.polito.MAD.group06.models.service.Service
import it.polito.MAD.group06.repository.ServiceRepository
import java.lang.Exception
import kotlin.concurrent.thread

class ServiceViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Repository
     */
    private val repositoryService = ServiceRepository(application)
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * Single [Service]
     */
    private var _singleServicePH = Service("")

    private val _pvtService = MutableLiveData<Service>().also {
        it.value = _singleServicePH
    }
    val service: LiveData<Service> = this._pvtService

    /**
     * List of Services
     */
    private val _services = MutableLiveData<List<Service>>()
    val listOfServices: LiveData<List<Service>> = _services

    init {
        listenerRegistration = db.collection("Service")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _services.value = emptyList()
                } else {
                    _services.value = value!!.mapNotNull { elem ->
                        elem.toService()
                    }
                }
            }
    }

    private fun DocumentSnapshot.toService(): Service? {
        return try {
            val serviceName = get("service_name") as String
            Service(serviceName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * getFullListOfServices
     */
    fun getFullListOfServices(): LiveData<List<Service>> {
        return this.repositoryService.services()
    }

    /**
     * Insertion of a new [Service]
     * @param service a new service
     */
    fun insertService(name:String) {
        thread {
            repositoryService.insertService(name)
        }
    }

    /**
     * Remove a service
     * @param id integer unique number identifying a [Service]
     */
    fun removeService(name: String) {
        thread {
            repositoryService.removeServiceWithName(name)
        }
    }

    /**
     * setSingleService is a method to update the [_pvtService] and it's called by the adapter to
     * trigger all the observators.
     * @param newService an object of class [Service] which contains all the information to fill
     * the Service view.
     */
    fun setSingleService(newService: Service) {
        this._singleServicePH = newService
        this._pvtService.value = _singleServicePH
    }

    /**
     * Unsubscribe from the Listener Registration
     */
    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
    }
}