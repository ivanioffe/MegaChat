package com.ioffeivan.feature.auth.domain.usecase

import com.ioffeivan.core.common.coroutine.IODispatcher
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.domain.base.UseCase
import com.ioffeivan.feature.auth.domain.repository.EmailVerificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SendEmailVerificationUseCase @Inject constructor(
    private val emailVerificationRepository: EmailVerificationRepository,
    @IODispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit, Nothing>(dispatcher) {
    override suspend fun execute(parameters: Unit): Result<Unit, Nothing> {
        return emailVerificationRepository.sendEmailVerification()
    }
}
