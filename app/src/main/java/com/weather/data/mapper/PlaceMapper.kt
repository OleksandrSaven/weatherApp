package com.weather.data.mapper



import com.weather.data.network.place.PlaceDataDto
import com.weather.domain.model.place.Location
import com.weather.domain.model.place.Place

fun PlaceDataDto.toPlace(): Place {
    val name = name
    val lat = latitude
    val long = longitude
    return Place(name, Location(lat, long))
}
