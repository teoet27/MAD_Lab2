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
                          activeAdsFlag:Boolean?=null,
                          savedAdsFlag:Boolean?=null,
                          filter: AdvFilter? = null) {
        if (searchedWord != null)
            _searchState.searchedWord = searchedWord
        if (sortParameter != null)
            _searchState.sortParameter = sortParameter
        if (sortUpFlag != null)
            _searchState.sortUpFlag = sortUpFlag
        if (myAdsFlag != null)
            _searchState.myAdsFlag = myAdsFlag
        if (activeAdsFlag != null)
            _searchState.activeAdsFlag = activeAdsFlag
        if (savedAdsFlag != null)
            _searchState.savedAdsFlag = savedAdsFlag
        if (filter != null)
            _searchState.filter = filter
        searchState.value = _searchState
    }

    // reset search state and initialize if you want some parameter
    fun resetSearchState(searchedWord: String? = null,
                         sortParameter: Int? = 0,
                         sortUpFlag: Boolean? = true,
                         myAdsFlag: Boolean? = false,
                         activeAdsFlag:Boolean?=false,
                         savedAdsFlag:Boolean?=false,
                         filter: AdvFilter? = AdvFilter()) {
        _searchState = SearchState(searchedWord,sortParameter,sortUpFlag,myAdsFlag,activeAdsFlag,savedAdsFlag,filter)
        searchState.value = _searchState
    }

}