package com.weather.di

import com.weather.data.repository.PlaceRepositoryImpl
import com.weather.domain.repository.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaceModule {

    @Binds
    abstract fun providePlaceRepository(impl: PlaceRepositoryImpl): PlaceRepository
}