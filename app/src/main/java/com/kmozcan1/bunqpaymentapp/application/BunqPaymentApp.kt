package com.kmozcan1.bunqpaymentapp.application

import android.app.Application
import com.kmozcan1.bunqpaymentapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
@HiltAndroidApp
class BunqPaymentApp : Application() {
    var isApiContextInitialized = false

    override fun onCreate() {
        super.onCreate()
        // init timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}