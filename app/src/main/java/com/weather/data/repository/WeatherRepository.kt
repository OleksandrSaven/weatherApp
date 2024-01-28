package com.weather.data.repository

import com.weather.data.mapper.toWeatherData
import com.weather.data.network.service.WeatherApiService
import com.weather.domain.model.weather.WeatherInfo
import com.weather.domain.repository.WeatherRepository
import com.weather.domain.util.Resource

class WeatherRepository(
): WeatherRepository {
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = WeatherApiService.retrofitService.getWeatherData(
                    lat = lat,
                    long = long)
                    .toWeatherData()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error.")
        }
    }
}
