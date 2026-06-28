package com.ioffeivan.feature.auth.domain.utils

import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.model.SignUpCredentials

internal data class AuthValidationError(
    val emailError: EmailValidator.Error?,
    val passwordError: PasswordValidator.Error?,
    val confirmPasswordError: PasswordValidator.Error? = null,
) {
    val hasErrors
        get() = emailError != null || passwordError != null || confirmPasswordError != null
}

internal fun validateAuthCredentials(authCredentials: AuthCredentials): AuthValidationError? {
    val emailError = EmailValidator.validate(authCredentials.email)
    val passwordError = PasswordValidator.validate(authCredentials.password)

    return AuthValidationError(
        emailError = emailError,
        passwordError = passwordError,
    ).takeIf { it.hasErrors }
}

internal fun validateSignUpCredentials(signUpCredentials: SignUpCredentials): AuthValidationError? {
    val emailError = EmailValidator.validate(signUpCredentials.email)
    val passwordError = PasswordValidator.validate(signUpCredentials.password)
    val confirmPasswordError =
        if (passwordError == null) {
            PasswordValidator.validateMismatch(
                signUpCredentials.password,
                signUpCredentials.confirmPassword,
            )
        } else {
            null
        }

    return AuthValidationError(
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError,
    ).takeIf { it.hasErrors }
}
