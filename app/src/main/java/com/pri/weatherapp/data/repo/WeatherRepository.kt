package com.pri.weatherapp.data.repo


import androidx.lifecycle.liveData
import com.pri.weatherapp.api.ApiService
import com.pri.weatherapp.data.model.Resource
import com.pri.weatherapp.data.model.WeatherReqModel
import com.pri.weatherapp.utils.convert
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: ApiService) {

    fun getWeatherData(reqModel: WeatherReqModel) = liveData {
        emit(Resource.loading())
        try {
            val response = apiService.getWeatherByLatLon(reqModel.convert())
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(msg = e.message))
            e.printStackTrace()
        }
    }

}