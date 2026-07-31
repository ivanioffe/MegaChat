package com.ioffeivan.feature.auth.domain.utils

import java.util.regex.Pattern

internal object EmailValidator {
    private val regex =
        Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+",
        )

    fun validate(email: String): Error? {
        return when {
            email.isBlank() -> Error.EMPTY
            !regex.matcher(email).matches() -> Error.INVALID
            else -> null
        }
    }

    enum class Error {
        EMPTY,
        INVALID,
    }
}
