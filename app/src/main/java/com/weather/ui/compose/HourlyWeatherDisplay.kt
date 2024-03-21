package com.weather.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.weather.domain.model.weather.WeatherDataHourly
import java.time.format.DateTimeFormatter

@Composable
fun HourlyWeatherDisplay(
    weatherDate: WeatherDataHourly,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
){
    val formattedTime = remember(weatherDate) {
        weatherDate.time.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
        ) {
        Text(
            text = formattedTime,
            color = textColor
        )
        Image(
            painter = painterResource(id = weatherDate.weatherType.iconId),
            contentDescription = null,
            modifier = Modifier.width(40.dp))
        Text(
            text = "${weatherDate.temperature}°C",
            color = textColor
        )
    }
}
