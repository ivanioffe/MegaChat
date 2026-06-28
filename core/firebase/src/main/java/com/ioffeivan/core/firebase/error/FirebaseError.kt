package com.ioffeivan.core.firebase.error

enum class FirebaseError {
    // Core
    ApiNotAvailable,
    Network,
    TooManyRequests,
    NoSignedInUser,

    // Auth
    AuthWeakPassword,
    AuthActionCode,
    AuthEmail,
    AuthWeb,
    AuthUserCollision,
    AuthInvalidCredentials,
    AuthInvalidUser,
    AuthMultiFactor,
    AuthRecentLoginRequired,
    AuthMissingActivityForRecaptcha,

    Unknown,
}
