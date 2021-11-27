package com.kmozcan1.bunqpaymentapp.application.di

import javax.inject.Qualifier

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 * Module for Dispatcher qualifiers
 * https://github.com/google/iosched/blob/main/shared/src/main/java/com/google/samples/apps/iosched/shared/di/CoroutinesQualifiers.kt
 */



@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationScope