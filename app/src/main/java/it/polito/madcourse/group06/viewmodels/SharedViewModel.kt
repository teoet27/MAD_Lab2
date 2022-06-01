package it.polito.madcourse.group06.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.madcourse.group06.utilities.AdvFilter
import it.polito.madcourse.group06.utilities.SearchState

class SharedViewModel : ViewModel() {

    private var _searchState = SearchState()
    private var _homePressedTwice:Boolean =false
    private var _pressCnt=0

    var searchState = MutableLiveData<SearchState>().also {
        it.value = _searchState
    }

    var homePressedTwice = MutableLiveData<Boolean>().also{
        it.value = _homePressedTwice
    }

    fun homeTabPressed(state:Boolean=true){
        _homePressedTwice=false
        homePressedTwice.value = _homePressedTwice
        if(state){
            _pressCnt++
            if (_pressCnt == 2) {
                _homePressedTwice = true
                homePressedTwice.value = _homePressedTwice
                _pressCnt = 0
            }
        }
        else _pressCnt = 0
    }

    fun updateSearchState(
        currentTab: String? = null,
        selectedSkill: String? = null,
        searchedWord: String? = null,
        sortParameter: Int? = null,
        sortUpFlag: Boolean? = null,
        myAdsFlag: Boolean? = null,
        activeAdsFlag: Boolean? = null,
        savedAdsFlag: Boolean? = null,
        filter: AdvFilter? = null
    ) {
        if(currentTab!=null)
            _searchState.currentTab=currentTab
        if (selectedSkill != null)
            _searchState.selectedSkill = selectedSkill
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
    fun resetSearchState(
        currentTab: String? = null,
        selectedSkill: String? = null,
        searchedWord: String? = null,
        sortParameter: Int? = 0,
        sortUpFlag: Boolean? = true,
        myAdsFlag: Boolean? = false,
        activeAdsFlag: Boolean? = false,
        savedAdsFlag: Boolean? = false,
        filter: AdvFilter? = AdvFilter()
    ) {
        _searchState = SearchState(
            currentTab,
            _searchState.selectedSkill,
            searchedWord,
            sortParameter,
            sortUpFlag,
            myAdsFlag,
            activeAdsFlag,
            savedAdsFlag,
            filter
        )
        searchState.value = _searchState
    }
}