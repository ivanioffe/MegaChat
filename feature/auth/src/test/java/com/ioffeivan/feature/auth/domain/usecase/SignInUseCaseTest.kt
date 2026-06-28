package com.ioffeivan.feature.auth.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.domain.base.AppError
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SignInUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var signInUseCase: SignInUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    private val validAuthCredentials =
        AuthCredentials(
            email = "email@email.com",
            password = "password",
        )

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        signInUseCase = SignInUseCase(authRepository, testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun whenAuthCredentialsAreInvalid_shouldReturnValidationErrorAndNotCallRepository() =
        runTest(testDispatcher) {
            val invalidAuthCredentials =
                AuthCredentials(
                    email = "email",
                    password = "password",
                )

            val actual = signInUseCase(invalidAuthCredentials)

            val businessRuleError = (actual as Result.BusinessRuleError).businessRuleError
            assertThat(businessRuleError).isInstanceOf(SignInUseCase.SignInError.Validation::class.java)
            coVerify(exactly = 0) { authRepository.signIn(any()) }
        }

    @Test
    fun whenRepositoryReturnsSuccess_shouldReturnSuccess() =
        runTest(testDispatcher) {
            val expected = Result.Success(Unit)
            coEvery { authRepository.signIn(validAuthCredentials) } returns expected

            val actual = signInUseCase(validAuthCredentials)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { authRepository.signIn(validAuthCredentials) }
        }

    @Test
    fun whenRepositoryReturnsInvalidCredentialsError_shouldMapToUseCaseInvalidCredentialsError() =
        runTest(testDispatcher) {
            val expected = Result.BusinessRuleError(SignInUseCase.SignInError.InvalidCredentials)
            coEvery {
                authRepository.signIn(validAuthCredentials)
            } returns Result.BusinessRuleError(AuthRepository.SignIn.InvalidCredentials)

            val actual = signInUseCase(validAuthCredentials)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { authRepository.signIn(validAuthCredentials) }
        }

    @Test
    fun whenRepositoryReturnsAppError_shouldPassThroughAppError() =
        runTest(testDispatcher) {
            val expected = Result.Error(AppError.NoNetwork)
            coEvery { authRepository.signIn(validAuthCredentials) } returns expected

            val actual = signInUseCase(validAuthCredentials)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { authRepository.signIn(validAuthCredentials) }
        }
}
