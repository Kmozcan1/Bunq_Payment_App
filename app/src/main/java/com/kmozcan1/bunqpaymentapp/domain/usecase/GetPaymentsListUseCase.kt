package com.kmozcan1.bunqpaymentapp.domain.usecase

import com.bunq.sdk.http.BunqResponse
import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.application.di.IoDispatcher
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.repository.BunqApiRepository
import com.kmozcan1.bunqpaymentapp.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */
class GetPaymentsListUseCase @Inject constructor(
    private val bunqApiRepository: BunqApiRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String?, BunqResponse<List<Payment>>?>(dispatcher) {
    override suspend fun execute(parameters: String?):
            Flow<UseCaseResult<BunqResponse<List<Payment>>>> {
        return bunqApiRepository.getPaymentsList(olderId = parameters)
    }
}