package com.ioffeivan.feature.auth.presentation.sign_in.composable

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
import com.ioffeivan.feature.auth.presentation.sign_in.SignInEvent
import com.ioffeivan.feature.auth.presentation.sign_in.SignInState
import com.ioffeivan.feature.auth.presentation.utils.AuthTestTags
import com.ioffeivan.feature.auth.presentation.utils.EmailState
import com.ioffeivan.feature.auth.presentation.utils.PasswordState
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
class SignInScreenTest {
    private val recordedEvents = mutableListOf<SignInEvent>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        recordedEvents.clear()
    }

    @Test
    fun emailTextField_whenTyping_shouldCallEmailChangedEvent() =
        runComposeUiTest {
            val email = "email@email.com"
            val expectedEvents = listOf(SignInEvent.EmailChanged(email))
            signInScreen()

            onNodeWithTag(AuthTestTags.EMAIL_INPUT)
                .performTextInput(email)

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun passwordTextField_whenTyping_shouldCallPasswordChangedEvent() =
        runComposeUiTest {
            val password = "password"
            val expectedEvents = listOf(SignInEvent.PasswordChanged(password))
            signInScreen()

            onNodeWithTag(AuthTestTags.PASSWORD_INPUT)
                .performTextInput(password)

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun passwordTextField_whenToggleVisibilityClicked_shouldCallPasswordVisibilityToggledEvent() =
        runComposeUiTest {
            val expectedEvents = listOf(SignInEvent.PasswordVisibilityToggled)
            signInScreen()

            onNodeWithContentDescription(
                label = context.getString(R.string.toggle_password_visibility),
                useUnmergedTree = true,
            )
                .performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun signInButton_whenClicked_shouldCallSignInClickedEvent() =
        runComposeUiTest {
            val filledState =
                SignInState.initial().copy(
                    email = EmailState.initial().copy(value = "email@email.com"),
                    password = PasswordState.initial().copy(value = "password"),
                )
            val expectedEvents = listOf(SignInEvent.SignInClicked)
            signInScreen(state = filledState)

            onNodeWithTag(AuthTestTags.SIGN_IN_BUTTON)
                .performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun textFields_whenKeyboardDoneClicked_shouldCallSignInClickedEventAndClearFocus() =
        runComposeUiTest {
            val filledState =
                SignInState.initial().copy(
                    email = EmailState.initial().copy(value = "email@email.com"),
                    password = PasswordState.initial().copy(value = "password"),
                )
            val textFields =
                listOf(
                    AuthTestTags.EMAIL_INPUT,
                    AuthTestTags.PASSWORD_INPUT,
                )
            val expectedEvents = listOf(SignInEvent.SignInClicked)

            signInScreen(state = filledState)

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
            val expectedEvents = listOf(SignInEvent.BackClicked)
            signInScreen()

            onNodeWithContentDescription("Navigate back")
                .performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    private fun ComposeUiTest.signInScreen(
        state: SignInState = SignInState.initial(),
    ) {
        setContent {
            MegaChatTheme {
                SignInScreen(
                    state = state,
                    onEvent = { recordedEvents.add(it) },
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
