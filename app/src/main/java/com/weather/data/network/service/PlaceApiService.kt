package com.weather.data.network.service

import com.weather.data.network.place.PlaceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_PLACE = "https://geocoding-api.open-meteo.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL_PLACE)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object PlaceApiService {
    val retrofitService: PlaceApi by lazy {
        retrofit.create(PlaceApi::class.java)
    }
}