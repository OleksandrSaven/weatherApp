package com.weather.data.network.place

import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceApi {
    @GET("v1/search?count=1&language=en&format=json")
    suspend fun getLocation(
       @Query("name") name: String
    ): PlaceDto
}
