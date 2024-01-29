package com.weather.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.domain.model.weather.WeatherDataHourly
import com.weather.domain.model.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "weather_hourly")
data class WeatherHourlyEntity(
    @PrimaryKey
    //val id: Long = 0,
    val time: String,
    val temperature: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val weatherType: Int
)

@Entity(tableName = "weather_daily")
data class WeatherDailyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val time: String,
    val weatherCode: Int,
    val minTemperature: Double,
    val maxTemperature: Double
)


 fun List<WeatherHourlyEntity>.toDomainModel(): List<WeatherDataHourly>{
     return map {
         WeatherDataHourly(
             time = LocalDateTime.parse(it.time, DateTimeFormatter.ISO_DATE_TIME),
             temperature = it.temperature,
             pressure = it.pressure,
             windSpeed = it.windSpeed,
             humidity = it.humidity,
             weatherType = WeatherType.fromWMO(it.weatherType)
         )
     }
 }
