package com.ioffeivan.feature.auth.presentation.sign_in

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.domain.base.AppError
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.testing.MainDispatcherExtension
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.domain.usecase.SignInUseCase
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
class SignInViewModelTest {
    private lateinit var signInUseCase: SignInUseCase
    private lateinit var signInViewModel: SignInViewModel

    @BeforeEach
    fun setUp() {
        signInUseCase = mockk()
        signInViewModel = SignInViewModel(signInUseCase)
    }

    @Test
    fun whenSignInUseCaseReturnsSuccess_shouldStopLoading() =
        runTest {
            coEvery { signInUseCase(any()) } returns Result.Success(Unit)

            signInViewModel.state.test {
                skipItems(1)

                signInViewModel.onEvent(SignInEvent.SignInClicked)

                val loadingState = awaitItem()
                assertThat(loadingState.isLoading).isTrue()

                val successState = awaitItem()
                assertThat(successState.isLoading).isFalse()
            }
        }

    @Test
    fun whenSignInUseCaseReturnsValidationError_shouldStopLoadingAndSetFieldErrors() =
        runTest {
            val validationError =
                SignInUseCase.SignInError.Validation(
                    AuthValidationError(
                        emailError = EmailValidator.Error.INVALID,
                        passwordError = PasswordValidator.Error.INVALID,
                    ),
                )
            val expectedMessage = validationError.toSignInMessage() as SignInMessage.ValidationError

            coEvery { signInUseCase(any()) } returns Result.BusinessRuleError(validationError)

            signInViewModel.state.test {
                skipItems(1)

                signInViewModel.onEvent(SignInEvent.SignInClicked)

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
    fun whenSignInUseCaseReturnsInvalidCredentialsError_shouldEmitShowErrorEffect() =
        runTest {
            val invalidCredentialsError = SignInUseCase.SignInError.InvalidCredentials
            coEvery { signInUseCase(any()) } returns Result.BusinessRuleError(invalidCredentialsError)

            signInViewModel.effect.test {
                signInViewModel.onEvent(SignInEvent.SignInClicked)

                val effect = awaitItem()
                assertThat(effect).isInstanceOf(SignInEffect.ShowError::class.java)
            }
        }

    @Test
    fun whenSignInUseCaseReturnsAppError_shouldEmitShowErrorEffect() =
        runTest {
            val appError = AppError.NoNetwork
            val expectedText = UiText.StringResource(corePresentationR.string.error_no_internet)

            coEvery { signInUseCase(any()) } returns Result.Error(appError)

            signInViewModel.effect.test {
                signInViewModel.onEvent(SignInEvent.SignInClicked)

                assertThat(awaitItem()).isEqualTo(SignInEffect.ShowError(expectedText))
            }
        }
}
