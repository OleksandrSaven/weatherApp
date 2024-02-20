package com.weather.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.weather.data.constants.AppConstants
import com.weather.data.network.place.PlaceApi
import com.weather.util.PlaceRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkPlaceModule {

    @Provides
    @Singleton
    @PlaceRetrofit
    fun providePlaceRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_PLACE)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiPlaceService(@PlaceRetrofit retrofit: Retrofit): PlaceApi {
        return retrofit.create(PlaceApi::class.java)
    }
}
