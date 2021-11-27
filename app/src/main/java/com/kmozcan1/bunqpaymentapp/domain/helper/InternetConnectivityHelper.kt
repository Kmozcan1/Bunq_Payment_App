package com.kmozcan1.bunqpaymentapp.domain.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 11/28/2021.
 */
class InternetConnectivityHelper @Inject constructor(
    @ApplicationContext private val context: Context) {

    private lateinit var internetReceiver: BroadcastReceiver

    fun observeInternet(): Flow<Boolean> {
        return callbackFlow {
            internetReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    var isConnected = false
                    val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val activeNetwork = connectivityManager.activeNetwork
                        if (activeNetwork != null) {
                            connectivityManager.getNetworkCapabilities(activeNetwork)?.run {
                                isConnected = when {
                                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                                    else -> false
                                }
                            }
                        }
                    } else {
                        // getActiveNetworkInfo() is deprecated for API 29,
                        // but only API 22 and below goes here
                        connectivityManager.run {
                            if (activeNetworkInfo != null) {
                                isConnected = activeNetworkInfo!!.isConnected
                            }
                        }
                    }
                    trySend(isConnected)
                }
            }

            // register the receiver
            val connectionFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
            context.registerReceiver(internetReceiver, connectionFilter)

            // unregister receiver on close
            awaitClose {
                context.unregisterReceiver(internetReceiver)
            }
        }
    }
}
