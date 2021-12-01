package com.kmozcan1.bunqpaymentapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.usecase.GetPaymentsListUseCase
import com.kmozcan1.bunqpaymentapp.domain.usecase.InitializeBunqApiContextUseCase
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPaymentsListUseCase: GetPaymentsListUseCase,
    private val initializeBunqApiContextUseCase: InitializeBunqApiContextUseCase
) : BaseViewModel<HomeViewState>() {

    /** Retrieves the bunq ApiContext using the GetBunqApiContextUseCase Interactor */
    fun getBunqApiContext() {
        viewModelScope.launch {
            val result = initializeBunqApiContextUseCase(Unit)
            if (result is UseCaseResult.Success) {
                setViewState(HomeViewState.BunqApiContext)
            }
        }
    }

    fun getPaymentsList() {
        viewModelScope.launch {
            when (val result = getPaymentsListUseCase(Unit)) {
                is UseCaseResult.Success -> {
                    setViewState(HomeViewState.PaymentList(result.data))
                }
            }
        }
    }
}