package com.ioffeivan.feature.auth.presentation.sign_up.mapper

import com.ioffeivan.feature.auth.domain.model.SignUpCredentials
import com.ioffeivan.feature.auth.presentation.sign_up.SignUpState

internal fun SignUpState.toSignUpCredentials(): SignUpCredentials {
    return SignUpCredentials(
        email = email.value,
        password = password.value,
        confirmPassword = confirmPassword.value,
    )
}
