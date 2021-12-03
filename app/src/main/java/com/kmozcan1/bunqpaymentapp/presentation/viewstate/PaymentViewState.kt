package com.kmozcan1.bunqpaymentapp.presentation.viewstate

import com.kmozcan1.bunqpaymentapp.domain.model.PaymentValidationModel

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
sealed class PaymentViewState {
    class PaymentValidation(val paymentValidationResult:
                            PaymentValidationModel): PaymentViewState()
    object PaymentProcessing : PaymentViewState()
    object PaymentSuccessful : PaymentViewState()
    object PaymentError : PaymentViewState()
    object PaymentNetworkError : PaymentViewState()
}
