package com.kmozcan1.bunqpaymentapp.domain.usecase

import com.kmozcan1.bunqpaymentapp.application.di.MainDispatcher
import com.kmozcan1.bunqpaymentapp.domain.model.PaymentModel
import com.kmozcan1.bunqpaymentapp.domain.model.PaymentValidationModel
import com.kmozcan1.bunqpaymentapp.domain.usecase.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 03-Dec-21.
 * Interactor class for validating the payment
 */
class ValidatePaymentUseCase @Inject constructor(@MainDispatcher dispatcher: CoroutineDispatcher):
    UseCase<PaymentModel, PaymentValidationModel>(dispatcher) {
    override suspend fun execute(parameters: PaymentModel): PaymentValidationModel {
        return PaymentValidationModel().validatePaymentRequest(payment = parameters)
    }
}