package com.ioffeivan.feature.auth.presentation.sign_in.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ioffeivan.core.designsystem.component.LoadingButton
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.core.ui.ObserveEffectsWithLifecycle
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.core.ui.onDebounceClick
import com.ioffeivan.feature.auth.R
import com.ioffeivan.feature.auth.presentation.component.AuthScreenContainer
import com.ioffeivan.feature.auth.presentation.component.EmailTextField
import com.ioffeivan.feature.auth.presentation.component.PasswordTextField
import com.ioffeivan.feature.auth.presentation.sign_in.SignInEffect
import com.ioffeivan.feature.auth.presentation.sign_in.SignInEvent
import com.ioffeivan.feature.auth.presentation.sign_in.SignInState
import com.ioffeivan.feature.auth.presentation.sign_in.SignInViewModel
import com.ioffeivan.feature.auth.presentation.utils.AuthTestTags
import com.ioffeivan.feature.auth.presentation.utils.EmailState
import com.ioffeivan.feature.auth.presentation.utils.PasswordState

@Composable
internal fun SignInRoute(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveEffectsWithLifecycle(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                SignInEffect.NavigateBack -> onNavigateBack()

                is SignInEffect.ShowError ->
                    snackbarHostState.showSnackbar(effect.message.asString(context))
            }
        },
    )

    SignInScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignInScreen(
    state: SignInState,
    onEvent: (SignInEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val onSignIn: () -> Unit = {
        onEvent(SignInEvent.SignInClicked)
        focusManager.clearFocus()
    }
    val imeAction = if (state.isFilled) ImeAction.Done else ImeAction.Next
    val keyboardActions = KeyboardActions(onDone = { onSignIn() })

    AuthScreenContainer(
        title = stringResource(R.string.sign_in),
        onBackClicked = { onEvent(SignInEvent.BackClicked) },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    ) {
        EmailTextField(
            email = state.email.value,
            onEmailChange = {
                onEvent(SignInEvent.EmailChanged(it))
            },
            enabled = !state.isLoading,
            errorMessage = state.email.errorMessage?.asString(),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = imeAction,
                ),
            keyboardActions = keyboardActions,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag(AuthTestTags.EMAIL_INPUT),
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            password = state.password.value,
            onPasswordChange = {
                onEvent(SignInEvent.PasswordChanged(it))
            },
            onShowPasswordToggle = {
                onEvent(SignInEvent.PasswordVisibilityToggled)
            },
            enabled = !state.isLoading,
            placeholder = {
                Text(text = stringResource(R.string.password))
            },
            showPassword = state.password.visibility,
            errorMessage = state.password.errorMessage?.asString(),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
            keyboardActions = keyboardActions,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag(AuthTestTags.PASSWORD_INPUT),
        )

        Spacer(modifier = Modifier.height(24.dp))

        LoadingButton(
            text = stringResource(R.string.sign_in),
            isLoading = state.isLoading,
            onClick = onDebounceClick(onClick = onSignIn),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag(AuthTestTags.SIGN_IN_BUTTON),
        )
    }
}

@Preview
@Composable
private fun SignInScreenInitialPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignInScreen(
            state = SignInState.initial(),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignInScreenInitialPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignInScreen(
            state = SignInState.initial(),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignInScreenLoadingPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignInScreen(
            state = SignInState.initial().copy(isLoading = true),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignInScreenLoadingPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignInScreen(
            state = SignInState.initial().copy(isLoading = true),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignInScreenFilledPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignInScreen(
            state =
                SignInState.initial().copy(
                    email = EmailState.initial().copy(value = "email@email.com"),
                    password = PasswordState.initial().copy(value = "password"),
                ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignInScreenFilledPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignInScreen(
            state =
                SignInState.initial().copy(
                    email = EmailState.initial().copy(value = "email@email.com"),
                    password = PasswordState.initial().copy(value = "password"),
                ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignInScreenErrorPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignInScreen(
            state =
                SignInState.initial().copy(
                    email =
                        EmailState.initial().copy(
                            value = "bad_email",
                            errorMessage = UiText.StaticString("Invalid email"),
                        ),
                    password =
                        PasswordState.initial().copy(
                            value = "123",
                            errorMessage = UiText.StaticString("Password is too short"),
                        ),
                ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignInScreenErrorPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignInScreen(
            state =
                SignInState.initial().copy(
                    email =
                        EmailState.initial().copy(
                            value = "bad_email",
                            errorMessage = UiText.StaticString("Invalid email"),
                        ),
                    password =
                        PasswordState.initial().copy(
                            value = "123",
                            errorMessage = UiText.StaticString("Password is too short"),
                        ),
                ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}
