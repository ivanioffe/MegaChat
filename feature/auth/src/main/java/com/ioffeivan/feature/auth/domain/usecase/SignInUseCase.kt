package com.ioffeivan.feature.auth.domain.usecase

import com.ioffeivan.core.common.coroutine.IODispatcher
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.domain.base.UseCase
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import com.ioffeivan.feature.auth.domain.utils.AuthValidationError
import com.ioffeivan.feature.auth.domain.utils.validateAuthCredentials
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @IODispatcher dispatcher: CoroutineDispatcher,
) : UseCase<AuthCredentials, Unit, SignInUseCase.SignInError>(dispatcher) {
    sealed class SignInError {
        data class Validation(val error: AuthValidationError) : SignInError()

        data object InvalidCredentials : SignInError()
    }

    override suspend fun execute(parameters: AuthCredentials): Result<Unit, SignInError> {
        validateAuthCredentials(parameters)?.let { validationError ->
            return Result.BusinessRuleError(SignInError.Validation(validationError))
        }

        return when (val result = authRepository.signIn(parameters)) {
            is Result.Success -> {
                Result.Success(Unit)
            }

            is Result.BusinessRuleError -> {
                when (result.businessRuleError) {
                    AuthRepository.SignIn.InvalidCredentials -> {
                        Result.BusinessRuleError(SignInError.InvalidCredentials)
                    }
                }
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}
