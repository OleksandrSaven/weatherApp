package com.weather.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.weather.ui.home.WeatherViewModel
import kotlinx.coroutines.delay

@Composable
fun Home(viewModel: WeatherViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(onSearch = { city -> viewModel.loadLocation(city) })
            WeatherCard(state = viewModel.state)
            WeatherForecastHourly(state = viewModel.state)
        }
        if(viewModel.state.loadingState) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        LaunchedEffect(viewModel.state.errorMessage) {
            viewModel.state.errorMessage?.let {
                delay(2000)
                viewModel.clearErrorMessage()
            }
        }
        viewModel.state.errorMessage?.let { error ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(
                        Alignment.Center
                    )
            ) {
                Text(
                    text = error,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun Week(viewModel: WeatherViewModel){
    WeatherForecastDaily(viewModel.state)
}
