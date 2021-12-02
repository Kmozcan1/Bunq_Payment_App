package com.kmozcan1.bunqpaymentapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.usecase.GetPaymentsListUseCase
import com.kmozcan1.bunqpaymentapp.domain.usecase.InitializeBunqApiContextUseCase
import com.kmozcan1.bunqpaymentapp.presentation.pagingsource.PaymentListPagingSource
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
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

    /**
     * Creates a Pager that paginates using getPaymentsListUseCase via PaymentListPagingSource
     * Collect pagination the data and updates the ViewState
     * */
    fun getPaymentsList() {
        val pager =  Pager(
            config = PagingConfig(initialLoadSize = 30, pageSize = 10, prefetchDistance = 10),
            pagingSourceFactory = {
                PaymentListPagingSource(
                    getPaymentsListUseCase = getPaymentsListUseCase
                )
            }
        ).flow.cachedIn(viewModelScope)

        viewModelScope.launch {
            pager.collectLatest {
                setViewState(HomeViewState.PaymentList(pagingData = it))
            }
        }
    }
}