package com.ioffeivan.feature.auth.presentation.sign_up

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.domain.base.AppError
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.testing.MainDispatcherExtension
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.domain.usecase.SignUpUseCase
import com.ioffeivan.feature.auth.domain.utils.AuthValidationError
import com.ioffeivan.feature.auth.domain.utils.EmailValidator
import com.ioffeivan.feature.auth.domain.utils.PasswordValidator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.ioffeivan.core.presentation.R as corePresentationR

@ExtendWith(MainDispatcherExtension::class)
class SignUpViewModelTest {
    private lateinit var signUpUseCase: SignUpUseCase
    private lateinit var signUpViewModel: SignUpViewModel

    @BeforeEach
    fun setUp() {
        signUpUseCase = mockk()
        signUpViewModel = SignUpViewModel(signUpUseCase)
    }

    @Test
    fun whenSignUpUseCaseReturnsSuccess_shouldStopLoading() =
        runTest {
            coEvery { signUpUseCase(any()) } returns Result.Success(Unit)

            signUpViewModel.state.test {
                skipItems(1)

                signUpViewModel.onEvent(SignUpEvent.SignUpClicked)

                val loadingState = awaitItem()
                assertThat(loadingState.isLoading).isTrue()

                val successState = awaitItem()
                assertThat(successState.isLoading).isFalse()
            }
        }

    @Test
    fun whenSignUpUseCaseReturnsValidationError_shouldStopLoadingAndSetFieldErrors() =
        runTest {
            val validationError =
                SignUpUseCase.SignUpError.Validation(
                    AuthValidationError(
                        emailError = EmailValidator.Error.INVALID,
                        passwordError = PasswordValidator.Error.INVALID,
                    ),
                )
            val expectedMessage = validationError.toSignUpMessage() as SignUpMessage.ValidationError
            coEvery { signUpUseCase(any()) } returns Result.BusinessRuleError(validationError)

            signUpViewModel.state.test {
                skipItems(1)

                signUpViewModel.onEvent(SignUpEvent.SignUpClicked)

                val loadingState = awaitItem()
                assertThat(loadingState.isLoading).isTrue()

                val businessRuleErrorState = awaitItem()
                assertThat(businessRuleErrorState.email.errorMessage)
                    .isEqualTo(expectedMessage.emailErrorMessage)
                assertThat(businessRuleErrorState.password.errorMessage)
                    .isEqualTo(expectedMessage.passwordErrorMessage)
                assertThat(businessRuleErrorState.isLoading).isFalse()
            }
        }

    @Test
    fun whenSignUpUseCaseReturnsUserExistsError_shouldEmitShowErrorEffect() =
        runTest {
            val userExistsError = SignUpUseCase.SignUpError.UserExists
            coEvery { signUpUseCase(any()) } returns Result.BusinessRuleError(userExistsError)

            signUpViewModel.effect.test {
                signUpViewModel.onEvent(SignUpEvent.SignUpClicked)

                val effect = awaitItem()
                assertThat(effect).isInstanceOf(SignUpEffect.ShowError::class.java)
            }
        }

    @Test
    fun whenSignUpUseCaseReturnsAppError_shouldEmitShowErrorEffect() =
        runTest {
            val appError = AppError.NoNetwork
            val expectedText = UiText.StringResource(corePresentationR.string.error_no_internet)
            coEvery { signUpUseCase(any()) } returns Result.Error(appError)

            signUpViewModel.effect.test {
                signUpViewModel.onEvent(SignUpEvent.SignUpClicked)

                assertThat(awaitItem()).isEqualTo(SignUpEffect.ShowError(expectedText))
            }
        }
}
