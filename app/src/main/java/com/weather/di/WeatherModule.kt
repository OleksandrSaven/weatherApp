package com.weather.di

import com.weather.data.database.dao.WeatherDailyDao
import com.weather.data.database.dao.WeatherHourlyDao
import com.weather.data.network.weaher.WeatherApi
import com.weather.data.repository.WeatherRepositoryImpl
import com.weather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object WeatherModule {

    @Provides
    @Singleton
    fun provideWeatherRepositoryImpl(hourlyDao: WeatherHourlyDao,
                                     dailyDao: WeatherDailyDao,
                                     weatherApi: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(hourlyDao, dailyDao, weatherApi)
    }
}