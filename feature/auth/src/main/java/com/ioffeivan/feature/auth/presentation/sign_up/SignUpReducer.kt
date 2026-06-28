package com.ioffeivan.feature.auth.presentation.sign_up

import com.ioffeivan.core.mvu.Reducer
import com.ioffeivan.core.mvu.ReducerResult
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.domain.model.SignUpCredentials
import com.ioffeivan.feature.auth.presentation.sign_up.mapper.toSignUpCredentials
import com.ioffeivan.feature.auth.presentation.utils.EmailState
import com.ioffeivan.feature.auth.presentation.utils.PasswordState

internal class SignUpReducer : Reducer<SignUpState, SignUpAction, SignUpCommand, SignUpEffect> {
    override fun reduce(
        previousState: SignUpState,
        action: SignUpAction,
    ): ReducerResult<SignUpState, SignUpCommand, SignUpEffect> {
        return result(previousState) {
            when (action) {
                is SignUpEvent.EmailChanged ->
                    state {
                        copy(
                            email =
                                email.copy(
                                    value = action.email,
                                    errorMessage = null,
                                ),
                        )
                    }

                is SignUpEvent.PasswordChanged ->
                    state {
                        copy(
                            password =
                                password.copy(
                                    value = action.password,
                                    errorMessage = null,
                                ),
                        )
                    }

                is SignUpEvent.ConfirmPasswordChanged ->
                    state {
                        copy(
                            confirmPassword =
                                confirmPassword.copy(
                                    value = action.confirmPassword,
                                    errorMessage = null,
                                ),
                        )
                    }

                SignUpEvent.PasswordVisibilityToggled ->
                    state {
                        copy(
                            password =
                                password.copy(
                                    visibility = !password.visibility,
                                ),
                        )
                    }

                SignUpEvent.ConfirmPasswordVisibilityToggled ->
                    state {
                        copy(
                            confirmPassword =
                                confirmPassword.copy(
                                    visibility = !confirmPassword.visibility,
                                ),
                        )
                    }

                SignUpEvent.SignUpClicked -> {
                    state {
                        copy(
                            email = email.copy(errorMessage = null),
                            password = password.copy(errorMessage = null),
                            confirmPassword = confirmPassword.copy(errorMessage = null),
                            isLoading = true,
                        )
                    }
                    command =
                        SignUpCommand.PerformSignUp(
                            signUpCredentials = previousState.toSignUpCredentials(),
                        )
                }

                SignUpEvent.BackClicked -> {
                    effect = SignUpEffect.NavigateBack
                }

                is SignUpMessage.ValidationError -> {
                    state {
                        copy(
                            email = email.copy(errorMessage = action.emailErrorMessage),
                            password = password.copy(errorMessage = action.passwordErrorMessage),
                            confirmPassword = confirmPassword.copy(errorMessage = action.confirmPasswordErrorMessage),
                            isLoading = false,
                        )
                    }
                }

                SignUpMessage.SignUpSuccess ->
                    state {
                        copy(isLoading = false)
                    }

                is SignUpMessage.SignUpError -> {
                    state { copy(isLoading = false) }
                    effect = SignUpEffect.ShowError(action.message)
                }
            }
        }
    }
}

internal data class SignUpState(
    val email: EmailState,
    val password: PasswordState,
    val confirmPassword: PasswordState,
    val isLoading: Boolean,
) : Reducer.State {
    val isFilled
        get() = email.value.isNotBlank() && password.value.isNotBlank() && confirmPassword.value.isNotBlank()

    companion object {
        fun initial(): SignUpState {
            return SignUpState(
                email = EmailState.initial(),
                password = PasswordState.initial(),
                confirmPassword = PasswordState.initial(),
                isLoading = false,
            )
        }
    }
}

internal sealed interface SignUpAction : Reducer.Action

internal sealed interface SignUpEvent : SignUpAction {
    data class EmailChanged(val email: String) : SignUpEvent

    data class PasswordChanged(val password: String) : SignUpEvent

    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpEvent

    data object PasswordVisibilityToggled : SignUpEvent

    data object ConfirmPasswordVisibilityToggled : SignUpEvent

    data object SignUpClicked : SignUpEvent

    data object BackClicked : SignUpEvent
}

internal sealed interface SignUpMessage : SignUpAction {
    data class ValidationError(
        val emailErrorMessage: UiText?,
        val passwordErrorMessage: UiText?,
        val confirmPasswordErrorMessage: UiText?,
    ) : SignUpMessage

    data object SignUpSuccess : SignUpMessage

    data class SignUpError(val message: UiText) : SignUpMessage
}

internal sealed interface SignUpCommand : Reducer.Command {
    data class PerformSignUp(val signUpCredentials: SignUpCredentials) : SignUpCommand
}

internal sealed interface SignUpEffect : Reducer.Effect {
    data class ShowError(val message: UiText) : SignUpEffect

    data object NavigateBack : SignUpEffect
}
