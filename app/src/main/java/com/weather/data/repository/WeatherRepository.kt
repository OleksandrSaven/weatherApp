package com.weather.data.repository

import com.weather.data.network.weaher.WeatherDailyDto
import com.weather.data.network.weaher.WeatherDto
import com.weather.data.network.weaher.WeatherHourlyDto
import com.weather.database.WeatherDailyEntity
import com.weather.database.WeatherHourlyEntity
import com.weather.domain.util.Resource

interface WeatherRepository {

    suspend fun saveWeatherHourlyData(weatherHourlyDto: WeatherHourlyDto)

    suspend fun saveWeatherDailyDate(weatherDailyDto: WeatherDailyDto)

    suspend fun getAllWeatherHourlyEntities(): List<WeatherHourlyEntity>

    suspend fun getAllWeatherDailyEntities(): List<WeatherDailyEntity>

    suspend fun deleteOldWeatherDailyData()

    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherDto>
}
