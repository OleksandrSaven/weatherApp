package com.weather.util

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PlaceRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WeatherRetrofit