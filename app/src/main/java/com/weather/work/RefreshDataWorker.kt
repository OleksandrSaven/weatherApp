package com.weather.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.weather.WeatherApp.Companion.context
import com.weather.data.network.service.WeatherApiService
import com.weather.data.repository.WeatherRepository
import com.weather.ui.home.LATITUDE
import com.weather.ui.home.LONGITUDE
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val weatherRepository = WeatherRepository()
        val sharedPreferences = context.getSharedPreferences("my_preferences",
            Context.MODE_PRIVATE)
        val latitude = sharedPreferences.getFloat(LATITUDE, 0.0F).toDouble()
        val longitude = sharedPreferences.getFloat(LONGITUDE, 0.0F).toDouble()

        return  try {
            val result = WeatherApiService.retrofitService.getWeatherData(latitude, longitude)
            weatherRepository.deleteAllWeatherData(context)
            weatherRepository.saveWeatherHourlyData(result.weatherHourly)
            weatherRepository.saveWeatherDailyDate(result.weatherDaily)
            Log.i("WORKER", result.toString())
            Result.success()

        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}
