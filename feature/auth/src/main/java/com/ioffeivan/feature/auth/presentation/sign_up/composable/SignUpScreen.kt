package com.ioffeivan.feature.auth.presentation.sign_up.composable

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
import com.ioffeivan.feature.auth.presentation.sign_up.SignUpEffect
import com.ioffeivan.feature.auth.presentation.sign_up.SignUpEvent
import com.ioffeivan.feature.auth.presentation.sign_up.SignUpState
import com.ioffeivan.feature.auth.presentation.sign_up.SignUpViewModel
import com.ioffeivan.feature.auth.presentation.utils.AuthTestTags
import com.ioffeivan.feature.auth.presentation.utils.EmailState
import com.ioffeivan.feature.auth.presentation.utils.PasswordState
import com.ioffeivan.core.ui.R as coreUiR

@Composable
internal fun SignUpRoute(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveEffectsWithLifecycle(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                SignUpEffect.NavigateBack -> onNavigateBack()

                is SignUpEffect.ShowError ->
                    snackbarHostState.showSnackbar(effect.message.asString(context))
            }
        },
    )

    SignUpScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUpScreen(
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val onSignUp: () -> Unit = {
        onEvent(SignUpEvent.SignUpClicked)
        focusManager.clearFocus()
    }
    val imeAction = if (state.isFilled) ImeAction.Done else ImeAction.Next
    val keyboardActions = KeyboardActions(onDone = { onSignUp() })

    AuthScreenContainer(
        title = stringResource(coreUiR.string.sign_up),
        onBackClicked = { onEvent(SignUpEvent.BackClicked) },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    ) {
        EmailTextField(
            email = state.email.value,
            onEmailChange = { onEvent(SignUpEvent.EmailChanged(it)) },
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

        Spacer(modifier = Modifier.height(12.dp))

        PasswordTextField(
            password = state.password.value,
            onPasswordChange = {
                onEvent(SignUpEvent.PasswordChanged(it))
            },
            onShowPasswordToggle = {
                onEvent(SignUpEvent.PasswordVisibilityToggled)
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
                    imeAction = imeAction,
                ),
            keyboardActions = keyboardActions,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag(AuthTestTags.PASSWORD_INPUT),
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordTextField(
            password = state.confirmPassword.value,
            onPasswordChange = {
                onEvent(SignUpEvent.ConfirmPasswordChanged(it))
            },
            onShowPasswordToggle = {
                onEvent(SignUpEvent.ConfirmPasswordVisibilityToggled)
            },
            enabled = !state.isLoading,
            placeholder = {
                Text(text = stringResource(R.string.confirm_password))
            },
            showPassword = state.confirmPassword.visibility,
            errorMessage = state.confirmPassword.errorMessage?.asString(),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
            keyboardActions = keyboardActions,
            toggleVisibilityContentDescription = stringResource(R.string.toggle_confirm_password_visibility),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag(AuthTestTags.CONFIRM_PASSWORD_INPUT),
        )

        Spacer(modifier = Modifier.height(24.dp))

        LoadingButton(
            text = stringResource(coreUiR.string.sign_up),
            isLoading = state.isLoading,
            onClick = onDebounceClick(onClick = onSignUp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag(AuthTestTags.SIGN_UP_BUTTON),
        )
    }
}

@Preview
@Composable
private fun SignUpScreenInitialPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignUpScreen(
            state = SignUpState.initial(),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignUpScreenInitialPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignUpScreen(
            state = SignUpState.initial(),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignUpScreenLoadingPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignUpScreen(
            state = SignUpState.initial().copy(isLoading = true),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignUpScreenLoadingPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignUpScreen(
            state = SignUpState.initial().copy(isLoading = true),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignUpScreenFilledPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignUpScreen(
            state =
                SignUpState.initial().copy(
                    email = EmailState.initial().copy(value = "email@email.com"),
                    password = PasswordState.initial().copy(value = "password"),
                    confirmPassword = PasswordState.initial().copy(value = "password"),
                ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignUpScreenFilledPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignUpScreen(
            state =
                SignUpState.initial().copy(
                    email = EmailState.initial().copy(value = "email@email.com"),
                    password = PasswordState.initial().copy(value = "password"),
                    confirmPassword = PasswordState.initial().copy(value = "password"),
                ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Preview
@Composable
private fun SignUpScreenErrorPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignUpScreen(
            state =
                SignUpState.initial().copy(
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
private fun SignUpScreenErrorPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignUpScreen(
            state =
                SignUpState.initial().copy(
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
