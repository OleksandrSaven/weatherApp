package com.weather.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.weather.WeatherApp.Companion.context
import com.weather.data.network.service.WeatherApiService
import com.weather.data.repository.WeatherRepository
import com.weather.ui.home.LATITUDE
import com.weather.ui.home.LONGITUDE
import retrofit2.HttpException

private const val DEFAULT_VALUE = 0.0F
private const val PREF_CITY = "my_preferences"

class RefreshDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val weatherRepository = WeatherRepository()
        val sharedPreferences = context.getSharedPreferences(PREF_CITY,
            Context.MODE_PRIVATE)
        val latitude = sharedPreferences.getFloat(LATITUDE, DEFAULT_VALUE).toDouble()
        val longitude = sharedPreferences.getFloat(LONGITUDE, DEFAULT_VALUE).toDouble()

        return  try {
            val result = WeatherApiService.retrofitService.getWeatherData(latitude, longitude)
            weatherRepository.deleteOldWeatherDailyData(context)
            weatherRepository.saveWeatherHourlyData(result.weatherHourly)
            weatherRepository.saveWeatherDailyDate(result.weatherDaily)
            Result.success()

        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}
