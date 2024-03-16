package com.pri.weatherapp.ui.citysearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CitySearchViewModel : ViewModel() {
    private val _searchCityQuery: MutableLiveData<String> = MutableLiveData()
    val searchedCityQuery: LiveData<String> = _searchCityQuery

    fun setSearchedCityQuery(q: String) {
        _searchCityQuery.postValue(q)
    }
}