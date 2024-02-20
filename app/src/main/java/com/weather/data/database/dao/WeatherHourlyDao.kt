package com.weather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.data.database.entity.WeatherHourlyEntity

@Dao
interface WeatherHourlyDao {
    @Query("SELECT * FROM weather_hourly WHERE time >= :currentDate")
    suspend fun getAllHourlyData(currentDate: String): List<WeatherHourlyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourly(weatherHourly: List<WeatherHourlyEntity>)
}
