package com.kmozcan1.bunqpaymentapp.data.repository

import com.bunq.sdk.context.ApiContext
import com.bunq.sdk.context.ApiEnvironmentType
import com.bunq.sdk.context.BunqContext
import com.bunq.sdk.model.generated.endpoint.Payment
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
        private const val API_CONTEXT_PATH = "bunq-test.conf"
        private val ENVIRONMENT_TYPE = ApiEnvironmentType.SANDBOX
        private const val API_KEY = "sandbox_e9e078c8848bf885a0057b02738c571425c104b59def4623772f3f89"
        private const val DEVICE_DESCRIPTION = "android"
    }

    /** Retrieves the ApiContext from SharedPrefs if it has been initialized before,
     * otherwise creates the ApiContext and save it to the SharedPrefs */
    override fun initializeBunqApiContext() {
        val apiContextPref = sharedPreferencesDataSource.getApiContextPref()

        if (apiContextPref != null) {
            // return the existing ApiContext from shared prefs
            ApiContext.fromJson(sharedPreferencesDataSource.getApiContextPref()).apply {
                ensureSessionActive()
            }.also { apiContext ->
                BunqContext.loadApiContext(apiContext)
            }
        } else {
            // create the ApiContext
            ApiContext.create(ENVIRONMENT_TYPE, API_KEY, DEVICE_DESCRIPTION).apply {
                ensureSessionActive()
            }.also { apiContext ->
                // share ApiContext to saved prefs before returning it
                sharedPreferencesDataSource.setApiContextPref(apiContext.toJson())
                BunqContext.loadApiContext(apiContext)
            }
        }
    }




    /** Retrieves the list of payments of the main account */
    override fun getPaymentsList(): List<Payment> {
        val mainAccountId = BunqContext.getUserContext().mainMonetaryAccountId

        /*val response = RequestInquiry.create(
            Amount(
                "5",
                "EUR"
            ),
            Pointer("EMAIL", "sugardaddy@bunq.com"),
            "money pls",
            false
        )

        Thread.sleep(1000)

        RequestInquiry.get(response.value)

        RequestInquiry.create(
            Amount("5.0", "EUR"),
            Pointer("EMAIL", "sugardaddy@bunq.com"),
            "Requesting some spending money.",
            false,
            mainAccountId
        )


        Thread.sleep(1000)
*/


        return Payment.list(mainAccountId).value
    }
}