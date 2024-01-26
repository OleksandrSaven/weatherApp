package com.weather.data.network.weaher

import com.squareup.moshi.Json

data class WeatherDailyDto(
    val time: List<String>,
    val weathercode: List<Int>,
    @field:Json(name = "temperature_2m_max")
    val maxTemperatures: List<Double>,
    @field:Json(name = "temperature_2m_min")
    val minTemperatures: List<Double>
)
