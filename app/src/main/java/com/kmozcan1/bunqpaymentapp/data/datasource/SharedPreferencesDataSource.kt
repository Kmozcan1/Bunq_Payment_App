package com.kmozcan1.bunqpaymentapp.data.datasource

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */
class SharedPreferencesDataSource @Inject constructor(@ApplicationContext context: Context) {
    companion object {
        const val API_CONTEXT_PREF_NAME = "API_CONTEXT"
        const val API_CONTEXT_PREF_KEY = "API_CONTEXT"
    }

    private val apiContextPref: SharedPreferences by lazy {
        context.getSharedPreferences(API_CONTEXT_PREF_NAME, Context.MODE_PRIVATE)
    }

    // Write ApiContext Json string to Shared Preferences
    fun setApiContextPref(apiContext: String) {
        apiContextPref.edit().apply {
            putString(API_CONTEXT_PREF_KEY, apiContext)
            apply()
        }
    }

    // Read ApiContext Json string from Shared Preferences
    fun getApiContextPref(): String? {
        return apiContextPref.getString(API_CONTEXT_PREF_KEY, null)
    }

}