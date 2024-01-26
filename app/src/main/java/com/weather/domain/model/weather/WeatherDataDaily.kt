package com.weather.domain.model.weather

import java.time.LocalDate

data class WeatherDataDaily (
    val maxTemperature: Double,
    val minTemperature: Double,
    val weatherType: WeatherType,
    val dayOfWeek: LocalDate
)