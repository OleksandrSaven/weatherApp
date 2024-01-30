package com.weather

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.weather.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val REPEAT_INTERVAL = 1L

class WeatherApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            //.setRequiresCharging(true)
            .setRequiresDeviceIdle(true)
            .build()

               val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
                   REPEAT_INTERVAL, TimeUnit.DAYS)
                   .setConstraints(constraints)
                   .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    companion object {
        @SuppressWarnings("StaticFieldLeak")
        lateinit var context: Context
    }
}
