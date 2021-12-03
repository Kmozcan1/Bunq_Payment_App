package com.kmozcan1.bunqpaymentapp.data.repository

import com.bunq.sdk.context.ApiContext
import com.bunq.sdk.context.ApiEnvironmentType
import com.bunq.sdk.context.BunqContext
import com.bunq.sdk.exception.UncaughtExceptionError
import com.bunq.sdk.http.BunqResponse
import com.bunq.sdk.model.generated.`object`.Amount
import com.bunq.sdk.model.generated.`object`.Pointer
import com.bunq.sdk.model.generated.endpoint.Payment
import com.bunq.sdk.model.generated.endpoint.RequestInquiry
import com.kmozcan1.bunqpaymentapp.data.datasource.SharedPreferencesDataSource
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.repository.BunqApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
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
        private val ENVIRONMENT_TYPE = ApiEnvironmentType.SANDBOX
        private const val API_KEY = "sandbox_e9e078c8848bf885a0057b02738c571425c104b59def4623772f3f89"
        private const val DEVICE_DESCRIPTION = "android"
        private const val CURRENCY_EURO = "EUR"
        private const val POINTER_TYPE_EMAIL = "EMAIL"
        private const val SUGAR_DADDY_EMAIL = "sugardaddy@bunq.com"
        private const val INITIAL_PAGE_SIZE = "30"
        private const val PAGE_SIZE = "10"
        private const val COUNT_PARAM_KEY = "count"
        private const val OLDER_ID_PARAM_KEY = "older_id"
    }

    /** Retrieves the ApiContext from SharedPrefs if it has been initialized before,
     * otherwise creates the ApiContext and save it to the SharedPrefs */
    override fun initializeBunqApiContext(): Flow<UseCaseResult<Unit>> {
        return flow {
            try {
                emit(UseCaseResult.Loading)
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
                emit(UseCaseResult.Success(Unit))
            } catch (e: UncaughtExceptionError) {
                Timber.e(e)
                emit(UseCaseResult.Error(e))
            } catch (e: java.lang.Exception) {
                Timber.e(e)
                emit(UseCaseResult.Error(e))
            }
        }
    }




    /** Retrieves the next (or the first) page of the main account */
    override fun getPaymentsList(
        olderId: String?
    ): Flow<UseCaseResult<BunqResponse<List<Payment>>>> {
        return flow {
            try {
                val mainAccountId = BunqContext.getUserContext().mainMonetaryAccountId

                // If urlParamsNextPage is null, create a map with key = "count" value "30"
                // if urlParamsNextPage is not null, add key = "count" value "10" to the existing map
                val params = if (olderId != null) {
                    mutableMapOf(COUNT_PARAM_KEY to PAGE_SIZE).apply {
                        this[OLDER_ID_PARAM_KEY] = olderId
                    }
                } else {
                    mutableMapOf(COUNT_PARAM_KEY to INITIAL_PAGE_SIZE)
                }
                emit(UseCaseResult.Success(Payment.list(mainAccountId, params)))
            } catch (e: UncaughtExceptionError) {
                Timber.e(e)
                emit(UseCaseResult.Error(e))
            } catch (e: Exception) {
                Timber.e(e)
                emit(UseCaseResult.Error(e))
            }
        }
    }

    /** Submits a payment */
    override fun submitPayment(
        email: String, amount: String, description: String
    ): Flow<UseCaseResult<Unit>> {
        return flow {
            emit(UseCaseResult.Loading)
            try {
                askForMoneyFromSugarDaddy(amount)
                var paymentResult = Payment.create(
                    Amount(amount,
                        CURRENCY_EURO
                    ),
                    Pointer(POINTER_TYPE_EMAIL, email),
                    description
                )
                emit(UseCaseResult.Success(Unit))
            } catch (e: UncaughtExceptionError) {
                Timber.e(e)
                emit(UseCaseResult.Error(e))
            } catch (e: Exception) {
                Timber.e(e)
                emit(UseCaseResult.Error(e))
            }
        }
    }

    /** Gets the payment with the id parameter */
    override fun getPayment(paymentId: Int): Flow<UseCaseResult<Payment>> {
        return flow {
            emit(UseCaseResult.Loading)
            try {
                emit(UseCaseResult.Success(Payment.get(paymentId).value))
            } catch (e: UncaughtExceptionError) {
                Timber.e(e)
                emit(UseCaseResult.Error(e))
            } catch (e: Exception) {
                Timber.e(e)
                emit(UseCaseResult.Error(e))
            }

        }
    }

    /** Asks the sugar daddy for money */
    private fun askForMoneyFromSugarDaddy(amount: String) {
        try {
            RequestInquiry.create(
                Amount(
                    amount,
                    CURRENCY_EURO
                ),
                Pointer(POINTER_TYPE_EMAIL, SUGAR_DADDY_EMAIL),
                "money pls",
                false
            )
            Thread.sleep(1000)
        } catch (e: UncaughtExceptionError) {
            Timber.e(e)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}