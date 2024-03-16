package com.pri.weatherapp.data.model


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("city")
    val city: City? = null,
    @SerializedName("list")
    val list: List<DailyWeather>? = null
)