package com.weather.domain.model.weather

data class WeatherState(
    var loadingState: Boolean,
    var city: String,
    var errorMessage: String?,
    var weatherByHourly: List<WeatherDataHourly>,
    var currentValue: WeatherDataHourly?
)
