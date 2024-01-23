package com.weather.data.network.weaher

import com.squareup.moshi.Json

data class WeatherDto(
    @field:Json(name = "hourly")
    val weatherData: WeatherDataDto
)
