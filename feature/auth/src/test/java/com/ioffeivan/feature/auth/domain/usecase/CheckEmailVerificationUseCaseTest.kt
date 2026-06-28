package com.ioffeivan.feature.auth.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.domain.base.AppError
import com.ioffeivan.core.domain.base.Result
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

class CheckEmailVerificationUseCaseTest {
    private lateinit var emailVerificationRepository: EmailVerificationRepository
    private lateinit var checkEmailVerificationUseCase: CheckEmailVerificationUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        emailVerificationRepository = mockk()
        checkEmailVerificationUseCase =
            CheckEmailVerificationUseCase(
                emailVerificationRepository,
                testDispatcher,
            )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun whenRepositoryReturnsSuccess_shouldReturnSuccess() =
        runTest(testDispatcher) {
            val expected = Result.Success(Unit)
            coEvery { emailVerificationRepository.checkEmailVerification() } returns expected

            val actual = checkEmailVerificationUseCase(Unit)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { emailVerificationRepository.checkEmailVerification() }
        }

    @Test
    fun whenRepositoryReturnsNoEmailVerificationError_shouldMapToUseCaseNoEmailVerificationError() =
        runTest(testDispatcher) {
            val expected = Result.BusinessRuleError(CheckEmailVerificationUseCase.Error.NoEmailVerification)
            coEvery {
                emailVerificationRepository.checkEmailVerification()
            } returns Result.BusinessRuleError(EmailVerificationRepository.CheckEmailVerificationError.NoEmailVerification)

            val actual = checkEmailVerificationUseCase(Unit)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { emailVerificationRepository.checkEmailVerification() }
        }

    @Test
    fun whenRepositoryReturnsAppError_shouldPassThroughAppError() =
        runTest(testDispatcher) {
            val expected = Result.Error(AppError.NoNetwork)
            coEvery { emailVerificationRepository.checkEmailVerification() } returns expected

            val actual = checkEmailVerificationUseCase(Unit)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { emailVerificationRepository.checkEmailVerification() }
        }
}
