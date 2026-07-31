package com.ioffeivan.feature.auth.domain.repository

import com.ioffeivan.core.domain.base.Result

internal interface EmailVerificationRepository {
    suspend fun sendEmailVerification(): Result<Unit, Nothing>

    suspend fun checkEmailVerification(): Result<Unit, CheckEmailVerificationError>

    sealed class CheckEmailVerificationError {
        data object NoEmailVerification : CheckEmailVerificationError()
    }
}
