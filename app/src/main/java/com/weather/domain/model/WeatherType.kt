package com.weather.domain.model

import androidx.annotation.DrawableRes
import com.weather.R

sealed class WeatherType(
    val description: String,
    @DrawableRes
    val iconId: Int
){
    object MainlyClear: WeatherType(
        description = "Mainly clear",
        iconId = R.drawable.ic_cloudy
    )
    object ClearSky : WeatherType(
        description = "Clear sky",
        iconId = R.drawable.ic_sunny
    )
    object PartlyCloudy : WeatherType(
        description =" Party Cloud",
        iconId = R.drawable.ic_cloudy
    )
    object Overcast : WeatherType(
        description = "Overcast",
        iconId = R.drawable.ic_cloudy
    )
    object Foggy : WeatherType(
        description = "Foggy",
        iconId = R.drawable.ic_very_cloudy
    )
    object DepositingRimeFog : WeatherType(
        description = "Depositing rime fog",
        iconId = R.drawable.ic_very_cloudy
    )
    object LightDrizzle : WeatherType(
        description = "Light drizzle",
        iconId = R.drawable.ic_rainshower
    )
    object ModerateDrizzle : WeatherType(
        description = "Moderate drizzle",
        iconId = R.drawable.ic_rainshower
    )
    object DenseDrizzle : WeatherType(
        description = "Dense drizzle",
        iconId = R.drawable.ic_rainshower
    )
    object LightFreezingDrizzle : WeatherType(
        description = "Slight freezing drizzle",
        iconId = R.drawable.ic_snowyrainy
    )
    object DenseFreezingDrizzle : WeatherType(
        description = "Dense freezing drizzle",
        iconId = R.drawable.ic_snowyrainy
    )
    object SlightRain : WeatherType(
        description = "Slight rain",
        iconId = R.drawable.ic_rainy
    )
    object ModerateRain : WeatherType(
        description = "Rainy",
        iconId = R.drawable.ic_rainy
    )
    object HeavyRain : WeatherType(
        description = "Heavy rain",
        iconId = R.drawable.ic_rainy
    )
    object HeavyFreezingRain: WeatherType(
        description = "Heavy freezing rain",
        iconId = R.drawable.ic_snowyrainy
    )
    object SlightSnowFall: WeatherType(
        description = "Slight snow fall",
        iconId = R.drawable.ic_snowy
    )
    object ModerateSnowFall: WeatherType(
        description = "Moderate snow fall",
        iconId = R.drawable.ic_heavysnow
    )
    object HeavySnowFall: WeatherType(
        description = "Heavy snow fall",
        iconId = R.drawable.ic_heavysnow
    )
    object SnowGrains: WeatherType(
        description = "Snow grains",
        iconId = R.drawable.ic_heavysnow
    )
    object SlightRainShowers: WeatherType(
        description = "Slight rain showers",
        iconId = R.drawable.ic_rainshower
    )
    object ModerateRainShowers: WeatherType(
        description = "Moderate rain showers",
        iconId = R.drawable.ic_rainshower
    )
    object ViolentRainShowers: WeatherType(
        description = "Violent rain showers",
        iconId = R.drawable.ic_rainshower
    )
    object SlightSnowShowers: WeatherType(
        description = "Light snow showers",
        iconId = R.drawable.ic_snowy
    )
    object HeavySnowShowers: WeatherType(
        description = "Heavy snow showers",
        iconId = R.drawable.ic_snowy
    )
    object ModerateThunderstorm: WeatherType(
        description = "Moderate thunderstorm",
        iconId = R.drawable.ic_thunder
    )
    object SlightHailThunderstorm: WeatherType(
        description = "Thunderstorm with slight hail",
        iconId = R.drawable.ic_rainythunder
    )
    object HeavyHailThunderstorm: WeatherType(
        description = "Thunderstorm with heavy hail",
        iconId = R.drawable.ic_rainythunder
    )

    companion object {
        fun fromWMO(code: Int): WeatherType {
            return when(code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
        }
    }
}
