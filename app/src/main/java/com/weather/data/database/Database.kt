package com.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.data.database.dao.WeatherDailyDao
import com.weather.data.database.dao.WeatherHourlyDao
import com.weather.data.database.entity.WeatherDailyEntity
import com.weather.data.database.entity.WeatherHourlyEntity

@Database(entities = [WeatherHourlyEntity::class, WeatherDailyEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherHourlyDao(): WeatherHourlyDao
    abstract fun weatherDailyDao(): WeatherDailyDao
}
