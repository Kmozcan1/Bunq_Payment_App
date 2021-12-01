package com.kmozcan1.bunqpaymentapp.domain.usecase

import com.kmozcan1.bunqpaymentapp.application.di.IoDispatcher
import com.kmozcan1.bunqpaymentapp.domain.helper.InternetConnectivityHelper
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */
class ObserveInternetConnectivityUseCase @Inject constructor(
    private val internetConnectivityHelper: InternetConnectivityHelper,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Any, Boolean>(dispatcher) {

    override suspend fun execute(parameters: Any): Flow<UseCaseResult<Boolean>> {
        return internetConnectivityHelper.observeInternet().map { connectivity ->
            UseCaseResult.Success(connectivity)
        }
    }
}
