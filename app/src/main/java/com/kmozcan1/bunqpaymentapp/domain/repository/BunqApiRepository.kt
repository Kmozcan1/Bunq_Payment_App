package com.kmozcan1.bunqpaymentapp.domain.repository

import com.bunq.sdk.http.BunqResponse
import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import kotlinx.coroutines.flow.Flow


/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */
interface BunqApiRepository {
    fun initializeBunqApiContext() : Flow<UseCaseResult<Unit>>
    fun getPaymentsList(olderId: String?): Flow<UseCaseResult<BunqResponse<List<Payment>>>>
    fun submitPayment(email: String, amount: String, description: String) : Flow<UseCaseResult<Unit>>
    fun getPayment(paymentId: Int) : Flow<UseCaseResult<Payment>>
}