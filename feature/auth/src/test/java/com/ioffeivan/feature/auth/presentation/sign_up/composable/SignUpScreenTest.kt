package com.ioffeivan.feature.auth.presentation.sign_up.composable

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.designsystem.theme.MegaChatTheme
import com.ioffeivan.feature.auth.R
import com.ioffeivan.feature.auth.presentation.sign_up.SignUpEvent
import com.ioffeivan.feature.auth.presentation.sign_up.SignUpState
import com.ioffeivan.feature.auth.presentation.utils.AuthTestTags
import com.ioffeivan.feature.auth.presentation.utils.EmailState
import com.ioffeivan.feature.auth.presentation.utils.PasswordState
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
class SignUpScreenTest {
    private val recordedEvents = mutableListOf<SignUpEvent>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun tearDown() {
        recordedEvents.clear()
    }

    @Test
    fun emailTextField_whenTyping_shouldCallEmailChangedEvent() =
        runComposeUiTest {
            val email = "email@email.com"
            val expectedEvents = listOf(SignUpEvent.EmailChanged(email))
            signUpScreen()

            onNodeWithTag(AuthTestTags.EMAIL_INPUT)
                .performTextInput(email)

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun passwordTextField_whenTyping_shouldCallPasswordChangedEvent() =
        runComposeUiTest {
            val password = "password"
            val expectedEvents = listOf(SignUpEvent.PasswordChanged(password))
            signUpScreen()

            onNodeWithTag(AuthTestTags.PASSWORD_INPUT)
                .performTextInput(password)

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun confirmPasswordTextField_whenTyping_shouldCallPasswordChangedEvent() =
        runComposeUiTest {
            val confirmPassword = "confirmPassword"
            val expectedEvents = listOf(SignUpEvent.ConfirmPasswordChanged(confirmPassword))
            signUpScreen()

            onNodeWithTag(AuthTestTags.CONFIRM_PASSWORD_INPUT)
                .performTextInput(confirmPassword)

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun passwordTextField_whenToggleVisibilityClicked_shouldCallPasswordVisibilityToggledEvent() =
        runComposeUiTest {
            val expectedEvents = listOf(SignUpEvent.PasswordVisibilityToggled)
            signUpScreen()

            onNodeWithContentDescription(
                label = context.getString(R.string.toggle_password_visibility),
                useUnmergedTree = true,
            ).performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun confirmPasswordTextField_whenToggleVisibilityClicked_shouldCallConfirmPasswordVisibilityToggledEvent() =
        runComposeUiTest {
            val expectedEvents = listOf(SignUpEvent.ConfirmPasswordVisibilityToggled)
            signUpScreen()

            onNodeWithContentDescription(
                label = context.getString(R.string.toggle_confirm_password_visibility),
                useUnmergedTree = true,
            ).performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun signUpButton_whenClicked_shouldCallSignUpClickedEvent() =
        runComposeUiTest {
            val filledState =
                SignUpState.initial().copy(
                    email = EmailState.initial().copy(value = "email@email.com"),
                    password = PasswordState.initial().copy(value = "password"),
                    confirmPassword = PasswordState.initial().copy(value = "password"),
                )
            val expectedEvents = listOf(SignUpEvent.SignUpClicked)
            signUpScreen(state = filledState)

            onNodeWithTag(AuthTestTags.SIGN_UP_BUTTON)
                .performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun textFields_whenKeyboardDoneClicked_shouldCallSignUpClickedEventAndClearFocus() =
        runComposeUiTest {
            val filledState =
                SignUpState.initial().copy(
                    email = EmailState.initial().copy(value = "email@email.com"),
                    password = PasswordState.initial().copy(value = "password"),
                    confirmPassword = PasswordState.initial().copy(value = "password"),
                )
            val textFields =
                listOf(
                    AuthTestTags.EMAIL_INPUT,
                    AuthTestTags.PASSWORD_INPUT,
                    AuthTestTags.CONFIRM_PASSWORD_INPUT,
                )
            val expectedEvents = listOf(SignUpEvent.SignUpClicked)
            signUpScreen(state = filledState)

            textFields.forEach { textFieldTag ->
                recordedEvents.clear()

                onNodeWithTag(textFieldTag)
                    .performClick()
                    .performImeAction()

                assertThat(recordedEvents).isEqualTo(expectedEvents)

                onNodeWithTag(textFieldTag)
                    .assertIsNotFocused()
            }
        }

    @Test
    fun navigationIcon_whenClicked_shouldCallBackClickedEvent() =
        runComposeUiTest {
            val expectedEvents = listOf(SignUpEvent.BackClicked)
            signUpScreen()

            onNodeWithContentDescription(label = context.getString(R.string.navigate_back))
                .performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    private fun ComposeUiTest.signUpScreen(
        state: SignUpState = SignUpState.initial(),
    ) {
        setContent {
            MegaChatTheme {
                SignUpScreen(
                    state = state,
                    onEvent = { recordedEvents.add(it) },
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
