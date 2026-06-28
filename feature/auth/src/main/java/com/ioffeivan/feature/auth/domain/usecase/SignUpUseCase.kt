package com.ioffeivan.feature.auth.domain.usecase

import com.ioffeivan.core.common.coroutine.IODispatcher
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.domain.base.UseCase
import com.ioffeivan.feature.auth.domain.model.SignUpCredentials
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import com.ioffeivan.feature.auth.domain.repository.EmailVerificationRepository
import com.ioffeivan.feature.auth.domain.utils.AuthValidationError
import com.ioffeivan.feature.auth.domain.utils.toAuthCredentials
import com.ioffeivan.feature.auth.domain.utils.validateSignUpCredentials
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    @IODispatcher dispatcher: CoroutineDispatcher,
) : UseCase<SignUpCredentials, Unit, SignUpUseCase.SignUpError>(dispatcher) {
    sealed class SignUpError {
        data class Validation(val error: AuthValidationError) : SignUpError()

        data object UserExists : SignUpError()
    }

    override suspend fun execute(parameters: SignUpCredentials): Result<Unit, SignUpError> {
        validateSignUpCredentials(parameters)?.let { validationError ->
            return Result.BusinessRuleError(SignUpError.Validation(validationError))
        }

        return when (
            val result =
                authRepository.signUp(parameters.toAuthCredentials())
        ) {
            is Result.Success -> {
                emailVerificationRepository.sendEmailVerification()
                Result.Success(Unit)
            }

            is Result.BusinessRuleError -> {
                when (result.businessRuleError) {
                    AuthRepository.SignUp.UserExists -> {
                        Result.BusinessRuleError(SignUpError.UserExists)
                    }
                }
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}
