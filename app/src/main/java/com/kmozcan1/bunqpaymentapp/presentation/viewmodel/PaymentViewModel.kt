package com.kmozcan1.bunqpaymentapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kmozcan1.bunqpaymentapp.domain.model.PaymentModel
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.usecase.SubmitPaymentUseCase
import com.kmozcan1.bunqpaymentapp.domain.usecase.ValidatePaymentUseCase
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.PaymentViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 02-Dec-21.
 */
@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val validatePaymentUseCase: ValidatePaymentUseCase,
    private val submitPaymentUseCase: SubmitPaymentUseCase
): BaseViewModel<PaymentViewState>() {
     /** Validates the payment by calling ValidatePaymentUseCase Interactor */
    fun validatePayment(email: String, amount: String, description: String) {
         val payment = PaymentModel(email, amount, description)
         viewModelScope.launch {
             when (val result = validatePaymentUseCase(payment)) {
                 is UseCaseResult.Success -> {
                    setViewState(PaymentViewState.PaymentValidation(result.data))
                 }
             }
         }
    }

    /**
     * Submit the payment using the SubmitPaymentUseCase Interactor
     * Updates the ViewState with the payment result
     * */
    fun submitPayment(email: String, amount: String, description: String) {
        viewModelScope.launch {
            submitPaymentUseCase(PaymentModel(email, amount, description)).collect { result ->
                when (result) {
                    is UseCaseResult.Error -> {
                        if (checkIfNetworkError(result.exception)) {
                            setViewState(PaymentViewState.PaymentNetworkError)
                        } else {
                            setViewState(PaymentViewState.PaymentError)
                        }
                    }
                    UseCaseResult.Loading -> {
                        setViewState(PaymentViewState.PaymentProcessing)
                    }
                    is UseCaseResult.Success -> {
                        setViewState(PaymentViewState.PaymentSuccessful)
                    }
                }
            }
        }
    }
}