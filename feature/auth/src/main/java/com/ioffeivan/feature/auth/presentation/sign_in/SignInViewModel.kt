package com.ioffeivan.feature.auth.presentation.sign_in

import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.domain.base.onBusinessRuleError
import com.ioffeivan.core.domain.base.onError
import com.ioffeivan.core.domain.base.onSuccess
import com.ioffeivan.core.mvu.BaseViewModel
import com.ioffeivan.core.presentation.asStringRes
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.R
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.usecase.SignInUseCase
import com.ioffeivan.feature.auth.presentation.utils.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) : BaseViewModel<SignInState, SignInAction, SignInEvent, SignInMessage, SignInCommand, SignInEffect>(
        initialState = SignInState.initial(),
        reducer = SignInReducer(),
    ) {
    override fun handleCommand(command: SignInCommand) {
        when (command) {
            is SignInCommand.PerformSignIn -> performSignIn(command.authCredentials)
        }
    }

    private fun performSignIn(credentials: AuthCredentials) {
        viewModelScope.launch {
            signInUseCase(credentials)
                .onSuccess {
                    onMessage(SignInMessage.SignInSuccess)
                }
                .onBusinessRuleError {
                    onMessage(it.toSignInMessage())
                }
                .onError {
                    onMessage(
                        SignInMessage.SignInError(
                            UiText.StringResource(it.asStringRes()),
                        ),
                    )
                }
        }
    }
}

internal fun SignInUseCase.SignInError.toSignInMessage(): SignInMessage {
    return when (this) {
        is SignInUseCase.SignInError.Validation -> {
            SignInMessage.ValidationError(
                emailErrorMessage = error.emailError?.toUiText(),
                passwordErrorMessage = error.passwordError?.toUiText(),
            )
        }

        SignInUseCase.SignInError.InvalidCredentials -> {
            SignInMessage.SignInError(
                UiText.StringResource(R.string.error_invalid_credentials),
            )
        }
    }
}
