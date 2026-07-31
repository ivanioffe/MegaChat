package com.ioffeivan.feature.auth.presentation.email_verification

import com.ioffeivan.core.mvu.assertMatches
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.R
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class EmailVerificationReducerTest {
    private val reducer = EmailVerificationReducer()

    @Nested
    inner class Event {
        @Test
        fun whenCheckEmailVerificationClicked_shouldStartCheckingAndEmitPerformCheckCommand() {
            val previousState = createEmailVerificationState(isChecking = false)
            val expectedState = previousState.copy(isChecking = true)
            val expectedCommand = EmailVerificationCommand.PerformCheckEmailVerification
            val action = EmailVerificationEvent.CheckEmailVerificationClicked

            reducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedCommand = expectedCommand,
                )
        }

        @Test
        fun whenResendEmailVerificationClicked_shouldStartResendingAndEmitPerformResendCommand() {
            val previousState = createEmailVerificationState(isResending = false)
            val expectedState = previousState.copy(isResending = true)
            val expectedCommand = EmailVerificationCommand.PerformResendEmailVerification
            val action = EmailVerificationEvent.ResendEmailVerificationClicked

            reducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedCommand = expectedCommand,
                )
        }
    }

    @Nested
    inner class Message {
        private val uiText = mockk<UiText>()

        @Test
        fun whenCheckEmailVerificationSuccess_shouldStopChecking() {
            val previousState = createEmailVerificationState(isChecking = true)
            val expectedState = previousState.copy(isChecking = false)
            val action = EmailVerificationMessage.CheckEmailVerificationSuccess

            reducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenNoEmailVerification_shouldStopCheckingAndEmitShowMessageEffect() {
            val previousState = createEmailVerificationState(isChecking = true)
            val expectedState = previousState.copy(isChecking = false)
            val expectedEffect =
                EmailVerificationEffect.ShowMessage(
                    UiText.StringResource(R.string.email_not_verified_yet),
                )
            val action = EmailVerificationMessage.NoEmailVerification

            reducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedEffect = expectedEffect,
                )
        }

        @Test
        fun whenCheckEmailVerificationError_shouldStopCheckingAndEmitShowMessageEffect() {
            val previousState = createEmailVerificationState(isChecking = true)
            val expectedState = previousState.copy(isChecking = false)
            val expectedEffect = EmailVerificationEffect.ShowMessage(uiText)
            val action = EmailVerificationMessage.CheckEmailVerificationError(message = uiText)

            reducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedEffect = expectedEffect,
                )
        }

        @Test
        fun whenResendEmailVerificationSuccess_shouldStopResendingAndEmitShowMessageEffect() {
            val previousState = createEmailVerificationState(isResending = true)
            val expectedState = previousState.copy(isResending = false)
            val expectedEffect =
                EmailVerificationEffect.ShowMessage(
                    UiText.StringResource(R.string.email_verification_sent_success),
                )
            val action = EmailVerificationMessage.ResendEmailVerificationSuccess

            reducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedEffect = expectedEffect,
                )
        }

        @Test
        fun whenResendEmailVerificationError_shouldStopResendingAndEmitShowMessageEffect() {
            val previousState = createEmailVerificationState(isResending = true)
            val expectedState = previousState.copy(isResending = false)
            val expectedEffect = EmailVerificationEffect.ShowMessage(uiText)
            val action = EmailVerificationMessage.ResendEmailVerificationError(message = uiText)

            reducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedEffect = expectedEffect,
                )
        }
    }

    private fun createEmailVerificationState(
        email: String? = "email@email.com",
        isChecking: Boolean = false,
        isResending: Boolean = false,
    ) = EmailVerificationState(email, isChecking, isResending)
}
