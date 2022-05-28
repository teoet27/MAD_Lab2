package it.polito.madcourse.group06.visualization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CircularLoading {
    private var _isLoading = true
    private val _pvtIsLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also { it.value = this._isLoading }
    val isLoading: LiveData<Boolean> = _pvtIsLoading

    fun setIsLoading(loading: Boolean) {
        this._isLoading = loading
        this._pvtIsLoading.value = this._isLoading
    }
}