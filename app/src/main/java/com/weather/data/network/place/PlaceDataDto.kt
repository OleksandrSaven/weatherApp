package com.weather.data.network.place

import com.squareup.moshi.Json

data class PlaceDataDto(
    @Json(name = "name")
    val name: String,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "country")
    val country: String,
)
