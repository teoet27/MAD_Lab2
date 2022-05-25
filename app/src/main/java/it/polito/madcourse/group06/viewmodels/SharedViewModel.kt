package it.polito.madcourse.group06.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.madcourse.group06.utilities.AdvFilter
import it.polito.madcourse.group06.utilities.SearchState

class SharedViewModel : ViewModel() {

    private var _selected = false
    private var _searchState = SearchState()

    var selected = MutableLiveData<Boolean>()
    var searchState = MutableLiveData<SearchState>().also {
        it.value = _searchState
    }

    fun select(value: Boolean) {
        _selected = value
        selected.value = _selected
    }

    fun updateSearchState(state: SearchState) {
        if (state.searchedWord != null)
            _searchState.searchedWord = state.searchedWord
        if (state.sortParameter != null)
            _searchState.sortParameter = state.sortParameter
        if (state.sortUpFlag != null)
            _searchState.sortUpFlag = state.sortUpFlag
        if (state.myAdsFlag != null)
            _searchState.myAdsFlag = state.myAdsFlag
        if (state.filter != null)
            _searchState.filter = state.filter
        searchState.value = _searchState
    }

    fun resetSearchState() {
        _searchState = SearchState(null, 0, true, false, AdvFilter())
        searchState.value = _searchState
    }

}