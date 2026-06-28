package com.ioffeivan.core.firebase.error

import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthWebException
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.ioffeivan.core.domain.base.AppError

internal fun FirebaseException.mapToFirebaseError(): FirebaseError {
    return when (this) {
        // Core
        is FirebaseApiNotAvailableException -> FirebaseError.ApiNotAvailable
        is FirebaseNetworkException -> FirebaseError.Network
        is FirebaseTooManyRequestsException -> FirebaseError.TooManyRequests
        is FirebaseNoSignedInUserException -> FirebaseError.NoSignedInUser

        // Auth
        is FirebaseAuthWeakPasswordException -> FirebaseError.AuthWeakPassword
        is FirebaseAuthActionCodeException -> FirebaseError.AuthActionCode
        is FirebaseAuthEmailException -> FirebaseError.AuthEmail
        is FirebaseAuthWebException -> FirebaseError.AuthWeb
        is FirebaseAuthUserCollisionException -> FirebaseError.AuthUserCollision
        is FirebaseAuthInvalidCredentialsException -> FirebaseError.AuthInvalidCredentials
        is FirebaseAuthInvalidUserException -> FirebaseError.AuthInvalidUser
        is FirebaseAuthMultiFactorException -> FirebaseError.AuthMultiFactor
        is FirebaseAuthRecentLoginRequiredException -> FirebaseError.AuthRecentLoginRequired
        is FirebaseAuthMissingActivityForRecaptchaException -> FirebaseError.AuthMissingActivityForRecaptcha

        else -> FirebaseError.Unknown
    }
}

internal fun FirebaseError.toAppError(): AppError {
    return when (this) {
        FirebaseError.Network -> AppError.NoNetwork
        FirebaseError.TooManyRequests -> AppError.TooManyRequests
        FirebaseError.ApiNotAvailable -> AppError.ServiceUnavailable
        else -> AppError.Unknown
    }
}
