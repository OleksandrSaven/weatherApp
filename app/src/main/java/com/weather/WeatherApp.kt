package com.weather

import android.app.Application
import android.content.Context

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressWarnings("StaticFieldLeak")
        lateinit var context: Context
    }
}