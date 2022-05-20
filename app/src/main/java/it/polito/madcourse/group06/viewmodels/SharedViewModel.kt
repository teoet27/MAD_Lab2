package it.polito.madcourse.group06.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.madcourse.group06.utilities.ServiceTools


class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Boolean>()
    val filter = MutableLiveData<ServiceTools.AdvFilter>()

    fun select(value: Boolean) {
        selected.value = value
    }

    fun setFilter(advFilter: ServiceTools.AdvFilter){
        filter.value=advFilter
    }
}