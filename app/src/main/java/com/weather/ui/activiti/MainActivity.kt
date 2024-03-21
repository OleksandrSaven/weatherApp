package com.weather.ui.activiti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.weathercompose.ui.theme.WeatherComposeTheme
import com.weather.ui.compose.MainScreen
import com.weather.ui.home.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity: ComponentActivity()  {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadLocation("Zhytomyr")
        setContent {
            WeatherComposeTheme {
                MainScreen()
            }
        }
    }
}



