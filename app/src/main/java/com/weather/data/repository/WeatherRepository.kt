package com.weather.data.repository


import android.content.Context
import com.weather.data.mapper.toWeatherData
import com.weather.data.mapper.toWeatherHourlyEntity
import com.weather.data.network.service.WeatherApiService
import com.weather.data.network.weaher.WeatherHourlyDto
import com.weather.db.WeatherDatabase
import com.weather.db.WeatherHourlyDao
import com.weather.db.WeatherHourlyEntity
import com.weather.domain.model.weather.WeatherInfo
import com.weather.domain.repository.WeatherRepository
import com.weather.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class WeatherRepository( private val hourlyDao: WeatherHourlyDao): WeatherRepository {
    suspend fun saveWeatherHourlyData(weatherHourlyDto: WeatherHourlyDto, weatherHourlyDao: WeatherHourlyDao) {
        withContext(Dispatchers.IO) {
            val weatherHourlyEntities = weatherHourlyDto.toWeatherHourlyEntity()
            weatherHourlyDao.insertHourly(weatherHourlyEntities)
        }
    }
    suspend fun getAllWeatherHourlyEntities(context: Context): List<WeatherHourlyEntity> {
        return withContext(Dispatchers.IO) {
            val currentDate = LocalDate.now().toString()
            val weatherHourlyDao = WeatherDatabase.getInstance(context).weatherHourlyDao()
            return@withContext weatherHourlyDao.getAllHourlyData(currentDate)
        }
    }

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return withContext(Dispatchers.IO) {
           try {
                Resource.Success(
                    data = WeatherApiService.retrofitService.getWeatherData(
                        lat = lat,
                        long = long
                    ).toWeatherData()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error.")
            }
        }
    }
}
