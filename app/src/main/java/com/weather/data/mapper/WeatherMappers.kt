package com.weather.data.mapper

import com.weather.data.network.weaher.WeatherDataDto
import com.weather.data.network.weaher.WeatherDto
import com.weather.domain.model.weather.WeatherData
import com.weather.domain.model.weather.WeatherInfo
import com.weather.domain.model.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>>{
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeed[index]
        val humidity = humidities[index]
        val pressure = pressures[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperature = temperature,
                pressure = pressure,
                humidity = humidity,
                windSpeed = windSpeed,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val now = LocalDateTime.now()
    val weatherDateMap = weatherData.toWeatherDataMap()
    val currentWeatherData = weatherDateMap[0]?.find {
        val hour = when{
            now.minute < 30 -> now.hour
            now.hour == 23 -> 12.00
            else -> now.hour + 1
        }
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay =  weatherDateMap,
        currentWeatherData = currentWeatherData
    )
}