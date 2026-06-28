package com.ioffeivan.feature.auth.presentation.sign_in

import com.ioffeivan.core.mvu.Reducer
import com.ioffeivan.core.mvu.ReducerResult
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.presentation.sign_in.mapper.toAuthCredentials
import com.ioffeivan.feature.auth.presentation.utils.EmailState
import com.ioffeivan.feature.auth.presentation.utils.PasswordState

internal class SignInReducer : Reducer<SignInState, SignInAction, SignInCommand, SignInEffect> {
    override fun reduce(
        previousState: SignInState,
        action: SignInAction,
    ): ReducerResult<SignInState, SignInCommand, SignInEffect> {
        return result(previousState) {
            when (action) {
                is SignInEvent.EmailChanged ->
                    state {
                        copy(
                            email =
                                email.copy(
                                    value = action.email,
                                    errorMessage = null,
                                ),
                        )
                    }

                is SignInEvent.PasswordChanged ->
                    state {
                        copy(
                            password =
                                password.copy(
                                    value = action.password,
                                    errorMessage = null,
                                ),
                        )
                    }

                SignInEvent.PasswordVisibilityToggled ->
                    state {
                        copy(
                            password =
                                password.copy(
                                    visibility = !password.visibility,
                                ),
                        )
                    }

                SignInEvent.SignInClicked -> {
                    state {
                        copy(
                            email = email.copy(errorMessage = null),
                            password = password.copy(errorMessage = null),
                            isLoading = true,
                        )
                    }
                    command =
                        SignInCommand.PerformSignIn(
                            authCredentials = previousState.toAuthCredentials(),
                        )
                }

                SignInEvent.BackClicked -> {
                    effect = SignInEffect.NavigateBack
                }

                is SignInMessage.ValidationError -> {
                    state {
                        copy(
                            email = email.copy(errorMessage = action.emailErrorMessage),
                            password = password.copy(errorMessage = action.passwordErrorMessage),
                            isLoading = false,
                        )
                    }
                }

                SignInMessage.SignInSuccess -> {
                    state { copy(isLoading = false) }
                }

                is SignInMessage.SignInError -> {
                    state { copy(isLoading = false) }
                    effect = SignInEffect.ShowError(action.message)
                }
            }
        }
    }
}

internal data class SignInState(
    val email: EmailState,
    val password: PasswordState,
    val isLoading: Boolean,
) : Reducer.State {
    val isFilled
        get() = email.value.isNotBlank() && password.value.isNotBlank()

    companion object {
        fun initial(): SignInState {
            return SignInState(
                email = EmailState.initial(),
                password = PasswordState.initial(),
                isLoading = false,
            )
        }
    }
}

internal sealed interface SignInAction : Reducer.Action

internal sealed interface SignInEvent : SignInAction {
    data class EmailChanged(val email: String) : SignInEvent

    data class PasswordChanged(val password: String) : SignInEvent

    data object PasswordVisibilityToggled : SignInEvent

    data object SignInClicked : SignInEvent

    data object BackClicked : SignInEvent
}

internal sealed interface SignInMessage : SignInAction {
    data class ValidationError(
        val emailErrorMessage: UiText?,
        val passwordErrorMessage: UiText?,
    ) : SignInMessage

    data object SignInSuccess : SignInMessage

    data class SignInError(val message: UiText) : SignInMessage
}

internal sealed interface SignInCommand : Reducer.Command {
    data class PerformSignIn(val authCredentials: AuthCredentials) : SignInCommand
}

internal sealed interface SignInEffect : Reducer.Effect {
    data class ShowError(val message: UiText) : SignInEffect

    data object NavigateBack : SignInEffect
}
