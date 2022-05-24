package it.polito.madcourse.group06.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.madcourse.group06.utilities.TimeslotTools

class SharedViewModel : ViewModel() {

    private var _selected = false
    private var _filter = TimeslotTools.AdvFilter()

    var selected = MutableLiveData<Boolean>()
    var filter = MutableLiveData<TimeslotTools.AdvFilter>().also {
        it.value = _filter
    }

    fun select(value: Boolean) {
        _selected = value
        selected.value = _selected
    }

    fun setFilter(advFilter: TimeslotTools.AdvFilter) {
        _filter = advFilter
        filter.value = _filter
    }

}