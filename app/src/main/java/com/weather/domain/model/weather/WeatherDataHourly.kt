package com.weather.domain.model.weather

import java.time.LocalDateTime

data class WeatherDataHourly(
    val time: LocalDateTime,
    val temperature: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val weatherType: WeatherType
)
