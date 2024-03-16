package com.pri.weatherapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pri.weatherapp.data.model.DailyWeather
import com.pri.weatherapp.databinding.ItemDailyForecastBinding
import com.pri.weatherapp.utils.setDataImage
import java.util.Calendar

class DailyForecastAdapter(private val data: List<DailyWeather>) :
    RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {
    private var nextIndex = 5
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDailyForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(nextIndex)
    }

    inner class ViewHolder(private val binding: ItemDailyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(index: Int) {
            for (i in index until data.size) {
                val item = data[i]
                val dt = item.dtTxt.orEmpty() // dt_text.format=2020-06-26 12:00:00
                val a = dt.trim().split(" ")
                if (i == data.size - 1 || a.getOrNull(1) == "12:00:00") {
                    val dateSplit = a.firstOrNull().orEmpty().split("-")
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.set(
                        dateSplit[0].toInt(),
                        dateSplit[1].toInt() - 1,
                        dateSplit[2].toInt()
                    )
                    val forecastDate = calendar.time
                    val dateString = forecastDate.toString()
                    val forecastDateSplit = dateString.trim().split(" ")
                    val date =
                        forecastDateSplit.getOrNull(0) + ", " + forecastDateSplit.getOrNull(1) + " " + forecastDateSplit.getOrNull(2)
                    binding.tvForecastDay.text = date
                    val temp = Math.round(item.main?.temp ?: 0.0).toString() + "°"
                    binding.tvForecastTemp.text = temp
                    val icon = item.weather?.firstOrNull()?.icon
                    binding.ivForecastIcon.setDataImage(icon)
                    nextIndex = i + 1
                    return
                }
            }
        }
    }
}