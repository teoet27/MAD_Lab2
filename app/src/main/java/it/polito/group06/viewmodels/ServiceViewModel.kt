package it.polito.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.polito.group06.models.advertisement.Advertisement
import it.polito.group06.models.service.Service
import it.polito.group06.repository.ServiceRepository
import kotlin.concurrent.thread

class ServiceViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Repository
     */
    private val repositoryService = ServiceRepository(application)

    /**
     * Single [Service]
     */
    private var _singleServicePH = Service(
        null, ""
    )
    private val _pvtService = MutableLiveData<Service>().also {
        it.value = _singleServicePH
    }
    val service: LiveData<Service> = this._pvtService

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
    fun insertAd(service: Service) {
        thread {
            repositoryService.insertService(service)
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
}