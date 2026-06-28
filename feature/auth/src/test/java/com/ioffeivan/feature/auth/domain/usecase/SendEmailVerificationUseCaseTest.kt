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

class SendEmailVerificationUseCaseTest {
    private lateinit var emailVerificationRepository: EmailVerificationRepository
    private lateinit var sendEmailVerificationUseCase: SendEmailVerificationUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        emailVerificationRepository = mockk()
        sendEmailVerificationUseCase =
            SendEmailVerificationUseCase(
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
            coEvery { emailVerificationRepository.sendEmailVerification() } returns expected

            val actual = sendEmailVerificationUseCase(Unit)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { emailVerificationRepository.sendEmailVerification() }
        }

    @Test
    fun whenRepositoryReturnsAppError_shouldPassThroughAppError() =
        runTest(testDispatcher) {
            val expected = Result.Error(AppError.NoNetwork)
            coEvery { emailVerificationRepository.sendEmailVerification() } returns expected

            val actual = sendEmailVerificationUseCase(Unit)

            assertThat(actual).isEqualTo(expected)
            coVerify(exactly = 1) { emailVerificationRepository.sendEmailVerification() }
        }
}
