package com.weather.data.repository

import com.weather.data.mapper.toPlace
import com.weather.data.network.service.PlaceApiService
import com.weather.domain.model.place.Place
import com.weather.domain.repository.PlaceRepository
import com.weather.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepositoryImpl(
): PlaceRepository {
    override suspend fun getPlace(name: String): Resource<Place> {
        return withContext(Dispatchers.IO){
        try {
            val response = PlaceApiService.retrofitService.getLocation(name)
            Resource.Success(
                data = response.placeData[0].toPlace()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}
}
