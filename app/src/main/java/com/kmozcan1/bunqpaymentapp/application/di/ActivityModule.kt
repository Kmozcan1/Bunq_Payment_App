package com.kmozcan1.bunqpaymentapp.application.di

import android.app.Activity
import com.kmozcan1.bunqpaymentapp.presentation.view.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun providesMainActivity(
        activity: Activity
    ): MainActivity {
        return activity as MainActivity
    }
}