package com.weather.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.R
import com.weather.domain.model.weather.WeatherState
import java.time.format.DateTimeFormatter

@Composable
fun WeatherCard(
    state: WeatherState,
    modifier: Modifier = Modifier
    ) {
    state.currentValue?.let { data ->
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = modifier.padding(16.dp),
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Row (modifier = Modifier.fillMaxWidth()){
                    state.city?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text(
                        text = "Today ${data.time.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = state.currentValue.weatherType.iconId),
                    contentDescription = null,
                    modifier = Modifier.height(150.dp)
                    )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${data.temperature}°C",
                    fontSize = 50.sp,
                    color = Color.White
                    )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = data.weatherType.description,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    WeatherDataDisplay(
                        value = data.pressure,
                        unit = "hpa" ,
                        icon = ImageVector.vectorResource(id = R.drawable.ic_pressure),
                        modifier = Modifier,
                        iconTint = Color.White
                    )
                    WeatherDataDisplay(
                        value = data.humidity,
                        unit = "%" ,
                        icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
                        modifier = Modifier,
                        iconTint = Color.White
                    )
                    WeatherDataDisplay(
                        value = data.windSpeed,
                        unit = "km/h" ,
                        icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
                        modifier = Modifier,
                        iconTint = Color.White
                    )
                }
            }
        }
    }
}
