package com.weather.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.R
import com.weather.WeatherApp
import com.weather.databinding.ItemWeekWeatherBinding
import com.weather.domain.model.weather.WeatherDataDaily

private const val FIRST_LETTER = 0
private const val LAST_LETTER = 3

class DaysAdapter: RecyclerView.Adapter<DaysAdapter.WeatherViewHolder> () {
    var weathers: List<WeatherDataDaily> = emptyList()
    set(newValue){
        field = newValue
        notifyDataSetChanged()
    }

    class WeatherViewHolder(
        val binding: ItemWeekWeatherBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeekWeatherBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weathers.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weathers[position]
        with(holder.binding) {
            picture.setImageResource(weather.weatherType.iconId)

            val maxTemperature = weather.maxTemperature
            val maxTemperatureFormatted = WeatherApp.context.resources.getString(R.string.maxTemperature_format,
                maxTemperature.toString())
            maxTempText.text = maxTemperatureFormatted

            val minTemperature = weather.minTemperature
            val minTemperatureFormatted = WeatherApp.context.resources.getString(R.string.minTemperature_format,
                minTemperature.toString())
            minTempText.text = minTemperatureFormatted

            val dayOfMonth = weather.dayOfWeek.dayOfMonth
            val dayOfWeek = weather.dayOfWeek.dayOfWeek.toString().substring(FIRST_LETTER, LAST_LETTER)
            dateText.text = WeatherApp.context.getString(R.string.day_month, dayOfMonth.toString(), dayOfWeek)
        }
    }
}
