package com.weather.ui.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weather.domain.model.weather.WeatherState

@Composable
fun WeatherForecastDaily(
    state: WeatherState
) {
    state.weatherByDaily?.let { data ->
        LazyColumn(content = {
            items(data) { dailyData ->
                DailyWeatherDisplay(
                    weatherData = dailyData,
                    modifier = Modifier
                        .height(200.dp)
                        .padding(horizontal = 8.dp)
                )
            }
        })
    }
}