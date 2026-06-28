package com.ioffeivan.feature.auth.domain.usecase

import com.ioffeivan.core.common.coroutine.IODispatcher
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.domain.base.UseCase
import com.ioffeivan.feature.auth.domain.repository.EmailVerificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class CheckEmailVerificationUseCase @Inject constructor(
    private val emailVerificationRepository: EmailVerificationRepository,
    @IODispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit, CheckEmailVerificationUseCase.Error>(dispatcher) {
    sealed class Error {
        data object NoEmailVerification : Error()
    }

    override suspend fun execute(parameters: Unit): Result<Unit, Error> {
        return when (val result = emailVerificationRepository.checkEmailVerification()) {
            is Result.Success -> {
                Result.Success(Unit)
            }

            is Result.BusinessRuleError -> {
                when (result.businessRuleError) {
                    EmailVerificationRepository.CheckEmailVerificationError.NoEmailVerification -> {
                        Result.BusinessRuleError(Error.NoEmailVerification)
                    }
                }
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}
