package com.weather.data.repository

import com.weather.data.mapper.toPlace
import com.weather.data.network.service.PlaceApiService
import com.weather.domain.model.place.Place
import com.weather.domain.repository.PlaceRepository
import com.weather.domain.util.Resource

class PlaceRepositoryImpl(
): PlaceRepository {
    override fun getPlace(name: String): Resource<Place> {
      return try {
          Resource.Success(
              data = PlaceApiService.retrofitService.getLocation(name).toPlace()
          )
      } catch (e: Exception) {
          e.printStackTrace()
          Resource.Error(e.message ?: "An unknown error.")
      }
    }
}
