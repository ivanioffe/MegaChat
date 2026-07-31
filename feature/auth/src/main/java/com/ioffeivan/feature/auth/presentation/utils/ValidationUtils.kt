package com.ioffeivan.feature.auth.presentation.utils

import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.auth.R
import com.ioffeivan.feature.auth.domain.utils.EmailValidator
import com.ioffeivan.feature.auth.domain.utils.PASSWORD_MAX_LENGTH
import com.ioffeivan.feature.auth.domain.utils.PASSWORD_MIN_LENGTH
import com.ioffeivan.feature.auth.domain.utils.PasswordValidator

internal fun EmailValidator.Error.toUiText(): UiText {
    return when (this) {
        EmailValidator.Error.EMPTY -> UiText.StringResource(R.string.error_email_empty)
        EmailValidator.Error.INVALID -> UiText.StringResource(R.string.error_email_invalid)
    }
}

internal fun PasswordValidator.Error.toUiText(): UiText {
    return when (this) {
        PasswordValidator.Error.EMPTY -> UiText.StringResource(R.string.error_password_empty)
        PasswordValidator.Error.INVALID -> UiText.StringResource(R.string.error_password_invalid)
        PasswordValidator.Error.TOO_SHORT ->
            UiText.StringResource(
                R.string.error_password_too_short,
                PASSWORD_MIN_LENGTH,
            )

        PasswordValidator.Error.TOO_LONG ->
            UiText.StringResource(
                R.string.error_password_too_long,
                PASSWORD_MAX_LENGTH,
            )

        PasswordValidator.Error.MISMATCH -> UiText.StringResource(R.string.error_password_mismatch)
    }
}
