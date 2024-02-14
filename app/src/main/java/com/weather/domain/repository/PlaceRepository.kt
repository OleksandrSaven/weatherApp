package com.weather.domain.repository

import com.weather.domain.model.place.Place
import com.weather.util.Resource

interface PlaceRepository {
    suspend fun getPlace(name: String): Resource<Place>
}
