package com.kmozcan1.bunqpaymentapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPaymentsListUseCase: GetPaymentsListUseCase,
    private val initializeBunqApiContextUseCase: InitializeBunqApiContextUseCase
) : BaseViewModel<HomeViewState>() {

    companion object {
        const val HAS_RETAINED_LIST_KEY = "hasRetainedList"
    }

    /** Retrieves the bunq ApiContext using the GetBunqApiContextUseCase Interactor */
    fun getBunqApiContext() {
        viewModelScope.launch {
            initializeBunqApiContextUseCase(Unit).collect { result ->
                when(result) {
                    is UseCaseResult.Error -> {
                         if (checkIfNetworkError(result.exception)) {
                             setViewState(HomeViewState.BunqApiContextNetworkError)
                         }
                    }
                    UseCaseResult.Loading -> {
                        setViewState(HomeViewState.BunqApiContextLoading)
                    }
                    is UseCaseResult.Success -> {
                        setViewState(HomeViewState.BunqApiContextSuccess)
                    }
                }

            }
        }
    }

    /**
     * Creates a Pager that paginates using getPaymentsListUseCase via PaymentListPagingSource
     * Collect pagination the data and updates the ViewState
     * */
    fun getPaymentsList() {
        setHasRetainedListState(false)
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

    fun setHasRetainedListState(hasRetainedList: Boolean) {
        savedStateHandle[HAS_RETAINED_LIST_KEY] = hasRetainedList
    }

    fun getHasRetainedListState(): Boolean? {
        return savedStateHandle.get<Boolean>(HAS_RETAINED_LIST_KEY)
    }
}