package com.weather.ui.compose.navigation

import com.weather.R

sealed class BottomItem(val title: String, val icon: Int, val route: String){
    data object Home: BottomItem("Home", R.drawable.baseline_home_24, route = "home")
    data object Week: BottomItem("Week", R.drawable.baseline_calendar_month_24, "week")
}
