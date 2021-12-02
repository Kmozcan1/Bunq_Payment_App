package com.kmozcan1.bunqpaymentapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.usecase.GetPaymentUseCase
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.PaymentDetailViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentDetailViewModel @Inject constructor(
    private val getPaymentUseCase: GetPaymentUseCase
): BaseViewModel<PaymentDetailViewState>() {

    /** Gets the payment details using GetPaymentUseCase interactor */
    fun getPaymentDetail(paymentId: Int) {
        viewModelScope.launch {
            getPaymentUseCase(paymentId).collect { result ->
                when(result) {
                    is UseCaseResult.Error -> {
                        setViewState(PaymentDetailViewState.PaymentDetailError)
                    }
                    UseCaseResult.Loading -> {
                        setViewState(PaymentDetailViewState.PaymentDetailLoading)
                    }
                    is UseCaseResult.Success -> {
                        setViewState(PaymentDetailViewState.PaymentDetailReady(result.data))
                    }
                }
            }
        }

    }

}