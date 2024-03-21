package com.weather.domain.model.weather

data class WeatherState(
    val loadingState: Boolean = false,
    val city: String? = null,
    var errorMessage: String? = null,
    val weatherByHourly: List<WeatherDataHourly>? = null,
    val currentValue: WeatherDataHourly? = null,
    val weatherByDaily: List<WeatherDataDaily>? = null
)
