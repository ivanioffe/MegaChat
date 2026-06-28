package com.ioffeivan.feature.auth.domain.utils

import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.model.SignUpCredentials

internal fun SignUpCredentials.toAuthCredentials(): AuthCredentials {
    return AuthCredentials(
        email = email,
        password = password,
    )
}
