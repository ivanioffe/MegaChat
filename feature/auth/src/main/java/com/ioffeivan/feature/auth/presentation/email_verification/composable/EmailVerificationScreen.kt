package com.ioffeivan.feature.auth.presentation.email_verification.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ioffeivan.core.designsystem.component.LoadingButton
import com.ioffeivan.core.designsystem.component.PrimaryOutlinedButton
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.core.ui.ObserveEffectsWithLifecycle
import com.ioffeivan.core.ui.onDebounceClick
import com.ioffeivan.feature.auth.R
import com.ioffeivan.feature.auth.presentation.email_verification.EmailVerificationEffect
import com.ioffeivan.feature.auth.presentation.email_verification.EmailVerificationEvent
import com.ioffeivan.feature.auth.presentation.email_verification.EmailVerificationState
import com.ioffeivan.feature.auth.presentation.email_verification.EmailVerificationViewModel
import com.ioffeivan.feature.auth.presentation.utils.AuthTestTags

@Composable
internal fun EmailVerificationRoute(
    modifier: Modifier = Modifier,
    viewModel: EmailVerificationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveEffectsWithLifecycle(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                is EmailVerificationEffect.ShowMessage ->
                    snackbarHostState.showSnackbar(effect.message.asString(context))
            }
        },
    )

    EmailVerificationScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
internal fun EmailVerificationScreen(
    state: EmailVerificationState,
    snackbarHostState: SnackbarHostState,
    onEvent: (EmailVerificationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val subtitleText =
        if (state.email != null) {
            val fullText = stringResource(R.string.email_verification_subtitle_with_email, state.email)
            val startIndex = fullText.indexOf(state.email)

            buildAnnotatedString {
                append(fullText)
                if (startIndex >= 0) {
                    addStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold),
                        start = startIndex,
                        end = startIndex + state.email.length,
                    )
                }
            }
        } else {
            AnnotatedString(stringResource(R.string.email_verification_subtitle_fallback))
        }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.email_verification_title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitleText,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoadingButton(
                text = stringResource(R.string.email_verification_i_verified),
                isLoading = state.isChecking,
                onClick = onDebounceClick { onEvent(EmailVerificationEvent.CheckEmailVerificationClicked) },
                enabled = !state.isChecking && !state.isResending,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag(AuthTestTags.CHECK_VERIFICATION_BUTTON),
            )

            Spacer(modifier = Modifier.height(8.dp))

            PrimaryOutlinedButton(
                text =
                    if (state.isResending) {
                        stringResource(R.string.email_verification_sending)
                    } else {
                        stringResource(R.string.email_verification_resend)
                    },
                onClick = onDebounceClick { onEvent(EmailVerificationEvent.ResendEmailVerificationClicked) },
                enabled = !state.isChecking && !state.isResending,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag(AuthTestTags.RESEND_EMAIL_BUTTON),
            )
        }
    }
}

@Preview
@Composable
private fun EmailVerificationScreenInitialPreviewLight() {
    PreviewContainer(darkTheme = false) {
        EmailVerificationScreen(
            state = EmailVerificationState.initial("email@email.com"),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationScreenInitialPreviewDark() {
    PreviewContainer(darkTheme = true) {
        EmailVerificationScreen(
            state = EmailVerificationState.initial("email@email.com"),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationScreenCheckingPreviewLight() {
    PreviewContainer(darkTheme = false) {
        EmailVerificationScreen(
            state = EmailVerificationState.initial("email@email.com").copy(isChecking = true),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationScreenCheckingPreviewDark() {
    PreviewContainer(darkTheme = true) {
        EmailVerificationScreen(
            state = EmailVerificationState.initial("email@email.com").copy(isChecking = true),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationScreenResendingPreviewLight() {
    PreviewContainer(darkTheme = false) {
        EmailVerificationScreen(
            state = EmailVerificationState.initial("email@email.com").copy(isResending = true),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationScreenResendingPreviewDark() {
    PreviewContainer(darkTheme = true) {
        EmailVerificationScreen(
            state = EmailVerificationState.initial("email@email.com").copy(isResending = true),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
        )
    }
}
