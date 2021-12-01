package com.kmozcan1.bunqpaymentapp.domain.usecase

import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.application.di.IoDispatcher
import com.kmozcan1.bunqpaymentapp.domain.repository.BunqApiRepository
import com.kmozcan1.bunqpaymentapp.domain.usecase.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */
class GetPaymentsListUseCase @Inject constructor(
    private val bunqApiRepository: BunqApiRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, List<Payment>>(dispatcher) {
    override suspend fun execute(parameters: Unit): List<Payment> {
        return bunqApiRepository.getPaymentsList()
    }
}