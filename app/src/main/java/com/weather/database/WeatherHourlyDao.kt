package com.weather.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherHourlyDao {
    @Query("SELECT * FROM weather_hourly WHERE time >= :currentDate")
    suspend fun getAllHourlyData(currentDate: String): List<WeatherHourlyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourly(weatherHourly: List<WeatherHourlyEntity>)
}
