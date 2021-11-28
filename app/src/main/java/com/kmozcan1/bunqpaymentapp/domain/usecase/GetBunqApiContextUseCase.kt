package com.kmozcan1.bunqpaymentapp.domain.usecase

import com.bunq.sdk.context.ApiContext
import com.kmozcan1.bunqpaymentapp.application.di.IoDispatcher
import com.kmozcan1.bunqpaymentapp.domain.repository.BunqApiRepository
import com.kmozcan1.bunqpaymentapp.domain.usecase.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 *
 * Returns the bunq ApiContext by using BunqApiRepository
 */
class GetBunqApiContextUseCase @Inject constructor(
    private val bunqApiRepository: BunqApiRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, ApiContext>(dispatcher) {
    override suspend fun execute(parameters: Unit): ApiContext {
        return bunqApiRepository.getBunqApiContext()
    }
}