package com.pri.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.pri.weatherapp.data.model.Coord
import com.pri.weatherapp.data.model.Resource
import com.pri.weatherapp.data.model.WeatherReqModel
import com.pri.weatherapp.data.model.WeatherResponse
import com.pri.weatherapp.data.repo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherApi = MutableLiveData<WeatherReqModel>()

    val weatherLiveData: LiveData<Resource<WeatherResponse>> = _weatherApi.switchMap {
        if (it == null) liveData { emit(Resource.error(msg = "Invalid location")) }
        else repository.getWeatherData(it)
    }

    fun callCurrentWeatherByCity(city: String) {
        _weatherApi.postValue(WeatherReqModel(q = city))
    }

    fun callWeatherApiByCoord(coord: Coord?) {
        coord?.let {
            _weatherApi.postValue(
                WeatherReqModel(
                    lat = coord.lat.toString(),
                    lon = coord.lon.toString()
                )
            )
        }
    }

    fun refresh() {
        _weatherApi.value?.let {
            _weatherApi.postValue(it)
        }
    }
}