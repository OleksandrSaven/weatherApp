package com.weather.di

import com.weather.data.repository.PlaceRepository
import com.weather.data.repository.PlaceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceModule {

    @Provides
    @Singleton
    fun providePlaceRepository(): PlaceRepository {
        return PlaceRepositoryImpl()
    }
}