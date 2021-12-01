package com.kmozcan1.bunqpaymentapp.domain.repository

import com.bunq.sdk.model.generated.endpoint.Payment


/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */
interface BunqApiRepository {
    fun initializeBunqApiContext()
    fun getPaymentsList() : List<Payment>
}