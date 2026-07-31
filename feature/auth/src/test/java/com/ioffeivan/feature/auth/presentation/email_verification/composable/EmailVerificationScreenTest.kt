package com.ioffeivan.feature.auth.presentation.email_verification.composable

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.designsystem.theme.MegaChatTheme
import com.ioffeivan.feature.auth.presentation.email_verification.EmailVerificationEvent
import com.ioffeivan.feature.auth.presentation.email_verification.EmailVerificationState
import com.ioffeivan.feature.auth.presentation.utils.AuthTestTags
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
class EmailVerificationScreenTest {
    private val recordedEvents = mutableListOf<EmailVerificationEvent>()

    @Before
    fun setUp() {
        recordedEvents.clear()
    }

    @Test
    fun checkEmailVerificationButton_whenClicked_shouldCallCheckEmailVerificationClickedEvent() =
        runComposeUiTest {
            val expectedEvents = listOf(EmailVerificationEvent.CheckEmailVerificationClicked)
            emailVerificationScreen()

            onNodeWithTag(AuthTestTags.CHECK_VERIFICATION_BUTTON)
                .performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    @Test
    fun resendEmailButton_whenClicked_shouldCallResendEmailVerificationClickedEvent() =
        runComposeUiTest {
            val expectedEvents = listOf(EmailVerificationEvent.ResendEmailVerificationClicked)
            emailVerificationScreen()

            onNodeWithTag(AuthTestTags.RESEND_EMAIL_BUTTON)
                .performClick()

            assertThat(recordedEvents).isEqualTo(expectedEvents)
        }

    private fun ComposeUiTest.emailVerificationScreen(
        state: EmailVerificationState = EmailVerificationState.initial("email@email.com"),
    ) {
        setContent {
            MegaChatTheme {
                EmailVerificationScreen(
                    state = state,
                    onEvent = { recordedEvents.add(it) },
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}
