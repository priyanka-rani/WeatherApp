package com.pri.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherReqModel(
    @SerializedName("q")
    val q: String? = null,
    @SerializedName("lat")
    val lat: String? = null,
    @SerializedName("lon")
    val lon: String? = null
)