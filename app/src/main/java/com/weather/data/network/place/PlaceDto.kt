package com.weather.data.network.place

import com.squareup.moshi.Json

data class PlaceDto(
    @field:Json(name = "results")
    val placeData: List<PlaceDataDto>
)
