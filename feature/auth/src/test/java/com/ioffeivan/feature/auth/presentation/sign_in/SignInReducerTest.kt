package com.ioffeivan.feature.auth.presentation.sign_in

import com.ioffeivan.core.mvu.assertMatches
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.presentation.sign_in.mapper.toAuthCredentials
import com.ioffeivan.feature.auth.presentation.utils.EmailState
import com.ioffeivan.feature.auth.presentation.utils.PasswordState
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SignInReducerTest {
    private val signInReducer = SignInReducer()
    private val initialEmail = EmailState.initial()
    private val initialPassword = PasswordState.initial()

    @Nested
    inner class Event {
        private val email = "email@email.com"
        private val password = "password"

        @Test
        fun whenEmailChanged_shouldUpdateEmailAndClearEmailError() {
            val previousState = createSignInState()
            val expectedState =
                previousState.copy(
                    email = initialEmail.copy(value = email, errorMessage = null),
                )
            val action = SignInEvent.EmailChanged(email)

            signInReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenPasswordChanged_shouldUpdatePasswordAndClearPasswordError() {
            val previousState = createSignInState()
            val expectedState =
                previousState.copy(
                    password = initialPassword.copy(value = password, errorMessage = null),
                )
            val action = SignInEvent.PasswordChanged(password)

            signInReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenPasswordVisibilityToggled_shouldToggleVisibility() {
            val previousState = createSignInState(password = initialPassword.copy(visibility = false))
            val expectedState =
                previousState.copy(
                    password = initialPassword.copy(visibility = true),
                )
            val action = SignInEvent.PasswordVisibilityToggled

            signInReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenSignInClicked_shouldStartLoadingAndClearErrorsAndEmitPerformSignInCommand() {
            val previousState = createSignInState(isLoading = false)
            val expectedState =
                previousState.copy(
                    email = initialEmail.copy(errorMessage = null),
                    password = initialPassword.copy(errorMessage = null),
                    isLoading = true,
                )
            val expectedCommand = SignInCommand.PerformSignIn(previousState.toAuthCredentials())
            val action = SignInEvent.SignInClicked

            signInReducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedCommand = expectedCommand,
                )
        }

        @Test
        fun whenBackClicked_shouldEmitNavigateBackEffect() {
            val previousState = createSignInState()
            val action = SignInEvent.BackClicked

            signInReducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = previousState,
                    expectedEffect = SignInEffect.NavigateBack,
                )
        }
    }

    @Nested
    inner class Message {
        private val uiText = mockk<UiText>()

        @Test
        fun whenValidationError_shouldStopLoadingAndSetErrors() {
            val previousState = createSignInState(isLoading = true)
            val expectedState =
                previousState.copy(
                    email = initialEmail.copy(errorMessage = uiText),
                    password = initialPassword.copy(errorMessage = uiText),
                    isLoading = false,
                )
            val action =
                SignInMessage.ValidationError(
                    emailErrorMessage = uiText,
                    passwordErrorMessage = uiText,
                )

            signInReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenSignInSuccess_shouldStopLoading() {
            val previousState = createSignInState(isLoading = true)
            val expectedState = previousState.copy(isLoading = false)
            val action = SignInMessage.SignInSuccess

            signInReducer.reduce(previousState, action)
                .assertMatches(expectedState)
        }

        @Test
        fun whenSignInError_shouldStopLoadingAndEmitShowErrorEffect() {
            val previousState = createSignInState(isLoading = true)
            val expectedState = previousState.copy(isLoading = false)
            val expectedEffect = SignInEffect.ShowError(uiText)
            val action = SignInMessage.SignInError(message = uiText)

            signInReducer.reduce(previousState, action)
                .assertMatches(
                    expectedState = expectedState,
                    expectedEffect = expectedEffect,
                )
        }
    }

    private fun createSignInState(
        email: EmailState = initialEmail,
        password: PasswordState = initialPassword,
        isLoading: Boolean = false,
    ) = SignInState(email, password, isLoading)
}
