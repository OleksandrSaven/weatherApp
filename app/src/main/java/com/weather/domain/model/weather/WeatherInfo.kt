package com.weather.domain.model.weather

data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?
)
