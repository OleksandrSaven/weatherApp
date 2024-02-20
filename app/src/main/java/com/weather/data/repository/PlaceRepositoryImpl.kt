package com.weather.data.repository

import com.weather.data.constants.AppConstants
import com.weather.data.mapper.toPlace
import com.weather.data.network.place.PlaceApi
import com.weather.domain.model.place.Place
import com.weather.domain.repository.PlaceRepository
import com.weather.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val FIRST_CITY = 0

class PlaceRepositoryImpl @Inject constructor(private val apiPlaceService : PlaceApi): PlaceRepository {
    override suspend fun getPlace(name: String): Resource<Place> {
        return withContext(Dispatchers.IO) {
        try {
            val response = apiPlaceService.getLocation(name)
            Resource.Success(
                data = response.placeData[FIRST_CITY].toPlace()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(AppConstants.ERROR_MESSAGE)
            }
        }
    }
}
