package com.weather.domain.repository

import com.weather.domain.model.weather.WeatherInfo
import com.weather.domain.util.Resource

interface WeatherRepository {
  suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}