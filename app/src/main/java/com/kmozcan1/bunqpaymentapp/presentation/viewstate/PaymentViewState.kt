package com.kmozcan1.bunqpaymentapp.presentation.viewstate

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
sealed class PaymentViewState {
    object PaymentProcessing : PaymentViewState()
    object PaymentSuccessful : PaymentViewState()
    object PaymentError : PaymentViewState()
}
