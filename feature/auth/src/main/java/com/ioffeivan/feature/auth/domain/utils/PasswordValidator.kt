package com.ioffeivan.feature.auth.domain.utils

internal const val PASSWORD_MIN_LENGTH = 8
internal const val PASSWORD_MAX_LENGTH = 4096

internal object PasswordValidator {
    fun validate(password: String): Error? {
        return when {
            password.isBlank() -> Error.EMPTY
            password.any { it == ' ' } -> Error.INVALID
            password.length < PASSWORD_MIN_LENGTH -> Error.TOO_SHORT
            password.length > PASSWORD_MAX_LENGTH -> Error.TOO_LONG
            else -> null
        }
    }

    fun validateMismatch(password: String, confirmPassword: String): Error? {
        return if (password != confirmPassword) Error.MISMATCH else null
    }

    enum class Error {
        EMPTY,
        INVALID,
        TOO_SHORT,
        TOO_LONG,

        MISMATCH,
    }
}
