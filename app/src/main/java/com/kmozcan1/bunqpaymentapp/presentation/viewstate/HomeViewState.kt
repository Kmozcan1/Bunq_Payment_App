package com.kmozcan1.bunqpaymentapp.presentation.viewstate

import androidx.paging.PagingData
import com.bunq.sdk.model.generated.endpoint.Payment

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
sealed class HomeViewState {
    object BunqApiContextSuccess : HomeViewState()
    object BunqApiContextLoading : HomeViewState()
    object BunqApiContextNetworkError : HomeViewState()
    class PaymentList (val pagingData: PagingData<Payment>) : HomeViewState()
    object PaymentListNetworkError : HomeViewState()
}
