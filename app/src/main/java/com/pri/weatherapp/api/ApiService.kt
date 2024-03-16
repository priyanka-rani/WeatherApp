package com.pri.weatherapp.api

import com.pri.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("/data/2.5/forecast")
    suspend fun getWeatherByLatLon(
        @QueryMap body: Map<String, String>,
        @Query("appid") appId: String = ApiInfo.apiKey,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}