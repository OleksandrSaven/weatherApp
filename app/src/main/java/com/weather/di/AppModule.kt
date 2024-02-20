package com.weather.di

import android.content.Context
import androidx.room.Room
import com.weather.data.database.WeatherDatabase
import com.weather.data.database.dao.WeatherDailyDao
import com.weather.data.database.dao.WeatherHourlyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(context,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDailyDao(database: WeatherDatabase): WeatherDailyDao {
        return database.weatherDailyDao();
    }

    @Provides
    @Singleton
    fun provideHourlyDao(database: WeatherDatabase): WeatherHourlyDao {
        return database.weatherHourlyDao();
    }
}
