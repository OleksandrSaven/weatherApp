package com.weather.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.weather.domain.model.weather.WeatherDataDaily
import java.time.format.DateTimeFormatter

@Composable
fun DailyWeatherDisplay(
    weatherData: WeatherDataDaily,
    modifier: Modifier = Modifier,
) {
    val formattedData = remember(weatherData) {
        weatherData.dayOfWeek.format(DateTimeFormatter.ofPattern("dd.MM")) + " " +
        weatherData.dayOfWeek.dayOfWeek
    }
    Row {
       Image(
            painter = painterResource(id = weatherData.weatherType.iconId),
            contentDescription = null,
            modifier = Modifier
                .width(125.dp)
                .padding(16.dp))
        Column {
            Text(
                text = formattedData,
                color = Color.White
            )
            Text(
                text = "Min temperature ${weatherData.minTemperature}°C",
                color = Color.White
            )
            Text(
                text = "Max temperature ${weatherData.maxTemperature}°C",
                color = Color.White
            )
        }
    }
}