package com.weather.data.mapper

import com.weather.data.network.weaher.WeatherDailyDto
import com.weather.data.network.weaher.WeatherHourlyDto
import com.weather.data.network.weaher.WeatherDto
import com.weather.domain.model.weather.WeatherDataDaily
import com.weather.domain.model.weather.WeatherDataHourly
import com.weather.domain.model.weather.WeatherInfo
import com.weather.domain.model.weather.WeatherType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedHourly(
    val index: Int,
    val data: WeatherDataHourly
)

private data class IndexedDaily(
    val index: Int,
    val data: WeatherDataDaily
)

fun WeatherHourlyDto.toWeatherHourly(): Map<Int, List<WeatherDataHourly>>{
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeed[index]
        val humidity = humidities[index]
        val pressure = pressures[index]
        IndexedHourly(
            index = index,
            data = WeatherDataHourly(
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

fun WeatherDailyDto.toWeatherDaily(): Map<Int, List<WeatherDataDaily>> {
    return time.mapIndexed { index, time ->
        val maxTemperature = maxTemperatures[index]
        val minTemperature = minTemperatures[index]
        val weatherCode = weathercode[index]
        IndexedDaily(
            index = index,
            data = WeatherDataDaily(
                maxTemperature = maxTemperature,
                minTemperature= minTemperature,
                weatherType = WeatherType.fromWMO(weatherCode),
                dayOfWeek = LocalDate.parse(time, DateTimeFormatter.ISO_DATE)
            )
        )
    }.groupBy{
        it.index / 7
    }.mapValues {
        it.value.map { it.data }
    }
}


fun WeatherDto.toWeatherData(): WeatherInfo {
    val now = LocalDateTime.now()
    val weatherDateMapHourly = weatherHourly.toWeatherHourly()
    val weatherDataMapDaily = weatherDaily.toWeatherDaily()
    val currentWeatherData = weatherDateMapHourly[0]?.find {
        val hour = when{
            now.minute < 30 -> now.hour
            now.hour == 23 -> 23
            else -> now.hour + 1
        }
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDatePerWeek = weatherDataMapDaily,
        weatherDataPerDay =  weatherDateMapHourly,
        currentWeatherData = currentWeatherData
    )
}
