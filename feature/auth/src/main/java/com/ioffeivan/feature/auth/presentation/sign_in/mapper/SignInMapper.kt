package com.ioffeivan.feature.auth.presentation.sign_in.mapper

import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.presentation.sign_in.SignInState

internal fun SignInState.toAuthCredentials(): AuthCredentials {
    return AuthCredentials(
        email = email.value,
        password = password.value,
    )
}
