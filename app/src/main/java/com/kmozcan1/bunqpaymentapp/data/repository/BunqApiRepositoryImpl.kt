package com.kmozcan1.bunqpaymentapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.bunq.sdk.context.ApiContext
import com.bunq.sdk.context.ApiEnvironmentType
import com.kmozcan1.bunqpaymentapp.data.datasource.SharedPreferencesDataSource
import com.kmozcan1.bunqpaymentapp.domain.repository.BunqApiRepository
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 *
 * Repository class that can be by the Use Case Interactors to call BunqApi related methods
 */
class BunqApiRepositoryImpl @Inject constructor(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : BunqApiRepository {
    companion object {
        val ENVIRONMENT_TYPE = ApiEnvironmentType.SANDBOX
        const val API_KEY = "sandbox_9b69210af1cff15b46745b2f503961f905de619b51d8896668cab13b"
        const val DEVICE_DESCRIPTION = "android"
    }

    /** Retrieves the ApiContext from SharedPrefs if it has been initialized before,
     * otherwise creates the ApiContext and save it to the SharedPrefs */
    override fun getBunqApiContext(): ApiContext {
        val apiContextPref = sharedPreferencesDataSource.getApiContextPref()

        return if (apiContextPref != null) {
            // return the existing ApiContext from shared prefs
            ApiContext.fromJson(sharedPreferencesDataSource.getApiContextPref())
        } else {
            // create the ApiContext
            ApiContext.create(ENVIRONMENT_TYPE, API_KEY, DEVICE_DESCRIPTION).also {
                // share ApiContext to saved prefs before returning it
                sharedPreferencesDataSource.setApiContextPref(it.toJson())
            }
        }


    }
}