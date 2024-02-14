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
import com.weather.domain.model.weather.WeatherState
import com.weather.domain.util.Resource
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
    private val placeRepository: PlaceRepository): ViewModel() {

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

    private var _currentValue = MutableLiveData<WeatherDataHourly?>()
    val currentHour: LiveData<WeatherDataHourly?>
        get() = _currentValue


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
                    _weatherState.value?.errorMessage  = result.message
                }
            }
            updateLoadingStatus(false)
        }
    }

    fun clearErrorMessage() {
        _weatherState.value?.errorMessage  = null
    }
    
    private fun loadWeatherInfo(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            when(val result = weatherRepository.getWeatherData(latitude, longitude)) {
                is Resource.Success -> {
                    val weather = result.data
                    if(weather != null) {
                        updateWeatherByHourly(weather.toWeatherData().weatherDataPerDay[CURRENT_DAY]!!)
                        _currentValue.value = weather.toWeatherData().currentWeatherData
                        _weatherByDaily.value = weather.toWeatherData().weatherDatePerWeek.values.flatten()
                        saveWeatherToDatabase(weather)
                    }
                }
                is Resource.Error -> {
                    _weatherState.value?.errorMessage = result.message
                }
            }
        }
    }

    private fun loadCacheWeather() {
        viewModelScope.launch {
            updateLoadingStatus(true)
            //_weatherState.value?.loadingState = true
            //_loadingState.value = true
            val allWeatherDailyEntities = weatherRepository.getAllWeatherDailyEntities()


            _weatherByDaily.value = allWeatherDailyEntities.map {
                    weatherDailyEntity -> weatherDailyEntity.toDomainModel()
            }

            //_weatherState.value?.city = sharedPreferences.getString(CITY_NAME, DEFAULT_CITY).toString()
            //_city.value = sharedPreferences.getString(CITY_NAME, DEFAULT_CITY)
            sharedPreferences.getString(CITY_NAME, DEFAULT_CITY)?.let { updateCity(it) }

            val weatherHourlyDto = weatherRepository.getAllWeatherHourlyEntities().toWeatherHourlyDto()

            updateWeatherByHourly(weatherHourlyDto.toWeatherHourly()[CURRENT_DAY]!!)
            //_hourly.value = weatherHourlyDto.toWeatherHourly()[CURRENT_DAY]

            _currentValue.value = weatherHourlyDto.toWeatherHourly()[CURRENT_DAY]?.find {
                val now = LocalDateTime.now()
                val hour = when {
                    now.minute < 30 -> now.hour
                    now.hour == 23 -> 23
                    else -> now.hour + 1
                }
                it.time.hour == hour
            }
            updateLoadingStatus(false)
           // _weatherState.value?.loadingState = false
           // _loadingState.value = false
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
        //_weatherState.value?.city = place.name
        //_city.value = place.name
        updateCity(place.name)
        sharedPreferences.edit().putString(CITY_NAME, place.name).apply()
        sharedPreferences.edit().putFloat(LATITUDE, latitude.toFloat()).apply()
        sharedPreferences.edit().putFloat(LONGITUDE, longitude.toFloat()).apply()
    }

    private fun updateCity(city: String) {
        val currentData = _weatherState.value ?:
        WeatherState(false, "", null, emptyList())
        _weatherState.value = currentData.copy(city = city)
    }

    private fun updateLoadingStatus(loadingStatus: Boolean) {
        val currentData = _weatherState.value ?:
        WeatherState(false, "", null, emptyList())
        _weatherState.value = currentData.copy(loadingState = loadingStatus)
    }

    private fun updateWeatherByHourly(hourly: List<WeatherDataHourly>) {
        val currentData = _weatherState.value ?:
        WeatherState(false, "", null, emptyList())
        _weatherState.value = currentData.copy(weatherByHourly = hourly)
    }
}
