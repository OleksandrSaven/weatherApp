package com.weather.data.repository

import com.weather.data.mapper.toWeatherDailyEntity
import com.weather.data.mapper.toWeatherHourlyEntity
import com.weather.data.network.service.WeatherApiService
import com.weather.data.network.weaher.WeatherDailyDto
import com.weather.data.network.weaher.WeatherDto
import com.weather.data.network.weaher.WeatherHourlyDto
import com.weather.database.WeatherDailyDao
import com.weather.database.WeatherDailyEntity
import com.weather.database.WeatherHourlyDao
import com.weather.database.WeatherHourlyEntity
import com.weather.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val  hourlyDao: WeatherHourlyDao,
                                                private val dailyDao: WeatherDailyDao): WeatherRepository {

    override suspend fun saveWeatherHourlyData(weatherHourlyDto: WeatherHourlyDto) {
        withContext(Dispatchers.IO) {
            val weatherHourlyEntities = weatherHourlyDto.toWeatherHourlyEntity()
            hourlyDao.insertHourly(weatherHourlyEntities)
        }
    }

    override suspend fun saveWeatherDailyDate(weatherDailyDto: WeatherDailyDto) {
        withContext(Dispatchers.IO) {
            val weatherDailyEntities = weatherDailyDto.toWeatherDailyEntity()
            dailyDao.insertDaily(weatherDailyEntities)
        }
    }

    override suspend fun getAllWeatherHourlyEntities(): List<WeatherHourlyEntity> {
        return withContext(Dispatchers.IO) {
            val currentDate = LocalDate.now().toString()
            hourlyDao.getAllHourlyData(currentDate)
        }
    }

    override suspend fun getAllWeatherDailyEntities(): List<WeatherDailyEntity> {
        return withContext(Dispatchers.IO) {
           dailyDao.getAllDailyData()
        }
    }

    override suspend fun deleteOldWeatherDailyData() {
        val currentDate = LocalDate.now().toString()
        dailyDao.deleteAll(currentDate)
    }

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherDto> {
        return withContext(Dispatchers.IO) {
           try {
                Resource.Success(
                    data = WeatherApiService.retrofitService.getWeatherData
                        (lat = lat, long = long)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error.")
            }
        }
    }
}
