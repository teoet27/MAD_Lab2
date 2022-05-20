package it.polito.MAD.group06.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Boolean>()

    fun select(value: Boolean) {
        selected.value = value
    }
}