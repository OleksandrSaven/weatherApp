package com.weather.ui.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.WeatherApp
import com.weather.WeatherApp.Companion.context
import com.weather.data.mapper.toWeatherData
import com.weather.data.mapper.toWeatherHourly
import com.weather.data.mapper.toWeatherHourlyDto
import com.weather.data.network.service.WeatherApiService
import com.weather.data.repository.PlaceRepository
import com.weather.data.repository.WeatherRepository
import com.weather.db.WeatherDatabase
import com.weather.domain.model.weather.WeatherDataDaily
import com.weather.domain.model.weather.WeatherDataHourly
import com.weather.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime


class HomeViewModel: ViewModel() {
    private val placeRepository = PlaceRepository()
    private val sharedPreferences = context.getSharedPreferences("my_preferences",
        Context.MODE_PRIVATE)

    private val database = WeatherDatabase.getInstance(WeatherApp.context)
    private val hourlyDao = database.weatherHourlyDao()
    private val repository = WeatherRepository(hourlyDao)

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    private var _city = MutableLiveData<String>()
    val city: LiveData<String>
        get() = _city

    private var _daily = MutableLiveData<List<WeatherDataDaily>>()
    val days: LiveData<List<WeatherDataDaily>>
        get() = _daily

    private var _currentValue = MutableLiveData<WeatherDataHourly?>()
    val currentHour: LiveData<WeatherDataHourly?>
        get() = _currentValue

    private var _hourly = MutableLiveData<List<WeatherDataHourly>>()
    val weatherByHourly: LiveData<List<WeatherDataHourly>>
        get() = _hourly

    init {
        loadCacheWeather()
    }

    fun loadLocation(city: String) {
        viewModelScope.launch {
            _loadingState.value = true
            when(val result = placeRepository.getPlace(city)) {
                is Resource.Success -> {
                    val place = result.data
                    if (place != null) {
                        latitude = place.location.lat
                        longitude = place.location.long
                        _city.value = place.name
                        sharedPreferences.edit().putString("city_name", place.name).apply()
                        loadWeatherInfo(latitude, longitude)
                    }
                }
                is Resource.Error -> {
                    val errorMessage = result.message

                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            _loadingState.value = false
        }
    }

    private fun loadWeatherInfo(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                WeatherApiService.retrofitService.getWeatherData(latitude, longitude)
            }
            try {
                repository.saveWeatherHourlyData(result.weatherHourly, hourlyDao)
                _hourly.value = result.toWeatherData().weatherDataPerDay[0]
                _daily.value = result.toWeatherData().weatherDatePerWeek.values.flatten()
                _currentValue.value = result.toWeatherData().currentWeatherData
            } catch (e : Exception){
                _hourly.value = ArrayList()
                _daily.value = ArrayList()
            }
        }
    }


    private fun loadCacheWeather() {
        viewModelScope.launch {
            val allWeatherHourlyEntities = repository
                .getAllWeatherHourlyEntities(context).toWeatherHourlyDto()

            _hourly.value = allWeatherHourlyEntities.toWeatherHourly().values.flatten()
            val now = LocalDateTime.now()
            _currentValue.value = allWeatherHourlyEntities.toWeatherHourly()[0]?.find {
                val hour = when {
                    now.minute < 30 -> now.hour
                    now.hour == 23 -> 23
                    else -> now.hour + 1
                }
                it.time.hour == hour
            }
            _city.value = sharedPreferences.getString("city_name", "An know")
        }
    }
}
