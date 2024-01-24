package com.weather.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.data.mapper.toWeatherInfo
import com.weather.data.network.service.WeatherApiService
import com.weather.domain.model.weather.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private var _weatherData = MutableLiveData<WeatherData?>()
    val weatherData: LiveData<WeatherData?>
        get() = _weatherData

    private var _weather = MutableLiveData<List<WeatherData>>()
    val weather: LiveData<List<WeatherData>>
        get() = _weather

    init {
        loadWeatherInfo()
        Log.i("LoadWeatherInfo", _weather.value.toString())
    }

    private  fun loadWeatherInfo() {
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                WeatherApiService.retrofitService.getWeatherData(50.265, 28.6767)
            }
            try {
                _weather.value = result.toWeatherInfo().weatherDataPerDay[0]
                _weatherData.value = result.toWeatherInfo().currentWeatherData
                Log.i("WeatherCoroutine", _weather.value.toString())
            } catch (e :  Exception){
                _weather.value = ArrayList()
            }
        }
    }
}