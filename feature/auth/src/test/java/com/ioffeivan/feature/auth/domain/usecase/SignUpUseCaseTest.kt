package com.ioffeivan.feature.auth.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.domain.base.AppError
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.model.SignUpCredentials
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import com.ioffeivan.feature.auth.domain.repository.EmailVerificationRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SignUpUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var emailVerificationRepository: EmailVerificationRepository
    private lateinit var signUpUseCase: SignUpUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    private val validSignUpCredentials =
        SignUpCredentials(
            email = "email@email.com",
            password = "password",
            confirmPassword = "password",
        )

    private val validAuthCredentials =
        AuthCredentials(
            email = "email@email.com",
            password = "password",
        )

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        emailVerificationRepository = mockk()
        signUpUseCase = SignUpUseCase(authRepository, emailVerificationRepository, testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun whenSignUpCredentialsAreInvalid_shouldReturnValidationErrorAndNotCallRepository() =
        runTest(testDispatcher) {
            val invalidSignUpCredentials =
                SignUpCredentials(
                    email = "email",
                    password = "password",
                    confirmPassword = "different_password",
                )

            val actual = signUpUseCase(invalidSignUpCredentials)

            val businessRuleError = (actual as Result.BusinessRuleError).businessRuleError
            assertThat(businessRuleError).isInstanceOf(SignUpUseCase.SignUpError.Validation::class.java)
            coVerify(exactly = 0) { authRepository.signUp(any()) }
        }

    @Test
    fun whenRepositoryReturnsSuccess_shouldCallSendEmailVerificationAndReturnSuccess() =
        runTest(testDispatcher) {
            val expected = Result.Success(Unit)
            coEvery { authRepository.signUp(validAuthCredentials) } returns expected
            coEvery { emailVerificationRepository.sendEmailVerification() } returns Result.Success(Unit)

            val actual = signUpUseCase(validSignUpCredentials)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { authRepository.signUp(validAuthCredentials) }
            coVerify(exactly = 1) { emailVerificationRepository.sendEmailVerification() }
        }

    @Test
    fun whenRepositoryReturnsUserExistsError_shouldMapToUseCaseUserExistsError() =
        runTest(testDispatcher) {
            val expected = Result.BusinessRuleError(SignUpUseCase.SignUpError.UserExists)
            coEvery {
                authRepository.signUp(validAuthCredentials)
            } returns Result.BusinessRuleError(AuthRepository.SignUp.UserExists)

            val actual = signUpUseCase(validSignUpCredentials)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { authRepository.signUp(validAuthCredentials) }
        }

    @Test
    fun whenRepositoryReturnsAppError_shouldPassThroughAppError() =
        runTest(testDispatcher) {
            val expected = Result.Error(AppError.NoNetwork)
            coEvery { authRepository.signUp(validAuthCredentials) } returns expected

            val actual = signUpUseCase(validSignUpCredentials)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { authRepository.signUp(validAuthCredentials) }
        }
}
