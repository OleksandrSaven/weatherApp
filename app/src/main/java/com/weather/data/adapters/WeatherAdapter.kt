package com.weather.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.databinding.ItemWeatherBinding
import com.weather.domain.model.WeatherData
import java.time.format.DateTimeFormatter

class WeatherAdapter: RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
     var weathers: List<WeatherData> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weathers.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData: WeatherData = weathers[position]
        with(holder.binding) {
            hourlyTxt.text = weatherData.time.format(DateTimeFormatter.ofPattern("HH:mm"))
            temperatureText.text = weatherData.temperature.toString() + "°C"
            picture.setImageResource(weatherData.weatherType.iconId)
        }
    }
    class WeatherViewHolder(
        val binding: ItemWeatherBinding
    ): RecyclerView.ViewHolder(binding.root)
}
