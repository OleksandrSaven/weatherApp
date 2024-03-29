package com.weather.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.domain.model.weather.WeatherDataDaily
import com.weather.domain.model.weather.WeatherType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "weather_daily")
data class WeatherDailyEntity(
    @PrimaryKey
    val time: String,
    val weatherCode: Int,
    val minTemperature: Double,
    val maxTemperature: Double
)

fun WeatherDailyEntity.toDomainModel(): WeatherDataDaily {
    return  WeatherDataDaily(
        dayOfWeek = LocalDate.parse(this.time, DateTimeFormatter.ISO_DATE),
        weatherType = WeatherType.fromWMO(this.weatherCode),
        maxTemperature = this.maxTemperature,
        minTemperature = this.minTemperature)
}
