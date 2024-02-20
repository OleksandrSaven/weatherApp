package com.weather.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.weather.data.constants.AppConstants
import com.weather.data.network.weaher.WeatherApi
import com.weather.util.WeatherRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkWeatherModule {

    @Provides
    @Singleton
    @WeatherRetrofit
    fun provideWeatherRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(AppConstants.BASE_URL_WEATHER)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(@WeatherRetrofit retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}
