package com.ioffeivan.feature.auth.domain.utils

import com.google.common.truth.Truth.assertThat
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.model.SignUpCredentials
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AuthValidationErrorTest {
    private val emailError = mockk<EmailValidator.Error>()
    private val passwordError = mockk<PasswordValidator.Error>()

    @BeforeEach
    fun setUp() {
        mockkObject(EmailValidator)
        mockkObject(PasswordValidator)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    private fun setUpValidators(
        emailError: EmailValidator.Error? = null,
        passwordError: PasswordValidator.Error? = null,
        confirmPasswordError: PasswordValidator.Error? = null,
    ) {
        every { EmailValidator.validate(any()) } returns emailError
        every { PasswordValidator.validate(any()) } returns passwordError
        every { PasswordValidator.validateMismatch(any(), any()) } returns confirmPasswordError
    }

    @Nested
    inner class ValidateAuthCredentials {
        @Test
        fun whenAuthCredentialsAreValid_shouldReturnNull() {
            setUpValidators()

            val credentials = createAuthCredentials()
            val actual = validateAuthCredentials(credentials)

            assertThat(actual).isNull()
        }

        @Test
        fun whenEmailIsInvalidButPasswordIsValid_shouldReturnOnlyEmailError() {
            val expected = AuthValidationError(emailError = emailError, passwordError = null)
            setUpValidators(emailError = emailError)

            val credentials = createAuthCredentials(email = INVALID_EMAIL)
            val actual = validateAuthCredentials(credentials)

            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun whenEmailIsValidButPasswordIsInvalid_shouldReturnOnlyPasswordError() {
            val expected = AuthValidationError(emailError = null, passwordError = passwordError)
            setUpValidators(passwordError = passwordError)

            val credentials = createAuthCredentials(password = INVALID_PASSWORD)
            val actual = validateAuthCredentials(credentials)

            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun whenAllAuthCredentialsAreInvalid_shouldReturnAllErrors() {
            val expected = AuthValidationError(emailError = emailError, passwordError = passwordError)
            setUpValidators(emailError = emailError, passwordError = passwordError)

            val credentials = createAuthCredentials(email = INVALID_EMAIL, password = INVALID_PASSWORD)
            val actual = validateAuthCredentials(credentials)

            assertThat(actual).isEqualTo(expected)
        }

        private fun createAuthCredentials(
            email: String = VALID_EMAIL,
            password: String = VALID_PASSWORD,
        ) = AuthCredentials(email, password)
    }

    @Nested
    inner class ValidateSignUpCredentials {
        private val confirmPasswordError = mockk<PasswordValidator.Error>()

        @Test
        fun whenSignUpCredentialsAreValid_shouldReturnNull() {
            setUpValidators()

            val credentials = createSignUpCredentials()
            val actual = validateSignUpCredentials(credentials)

            assertThat(actual).isNull()
        }

        @Test
        fun whenEmailIsInvalidButPasswordsAreValid_shouldReturnOnlyEmailError() {
            val expected =
                AuthValidationError(
                    emailError = emailError,
                    passwordError = null,
                    confirmPasswordError = null,
                )
            setUpValidators(emailError = emailError)

            val credentials = createSignUpCredentials(email = INVALID_EMAIL)
            val actual = validateSignUpCredentials(credentials)

            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun whenEmailIsValidButPasswordIsInvalid_shouldReturnPasswordErrorAndSkipMismatchCheck() {
            val expected =
                AuthValidationError(
                    emailError = null,
                    passwordError = passwordError,
                    confirmPasswordError = null,
                )
            setUpValidators(passwordError = passwordError)

            val credentials = createSignUpCredentials(password = INVALID_PASSWORD)
            val actual = validateSignUpCredentials(credentials)

            assertThat(actual).isEqualTo(expected)
            verify(exactly = 0) { PasswordValidator.validateMismatch(any(), any()) }
        }

        @Test
        fun whenEmailAndPasswordAreValidButPasswordsDoNotMatch_shouldReturnOnlyConfirmPasswordError() {
            val expected =
                AuthValidationError(
                    emailError = null,
                    passwordError = null,
                    confirmPasswordError = confirmPasswordError,
                )
            setUpValidators(confirmPasswordError = confirmPasswordError)

            val credentials = createSignUpCredentials(confirmPassword = DIFFERENT_PASSWORD)
            val actual = validateSignUpCredentials(credentials)

            assertThat(actual).isEqualTo(expected)
            verify(exactly = 1) {
                PasswordValidator.validateMismatch(VALID_PASSWORD, DIFFERENT_PASSWORD)
            }
        }

        @Test
        fun whenAllSignUpFieldsAreInvalid_shouldReturnEmailAndPasswordErrorsAndSkipMismatchCheck() {
            val expected =
                AuthValidationError(
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = null,
                )
            setUpValidators(emailError = emailError, passwordError = passwordError)

            val credentials = createSignUpCredentials(email = INVALID_EMAIL, password = INVALID_PASSWORD)
            val actual = validateSignUpCredentials(credentials)

            assertThat(actual).isEqualTo(expected)
            verify(exactly = 0) { PasswordValidator.validateMismatch(any(), any()) }
        }

        private fun createSignUpCredentials(
            email: String = VALID_EMAIL,
            password: String = VALID_PASSWORD,
            confirmPassword: String = VALID_PASSWORD,
        ) = SignUpCredentials(email, password, confirmPassword)
    }

    companion object {
        private const val VALID_EMAIL = "valid@email.com"
        private const val INVALID_EMAIL = "invalidEmail"
        private const val VALID_PASSWORD = "validPassword"
        private const val INVALID_PASSWORD = "invalidPassword"
        private const val DIFFERENT_PASSWORD = "differentPassword"
    }
}
