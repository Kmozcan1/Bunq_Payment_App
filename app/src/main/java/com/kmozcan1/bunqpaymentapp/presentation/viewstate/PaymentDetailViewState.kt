package com.kmozcan1.bunqpaymentapp.presentation.viewstate

import com.bunq.sdk.model.generated.endpoint.Payment

/**
 * Created by Kadir Mert Özcan on 01-Dec-21.
 */
sealed class PaymentDetailViewState {
    object PaymentDetailError : PaymentDetailViewState()
    object PaymentDetailLoading : PaymentDetailViewState()
    class PaymentDetailReady(val paymentDetail: Payment) : PaymentDetailViewState()
}
