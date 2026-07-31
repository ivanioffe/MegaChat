package com.ioffeivan.feature.auth.presentation.sign_up

import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.domain.base.onBusinessRuleError
import com.ioffeivan.core.domain.base.onError
import com.ioffeivan.core.domain.base.onSuccess
import com.ioffeivan.core.mvu.BaseViewModel
import com.ioffeivan.core.presentation.asStringRes
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.R
import com.ioffeivan.feature.auth.domain.model.SignUpCredentials
import com.ioffeivan.feature.auth.domain.usecase.SignUpUseCase
import com.ioffeivan.feature.auth.presentation.utils.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpState, SignUpAction, SignUpEvent, SignUpMessage, SignUpCommand, SignUpEffect>(
        initialState = SignUpState.initial(),
        reducer = SignUpReducer(),
    ) {
    override fun handleCommand(command: SignUpCommand) {
        when (command) {
            is SignUpCommand.PerformSignUp -> performSignUp(command.signUpCredentials)
        }
    }

    private fun performSignUp(signUpCredentials: SignUpCredentials) {
        viewModelScope.launch {
            signUpUseCase(signUpCredentials)
                .onSuccess {
                    onMessage(SignUpMessage.SignUpSuccess)
                }
                .onBusinessRuleError {
                    onMessage(it.toSignUpMessage())
                }
                .onError {
                    onMessage(
                        SignUpMessage.SignUpError(
                            UiText.StringResource(it.asStringRes()),
                        ),
                    )
                }
        }
    }
}

internal fun SignUpUseCase.SignUpError.toSignUpMessage(): SignUpMessage {
    return when (this) {
        is SignUpUseCase.SignUpError.Validation -> {
            SignUpMessage.ValidationError(
                emailErrorMessage = error.emailError?.toUiText(),
                passwordErrorMessage = error.passwordError?.toUiText(),
                confirmPasswordErrorMessage = error.confirmPasswordError?.toUiText(),
            )
        }

        is SignUpUseCase.SignUpError.UserExists ->
            SignUpMessage.SignUpError(
                UiText.StringResource(R.string.error_user_exists),
            )
    }
}
