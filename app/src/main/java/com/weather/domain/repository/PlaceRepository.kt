package com.weather.domain.repository

import com.weather.domain.model.place.Place
import com.weather.domain.util.Resource

interface PlaceRepository {
    fun getPlace(name: String): Resource<Place>
}