package com.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.R
import com.weather.WeatherApp
import com.weather.databinding.ItemDayWeatherBinding
import com.weather.domain.model.weather.WeatherDataHourly
import java.time.format.DateTimeFormatter

class HourlyAdapter: RecyclerView.Adapter<HourlyAdapter.WeatherViewHolder>() {
     var weathers: List<WeatherDataHourly> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayWeatherBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weathers.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData: WeatherDataHourly = weathers[position]
        with(holder.binding) {

            hourText.text = weatherData.time.format(DateTimeFormatter.ofPattern("HH:mm"))

            val temperature = weatherData.temperature
            val temperatureFormatter = WeatherApp.context.resources.getString(R.string.temperature_format,
                temperature.toString())
            temperatureText.text = temperatureFormatter

            picture.setImageResource(weatherData.weatherType.iconId)
        }
    }

    class WeatherViewHolder(
        val binding: ItemDayWeatherBinding
    ): RecyclerView.ViewHolder(binding.root)
}
