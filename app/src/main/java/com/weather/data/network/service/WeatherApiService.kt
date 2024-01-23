package com.weather.data.network.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.weather.data.network.weaher.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL_WEATHER = "https://api.open-meteo.com/"

private var retrofit  = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL_WEATHER)
    .build()

object WeatherApiService {
    val retrofitService: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}
