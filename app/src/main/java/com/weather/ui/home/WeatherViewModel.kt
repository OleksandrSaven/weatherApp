package com.weather.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.data.mapper.toWeatherDaily
import com.weather.data.mapper.toWeatherHourly
import com.weather.data.network.weaher.WeatherDto
import com.weather.domain.model.weather.WeatherState
import com.weather.domain.repository.PlaceRepository
import com.weather.domain.repository.WeatherRepository
import com.weather.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val placeRepository: PlaceRepository
): ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadLocation(city: String) {
        viewModelScope.launch {
            state = state.copy(
                loadingState = true,
                errorMessage = null
            )
            when(val result = placeRepository.getPlace(city)) {
                is Resource.Success -> {
                    when (val res = result.data?.location?.let {
                            repository.getWeatherData(it.lat, result.data.location.long) }) {
                        is Resource.Success -> {
                            state = state.copy(
                                loadingState = false,
                                city = city,

                                currentValue = res.data?.weatherHourly?.toWeatherHourly()?.get(0)?.find {
                                    val now = LocalDateTime.now()
                                    val hour = when {
                                        now.minute < 30 -> now.hour
                                        now.hour == 23 -> 23
                                        else -> now.hour + 1
                                    }
                                    it.time.hour == hour
                                },
                                weatherByHourly = res.data?.weatherHourly?.toWeatherHourly()?.get(0),
                                weatherByDaily = res.data?.weatherDaily?.toWeatherDaily()?.values?.flatten()

                            )
                            res.data?.let { data ->
                                saveWeatherToDatabase(data) }
                        }
                        is Resource.Error -> {
                            error("Unknown error!")
                        }
                        else -> {
                            error("Some gone wrong")
                        }
                    }
                }
                is Resource.Error -> {
                    state = state.copy(
                        loadingState = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun clearErrorMessage() {
        state = state.copy(
            errorMessage = null
        )
    }
    private suspend fun saveWeatherToDatabase(weatherDto: WeatherDto) {
        repository.saveWeatherHourlyData(weatherDto.weatherHourly)
        repository.saveWeatherDailyDate(weatherDto.weatherDaily)
    }
}
