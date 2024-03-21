package com.weather.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weather.ui.compose.Home
import com.weather.ui.compose.Week
import com.weather.ui.home.WeatherViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: WeatherViewModel) {
    NavHost(
        navController = navController,
        startDestination = "home")
    {
        composable("home") {
            Home(viewModel)
        }
        composable("week") {
            Week(viewModel)
        }
    }
}