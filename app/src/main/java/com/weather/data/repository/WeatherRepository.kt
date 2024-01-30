package com.weather.data.repository


import android.content.Context
import com.weather.WeatherApp
import com.weather.data.mapper.toWeatherDailyEntity
import com.weather.data.mapper.toWeatherData
import com.weather.data.mapper.toWeatherHourlyEntity
import com.weather.data.network.service.WeatherApiService
import com.weather.data.network.weaher.WeatherDailyDto
import com.weather.data.network.weaher.WeatherHourlyDto
import com.weather.database.WeatherDailyEntity
import com.weather.database.WeatherDatabase
import com.weather.database.WeatherHourlyEntity
import com.weather.domain.model.weather.WeatherInfo
import com.weather.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class WeatherRepository() {
    private val database = WeatherDatabase

    suspend fun saveWeatherHourlyData(weatherHourlyDto: WeatherHourlyDto) {
        withContext(Dispatchers.IO) {
            val weatherHourlyEntities = weatherHourlyDto.toWeatherHourlyEntity()
            database.getInstance(WeatherApp.context)
                .weatherHourlyDao().insertHourly(weatherHourlyEntities)
        }
    }

    suspend fun saveWeatherDailyDate(weatherDailyDto: WeatherDailyDto) {
        withContext(Dispatchers.IO) {
            val weatherDailyEntities = weatherDailyDto.toWeatherDailyEntity()
            database.getInstance(WeatherApp.context)
                .weatherDailyDao().insertDaily(weatherDailyEntities)
        }
    }

    suspend fun getAllWeatherHourlyEntities(context: Context): List<WeatherHourlyEntity> {
        return withContext(Dispatchers.IO) {
            val currentDate = LocalDate.now().toString()
            database.getInstance(context).weatherHourlyDao().getAllHourlyData(currentDate)
        }
    }

    suspend fun getAllWeatherDailyEntities(context: Context): List<WeatherDailyEntity> {
        return withContext(Dispatchers.IO) {
           database.getInstance(context).weatherDailyDao().getAllDailyData()
        }
    }

    suspend fun deleteOldWeatherDailyData(context: Context) {
        val currentDate = LocalDate.now().toString()
        database.getInstance(context).weatherDailyDao().deleteAll(currentDate)
    }

    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
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
