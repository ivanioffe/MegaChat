package com.ioffeivan.feature.auth.presentation.utils

import com.ioffeivan.core.ui.UiText

internal data class EmailState(
    val value: String,
    val errorMessage: UiText?,
) {
    companion object {
        fun initial(): EmailState {
            return EmailState(
                value = "",
                errorMessage = null,
            )
        }
    }
}

internal data class PasswordState(
    val value: String,
    val visibility: Boolean,
    val errorMessage: UiText?,
) {
    companion object {
        fun initial(): PasswordState {
            return PasswordState(
                value = "",
                visibility = false,
                errorMessage = null,
            )
        }
    }
}
