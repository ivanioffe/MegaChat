package com.ioffeivan.feature.auth.presentation.email_verification

import com.ioffeivan.core.mvu.Reducer
import com.ioffeivan.core.mvu.ReducerResult
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.R

internal class EmailVerificationReducer : Reducer<
    EmailVerificationState,
    EmailVerificationAction,
    EmailVerificationCommand,
    EmailVerificationEffect,
> {
    override fun reduce(
        previousState: EmailVerificationState,
        action: EmailVerificationAction,
    ): ReducerResult<EmailVerificationState, EmailVerificationCommand, EmailVerificationEffect> {
        return result(previousState) {
            when (action) {
                EmailVerificationEvent.CheckEmailVerificationClicked -> {
                    state {
                        copy(isChecking = true)
                    }
                    command = EmailVerificationCommand.PerformCheckEmailVerification
                }

                EmailVerificationEvent.ResendEmailVerificationClicked -> {
                    state {
                        copy(isResending = true)
                    }
                    command = EmailVerificationCommand.PerformResendEmailVerification
                }

                EmailVerificationMessage.CheckEmailVerificationSuccess -> {
                    state {
                        copy(isChecking = false)
                    }
                }

                EmailVerificationMessage.NoEmailVerification -> {
                    state {
                        copy(isChecking = false)
                    }
                    effect =
                        EmailVerificationEffect.ShowMessage(
                            UiText.StringResource(R.string.email_not_verified_yet),
                        )
                }

                is EmailVerificationMessage.CheckEmailVerificationError -> {
                    state {
                        copy(isChecking = false)
                    }
                    effect = EmailVerificationEffect.ShowMessage(action.message)
                }

                EmailVerificationMessage.ResendEmailVerificationSuccess -> {
                    state {
                        copy(isResending = false)
                    }
                    effect =
                        EmailVerificationEffect.ShowMessage(
                            UiText.StringResource(R.string.email_verification_sent_success),
                        )
                }

                is EmailVerificationMessage.ResendEmailVerificationError -> {
                    state {
                        copy(isResending = false)
                    }
                    effect = EmailVerificationEffect.ShowMessage(action.message)
                }
            }
        }
    }
}

internal data class EmailVerificationState(
    val email: String?,
    val isChecking: Boolean,
    val isResending: Boolean,
) : Reducer.State {
    companion object {
        fun initial(email: String?): EmailVerificationState {
            return EmailVerificationState(
                email = email,
                isChecking = false,
                isResending = false,
            )
        }
    }
}

internal sealed interface EmailVerificationAction : Reducer.Action

internal sealed interface EmailVerificationEvent : EmailVerificationAction {
    data object CheckEmailVerificationClicked : EmailVerificationEvent

    data object ResendEmailVerificationClicked : EmailVerificationEvent
}

internal sealed interface EmailVerificationMessage : EmailVerificationAction {
    data object CheckEmailVerificationSuccess : EmailVerificationMessage

    data object NoEmailVerification : EmailVerificationMessage

    data class CheckEmailVerificationError(val message: UiText) : EmailVerificationMessage

    data object ResendEmailVerificationSuccess : EmailVerificationMessage

    data class ResendEmailVerificationError(val message: UiText) : EmailVerificationMessage
}

internal sealed interface EmailVerificationEffect : Reducer.Effect {
    data class ShowMessage(val message: UiText) : EmailVerificationEffect
}

internal sealed interface EmailVerificationCommand : Reducer.Command {
    data object PerformCheckEmailVerification : EmailVerificationCommand

    data object PerformResendEmailVerification : EmailVerificationCommand
}
