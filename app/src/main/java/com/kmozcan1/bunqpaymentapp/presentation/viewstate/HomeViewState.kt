package com.kmozcan1.bunqpaymentapp.presentation.viewstate

import com.bunq.sdk.model.generated.endpoint.Payment

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
sealed class HomeViewState {
    object BunqApiContext : HomeViewState()
    class PaymentList (val paymentList: List<Payment>) : HomeViewState()
}
