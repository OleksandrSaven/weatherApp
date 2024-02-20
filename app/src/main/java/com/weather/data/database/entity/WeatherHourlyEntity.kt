package com.weather.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_hourly")
data class WeatherHourlyEntity(
    @PrimaryKey
    val time: String,
    val temperature: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val weatherType: Int
)
