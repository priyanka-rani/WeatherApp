package com.pri.weatherapp.utils

import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pri.weatherapp.R

/*to convert serializable to map of String*/
//convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val gson = Gson()
    val json = if (this is String) this else gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}



fun ImageView.setDataImage(value: String?) {
    when (value) {
        "01d" -> R.drawable.w01d
        "01n" -> R.drawable.w01d
        "02d" -> R.drawable.w02d
        "02n" -> R.drawable.w02d
        "03d" -> R.drawable.w03d
        "03n" -> R.drawable.w03d
        "04d" -> R.drawable.w04d
        "04n" -> R.drawable.w04d
        "09d" -> R.drawable.w09d
        "09n" -> R.drawable.w09d
        "10d" -> R.drawable.w10d
        "10n" -> R.drawable.w10d
        "11d" -> R.drawable.w11d
        "11n" -> R.drawable.w11d
        "13d" -> R.drawable.w13d
        "13n" -> R.drawable.w13d
        else -> R.drawable.w03d
    }.also { imageRes ->
        setImageResource(imageRes)
    }
}
