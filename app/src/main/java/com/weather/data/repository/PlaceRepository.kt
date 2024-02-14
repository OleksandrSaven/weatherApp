package com.weather.data.repository

import com.weather.domain.model.place.Place
import com.weather.domain.util.Resource

interface PlaceRepository {
    suspend fun getPlace(name: String): Resource<Place>
}