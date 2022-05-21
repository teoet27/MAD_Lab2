package it.polito.madcourse.group06.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.madcourse.group06.utilities.TimeslotTools


class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Boolean>()
    val filter = MutableLiveData<TimeslotTools.AdvFilter>()
    val sortParam = MutableLiveData<String>()
    val sort_up = MutableLiveData<Boolean>()

    fun select(value: Boolean) {
        selected.value = value
    }

    fun setFilter(advFilter: TimeslotTools.AdvFilter){
        filter.value=advFilter
    }

    fun setSortParam(name:String){
        sortParam.value=name
    }

    fun setSortUpDirection(value: Boolean){
        sort_up.value=value
    }
}