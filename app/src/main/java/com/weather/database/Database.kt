package com.weather.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherHourlyEntity::class, WeatherDailyEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherHourlyDao(): WeatherHourlyDao
    abstract fun weatherDailyDao(): WeatherDailyDao
}
