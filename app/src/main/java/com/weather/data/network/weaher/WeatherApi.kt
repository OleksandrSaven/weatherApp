package com.weather.data.network.weaher

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl&&daily=weathercode&daily=temperature_2m_max,temperature_2m_min")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherDto
}
