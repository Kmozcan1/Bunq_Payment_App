package com.kmozcan1.bunqpaymentapp.application.di

import com.kmozcan1.bunqpaymentapp.data.datasource.SharedPreferencesDataSource
import com.kmozcan1.bunqpaymentapp.data.repository.BunqApiRepositoryImpl
import com.kmozcan1.bunqpaymentapp.domain.repository.BunqApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideBunqApiRepository(sharedPreferencesDataSource: SharedPreferencesDataSource) :
            BunqApiRepository = BunqApiRepositoryImpl(sharedPreferencesDataSource)
}