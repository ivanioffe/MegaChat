package com.ioffeivan.feature.onboarding.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ioffeivan.core.auth.GoogleAuthManager
import com.ioffeivan.core.designsystem.component.PrimaryOutlinedButton
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.core.ui.onDebounceClick
import com.ioffeivan.feature.onboarding.R
import com.ioffeivan.feature.onboarding.presentation.OnboardingViewModel
import com.ioffeivan.feature.onboarding.presentation.component.SignInWithGoogleButton
import com.ioffeivan.feature.onboarding.presentation.component.SignInWithPasswordButton
import kotlinx.coroutines.launch
import com.ioffeivan.core.ui.R as coreUiR

@Composable
internal fun OnboardingRoute(
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    OnboardingScreen(
        snackbarHostState = snackbarHostState,
        onSignInWithGoogleButtonClick = {
            scope.launch {
                when (viewModel.googleAuthManager.signIn(context)) {
                    GoogleAuthManager.SignInResult.Error -> {
                        snackbarHostState.showSnackbar(
                            UiText.StringResource(R.string.error_sign_in).asString(context),
                        )
                    }

                    else -> {}
                }
            }
        },
        onSignInWithPasswordButtonClick = onNavigateToSignIn,
        onSignUpButtonClick = onNavigateToSignUp,
        modifier = modifier,
    )
}

@Composable
internal fun OnboardingScreen(
    snackbarHostState: SnackbarHostState,
    onSignInWithGoogleButtonClick: () -> Unit,
    onSignInWithPasswordButtonClick: () -> Unit,
    onSignUpButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .weight(1f),
            ) {
                Text(
                    text = stringResource(coreUiR.string.app_name),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.CenterVertically,
                    ),
                modifier =
                    Modifier
                        .weight(1f),
            ) {
                SignInWithGoogleButton(
                    onClick = onDebounceClick(onClick = onSignInWithGoogleButtonClick),
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )

                SignInWithPasswordButton(
                    onClick = onDebounceClick(onClick = onSignInWithPasswordButtonClick),
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )

                PrimaryOutlinedButton(
                    text = stringResource(coreUiR.string.sign_up),
                    onClick = onDebounceClick(onClick = onSignUpButtonClick),
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreviewLight() {
    PreviewContainer(darkTheme = false) {
        OnboardingScreen(
            snackbarHostState = SnackbarHostState(),
            onSignInWithGoogleButtonClick = {},
            onSignInWithPasswordButtonClick = {},
            onSignUpButtonClick = {},
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenPreviewDark() {
    PreviewContainer(darkTheme = true) {
        OnboardingScreen(
            snackbarHostState = SnackbarHostState(),
            onSignInWithGoogleButtonClick = {},
            onSignInWithPasswordButtonClick = {},
            onSignUpButtonClick = {},
        )
    }
}
