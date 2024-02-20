package com.weather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.data.database.entity.WeatherDailyEntity

@Dao
interface WeatherDailyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDaily(weatherDaily: List<WeatherDailyEntity>)

    @Query("SELECT * FROM weather_daily")
    suspend fun getAllDailyData(): List<WeatherDailyEntity>

    @Query("DELETE FROM weather_daily WHERE time < :currentDate")
    suspend fun deleteAll(currentDate: String)
}
