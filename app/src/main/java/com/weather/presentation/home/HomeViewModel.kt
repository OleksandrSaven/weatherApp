package com.weather.presentation.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.data.mapper.toWeatherData
import com.weather.data.network.service.PlaceApiService
import com.weather.data.network.service.WeatherApiService
import com.weather.domain.model.weather.WeatherDataDaily
import com.weather.domain.model.weather.WeatherDataHourly
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel() : ViewModel() {

     var latitude: Double = 50.45466

     var longitude: Double = 30.5238


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
        loadLocation("Lviv")

        Log.i("LoadWeatherInfo", _hourly.value.toString())
        Log.i("loadWeather", latitude.toString())
        loadWeatherInfo(latitude, longitude)
    }

    fun loadLocation(city: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                PlaceApiService.retrofitService.getLocation(city)
            }
            try {
                latitude = result.placeData[0].latitude
                longitude = result.placeData[0].longitude
                Log.i("In", longitude.toString())
                Log.i("In", latitude.toString())
                loadWeatherInfo(latitude, longitude)
            } catch (e: Exception) {

            }
        }
    }

    fun loadWeatherInfo(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                WeatherApiService.retrofitService.getWeatherData(latitude, longitude)
            }
            try {
                _hourly.value = result.toWeatherData().weatherDataPerDay[0]
                _daily.value = result.toWeatherData().weatherDatePerWeek.values.flatten()
                _currentValue.value = result.toWeatherData().currentWeatherData
            } catch (e :  Exception){
                _hourly.value = ArrayList()
                _daily.value = ArrayList()
            }
        }
    }
}
