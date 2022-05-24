package it.polito.madcourse.group06.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.madcourse.group06.utilities.TimeslotTools


class SharedViewModel : ViewModel() {
    private var _selected_skill = ""
    private var _selected = false
    private var _sortParam = "Title"
    private var _sortUp = false
    private var _lastSortUp = false
    private var _filter = TimeslotTools.AdvFilter()
    private var _updateRV = false

    var selected_skill = MutableLiveData<String>().also {
        it.value = _selected_skill
    }
    var selected = MutableLiveData<Boolean>()
    var sortParam = MutableLiveData<String>().also {
        it.value = _sortParam
    }
    var sortUp = MutableLiveData<Boolean>().also {
        it.value = _sortUp
    }
    var lastSortUp = MutableLiveData<Boolean>().also {
        it.value = _lastSortUp
    }
    var filter = MutableLiveData<TimeslotTools.AdvFilter>().also {
        it.value = _filter
    }
    var updateRV = MutableLiveData<Boolean>().also {
        it.value = _updateRV
    }

    fun selectSkill(skill: String) {
        _selected_skill = skill
        selected_skill.value = _selected_skill
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

    fun getSortParam(): String {
        return sortParam.value!!
    }

    fun updateSort() {
        sortUp.value = _sortUp
    }

    fun toggleSortDirection() {
        _lastSortUp = _sortUp
        lastSortUp.value = _lastSortUp
        _sortUp = !_sortUp
        sortUp.value = _sortUp
    }

    fun setSortUp() {
        _sortUp = true
        sortUp.value = _sortUp
    }

    fun updateRV() {
        _updateRV = true
        updateRV.value = _updateRV
    }
}