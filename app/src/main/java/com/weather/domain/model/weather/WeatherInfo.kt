package com.weather.domain.model.weather

data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherDataHourly>>,
    val weatherDatePerWeek: Map<Int, List<WeatherDataDaily>>,
    val currentWeatherData: WeatherDataHourly?
)
