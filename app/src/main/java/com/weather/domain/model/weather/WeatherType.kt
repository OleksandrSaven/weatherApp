package com.weather.domain.model.weather

import androidx.annotation.DrawableRes
import com.weather.R

sealed class WeatherType(
    val description: String,
    @DrawableRes
    val iconId: Int
){
   data object MainlyClear: WeatherType(
        description = "Mainly clear",
        iconId = R.drawable.ic_cloudy
    )
    data object ClearSky : WeatherType(
        description = "Clear sky",
        iconId = R.drawable.ic_sunny
    )
    data object PartlyCloudy : WeatherType(
        description =" Party Cloud",
        iconId = R.drawable.ic_cloudy
    )
    data object Overcast : WeatherType(
        description = "Overcast",
        iconId = R.drawable.ic_cloudy
    )
    data object Foggy : WeatherType(
        description = "Foggy",
        iconId = R.drawable.ic_very_cloudy
    )
    data object DepositingRimeFog : WeatherType(
        description = "Depositing rime fog",
        iconId = R.drawable.ic_very_cloudy
    )
    data object LightDrizzle : WeatherType(
        description = "Light drizzle",
        iconId = R.drawable.ic_rainshower
    )
    data object ModerateDrizzle : WeatherType(
        description = "Moderate drizzle",
        iconId = R.drawable.ic_rainshower
    )
    data object DenseDrizzle : WeatherType(
        description = "Dense drizzle",
        iconId = R.drawable.ic_rainshower
    )
    data object LightFreezingDrizzle : WeatherType(
        description = "Slight freezing drizzle",
        iconId = R.drawable.ic_snowyrainy
    )
    data object DenseFreezingDrizzle : WeatherType(
        description = "Dense freezing drizzle",
        iconId = R.drawable.ic_snowyrainy
    )
    data object SlightRain : WeatherType(
        description = "Slight rain",
        iconId = R.drawable.ic_rainy
    )
    data object ModerateRain : WeatherType(
        description = "Rainy",
        iconId = R.drawable.ic_rainy
    )
    data object HeavyRain : WeatherType(
        description = "Heavy rain",
        iconId = R.drawable.ic_rainy
    )
    data object HeavyFreezingRain: WeatherType(
        description = "Heavy freezing rain",
        iconId = R.drawable.ic_snowyrainy
    )
    data object SlightSnowFall: WeatherType(
        description = "Slight snow fall",
        iconId = R.drawable.ic_snowy
    )
    data object ModerateSnowFall: WeatherType(
        description = "Moderate snow fall",
        iconId = R.drawable.ic_heavysnow
    )
    data object HeavySnowFall: WeatherType(
        description = "Heavy snow fall",
        iconId = R.drawable.ic_heavysnow
    )
    data object SnowGrains: WeatherType(
        description = "Snow grains",
        iconId = R.drawable.ic_heavysnow
    )
    data object SlightRainShowers: WeatherType(
        description = "Slight rain showers",
        iconId = R.drawable.ic_rainshower
    )
    data object ModerateRainShowers: WeatherType(
        description = "Moderate rain showers",
        iconId = R.drawable.ic_rainshower
    )
    data object ViolentRainShowers: WeatherType(
        description = "Violent rain showers",
        iconId = R.drawable.ic_rainshower
    )
    data object SlightSnowShowers: WeatherType(
        description = "Light snow showers",
        iconId = R.drawable.ic_snowy
    )
    data object HeavySnowShowers: WeatherType(
        description = "Heavy snow showers",
        iconId = R.drawable.ic_snowy
    )
    data object ModerateThunderstorm: WeatherType(
        description = "Moderate thunderstorm",
        iconId = R.drawable.ic_thunder
    )
    data object SlightHailThunderstorm: WeatherType(
        description = "Thunderstorm with slight hail",
        iconId = R.drawable.ic_rainythunder
    )
    data object HeavyHailThunderstorm: WeatherType(
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
