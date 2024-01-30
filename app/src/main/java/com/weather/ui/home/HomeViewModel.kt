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
import com.weather.data.repository.PlaceRepository
import com.weather.data.repository.WeatherRepository
import com.weather.database.toDomainModel
import com.weather.domain.model.place.Place
import com.weather.domain.model.weather.WeatherDataDaily
import com.weather.domain.model.weather.WeatherDataHourly
import com.weather.domain.util.Resource
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val CITY_NAME = "city_name"
private const val DEFAULT_CITY = "Kyiv"
private const val CURRENT_DAY = 0
private const val INIT_VALUE = 0.0
private const val OFFLINE_MESSAGE = "Please, check connection you are offline."
const val LATITUDE = "latitude"
const val LONGITUDE = "longitude"


class HomeViewModel: ViewModel() {
    private val placeRepository = PlaceRepository()
    private val weatherRepository = WeatherRepository()
    private val sharedPreferences = context.getSharedPreferences("my_preferences",
        Context.MODE_PRIVATE)

    private var latitude: Double = INIT_VALUE
    private var longitude: Double = INIT_VALUE

    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    private var _city = MutableLiveData<String>()
    val city: LiveData<String>
        get() = _city

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var _weatherByDaily = MutableLiveData<List<WeatherDataDaily>>()
    val weatherByDaily: LiveData<List<WeatherDataDaily>>
        get() = _weatherByDaily

    private var _currentValue = MutableLiveData<WeatherDataHourly?>()
    val currentHour: LiveData<WeatherDataHourly?>
        get() = _currentValue

    private var _hourly = MutableLiveData<List<WeatherDataHourly>>()
    val weatherByHourly: LiveData<List<WeatherDataHourly>>
        get() = _hourly

    init {
        if (!isOnline()) {
            loadCacheWeather()
            _errorMessage.value = OFFLINE_MESSAGE
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
            _loadingState.value = true
            when(val result = placeRepository.getPlace(city)) {
                is Resource.Success -> {
                    val place = result.data
                    if (place != null) {
                        setupData(place)
                        loadWeatherInfo(latitude, longitude)
                    }
                }
                is Resource.Error -> {
                     _errorMessage.value = result.message
                }
            }
            _loadingState.value = false
        }
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
    
    private fun loadWeatherInfo(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            when(val result = weatherRepository.getWeatherData(latitude, longitude)) {
                is Resource.Success -> {
                    val weather = result.data
                    if(weather != null) {
                        _hourly.value = weather.toWeatherData().weatherDataPerDay[CURRENT_DAY]
                        _currentValue.value = weather.toWeatherData().currentWeatherData
                        _weatherByDaily.value = weather.toWeatherData().weatherDatePerWeek.values.flatten()
                        saveWeatherToDatabase(weather)
                    }
                }
                is Resource.Error -> {
                    _errorMessage.value = result.message
                }
            }
        }
    }

    private fun loadCacheWeather() {
        viewModelScope.launch {
            _loadingState.value = true
            val allWeatherDailyEntities = weatherRepository.getAllWeatherDailyEntities(context)
            _weatherByDaily.value = allWeatherDailyEntities.toDomainModel()
            _city.value = sharedPreferences.getString(CITY_NAME, DEFAULT_CITY)

            val weatherHourlyDto = weatherRepository.getAllWeatherHourlyEntities(context).toWeatherHourlyDto()
            _hourly.value = weatherHourlyDto.toWeatherHourly()[CURRENT_DAY]

            _currentValue.value = weatherHourlyDto.toWeatherHourly()[CURRENT_DAY]?.find {
                val now = LocalDateTime.now()
                val hour = when {
                    now.minute < 30 -> now.hour
                    now.hour == 23 -> 23
                    else -> now.hour + 1
                }
                it.time.hour == hour
            }
            _loadingState.value = false
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
        _city.value = place.name
        sharedPreferences.edit().putString(CITY_NAME, place.name).apply()
        sharedPreferences.edit().putFloat(LATITUDE, latitude.toFloat()).apply()
        sharedPreferences.edit().putFloat(LONGITUDE, longitude.toFloat()).apply()
    }
}
