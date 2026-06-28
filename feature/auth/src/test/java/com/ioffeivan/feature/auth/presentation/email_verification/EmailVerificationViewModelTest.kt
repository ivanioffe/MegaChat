package com.ioffeivan.feature.auth.presentation.email_verification

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.domain.base.AppError
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.testing.MainDispatcherExtension
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.R
import com.ioffeivan.feature.auth.domain.usecase.CheckEmailVerificationUseCase
import com.ioffeivan.feature.auth.domain.usecase.SendEmailVerificationUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.ioffeivan.core.presentation.R as corePresentationR

@ExtendWith(MainDispatcherExtension::class)
class EmailVerificationViewModelTest {
    private lateinit var checkEmailVerificationUseCase: CheckEmailVerificationUseCase
    private lateinit var sendEmailVerificationUseCase: SendEmailVerificationUseCase
    private lateinit var emailVerificationViewModel: EmailVerificationViewModel
    private val email = "email@email.com"

    @BeforeEach
    fun setUp() {
        checkEmailVerificationUseCase = mockk()
        sendEmailVerificationUseCase = mockk()
        emailVerificationViewModel =
            EmailVerificationViewModel(
                checkEmailVerificationUseCase = checkEmailVerificationUseCase,
                sendEmailVerificationUseCase = sendEmailVerificationUseCase,
                email = email,
            )
    }

    @Nested
    inner class PerformCheckEmailVerification {
        @Test
        fun whenCheckEmailVerificationUseCaseReturnsSuccess_shouldStopChecking() =
            runTest {
                coEvery { checkEmailVerificationUseCase(Unit) } returns Result.Success(Unit)

                emailVerificationViewModel.state.test {
                    skipItems(1)

                    emailVerificationViewModel.onEvent(EmailVerificationEvent.CheckEmailVerificationClicked)

                    val checkingState = awaitItem()
                    assertThat(checkingState.isChecking).isTrue()

                    val successState = awaitItem()
                    assertThat(successState.isChecking).isFalse()
                }
            }

        @Test
        fun whenCheckEmailVerificationUseCaseReturnsNoEmailVerificationError_shouldEmitShowMessageEffect() =
            runTest {
                val error = CheckEmailVerificationUseCase.Error.NoEmailVerification
                val expectedText = UiText.StringResource(R.string.email_not_verified_yet)

                coEvery { checkEmailVerificationUseCase(Unit) } returns Result.BusinessRuleError(error)

                emailVerificationViewModel.effect.test {
                    emailVerificationViewModel.onEvent(EmailVerificationEvent.CheckEmailVerificationClicked)

                    val effect = awaitItem()
                    assertThat(effect).isEqualTo(EmailVerificationEffect.ShowMessage(expectedText))
                }
            }

        @Test
        fun whenCheckEmailVerificationUseCaseReturnsAppError_shouldEmitShowMessageEffect() =
            runTest {
                val appError = AppError.NoNetwork
                val expectedText = UiText.StringResource(corePresentationR.string.error_no_internet)

                coEvery { checkEmailVerificationUseCase(Unit) } returns Result.Error(appError)

                emailVerificationViewModel.effect.test {
                    emailVerificationViewModel.onEvent(EmailVerificationEvent.CheckEmailVerificationClicked)

                    assertThat(awaitItem()).isEqualTo(EmailVerificationEffect.ShowMessage(expectedText))
                }
            }
    }

    @Nested
    inner class PerformSendEmailVerification {
        @Test
        fun whenSendEmailVerificationUseCaseReturnsSuccess_shouldStopResending() =
            runTest {
                coEvery { sendEmailVerificationUseCase(Unit) } returns Result.Success(Unit)

                emailVerificationViewModel.state.test {
                    skipItems(1)

                    emailVerificationViewModel.onEvent(EmailVerificationEvent.ResendEmailVerificationClicked)

                    val resendingState = awaitItem()
                    assertThat(resendingState.isResending).isTrue()

                    val successState = awaitItem()
                    assertThat(successState.isResending).isFalse()
                }
            }

        @Test
        fun whenSendEmailVerificationUseCaseReturnsAppError_shouldEmitShowMessageEffect() =
            runTest {
                val appError = AppError.NoNetwork
                val expectedText = UiText.StringResource(corePresentationR.string.error_no_internet)

                coEvery { sendEmailVerificationUseCase(Unit) } returns Result.Error(appError)

                emailVerificationViewModel.effect.test {
                    emailVerificationViewModel.onEvent(EmailVerificationEvent.ResendEmailVerificationClicked)

                    assertThat(awaitItem()).isEqualTo(EmailVerificationEffect.ShowMessage(expectedText))
                }
            }
    }
}
