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

    fun updateSearchState(searchedWord: String? = null,
                          sortParameter: Int? = null,
                          sortUpFlag: Boolean? = null,
                          myAdsFlag: Boolean? = null,
                          filter: AdvFilter? = null) {
        if (searchedWord != null)
            _searchState.searchedWord = searchedWord
        if (sortParameter != null)
            _searchState.sortParameter = sortParameter
        if (sortUpFlag != null)
            _searchState.sortUpFlag = sortUpFlag
        if (myAdsFlag != null)
            _searchState.myAdsFlag = myAdsFlag
        if (filter != null)
            _searchState.filter = filter
        searchState.value = _searchState
    }

    fun resetSearchState() {
        _searchState = SearchState(null, 0, true, false, AdvFilter())
        searchState.value = _searchState
    }

}