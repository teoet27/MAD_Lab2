package it.polito.madcourse.group06.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.madcourse.group06.utilities.TimeslotTools


class SharedViewModel : ViewModel() {
    private var _selected = false
    private var _sortParam = "Title"
    private var _sortUp = false
    private var _filter = TimeslotTools.AdvFilter()
    private var _updateRV = false

    var selected = MutableLiveData<Boolean>()
    var sortParam = MutableLiveData<String>().also {
        it.value = _sortParam
    }
    var sortUp = MutableLiveData<Boolean>().also {
        it.value = _sortUp
    }
    var filter = MutableLiveData<TimeslotTools.AdvFilter>().also {
        it.value = _filter
    }
    var updateRV = MutableLiveData<Boolean>().also{
        it.value=_updateRV
    }


    fun select(value: Boolean) {
        _selected = value
        selected.value = _selected
    }

    fun setFilter(advFilter: TimeslotTools.AdvFilter) {
        _filter = advFilter
        filter.value = _filter
    }

    fun setSortParam(name: String) {
        _sortParam = name
        sortParam.value = _sortParam
    }

    fun toggleSortDirection() {
        _sortUp = !_sortUp
        sortUp.value = _sortUp
    }
    fun updateRV(){
        _updateRV=true
        updateRV.value=_updateRV
    }
}