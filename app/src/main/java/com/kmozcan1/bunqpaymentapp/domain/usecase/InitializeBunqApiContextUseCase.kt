package com.kmozcan1.bunqpaymentapp.domain.usecase

import com.kmozcan1.bunqpaymentapp.application.di.IoDispatcher
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.repository.BunqApiRepository
import com.kmozcan1.bunqpaymentapp.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 *
 * Returns the bunq ApiContext by using BunqApiRepository
 */
class InitializeBunqApiContextUseCase @Inject constructor(
    private val bunqApiRepository: BunqApiRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(parameters: Unit): Flow<UseCaseResult<Unit>> {
        return bunqApiRepository.initializeBunqApiContext()
    }
}