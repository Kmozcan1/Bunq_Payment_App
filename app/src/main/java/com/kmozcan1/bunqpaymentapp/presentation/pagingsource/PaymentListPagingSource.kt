package com.kmozcan1.bunqpaymentapp.presentation.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bunq.sdk.exception.UncaughtExceptionError
import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.usecase.GetPaymentsListUseCase
import kotlinx.coroutines.flow.collect
import timber.log.Timber

/**
 * Created by Kadir Mert Özcan on 02-Dec-21.
 *
 * PagingSource class for the payments.
 * Despite my by efforts, it loads all payments before scrolling
 */
class PaymentListPagingSource(
    private val getPaymentsListUseCase: GetPaymentsListUseCase
) : PagingSource<String, Payment>() {

    override suspend fun load(params: LoadParams<String>):
            LoadResult<String, Payment> {
        return try {
            // first key is always null, which makes the BunqApi return the first page
            val urlParamsPreviousPage = params.key
            var nextKey: String? = null
            var paymentList = emptyList<Payment>()
            getPaymentsListUseCase(urlParamsPreviousPage).collect { result ->
                when (result) {
                    is UseCaseResult.Error -> {
                        throw result.exception
                    }
                    is UseCaseResult.Success -> {
                        paymentList = result.data!!.value
                        nextKey = if (result.data.pagination.olderId == null) {
                            null
                        } else {
                            result.data.pagination.olderId.toString()
                        }
                    }
                }
            }

            LoadResult.Page(
                data = paymentList,
                nextKey = nextKey,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        } catch (e: UncaughtExceptionError) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Payment>): String? {
        return null
    }

    override val keyReuseSupported: Boolean
        get() = true
}