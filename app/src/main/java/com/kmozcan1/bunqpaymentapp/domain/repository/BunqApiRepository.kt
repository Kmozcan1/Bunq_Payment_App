package com.kmozcan1.bunqpaymentapp.domain.repository

import com.bunq.sdk.context.ApiContext


/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */
interface BunqApiRepository {
    fun getBunqApiContext() : ApiContext
}