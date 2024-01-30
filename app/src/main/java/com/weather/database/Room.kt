package com.weather.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface WeatherHourlyDao {
    @Query("SELECT * FROM weather_hourly WHERE time >= :currentDate")
    suspend fun getAllHourlyData(currentDate: String): List<WeatherHourlyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourly(weatherHourly: List<WeatherHourlyEntity>)
}

@Dao
interface WeatherDailyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDaily(weatherDaily: List<WeatherDailyEntity>)

    @Query("SELECT * FROM weather_daily")
    suspend fun getAllDailyData(): List<WeatherDailyEntity>

    @Query("DELETE FROM weather_daily")
    suspend fun deleteAll()
}

@Database(entities = [WeatherHourlyEntity::class, WeatherDailyEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherHourlyDao(): WeatherHourlyDao
    abstract fun weatherDailyDao(): WeatherDailyDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
