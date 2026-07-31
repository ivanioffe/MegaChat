package com.ioffeivan.feature.auth.presentation.sign_up

import com.ioffeivan.core.mvu.assertMatches
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.presentation.sign_up.mapper.toSignUpCredentials
import com.ioffeivan.feature.auth.presentation.utils.EmailState
import com.ioffeivan.feature.auth.presentation.utils.PasswordState
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SignUpReducerTest {
    private val signUpReducer = SignUpReducer()
    private val initialEmail = EmailState.initial()
    private val initialPassword = PasswordState.initial()

    @Nested
    inner class Event {
        private val email = "email@email.com"
        private val password = "password"

        @Test
        fun whenEmailChanged_shouldUpdateEmailAndClearEmailError() {
            val previousState = createSignUpState()
            val expectedState =
                previousState.copy(
                    email = initialEmail.copy(value = email, errorMessage = null),
                )
            val action = SignUpEvent.EmailChanged(email)

            signUpReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenPasswordChanged_shouldUpdatePasswordAndClearPasswordError() {
            val previousState = createSignUpState()
            val expectedState =
                previousState.copy(
                    password = initialPassword.copy(value = password, errorMessage = null),
                )
            val action = SignUpEvent.PasswordChanged(password)

            signUpReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenConfirmPasswordChanged_shouldUpdateConfirmPasswordAndClearConfirmPasswordError() {
            val previousState = createSignUpState()
            val expectedState =
                previousState.copy(
                    confirmPassword = initialPassword.copy(value = password, errorMessage = null),
                )
            val action = SignUpEvent.ConfirmPasswordChanged(password)

            signUpReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenPasswordVisibilityToggled_shouldToggleVisibility() {
            val previousState = createSignUpState(password = initialPassword.copy(visibility = false))
            val expectedState =
                previousState.copy(
                    password = initialPassword.copy(visibility = true),
                )
            val action = SignUpEvent.PasswordVisibilityToggled

            signUpReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenConfirmPasswordVisibilityToggled_shouldToggleVisibility() {
            val previousState =
                createSignUpState(confirmPassword = initialPassword.copy(visibility = false))
            val expectedState =
                previousState.copy(
                    confirmPassword = initialPassword.copy(visibility = true),
                )
            val action = SignUpEvent.ConfirmPasswordVisibilityToggled

            signUpReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenSignUpClicked_shouldStartLoadingAndClearErrorsAndEmitPerformSignUpCommand() {
            val previousState = createSignUpState(isLoading = false)
            val expectedState =
                previousState.copy(
                    email = initialEmail.copy(errorMessage = null),
                    password = initialPassword.copy(errorMessage = null),
                    confirmPassword = initialPassword.copy(errorMessage = null),
                    isLoading = true,
                )
            val expectedCommand = SignUpCommand.PerformSignUp(previousState.toSignUpCredentials())
            val action = SignUpEvent.SignUpClicked

            signUpReducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedCommand = expectedCommand,
                )
        }

        @Test
        fun whenBackClicked_shouldEmitNavigateBackEffect() {
            val previousState = createSignUpState()
            val action = SignUpEvent.BackClicked

            signUpReducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = previousState,
                    expectedEffect = SignUpEffect.NavigateBack,
                )
        }
    }

    @Nested
    inner class Message {
        private val uiText = mockk<UiText>()

        @Test
        fun whenValidationError_shouldStopLoadingAndSetErrors() {
            val previousState = createSignUpState(isLoading = true)
            val expectedState =
                previousState.copy(
                    email = initialEmail.copy(errorMessage = uiText),
                    password = initialPassword.copy(errorMessage = uiText),
                    confirmPassword = initialPassword.copy(errorMessage = null),
                    isLoading = false,
                )
            val action =
                SignUpMessage.ValidationError(
                    emailErrorMessage = uiText,
                    passwordErrorMessage = uiText,
                    confirmPasswordErrorMessage = null,
                )

            signUpReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenSignUpSuccess_shouldStopLoading() {
            val previousState = createSignUpState(isLoading = true)
            val expectedState = previousState.copy(isLoading = false)
            val action = SignUpMessage.SignUpSuccess

            signUpReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenSignUpError_shouldStopLoadingAndEmitShowErrorEffect() {
            val previousState = createSignUpState(isLoading = true)
            val expectedState = previousState.copy(isLoading = false)
            val expectedEffect = SignUpEffect.ShowError(uiText)
            val action = SignUpMessage.SignUpError(message = uiText)

            signUpReducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedEffect = expectedEffect,
                )
        }
    }

    private fun createSignUpState(
        email: EmailState = initialEmail,
        password: PasswordState = initialPassword,
        confirmPassword: PasswordState = initialPassword,
        isLoading: Boolean = false,
    ) = SignUpState(email, password, confirmPassword, isLoading)
}
