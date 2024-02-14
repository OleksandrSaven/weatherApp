package com.weather.ui.home

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.WeatherApp.Companion.context
import com.weather.data.mapper.toWeatherData
import com.weather.data.mapper.toWeatherHourly
import com.weather.data.mapper.toWeatherHourlyDto
import com.weather.data.network.weaher.WeatherDto
import com.weather.database.toDomainModel
import com.weather.domain.model.place.Place
import com.weather.domain.model.weather.WeatherDataDaily
import com.weather.domain.model.weather.WeatherDataHourly
import com.weather.domain.model.weather.WeatherState
import com.weather.domain.repository.PlaceRepository
import com.weather.domain.repository.WeatherRepository
import com.weather.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

private const val CITY_NAME = "city_name"
private const val DEFAULT_CITY = "Kyiv"
private const val CURRENT_DAY = 0
private const val INIT_VALUE = 0.0
private const val OFFLINE_MESSAGE = "Please, check connection you are offline."
const val LATITUDE = "latitude"
const val LONGITUDE = "longitude"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val placeRepository: PlaceRepository
): ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("my_preferences",
        Context.MODE_PRIVATE)

    private var latitude: Double = INIT_VALUE
    private var longitude: Double = INIT_VALUE

    private val _weatherState = MutableLiveData<WeatherState>()
    val weatherState : LiveData<WeatherState>
        get() = _weatherState

    private var _weatherByDaily = MutableLiveData<List<WeatherDataDaily>>()
    val weatherByDaily: LiveData<List<WeatherDataDaily>>
        get() = _weatherByDaily


    init {
        if (!isOnline()) {
            loadCacheWeather()
            _weatherState.value?.errorMessage = OFFLINE_MESSAGE
        } else {
            sharedPreferences.getString(CITY_NAME, DEFAULT_CITY).let {
                if (it != null) {
                    loadLocation(it)
                }
            }
        }
    }

    fun loadLocation(city: String) {
        viewModelScope.launch {
            updateLoadingStatus(true)
            when(val result = placeRepository.getPlace(city)) {
                is Resource.Success -> {
                    val place = result.data
                    if (place != null) {
                        setupData(place)
                        loadWeatherInfo(latitude, longitude)
                    }
                }
                is Resource.Error -> {
                    result.message?.let { updateErrorMessage(it) }
                }
            }
            updateLoadingStatus(false)
        }
    }

    fun clearErrorMessage() {
        updateErrorMessage(null)
    }
    
    private fun loadWeatherInfo(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            when(val result = weatherRepository.getWeatherData(latitude, longitude)) {
                is Resource.Success -> {
                    val weather = result.data
                    if(weather != null) {
                        updateWeatherByHourly(weather.toWeatherData().weatherDataPerDay[CURRENT_DAY]!!)
                        updateWeatherCurrentValue(weather.toWeatherData().currentWeatherData!!)
                        _weatherByDaily.value = weather.toWeatherData().weatherDatePerWeek.values.flatten()
                        saveWeatherToDatabase(weather)
                    }
                }
                is Resource.Error -> {
                    updateErrorMessage(result.message)
                }
            }
        }
    }

    private fun loadCacheWeather() {
        viewModelScope.launch {
            updateLoadingStatus(true)
            val allWeatherDailyEntities = weatherRepository.getAllWeatherDailyEntities()
            _weatherByDaily.value = allWeatherDailyEntities.map {
                    weatherDailyEntity -> weatherDailyEntity.toDomainModel()
            }
            sharedPreferences.getString(CITY_NAME, DEFAULT_CITY)?.let { updateCity(it) }
            val weatherHourlyDto = weatherRepository.getAllWeatherHourlyEntities().toWeatherHourlyDto()
            weatherHourlyDto.toWeatherHourly()[CURRENT_DAY]?.let { updateWeatherByHourly(it) }
            weatherHourlyDto.toWeatherHourly()[CURRENT_DAY]?.find {
                val now = LocalDateTime.now()
                val hour = when {
                    now.minute < 30 -> now.hour
                    now.hour == 23 -> 23
                    else -> now.hour + 1
                }
                it.time.hour == hour
            }?.let { updateWeatherCurrentValue(it) }
            updateLoadingStatus(false)
        }
    }

    private suspend fun saveWeatherToDatabase(weatherDto: WeatherDto) {
        weatherRepository.saveWeatherHourlyData(weatherDto.weatherHourly)
        weatherRepository.saveWeatherDailyDate(weatherDto.weatherDaily)
    }

    private fun isOnline(): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
    
    private fun setupData(place: Place) {
        latitude = place.location.lat
        longitude = place.location.long
        updateCity(place.name)
        sharedPreferences.edit().putString(CITY_NAME, place.name).apply()
        sharedPreferences.edit().putFloat(LATITUDE, latitude.toFloat()).apply()
        sharedPreferences.edit().putFloat(LONGITUDE, longitude.toFloat()).apply()
    }

    private fun updateCity(city: String) {
        val currentData = _weatherState.value ?:
        WeatherState(false, "", null, emptyList(), null)
        _weatherState.value = currentData.copy(city = city)
    }

    private fun updateLoadingStatus(loadingStatus: Boolean) {
        val currentData = _weatherState.value ?:
        WeatherState(false, "", null, emptyList(), null)
        _weatherState.value = currentData.copy(loadingState = loadingStatus)
    }

    private fun updateErrorMessage(errorMessage: String?) {
        val currentData = _weatherState.value ?:
        WeatherState(false, "", null, emptyList(), null)
        _weatherState.value = currentData.copy(errorMessage = errorMessage)
    }

    private fun updateWeatherByHourly(hourly: List<WeatherDataHourly>) {
        val currentData = _weatherState.value ?:
        WeatherState(false, "", null, emptyList(), null)
        _weatherState.value = currentData.copy(weatherByHourly = hourly)
    }

    private fun updateWeatherCurrentValue(currentValue: WeatherDataHourly) {
        val currentData = _weatherState.value ?:
        WeatherState(false, "", null, emptyList(), null)
        _weatherState.value = currentData.copy(currentValue = currentValue)
    }
}
