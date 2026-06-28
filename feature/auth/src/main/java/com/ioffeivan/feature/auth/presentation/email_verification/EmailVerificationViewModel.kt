package com.ioffeivan.feature.auth.presentation.email_verification

import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.domain.base.onBusinessRuleError
import com.ioffeivan.core.domain.base.onError
import com.ioffeivan.core.domain.base.onSuccess
import com.ioffeivan.core.mvu.BaseViewModel
import com.ioffeivan.core.presentation.asStringRes
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.domain.usecase.CheckEmailVerificationUseCase
import com.ioffeivan.feature.auth.domain.usecase.SendEmailVerificationUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EmailVerificationViewModel.Factory::class)
internal class EmailVerificationViewModel @AssistedInject constructor(
    private val checkEmailVerificationUseCase: CheckEmailVerificationUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    @Assisted email: String?,
) : BaseViewModel<
        EmailVerificationState,
        EmailVerificationAction,
        EmailVerificationEvent,
        EmailVerificationMessage,
        EmailVerificationCommand,
        EmailVerificationEffect,
    >(
        initialState = EmailVerificationState.initial(email),
        reducer = EmailVerificationReducer(),
    ) {
    override fun handleCommand(command: EmailVerificationCommand) {
        when (command) {
            EmailVerificationCommand.PerformCheckEmailVerification ->
                performCheckEmailVerification()

            EmailVerificationCommand.PerformResendEmailVerification ->
                performSendEmailVerification()
        }
    }

    private fun performCheckEmailVerification() {
        viewModelScope.launch {
            checkEmailVerificationUseCase(Unit)
                .onSuccess {
                    onMessage(EmailVerificationMessage.CheckEmailVerificationSuccess)
                }
                .onBusinessRuleError {
                    when (it) {
                        CheckEmailVerificationUseCase.Error.NoEmailVerification -> {
                            onMessage(EmailVerificationMessage.NoEmailVerification)
                        }
                    }
                }
                .onError {
                    onMessage(
                        EmailVerificationMessage.CheckEmailVerificationError(
                            UiText.StringResource(it.asStringRes()),
                        ),
                    )
                }
        }
    }

    private fun performSendEmailVerification() {
        viewModelScope.launch {
            sendEmailVerificationUseCase(Unit)
                .onSuccess {
                    onMessage(EmailVerificationMessage.ResendEmailVerificationSuccess)
                }
                .onError {
                    onMessage(
                        EmailVerificationMessage.ResendEmailVerificationError(
                            UiText.StringResource(it.asStringRes()),
                        ),
                    )
                }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(email: String?): EmailVerificationViewModel
    }
}
